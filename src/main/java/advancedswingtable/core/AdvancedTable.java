package advancedswingtable.core;

import advancedswingtable.enums.TableSelectionMode;
import advancedswingtable.model.PaginationModel;
import advancedswingtable.renderers.StripedCellRenderer;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

public class AdvancedTable extends JTable {

    private boolean stripedRows = false;
    private boolean showSelection = true;
    private boolean filterable = false;

    private final PaginationModel paginationModel =
            new PaginationModel();

    public AdvancedTable() {
        super();
    }

    public void configureColumns(AdvancedTableColumn... columns) {

        setModel(new AdvancedTableModel(columns));
        configureColumnProperties(columns);
    }

    private void configureColumnProperties(
            AdvancedTableColumn... columns) {

        for (int i = 0; i < columns.length; i++) {

            AdvancedTableColumn column = columns[i];

            if (column.getPreferredWidth() > 0) {
                getColumnModel()
                        .getColumn(i)
                        .setPreferredWidth(
                                column.getPreferredWidth()
                        );
            }

            TableCellRenderer renderer =
                    column.getCustomRenderer();

            if (renderer != null) {
                getColumnModel()
                        .getColumn(i)
                        .setCellRenderer(renderer);
            }

            TableCellEditor editor =
                    column.getCustomEditor();

            if (editor != null) {
                getColumnModel()
                        .getColumn(i)
                        .setCellEditor(editor);
            }
        }
    }

    public void addRow(Object... values) {

        if (!(getModel() instanceof AdvancedTableModel model)) {
            throw new IllegalStateException(
                    "A tabela ainda não foi configurada. "
                    + "Chame configureColumns() antes de addRow()."
            );
        }

        model.addData(values);
    }

    public int getAdvTableRowHeight() {
        return getRowHeight();
    }

    public void setAdvTableRowHeight(int height) {
        setRowHeight(height);
    }

    public boolean isAdvShowHeader() {
        return getTableHeader().isVisible();
    }

    public void setAdvShowHeader(boolean showHeader) {
        getTableHeader().setVisible(showHeader);
    }

    public boolean isAdvStripedRows() {
        return stripedRows;
    }

    public void setAdvStripedRows(boolean stripedRows) {

        this.stripedRows = stripedRows;

        updateStripedRows();

        repaint();
    }

    private void updateStripedRows() {

        if (stripedRows) {

            setDefaultRenderer(
                    Object.class,
                    new StripedCellRenderer()
            );

        } else {

            setDefaultRenderer(
                    Object.class,
                    new DefaultTableCellRenderer()
            );
        }
    }

    public boolean isAdvAutoResizeColumns() {
        return getAutoResizeMode() != AUTO_RESIZE_OFF;
    }

    public void setAdvAutoResizeColumns(boolean enabled) {

        setAutoResizeMode(
                enabled
                        ? AUTO_RESIZE_SUBSEQUENT_COLUMNS
                        : AUTO_RESIZE_OFF
        );
    }

    public boolean isAdvRowSelectable() {
        return getRowSelectionAllowed();
    }

    public void setAdvRowSelectable(boolean selectable) {

        setRowSelectionAllowed(selectable);

        if (!selectable) {
            clearSelection();
        }
    }

    public TableSelectionMode getAdvSelectionModeType() {

        return switch (
                getSelectionModel().getSelectionMode()
        ) {

            case ListSelectionModel.SINGLE_SELECTION ->
                    TableSelectionMode.SINGLE;

            case ListSelectionModel.SINGLE_INTERVAL_SELECTION ->
                    TableSelectionMode.SINGLE_INTERVAL;

            case ListSelectionModel.MULTIPLE_INTERVAL_SELECTION ->
                    TableSelectionMode.MULTIPLE_INTERVAL;

            default ->
                    TableSelectionMode.SINGLE;
        };
    }

    public void setAdvSelectionModeType(
            TableSelectionMode mode) {

        if (mode == null) {
            mode = TableSelectionMode.SINGLE;
        }

        getSelectionModel()
                .setSelectionMode(
                        mode.getSwingMode()
                );
    }

    public boolean isAdvShowGridLines() {
        return getShowHorizontalLines()
                || getShowVerticalLines();
    }

    public void setAdvShowGridLines(
            boolean showGridLines) {

        setShowHorizontalLines(showGridLines);
        setShowVerticalLines(showGridLines);
    }

    public boolean isAdvResizableColumns() {
        return getTableHeader()
                .getResizingAllowed();
    }

    public void setAdvResizableColumns(
            boolean resizableColumns) {

        getTableHeader()
                .setResizingAllowed(resizableColumns);
    }

    public boolean isAdvReorderableColumns() {
        return getTableHeader()
                .getReorderingAllowed();
    }

    public void setAdvReorderableColumns(
            boolean reorderableColumns) {

        getTableHeader()
                .setReorderingAllowed(reorderableColumns);
    }

    public boolean isAdvSortable() {
        return getRowSorter() != null;
    }

    public void setAdvSortable(boolean sortable) {

        if (sortable) {
            setAutoCreateRowSorter(true);
        } else {
            setAutoCreateRowSorter(false);
            setRowSorter(null);
        }
    }

    public boolean isAdvShowSelection() {
        return showSelection;
    }

    public void setAdvShowSelection(
            boolean showSelection) {

        this.showSelection = showSelection;
        repaint();
    }

    public boolean isAdvFilterable() {
        return filterable;
    }

    public void setAdvFilterable(boolean filterable) {

        this.filterable = filterable;

        TableRowSorter<AdvancedTableModel> sorter =
                getAdvancedRowSorter();

        if (!filterable && sorter != null) {
            sorter.setRowFilter(null);
        }
    }

    public void setAdvFilter(String text) {

        if (!filterable) {
            return;
        }

        if (getRowSorter() == null) {
            setAutoCreateRowSorter(true);
        }

        TableRowSorter<AdvancedTableModel> sorter =
                getAdvancedRowSorter();

        if (sorter == null) {
            return;
        }

        if (text == null || text.isBlank()) {

            sorter.setRowFilter(null);
            return;
        }

        sorter.setRowFilter(
                RowFilter.regexFilter(
                        "(?i)"
                        + java.util.regex.Pattern.quote(text)
                )
        );
    }

    @SuppressWarnings("unchecked")
    private TableRowSorter<AdvancedTableModel>
            getAdvancedRowSorter() {

        if (!(getRowSorter() instanceof TableRowSorter<?>)) {
            return null;
        }

        return (TableRowSorter<AdvancedTableModel>)
                getRowSorter();
    }

    public PaginationModel getAdvPaginationModel() {
        return paginationModel;
    }

    public int getAdvCurrentPage() {
        return paginationModel.getCurrentPage();
    }

    public void setAdvCurrentPage(int page) {
        paginationModel.setCurrentPage(page);
    }
}