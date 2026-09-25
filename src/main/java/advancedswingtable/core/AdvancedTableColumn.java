package advancedswingtable.core;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class AdvancedTableColumn {

    private final String name;
    private final Class<?> type;

    private boolean editable = false;
    private int preferredWidth = -1;

    private TableCellRenderer customRenderer;
    private TableCellEditor customEditor;

    public AdvancedTableColumn(
            String name,
            Class<?> type) {

        this(name, type, false);
    }

    public AdvancedTableColumn(
            String name,
            Class<?> type,
            boolean editable) {

        this.name = name;
        this.type = type;
        this.editable = editable;
    }

    public AdvancedTableColumn(
            String name,
            Class<?> type,
            boolean editable,
            int preferredWidth) {

        this(name, type, editable);
        this.preferredWidth = preferredWidth;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public int getPreferredWidth() {
        return preferredWidth;
    }

    public void setPreferredWidth(int preferredWidth) {
        this.preferredWidth = preferredWidth;
    }

    public TableCellRenderer getCustomRenderer() {
        return customRenderer;
    }

    public void setCustomRenderer(
            TableCellRenderer customRenderer) {

        this.customRenderer = customRenderer;
    }

    public TableCellEditor getCustomEditor() {
        return customEditor;
    }

    public void setCustomEditor(
            TableCellEditor customEditor) {

        this.customEditor = customEditor;
    }
}