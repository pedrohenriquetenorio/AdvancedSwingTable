package advancedswingtable.renderers;

import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ActionPanelRenderer extends JPanel
        implements TableCellRenderer {

    private final JButton btnEditar =
            new JButton("Editar");

    private final JButton btnExcluir =
            new JButton("Excluir");

    public ActionPanelRenderer() {

        setLayout(new FlowLayout(
                FlowLayout.CENTER,
                4,
                2
        ));

        setOpaque(true);

        btnEditar.setFocusPainted(false);
        btnExcluir.setFocusPainted(false);

        add(btnEditar);
        add(btnExcluir);
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

        return this;
    }
}