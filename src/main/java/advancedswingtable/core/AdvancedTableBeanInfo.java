package advancedswingtable.core;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

public class AdvancedTableBeanInfo extends java.beans.SimpleBeanInfo {

    private final PropertyDescriptor[] properties;

    public AdvancedTableBeanInfo() {

        try {

            properties = new PropertyDescriptor[]{
                new PropertyDescriptor(
                        "advTableRowHeight",
                        AdvancedTable.class
                ),
                new PropertyDescriptor(
                        "advShowHeader",
                        AdvancedTable.class
                ),
                new PropertyDescriptor(
                        "advStripedRows",
                        AdvancedTable.class
                ),
                new PropertyDescriptor(
                        "advRowHoverEnabled",
                        AdvancedTable.class
                ),
                new PropertyDescriptor(
                        "advAutoResizeColumns",
                        AdvancedTable.class
                ),
                new PropertyDescriptor(
                        "advRowSelectable",
                        AdvancedTable.class
                ),
                new PropertyDescriptor(
                        "advSelectionModeType",
                        AdvancedTable.class
                ),
                new PropertyDescriptor(
                        "advShowGridLines",
                        AdvancedTable.class
                ),
                new PropertyDescriptor(
                        "advResizableColumns",
                        AdvancedTable.class
                ),
                new PropertyDescriptor(
                        "advReorderableColumns",
                        AdvancedTable.class
                ),
                new PropertyDescriptor(
                        "advSortable",
                        AdvancedTable.class
                ),
                new PropertyDescriptor(
                        "advShowSelection",
                        AdvancedTable.class
                ),
                new PropertyDescriptor(
                        "advFilterable",
                        AdvancedTable.class
                ),
                new PropertyDescriptor(
                        "advFilter",
                        AdvancedTable.class
                ),
                new PropertyDescriptor(
                        "advPageSize",
                        AdvancedTable.class
                ),
                new PropertyDescriptor(
                        "advEmptyText",
                        AdvancedTable.class
                )
            };

        } catch (IntrospectionException ex) {
            throw new RuntimeException(
                    "Erro ao criar propriedades do AdvancedTable.",
                    ex
            );
        }
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(
                AdvancedTable.class
        );
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return properties;
    }

    /**
     * Sem isto, o Introspector usa SOMENTE as propriedades "adv*"
     * acima e esconde as propriedades nativas do JTable/JComponent
     * (font, background, rowHeight, etc.) na aba de propriedades do
     * NetBeans. Ao mesclar o BeanInfo padrão da superclasse aqui, as
     * duas listas aparecem juntas no editor de propriedades.
     */
    @Override
    public BeanInfo[] getAdditionalBeanInfo() {

        try {

            return new BeanInfo[]{
                Introspector.getBeanInfo(
                        AdvancedTable.class.getSuperclass()
                )
            };

        } catch (IntrospectionException ex) {
            return new BeanInfo[0];
        }
    }
}