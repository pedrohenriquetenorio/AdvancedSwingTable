package advancedswingtable.pagination;

import advancedswingtable.core.AdvancedTable;
import advancedswingtable.model.PaginationModel;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PaginationPanel extends JPanel {

    private AdvancedTable table;
    private PaginationController controller;

    private final JButton btnFirst = new JButton("<<");
    private final JButton btnPrevious = new JButton("<");

    private final JLabel lblPage = new JLabel("Página");
    private final JTextField txtPage = new JTextField("1", 3);
    private final JLabel lblTotalPages = new JLabel("de 1");

    private final JButton btnNext = new JButton(">");
    private final JButton btnLast = new JButton(">>");

    private final JLabel lblPageSize = new JLabel("Itens:");
    private final JComboBox<Integer> cbPageSize =
            new JComboBox<>(new Integer[]{10, 20, 50, 100});

    public PaginationPanel() {

        add(btnFirst);
        add(btnPrevious);
        add(lblPage);
        add(txtPage);
        add(lblTotalPages);
        add(btnNext);
        add(btnLast);
        add(lblPageSize);
        add(cbPageSize);

        btnFirst.addActionListener(e -> {

            if (controller == null) {
                return;
            }

            controller.firstPage();
            updatePageControls();
        });

        btnPrevious.addActionListener(e -> {

            if (controller == null) {
                return;
            }

            controller.previousPage();
            updatePageControls();
        });

        btnNext.addActionListener(e -> {

            if (controller == null) {
                return;
            }

            controller.nextPage();
            updatePageControls();
        });

        btnLast.addActionListener(e -> {

            if (controller == null) {
                return;
            }

            controller.lastPage();
            updatePageControls();
        });

        txtPage.addActionListener(e -> goToTypedPage());

        cbPageSize.addActionListener(e -> {

            if (table == null || controller == null) {
                return;
            }

            Integer pageSize =
                    (Integer) cbPageSize.getSelectedItem();

            if (pageSize == null) {
                return;
            }

            table.setAdvPageSize(pageSize);
        });
    }

    public AdvancedTable getTable() {
        return table;
    }

    public void setTable(AdvancedTable table) {

        if (this.controller != null) {
            // Evita vazar o listener do model antigo e garante que
            // só o controller atual controle o filtro da tabela.
            this.controller.dispose();
        }

        this.table = table;

        this.controller =
                new PaginationController(table);

        Integer currentPageSize =
                table.getAdvPageSize();

        cbPageSize.setSelectedItem(
                currentPageSize
        );

        controller.refresh();

        updatePageControls();
    }

    private void goToTypedPage() {

        if (controller == null) {
            return;
        }

        try {

            int page =
                    Integer.parseInt(
                            txtPage.getText().trim()
                    );

            controller.goToPage(page);

        } catch (NumberFormatException ex) {
        }

        updatePageControls();
    }

    private void updatePageControls() {

        if (controller == null) {
            return;
        }

        PaginationModel pagination =
                controller.getPagination();

        int currentPage =
                pagination.getCurrentPage();

        int totalPages =
                pagination.getTotalPages();

        txtPage.setText(
                String.valueOf(currentPage)
        );

        lblTotalPages.setText(
                "de " + totalPages
        );

        btnFirst.setEnabled(
                pagination.hasPreviousPage()
        );

        btnPrevious.setEnabled(
                pagination.hasPreviousPage()
        );

        btnNext.setEnabled(
                pagination.hasNextPage()
        );

        btnLast.setEnabled(
                pagination.hasNextPage()
        );

        firePropertyChange(
                "advPaginationStatus",
                null,
                getAdvPaginationStatus()
        );
    }

    public String getAdvPaginationStatus() {

        if (controller == null) {
            return "Exibindo 0–0 de 0 registros";
        }

        PaginationModel pagination =
                controller.getPagination();

        int totalItems =
                pagination.getTotalItems();

        if (totalItems == 0) {
            return "Exibindo 0–0 de 0 registros";
        }

        int start =
                pagination.getStartIndex() + 1;

        int end =
                pagination.getEndIndex();

        return "Exibindo "
                + start
                + "–"
                + end
                + " de "
                + totalItems
                + " registros";
    }
}