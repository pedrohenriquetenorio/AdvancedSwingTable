package advancedswingtable.core;

import advancedswingtable.enums.TableSelectionMode;
import advancedswingtable.model.PaginationModel;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

public class AdvancedTable extends JTable {

    private boolean stripedRows = false;
    private boolean showSelection = true;
    private boolean filterable = false;
    private String advEmptyText = "Nenhum registro encontrado";
    private String advFilterText = "";

    private final PaginationModel paginationModel =
            new PaginationModel();

    /**
     * Quando um controlador externo (ex: PaginationController) assume
     * o RowSorter da tabela, ele se registra aqui via
     * setFilterRefreshHandler(). A partir desse momento, toda mudança
     * de filtro é delegada a ele, em vez da tabela mexer no RowSorter
     * por conta própria — evita dois RowFilters concorrentes no mesmo
     * sorter (o bug de duplicação entre AdvancedTable e
     * PaginationController).
     */
    private Runnable filterRefreshHandler = null;

    public AdvancedTable() {
        super();
        setFillsViewportHeight(true);
    }

    public void configureColumns(
            AdvancedTableColumn... columns) {

        setModel(new AdvancedTableModel(columns));

        // Um novo model invalida qualquer RowSorter/handler que
        // apontava para o model anterior. Se você usa paginação,
        // chame paginationPanel.setTable(table) de novo depois disto.
        setRowSorter(null);
        filterRefreshHandler = null;

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

        if (!(getModel()
                instanceof AdvancedTableModel model)) {

            throw new IllegalStateException(
                    "A tabela ainda não foi configurada. "
                    + "Chame configureColumns() antes de addRow()."
            );
        }

        model.addData(values);
    }

    public void removeRow(int viewRow) {

        if (!(getModel()
                instanceof AdvancedTableModel model)) {

            throw new IllegalStateException(
                    "A tabela ainda não foi configurada."
            );
        }

        if (viewRow < 0 || viewRow >= getRowCount()) {
            throw new IndexOutOfBoundsException(
                    "Índice de linha inválido: " + viewRow
            );
        }

        int modelRow =
                convertRowIndexToModel(viewRow);

        model.removeRow(modelRow);
    }

    public void removeModelRow(int modelRow) {

        if (!(getModel()
                instanceof AdvancedTableModel model)) {

            throw new IllegalStateException(
                    "A tabela ainda não foi configurada."
            );
        }

        model.removeRow(modelRow);
    }

   public void clearRows() {

    if (!(getModel()
            instanceof AdvancedTableModel model)) {

        throw new IllegalStateException(
                "A tabela ainda não foi configurada."
        );
    }

    model.clear();

    revalidate();
    repaint();
}

    public int getAdvRowCount() {
        return getRowCount();
    }

