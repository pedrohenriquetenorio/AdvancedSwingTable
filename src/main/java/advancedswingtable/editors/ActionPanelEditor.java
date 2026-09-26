package advancedswingtable.editors;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.function.BiConsumer;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ActionPanelEditor
        extends AbstractCellEditor
        implements TableCellEditor {

    private final JPanel panel;

    private final JButton btnEditar =
            new JButton("Editar");

    private final JButton btnExcluir =
            new JButton("Excluir");

    private JTable table;
    private int viewRow;

    private String text;

    private BiConsumer<JTable, Integer> editAction;
    private BiConsumer<JTable, Integer> deleteAction;

    private boolean processingClick = false;

    public ActionPanelEditor() {

        panel = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        4,
                        2
                )
        );

        panel.setOpaque(true);

        btnEditar.setFocusPainted(false);
        btnExcluir.setFocusPainted(false);

        panel.add(btnEditar);
        panel.add(btnExcluir);

        btnEditar.addActionListener(e ->
                executeAction(editAction)
        );

        btnExcluir.addActionListener(e ->
                executeAction(deleteAction)
        );
    }

    private void executeAction(
            BiConsumer<JTable, Integer> action) {

        if (processingClick || table == null) {
            return;
        }

        processingClick = true;

        int modelRow =
                table.convertRowIndexToModel(viewRow);

        if (action != null) {
            action.accept(
                    table,
                    modelRow
            );
        }

        fireEditingStopped();

        processingClick = false;
    }

    public void setEditAction(
            BiConsumer<JTable, Integer> editAction) {

        this.editAction = editAction;
    }

    public void setDeleteAction(
            BiConsumer<JTable, Integer> deleteAction) {

        this.deleteAction = deleteAction;
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table,
            Object value,
            boolean isSelected,
            int row,
            int column) {

        this.table = table;
        this.viewRow = row;

        this.text =
                value != null
                        ? value.toString()
                        : "";

        processingClick = false;

        panel.setBackground(
                isSelected
                        ? table.getSelectionBackground()
                        : table.getBackground()
        );

        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return text;
    }

    public int getViewRow() {
        return viewRow;
    }
}