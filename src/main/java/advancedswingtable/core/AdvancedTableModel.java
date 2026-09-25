package advancedswingtable.core;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class AdvancedTableModel extends AbstractTableModel {

    private final AdvancedTableColumn[] columns;
    private final List<Object[]> data = new ArrayList<>();

    public AdvancedTableModel(AdvancedTableColumn[] columns) {
        if (columns == null || columns.length == 0) {
            throw new IllegalArgumentException(
                    "A tabela precisa ter pelo menos uma coluna."
            );
        }

        this.columns = columns.clone();
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column].getName();
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return columns[column].getType();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return columns[column].isEditable();
    }

    @Override
    public Object getValueAt(int row, int column) {
        return data.get(row)[column];
    }

    @Override
    public void setValueAt(
            Object value,
            int row,
            int column) {

        data.get(row)[column] = value;

        fireTableCellUpdated(row, column);
    }

    public void addData(Object... values) {

        if (values == null) {
            throw new IllegalArgumentException(
                    "Os valores da linha não podem ser nulos."
            );
        }

        if (values.length != columns.length) {
            throw new IllegalArgumentException(
                    "Quantidade de valores diferente da quantidade de colunas."
            );
        }

        data.add(values.clone());

        int index = data.size() - 1;

        fireTableRowsInserted(index, index);
    }

    public void removeRow(int row) {

        checkRowIndex(row);

        data.remove(row);

        fireTableRowsDeleted(row, row);
    }

    public void clear() {

        int lastRow = data.size() - 1;

        data.clear();

        if (lastRow >= 0) {
            fireTableRowsDeleted(0, lastRow);
        }
    }

    public Object[] getRow(int row) {

        checkRowIndex(row);

        return data.get(row).clone();
    }

    public AdvancedTableColumn[] getColumns() {
        return columns.clone();
    }

    private void checkRowIndex(int row) {

        if (row < 0 || row >= data.size()) {
            throw new IndexOutOfBoundsException(
                    "Índice de linha inválido: " + row
            );
        }
    }
}