    public int getAdvModelRowCount() {

        if (!(getModel()
                instanceof AdvancedTableModel model)) {

            return 0;
        }

        return model.getRowCount();
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

    public void setAdvStripedRows(
            boolean stripedRows) {

        this.stripedRows = stripedRows;

        repaint();
    }

    public boolean isAdvAutoResizeColumns() {
        return getAutoResizeMode() != AUTO_RESIZE_OFF;
    }

    public void setAdvAutoResizeColumns(
            boolean enabled) {

        setAutoResizeMode(
                enabled
                        ? AUTO_RESIZE_SUBSEQUENT_COLUMNS
                        : AUTO_RESIZE_OFF
        );
    }

    public boolean isAdvRowSelectable() {
        return getRowSelectionAllowed();
    }

    public void setAdvRowSelectable(
            boolean selectable) {

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

    public String getAdvEmptyText() {
    return advEmptyText;
}

public void setAdvEmptyText(String text) {

    this.advEmptyText =
            text == null || text.isBlank()
                    ? "Nenhum registro encontrado"
                    : text;

    repaint();
}

@Override
protected void paintComponent(java.awt.Graphics g) {

    super.paintComponent(g);

    if (getRowCount() > 0) {
        return;
    }

    java.awt.Graphics2D g2 =
            (java.awt.Graphics2D) g.create();

    try {

        g2.setColor(getForeground());

        java.awt.FontMetrics metrics =
                g2.getFontMetrics();

        int textWidth =
                metrics.stringWidth(advEmptyText);

        int x =
                (getWidth() - textWidth) / 2;

        int y =
                (getHeight()
                        - metrics.getHeight()) / 2
                + metrics.getAscent();

        g2.drawString(
                advEmptyText,
                Math.max(x, 10),
                Math.max(y, metrics.getAscent())
        );

    } finally {

        g2.dispose();
    }
}
    
    
    @Override
    public boolean isCellSelected(
            int row,
            int column) {

        if (!showSelection) {
            return false;
        }

        return super.isCellSelected(row, column);
    }

    /**
     * Aplica o efeito zebrado a QUALQUER renderer da tabela —
     * inclusive renderers customizados por coluna, como o de ações —
     * sem precisar registrar um DefaultRenderer manualmente por tipo.
     * A cor de seleção sempre tem prioridade sobre a listra, e as
     * cores respeitam o tema atual do FlatLaf (claro/escuro) porque
     * vêm do UIManager.
     */
    @Override
    public Component prepareRenderer(
            TableCellRenderer renderer,
            int row,
            int column) {

        Component component =
                super.prepareRenderer(renderer, row, column);

        if (stripedRows && !isCellSelected(row, column)) {

            component.setBackground(
                    row % 2 == 0
                            ? getBackground()
                            : getAlternateRowColor()
            );
        }

        return component;
    }

    private Color getAlternateRowColor() {

        Color color =
                UIManager.getColor("Table.alternateRowColor");

        return color != null ? color : getBackground();
    }

    public boolean isAdvFilterable() {
        return filterable;
    }

    public void setAdvFilterable(
            boolean filterable) {

        boolean oldValue = this.filterable;

        this.filterable = filterable;

        if (!filterable) {
            advFilterText = "";
        }

        applyFilterChange();

        firePropertyChange(
                "advFilterable",
                oldValue,
                filterable
        );
    }

    public String getAdvFilter() {
        return advFilterText;
    }

    public void setAdvFilter(String text) {

        if (!filterable) {
            return;
        }

        String oldValue = advFilterText;

        advFilterText =
                text == null
                        ? ""
                        : text.trim();

        applyFilterChange();

        firePropertyChange(
                "advFilter",
                oldValue,
                advFilterText
        );
    }

    /**
     * Ponto único de aplicação do filtro. Se um PaginationController
     * estiver registrado, delega a ele — que sabe combinar o filtro
     * de texto com a filtragem de página. Sem controlador registrado,
     * aplica um filtro simples direto no RowSorter, para a tabela
     * continuar funcional mesmo sem paginação.
     */
    private void applyFilterChange() {

        if (filterRefreshHandler != null) {
            filterRefreshHandler.run();
            return;
        }

        applyStandaloneTextFilter();
    }

    private void applyStandaloneTextFilter() {

        if (getRowSorter() == null) {
            setAutoCreateRowSorter(true);
        }

        TableRowSorter<AdvancedTableModel> sorter =
                getAdvancedRowSorter();

        if (sorter == null) {
            return;
        }

        if (!filterable || advFilterText.isBlank()) {

            sorter.setRowFilter(null);

            return;
        }

        sorter.setRowFilter(
                RowFilter.regexFilter(
                        "(?i)"
                        + java.util.regex.Pattern.quote(
                                advFilterText
                        )
                )
        );
    }

    @SuppressWarnings("unchecked")
    private TableRowSorter<AdvancedTableModel>
            getAdvancedRowSorter() {

        if (!(getRowSorter()
                instanceof TableRowSorter<?>)) {

            return null;
        }

        return (TableRowSorter<AdvancedTableModel>)
                getRowSorter();
    }

    /**
     * Usado por PaginationController para assumir o controle da
     * filtragem (texto + paginação) neste RowSorter. Passe null para
     * devolver o controle à tabela (ex: quando o controlador é
     * descartado via dispose()).
     */
    public void setFilterRefreshHandler(Runnable handler) {
        this.filterRefreshHandler = handler;
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

    public int getAdvPageSize() {
        return paginationModel.getPageSize();
    }

    public void setAdvPageSize(int pageSize) {

        int oldValue =
                paginationModel.getPageSize();

        paginationModel.setPageSize(pageSize);

        firePropertyChange(
                "advPageSize",
                oldValue,
                paginationModel.getPageSize()
        );
    }
}