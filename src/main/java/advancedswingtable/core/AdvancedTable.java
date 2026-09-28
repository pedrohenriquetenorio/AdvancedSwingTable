package advancedswingtable.core;

import advancedswingtable.enums.TableSelectionMode;
import advancedswingtable.model.PaginationModel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

public class AdvancedTable extends JTable {

    private boolean stripedRows = false;
    private boolean showSelection = true;
    private boolean filterable = false;
    private boolean rowHoverEnabled = false;

    private String advEmptyText = "Nenhum registro encontrado";
    private String advFilterText = "";

    private int hoveredRow = -1;

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

        // Necessário para que getToolTipText(MouseEvent) seja
        // consultado dinamicamente pelo ToolTipManager — sem isto,
        // como nunca chamamos setToolTipText(String), o componente
        // não fica registrado e a sobrescrita nunca é acionada.
        ToolTipManager.sharedInstance().registerComponent(this);

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                updateHoveredRow(rowAtPoint(e.getPoint()));
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                updateHoveredRow(-1);
            }
        });
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

    public boolean isAdvRowHoverEnabled() {
        return rowHoverEnabled;
    }

    public void setAdvRowHoverEnabled(boolean rowHoverEnabled) {

        this.rowHoverEnabled = rowHoverEnabled;

        if (!rowHoverEnabled && hoveredRow != -1) {

            int previous = hoveredRow;

            hoveredRow = -1;

            repaintRow(previous);
        }
    }

    private void updateHoveredRow(int row) {

        if (!rowHoverEnabled || row == hoveredRow) {
            return;
        }

        int previous = hoveredRow;

        hoveredRow = row;

        repaintRow(previous);
        repaintRow(hoveredRow);
    }

    private void repaintRow(int row) {

        if (row < 0 || row >= getRowCount()) {
            return;
        }

        Rectangle rect = getCellRect(row, 0, true);
        rect.width = getWidth();

        repaint(rect);
    }

    private Color getHoverRowColor() {

        Color base = getBackground();
        Color accent = getSelectionBackground();

        int r = (base.getRed() * 85 + accent.getRed() * 15) / 100;
        int g = (base.getGreen() * 85 + accent.getGreen() * 15) / 100;
        int b = (base.getBlue() * 85 + accent.getBlue() * 15) / 100;

        return new Color(r, g, b);
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
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (getRowCount() > 0) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();

        try {

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            );

            g2.setColor(getEmptyTextColor());

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

    private Color getEmptyTextColor() {

        // "Label.disabledForeground" é uma cor mais apagada que o
        // texto normal, própria pra mensagens de estado vazio — e
        // acompanha automaticamente o tema claro/escuro do FlatLaf.
        Color color =
                UIManager.getColor("Label.disabledForeground");

        return color != null ? color : getForeground();
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
     * Aplica o efeito zebrado e/ou o realce de hover a QUALQUER
     * renderer da tabela — inclusive renderers customizados por
     * coluna, como o de ações — sem precisar registrar um
     * DefaultRenderer manualmente por tipo. Prioridade das cores:
     * seleção > hover > zebra > cor padrão. As cores respeitam o
     * tema atual do FlatLaf (claro/escuro) porque vêm do UIManager
     * ou são derivadas das cores atuais da tabela.
     */
    @Override
    public Component prepareRenderer(
            TableCellRenderer renderer,
            int row,
            int column) {

        Component component =
                super.prepareRenderer(renderer, row, column);

        if (isCellSelected(row, column)) {
            return component;
        }

        if (rowHoverEnabled && row == hoveredRow) {

            component.setBackground(getHoverRowColor());

            return component;
        }

        if (stripedRows) {

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

    @Override
    public String getToolTipText(MouseEvent event) {

        Point point = event.getPoint();

        int viewRow = rowAtPoint(point);
        int viewColumn = columnAtPoint(point);

        if (viewRow < 0 || viewColumn < 0) {
            return super.getToolTipText(event);
        }

        Object value = getValueAt(viewRow, viewColumn);

        if (value == null) {
            return super.getToolTipText(event);
        }

        String text = value.toString();

        if (text.isBlank()) {
            return super.getToolTipText(event);
        }

        // Só mostra o tooltip quando o conteúdo realmente não cabe
        // na largura atual da coluna — evita tooltip aparecendo em
        // toda célula, mesmo nas que já mostram o valor inteiro.
        TableCellRenderer renderer = getCellRenderer(viewRow, viewColumn);
        Component rendered = prepareRenderer(renderer, viewRow, viewColumn);

        int columnWidth =
                getColumnModel().getColumn(viewColumn).getWidth();

        if (rendered.getPreferredSize().width <= columnWidth) {
            return super.getToolTipText(event);
        }

        return text;
    }

    /**
     * Ajusta a largura de todas as colunas ao conteúdo atualmente
     * visível (cabeçalho + linhas da página exibida), com uma
     * margem padrão de 10px. Chame depois de popular a tabela.
     */
    public void packColumnWidths() {
        packColumnWidths(10);
    }

    public void packColumnWidths(int margin) {

        for (int column = 0; column < getColumnCount(); column++) {
            packColumn(column, margin);
        }
    }

    private void packColumn(int column, int margin) {

        TableColumn tableColumn =
                getColumnModel().getColumn(column);

        int preferredWidth = 0;

        if (getTableHeader() != null) {

            TableCellRenderer headerRenderer =
                    getTableHeader().getDefaultRenderer();

            Component headerComponent =
                    headerRenderer.getTableCellRendererComponent(
                            this,
                            tableColumn.getHeaderValue(),
                            false,
                            false,
                            -1,
                            column
                    );

            preferredWidth = Math.max(
                    preferredWidth,
                    headerComponent.getPreferredSize().width
            );
        }

        for (int row = 0; row < getRowCount(); row++) {

            TableCellRenderer cellRenderer =
                    getCellRenderer(row, column);

            Component cellComponent =
                    prepareRenderer(cellRenderer, row, column);

            preferredWidth = Math.max(
                    preferredWidth,
                    cellComponent.getPreferredSize().width
            );
        }

        tableColumn.setPreferredWidth(preferredWidth + margin);
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