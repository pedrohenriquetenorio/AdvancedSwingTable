package advancedswingtable.pagination;

import advancedswingtable.core.AdvancedTable;
import advancedswingtable.core.AdvancedTableModel;
import advancedswingtable.model.PaginationModel;

import javax.swing.RowFilter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableRowSorter;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class PaginationController {

    private final AdvancedTable table;
    private final AdvancedTableModel fullModel;
    private final PaginationModel pagination;

    private final List<Integer> currentPageModelRows
            = new ArrayList<>();

    private boolean updating = false;
    private boolean disposed = false;

    private final TableModelListener modelListener
            = this::modelChanged;

    private final PropertyChangeListener pageSizeListener
            = event -> refresh();

    public PaginationController(AdvancedTable table) {

        if (table == null) {
            throw new IllegalArgumentException(
                    "A tabela não pode ser nula."
            );
        }

        if (!(table.getModel() instanceof AdvancedTableModel model)) {

            throw new IllegalStateException(
                    "A tabela precisa estar configurada com "
                    + "AdvancedTableModel. Chame configureColumns() "
                    + "antes de associar a paginação."
            );
        }

        this.table = table;
        this.pagination
                = table.getAdvPaginationModel();
        this.fullModel = model;

        configureSorter();

        fullModel.addTableModelListener(modelListener);

        table.addPropertyChangeListener(
                "advPageSize",
                pageSizeListener
        );

        // A partir de agora este controlador é o dono exclusivo da
        // filtragem (texto + paginação) no RowSorter da tabela — a
        // AdvancedTable deixa de mexer no sorter diretamente enquanto
        // este handler estiver registrado.
        table.setFilterRefreshHandler(this::refresh);
    }

    private void modelChanged(TableModelEvent event) {
        refresh();
    }

    private void configureSorter() {

        if (!(table.getRowSorter() instanceof TableRowSorter<?>)) {

            table.setRowSorter(
                    new TableRowSorter<>(fullModel)
            );
        }
    }

    @SuppressWarnings("unchecked")
    private TableRowSorter<AdvancedTableModel>
            getSorter() {

        if (!(table.getRowSorter() instanceof TableRowSorter<?>)) {

            return null;
        }

        return (TableRowSorter<AdvancedTableModel>) table.getRowSorter();
    }

    public void refresh() {

        if (updating || disposed) {
            return;
        }

        updating = true;

        try {

            TableRowSorter<AdvancedTableModel> sorter
                    = getSorter();

            if (sorter == null) {
                return;
            }

            RowFilter<AdvancedTableModel, Integer> searchFilter
                    = createSearchFilter();

            sorter.setRowFilter(searchFilter);

            int totalItems
                    = sorter.getViewRowCount();

            pagination.setTotalItems(totalItems);

            currentPageModelRows.clear();

            if (totalItems == 0) {

                return;
            }

            applyPaginationFilter(
                    sorter,
                    searchFilter
            );

        } finally {

            updating = false;
        }
    }

    private RowFilter<AdvancedTableModel, Integer>
            createSearchFilter() {

        if (!table.isAdvFilterable()) {
            return null;
        }

        String text
                = table.getAdvFilter();

        if (text == null || text.isBlank()) {
            return null;
        }

        return RowFilter.regexFilter(
                "(?i)"
                + java.util.regex.Pattern.quote(text)
        );
    }

    private void applyPaginationFilter(
            TableRowSorter<AdvancedTableModel> sorter,
            RowFilter<AdvancedTableModel, Integer> searchFilter) {

        int start
                = pagination.getStartIndex();

        int end
                = Math.min(
                        pagination.getEndIndex(),
                        sorter.getViewRowCount()
                );

        currentPageModelRows.clear();

        for (int viewRow = start;
                viewRow < end;
                viewRow++) {

            int modelRow
                    = sorter.convertRowIndexToModel(viewRow);

            currentPageModelRows.add(modelRow);
        }

        RowFilter<AdvancedTableModel, Integer> paginationFilter
                = new RowFilter<>() {

            @Override
            public boolean include(
                    Entry<? extends AdvancedTableModel, ? extends Integer> entry) {

                return currentPageModelRows.contains(
                        entry.getIdentifier()
                );
            }
        };

        List<RowFilter<? super AdvancedTableModel, ? super Integer>> filters
                = new ArrayList<>();

        if (searchFilter != null) {
            filters.add(searchFilter);
        }

        filters.add(paginationFilter);

        sorter.setRowFilter(
                RowFilter.andFilter(filters)
        );
    }

    public void nextPage() {

        if (!pagination.hasNextPage()) {
            return;
        }

        pagination.nextPage();
        refresh();
    }

    public void previousPage() {

        if (!pagination.hasPreviousPage()) {
            return;
        }

        pagination.previousPage();
        refresh();
    }

    public void firstPage() {

        pagination.setCurrentPage(1);
        refresh();
    }

    public void lastPage() {

        pagination.setCurrentPage(
                pagination.getTotalPages()
        );

        refresh();
    }

    public void goToPage(int page) {

        pagination.setCurrentPage(page);
        refresh();
    }

    public Object[][] getCurrentPageData() {

        int columnCount
                = fullModel.getColumnCount();

        Object[][] data
                = new Object[currentPageModelRows.size()][columnCount];

        for (int i = 0;
                i < currentPageModelRows.size();
                i++) {

            int modelRow
                    = currentPageModelRows.get(i);

            for (int column = 0;
                    column < columnCount;
                    column++) {

                data[i][column]
                        = fullModel.getValueAt(
                                modelRow,
                                column
                        );
            }
        }

        return data;
    }

    public PaginationModel getPagination() {
        return pagination;
    }

    /**
     * Desliga este controlador: remove os listeners do model e da
     * tabela, e devolve à AdvancedTable o controle da filtragem.
     * Chame antes de descartar o controlador (ex: ao trocar de
     * tabela em um PaginationPanel) para não vazar listeners.
     */
    public void dispose() {

        if (disposed) {
            return;
        }

        disposed = true;

        fullModel.removeTableModelListener(modelListener);

        table.removePropertyChangeListener(
                "advPageSize",
                pageSizeListener
        );

        table.setFilterRefreshHandler(null);
    }
}