package advancedswingtable.renderers;

import advancedswingtable.core.AdvancedTableAction;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class AdvancedActionsRenderer extends JPanel
        implements TableCellRenderer {

    private final List<AdvancedTableAction> actions;
    private final List<JButton> buttons =
            new ArrayList<>();

    public AdvancedActionsRenderer(
            AdvancedTableAction... actions) {

        if (actions == null || actions.length == 0) {
            throw new IllegalArgumentException(
                    "A coluna de ações precisa ter pelo menos uma ação."
            );
        }

        this.actions =
                List.of(actions);

        // vgap reduzido (era 2) pra caber melhor em linhas baixas
        // sem cortar o botão embaixo.
        setLayout(
                new FlowLayout(
                        FlowLayout.CENTER,
                        6,
                        1
                )
        );

        setOpaque(true);

        createButtons();
    }

    private void createButtons() {

        removeAll();
        buttons.clear();

        for (AdvancedTableAction action : actions) {

            JButton button =
                    new JButton(
                            action.getText(),
                            action.getIcon()
                    );

            button.setFocusPainted(false);
            button.setFocusable(false);

            // Botão mantém o visual normal do Look&Feel (fundo e
            // borda próprios), em vez de ficar transparente. Isso
            // evita que, sobre uma linha selecionada, ele vire só
            // texto sem contraste — o botão continua legível e com
            // forma própria em qualquer linha.
            button.setMargin(new Insets(1, 10, 1, 10));

            buttons.add(button);
            add(button);
        }
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {

        setBackground(
                isSelected
                        ? table.getSelectionBackground()
                        : table.getBackground()
        );

        setForeground(
                isSelected
                        ? table.getSelectionForeground()
                        : table.getForeground()
        );

        int modelRow =
                row >= 0
                        ? table.convertRowIndexToModel(row)
                        : -1;

        for (int i = 0;
                i < actions.size();
                i++) {

            boolean enabled =
                    modelRow < 0
                            || actions.get(i)
                                    .isEnabledFor(modelRow);

            buttons.get(i)
                    .setEnabled(enabled);
        }

        return this;
    }
}