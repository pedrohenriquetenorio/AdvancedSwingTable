package advancedswingtable.editors;

import advancedswingtable.core.AdvancedTableAction;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javax.swing.table.TableCellEditor;

public class AdvancedActionsEditor
        extends AbstractCellEditor
        implements TableCellEditor {

    private final JPanel panel;

    private final AdvancedTableAction[] actions;

    private final List<JButton> buttons =
            new ArrayList<>();

    private JTable table;

    private int viewRow = -1;

    private int viewColumn = -1;

    private String text = "";

    private boolean processingClick = false;

    /**
     * Escuta mudanças na seleção da JTable.
     *
     * O editor é um componente filho da JTable e pode permanecer
     * visualmente com a pintura anterior enquanto a seleção da linha
     * já mudou. Atualizamos explicitamente o painel para evitar esse
     * atraso visual.
     */
    private final ListSelectionListener selectionListener =
            this::selectionChanged;

    private boolean selectionListenerInstalled = false;

    public AdvancedActionsEditor(
            AdvancedTableAction... actions) {

        if (actions == null || actions.length == 0) {
            throw new IllegalArgumentException(
                    "A coluna de ações precisa ter pelo menos uma ação."
            );
        }

        this.actions = actions.clone();

        // Mesmo vgap reduzido do renderer, para não "pular" de
        // tamanho quando a célula entra/sai do modo de edição.
        panel = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        6,
                        1
                )
        );

        panel.setOpaque(true);

        createButtons();
    }

    private void createButtons() {

        panel.removeAll();
        buttons.clear();

        for (AdvancedTableAction action : actions) {

            JButton button =
                    new JButton(
                            action.getText(),
                            action.getIcon()
                    );

            button.setFocusPainted(false);

            // O botão não deve receber foco de teclado.
            // Assim a JTable continua sendo o componente focado
            // enquanto o usuário interage com a ação.
            button.setFocusable(false);

            button.setMargin(
                    new Insets(
                            1,
                            10,
                            1,
                            10
                    )
            );

            button.addActionListener(e ->
                    executeAction(action)
            );

            buttons.add(button);
            panel.add(button);
        }
    }

    private void executeAction(
            AdvancedTableAction action) {

        if (processingClick || table == null) {
            return;
        }

        processingClick = true;

        try {

            if (viewRow < 0
                    || viewRow >= table.getRowCount()) {
                return;
            }

            int modelRow =
                    table.convertRowIndexToModel(
                            viewRow
                    );

            boolean executed =
                    action.execute(
                            table,
                            modelRow
                    );

            if (executed) {
                fireEditingStopped();
            }

        } finally {

            processingClick = false;
        }
    }

    /**
     * Chamado sempre que a seleção da JTable muda.
     */
    private void selectionChanged(
            ListSelectionEvent event) {

        if (event.getValueIsAdjusting()) {
            return;
        }

        if (table == null
                || viewRow < 0
                || viewColumn < 0) {
            return;
        }

        updatePanelBackground();

        /*
         * O repaint é importante porque o editor é um componente
         * separado dentro da JTable. A JTable pode ter repintado
         * a linha sem que o painel do editor tenha sido redesenhado
         * imediatamente.
         */
        panel.repaint();
    }

    /**
     * Instala o listener da seleção na tabela atual.
     */
    private void installSelectionListener(
            JTable table) {

        if (selectionListenerInstalled
                || table == null) {
            return;
        }

        table.getSelectionModel()
                .addListSelectionListener(
                        selectionListener
                );

        table.getColumnModel()
                .getSelectionModel()
                .addListSelectionListener(
                        selectionListener
                );

        selectionListenerInstalled = true;
    }

    /**
     * Remove os listeners da tabela anterior.
     */
    private void uninstallSelectionListener() {

        if (!selectionListenerInstalled
                || table == null) {
            return;
        }

        table.getSelectionModel()
                .removeListSelectionListener(
                        selectionListener
                );

        table.getColumnModel()
                .getSelectionModel()
                .removeListSelectionListener(
                        selectionListener
                );

        selectionListenerInstalled = false;
    }

    /**
     * Atualiza a cor do fundo do editor de acordo com o estado
     * REAL da célula na JTable.
     *
     * Não depende exclusivamente do parâmetro isSelected recebido
     * quando o editor é criado.
     */
    private void updatePanelBackground() {

        if (table == null
                || viewRow < 0
                || viewColumn < 0) {
            return;
        }

        boolean selected =
                table.isCellSelected(
                        viewRow,
                        viewColumn
                );

        panel.setBackground(
                selected
                        ? table.getSelectionBackground()
                        : table.getBackground()
        );

        panel.setForeground(
                selected
                        ? table.getSelectionForeground()
                        : table.getForeground()
        );
    }

    @Override
    public Object getCellEditorValue() {
        return text;
    }

    public int getViewRow() {
        return viewRow;
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table,
            Object value,
            boolean isSelected,
            int row,
            int column) {

        /*
         * Se o editor estiver sendo reutilizado em outra JTable,
         * removemos primeiro os listeners da tabela anterior.
         */
        if (this.table != table) {

            uninstallSelectionListener();

            this.table = table;

            installSelectionListener(table);
        }

        this.viewRow = row;
        this.viewColumn = column;

        this.text =
                value != null
                        ? value.toString()
                        : "";

        processingClick = false;

        /*
         * Aqui usamos o estado REAL da JTable em vez de confiar
         * somente no isSelected recebido pelo Swing.
         */
        updatePanelBackground();

        int modelRow =
                table.convertRowIndexToModel(row);

        for (int i = 0;
             i < actions.length;
             i++) {

            buttons.get(i).setEnabled(
                    actions[i].isEnabledFor(
                            modelRow
                    )
            );
        }

        /*
         * Garante que a atualização visual aconteça imediatamente
         * no próximo ciclo de pintura do EDT.
         */
        SwingUtilities.invokeLater(() -> {

            if (this.table == table
                    && this.viewRow == row
                    && this.viewColumn == column) {

                updatePanelBackground();

                panel.revalidate();
                panel.repaint();
            }
        });

        return panel;
    }

    @Override
    public void cancelCellEditing() {

        super.cancelCellEditing();
    }
}