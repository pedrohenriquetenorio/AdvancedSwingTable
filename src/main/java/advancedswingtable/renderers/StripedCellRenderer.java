package advancedswingtable.renderers;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

public class StripedCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {

        Component component = super.getTableCellRendererComponent(
                table,
                value,
                isSelected,
                hasFocus,
                row,
                column
        );

        if (isSelected) {
            return component;
        }

        Color alternateRowColor =
                UIManager.getColor("Table.alternateRowColor");

        if (alternateRowColor == null) {
            alternateRowColor = table.getBackground();
        }

        if (row % 2 == 0) {
            component.setBackground(table.getBackground());
        } else {
            component.setBackground(alternateRowColor);
        }

        return component;
    }
}