/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package advancedswingtable.renderers;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TestRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {

        Component component =
                super.getTableCellRendererComponent(
                        table,
                        value,
                        isSelected,
                        hasFocus,
                        row,
                        column
                );

        if (!isSelected) {
            component.setBackground(Color.YELLOW);
        }

        return component;
    }
}