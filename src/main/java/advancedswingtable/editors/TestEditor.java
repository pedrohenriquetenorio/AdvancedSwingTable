/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package advancedswingtable.editors;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
/**
 *
 * @author Pedro
 */
public class TestEditor extends DefaultCellEditor {

    public TestEditor() {
        super(new JTextField());
    }
}