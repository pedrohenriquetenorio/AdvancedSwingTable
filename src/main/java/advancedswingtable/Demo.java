/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package advancedswingtable;

import advancedswingtable.core.AdvancedTableColumn;
import advancedswingtable.editors.ActionPanelEditor;
import java.beans.Beans;
import com.formdev.flatlaf.FlatLightLaf;
import advancedswingtable.pagination.PaginationController;
/**
 *
 * @author Pedro
 */
public class Demo extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Demo.class.getName());
  PaginationController pagination;
     
    /**
     * Creates new form Demo
     */
  public Demo() {

    initComponents();

    if (Beans.isDesignTime()) {
        return;
    }

    paginationPanel1.addPropertyChangeListener(
            "advPaginationStatus",
            evt -> jLabel1.setText(
                    paginationPanel1.getAdvPaginationStatus()
            )
    );

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

        nome.setCustomEditor(
                new advancedswingtable.editors.TestEditor()
        );

        nome.setCustomRenderer(
                new advancedswingtable.renderers.TestRenderer()
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

        ActionPanelEditor actionEditor =
                new ActionPanelEditor();

        actionEditor.setEditAction((table, modelRow) -> {

            System.out.println(
                    "===== EDITAR ====="
            );

            System.out.println(
                    "Linha modelo: "
                    + modelRow
            );

            System.out.println(
                    "Código: "
                    + table.getModel().getValueAt(
                            modelRow,
                            0
                    )
            );

            System.out.println(
                    "Nome: "
                    + table.getModel().getValueAt(
                            modelRow,
                            1
                    )
            );
        });

        actionEditor.setDeleteAction((table, modelRow) -> {

            System.out.println(
                    "===== EXCLUIR ====="
            );

            System.out.println(
                    "Linha modelo: "
                    + modelRow
            );

            System.out.println(
                    "Código: "
                    + table.getModel().getValueAt(
                            modelRow,
                            0
                    )
            );

            System.out.println(
                    "Nome: "
                    + table.getModel().getValueAt(
                            modelRow,
                            1
                    )
            );
        });

        acoes.setCustomRenderer(
                new advancedswingtable.renderers.ActionPanelRenderer()
        );

        acoes.setCustomEditor(actionEditor);

        advancedTable1.configureColumns(
                codigo,
                nome,
                preco,
                ativo,
                acoes
        );

        for (int i = 1; i <= 100; i++) {

            advancedTable1.addRow(
                    (long) i,
                    "Produto " + i,
                    i * 10.50,
                    i % 2 == 0,
                    "Editar"
            );
        }

        paginationPanel1.setTable(
                advancedTable1
        );

        pagination =
                new PaginationController(
                        advancedTable1
                );

        pagination.refresh();

        advancedTable1.addRow(
                101L,
                "Produto 101",
                1060.50,
                true,
                "Editar"
        );

        advancedTable1.addRow(
                102L,
                "Produto 102",
                1071.0,
                false,
                "Editar"
        );

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

        txtFiltro = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        paginationPanel1 = new advancedswingtable.pagination.PaginationPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        advancedTable1 = new advancedswingtable.core.AdvancedTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtFiltro.addActionListener(this::txtFiltroActionPerformed);
        txtFiltro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFiltroKeyReleased(evt);
            }
        });

        jLabel1.setText("jLabel1");

        advancedTable1.setAdvFilterable(true);
        jScrollPane1.setViewportView(advancedTable1);

        jButton1.setText("jButton1");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(paginationPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 888, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(213, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(325, 325, 325))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(paginationPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroActionPerformed
      
    }//GEN-LAST:event_txtFiltroActionPerformed

    private void txtFiltroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroKeyReleased
      advancedTable1.setAdvFilter(txtFiltro.getText());
    }//GEN-LAST:event_txtFiltroKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        advancedTable1.clearRows();
    }//GEN-LAST:event_jButton1ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Demo().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private advancedswingtable.core.AdvancedTable advancedTable1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private advancedswingtable.pagination.PaginationPanel paginationPanel1;
    private javax.swing.JTextField txtFiltro;
    // End of variables declaration//GEN-END:variables
}
