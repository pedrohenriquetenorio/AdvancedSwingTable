/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Teste;

import advancedswingtable.core.AdvancedTableAction;
import advancedswingtable.core.AdvancedTableColumn;
import advancedswingtable.editors.AdvancedActionsEditor;
import advancedswingtable.renderers.AdvancedActionsRenderer;
import com.formdev.flatlaf.FlatLightLaf;

/**
 *
 * @author Pedro
 */
public class Teste extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Teste.class.getName());

    /**
     * Creates new form Teste
     */
    public Teste() {
        initComponents();
        testarTabela();
        
    }
private void testarTabela() {

    AdvancedTableColumn codigo =
            new AdvancedTableColumn(
                    "Código",
                    Long.class
            );

    AdvancedTableColumn nome =
            new AdvancedTableColumn(
                    "Nome",
                    String.class,
                    true
            );

    AdvancedTableColumn preco =
            new AdvancedTableColumn(
                    "Preço",
                    Double.class
            );

    AdvancedTableColumn ativo =
            new AdvancedTableColumn(
                    "Ativo",
                    Boolean.class
            );

    AdvancedTableColumn acoes =
            new AdvancedTableColumn(
                    "Ações",
                    String.class,
                    true
            );


    AdvancedTableAction editar =
            new AdvancedTableAction(
                    "Editar",
                    (table, modelRow) -> {

                        System.out.println("===== EDITAR =====");
                        System.out.println(
                                "Linha modelo: " + modelRow
                        );

                        System.out.println(
                                "Código: "
                                + table.getModel().getValueAt(
                                        modelRow, 0
                                )
                        );

                        System.out.println(
                                "Nome: "
                                + table.getModel().getValueAt(
                                        modelRow, 1
                                )
                        );
                    }
            );


    AdvancedTableAction excluir =
            new AdvancedTableAction(
                    "Excluir",
                    (table, modelRow) -> {

                        System.out.println("===== EXCLUIR =====");
                        System.out.println(
                                "Linha modelo: " + modelRow
                        );

                        System.out.println(
                                "Código: "
                                + table.getModel().getValueAt(
                                        modelRow, 0
                                )
                        );

                        System.out.println(
                                "Nome: "
                                + table.getModel().getValueAt(
                                        modelRow, 1
                                )
                        );
                    }
            );


    AdvancedTableAction visualizar =
            new AdvancedTableAction(
                    "Visualizar",
                    (table, modelRow) -> {

                        System.out.println("===== VISUALIZAR =====");
                        System.out.println(
                                "Linha modelo: " + modelRow
                        );

                        System.out.println(
                                "Código: "
                                + table.getModel().getValueAt(
                                        modelRow, 0
                                )
                        );

                        System.out.println(
                                "Nome: "
                                + table.getModel().getValueAt(
                                        modelRow, 1
                                )
                        );

                        System.out.println(
                                "Preço: "
                                + table.getModel().getValueAt(
                                        modelRow, 2
                                )
                        );

                        System.out.println(
                                "Ativo: "
                                + table.getModel().getValueAt(
                                        modelRow, 3
                                )
                        );
                    }
            );


    acoes.setCustomRenderer(
            new AdvancedActionsRenderer(
                    visualizar,
                    editar,
                    excluir
            )
    );

    acoes.setCustomEditor(
            new AdvancedActionsEditor(
                    visualizar,
                    editar,
                    excluir
            )
    );


    advancedTable1.configureColumns(
            codigo,
            nome,
            preco,
            ativo,
            acoes
    );


    // =========================================================
    // ARRAY DE DADOS
    // =========================================================

    Object[][] dados = {

        {1L, "Produto 1", 10.50, true, ""},
        {2L, "Produto 2", 21.00, false, ""},
        {3L, "Produto 3", 31.50, true, ""},
        {4L, "Produto 4", 42.00, false, ""},
        {5L, "Produto 5", 52.50, true, ""},
        {6L, "Produto 6", 63.00, false, ""},
        {7L, "Produto 7", 73.50, true, ""},
        {8L, "Produto 8", 84.00, false, ""},
        {9L, "Produto 9", 94.50, true, ""},
        {10L, "Produto 10", 105.00, false, ""},

        {11L, "Produto 11", 115.50, true, ""},
        {12L, "Produto 12", 126.00, false, ""},
        {13L, "Produto 13", 136.50, true, ""},
        {14L, "Produto 14", 147.00, false, ""},
        {15L, "Produto 15", 157.50, true, ""},
        {16L, "Produto 16", 168.00, false, ""},
        {17L, "Produto 17", 178.50, true, ""},
        {18L, "Produto 18", 189.00, false, ""},
        {19L, "Produto 19", 199.50, true, ""},
        {20L, "Produto 20", 210.00, false, ""},

        {21L, "Produto 21", 220.50, true, ""},
        {22L, "Produto 22", 231.00, false, ""},
        {23L, "Produto 23", 241.50, true, ""},
        {24L, "Produto 24", 252.00, false, ""},
        {25L, "Produto 25", 262.50, true, ""},
        {26L, "Produto 26", 273.00, false, ""},
        {27L, "Produto 27", 283.50, true, ""},
        {28L, "Produto 28", 294.00, false, ""},
        {29L, "Produto 29", 304.50, true, ""},
        {30L, "Produto 30", 315.00, false, ""}
    };


    // =========================================================
    // PREENCHIMENTO
    // =========================================================

    for (Object[] linha : dados) {
        advancedTable1.addRow(linha);
    }


    // =========================================================
    // PAGINAÇÃO
    // =========================================================

    paginationPanel1.setTable(
            advancedTable1
    );


    // =========================================================
    // TESTE DE INSERÇÃO POSTERIOR
    // =========================================================

    advancedTable1.addRow(
            31L,
            "Produto 31",
            325.50,
            true,
            ""
    );

    advancedTable1.addRow(
            32L,
            "Produto 32",
            336.00,
            false,
            ""
    );


    // =========================================================
    // STATUS
    // =========================================================

    jLabel1.setText(
            paginationPanel1.getAdvPaginationStatus()
    );
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        advancedTable1 = new advancedswingtable.core.AdvancedTable();
        jLabel1 = new javax.swing.JLabel();
        paginationPanel1 = new advancedswingtable.pagination.PaginationPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        advancedTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(advancedTable1);

        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(101, 101, 101))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
                    .addComponent(paginationPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paginationPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
       FlatLightLaf.setup();
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Teste().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private advancedswingtable.core.AdvancedTable advancedTable1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private advancedswingtable.pagination.PaginationPanel paginationPanel1;
    // End of variables declaration//GEN-END:variables
}
