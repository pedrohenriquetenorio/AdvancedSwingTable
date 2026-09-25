package advancedswingtable.pagination;

import advancedswingtable.core.AdvancedTable;
import advancedswingtable.core.AdvancedTableModel;
import advancedswingtable.model.PaginationModel;

import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

public class PaginationController {

    private final AdvancedTable table;
    private final AdvancedTableModel fullModel;
    private final PaginationModel pagination;

    public PaginationController(AdvancedTable table) {

        if (table == null) {
            throw new IllegalArgumentException(
                    "A tabela não pode ser nula."
            );
        }

        if (!(table.getModel() instanceof AdvancedTableModel model)) {
            throw new IllegalStateException(
                    "A tabela precisa estar configurada com AdvancedTableModel."
            );
        }

        this.table = table;
        this.pagination = table.getAdvPaginationModel();
        this.fullModel = model;
    }

    public void refresh() {

        pagination.setTotalItems(
                fullModel.getRowCount()
        );

        showCurrentPage();
    }

    public void nextPage() {

        if (!pagination.hasNextPage()) {
            return;
        }

        pagination.nextPage();
        showCurrentPage();
    }

    public void previousPage() {

        if (!pagination.hasPreviousPage()) {
            return;
        }

        pagination.previousPage();
        showCurrentPage();
    }

    public void firstPage() {

        pagination.setCurrentPage(1);
        showCurrentPage();
    }

    public void lastPage() {

        pagination.setCurrentPage(
                pagination.getTotalPages()
        );

        showCurrentPage();
    }

    public void goToPage(int page) {

        pagination.setCurrentPage(page);
        showCurrentPage();
    }

    public Object[][] getCurrentPageData() {

        int start = pagination.getStartIndex();
        int end = pagination.getEndIndex();

        int columnCount = fullModel.getColumnCount();

        Object[][] data =
                new Object[end - start][columnCount];

        for (int row = start; row < end; row++) {

            for (int column = 0; column < columnCount; column++) {

                data[row - start][column] =
                        fullModel.getValueAt(row, column);
            }
        }

        return data;
    }

    public void showCurrentPage() {

        TableRowSorter<AdvancedTableModel> sorter =
                getTableRowSorter();

        if (sorter == null) {

            sorter = new TableRowSorter<>(fullModel);

            table.setRowSorter(sorter);
        }

        int start = pagination.getStartIndex();
        int end = pagination.getEndIndex();

        RowFilter<AdvancedTableModel, Integer> paginationFilter =
                new RowFilter<AdvancedTableModel, Integer>() {

                    @Override
                    public boolean include(
                            Entry<? extends AdvancedTableModel, ? extends Integer> entry) {

                        int modelRow = entry.getIdentifier();

                        return modelRow >= start
                                && modelRow < end;
                    }
                };

        sorter.setRowFilter(paginationFilter);
    }

    @SuppressWarnings("unchecked")
    private TableRowSorter<AdvancedTableModel> getTableRowSorter() {

        if (!(table.getRowSorter()
                instanceof TableRowSorter<?>)) {

            return null;
        }

        return (TableRowSorter<AdvancedTableModel>)
                table.getRowSorter();
    }

    public PaginationModel getPagination() {
        return pagination;
    }
}