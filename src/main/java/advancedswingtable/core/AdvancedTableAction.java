package advancedswingtable.core;

import java.util.function.BiConsumer;
import java.util.function.IntPredicate;
import javax.swing.Icon;
import javax.swing.JTable;

/**
 * Representa uma ação de botão dentro de uma coluna de ações da
 * AdvancedTable (ex: Editar, Excluir, Visualizar...). É apenas "o
 * que este botão faz quando clicado nesta linha" — a renderização
 * fica por conta de AdvancedActionsRenderer/AdvancedActionsEditor.
 *
 * Uso básico:
 *   new AdvancedTableAction("Editar", (table, modelRow) -> ...)
 *
 * Com ícone e/ou habilitação condicional por linha:
 *   new AdvancedTableAction("Excluir", (table, modelRow) -> ...)
 *       .withIcon(iconeLixeira)
 *       .enabledWhen(modelRow -> !estaCancelado(modelRow))
 */
public class AdvancedTableAction {

    private final String text;
    private final BiConsumer<JTable, Integer> action;

    private Icon icon;
    private IntPredicate enabledWhen;

    public AdvancedTableAction(
            String text,
            BiConsumer<JTable, Integer> action) {

        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(
                    "O texto da ação não pode ser vazio."
            );
        }

        if (action == null) {
            throw new IllegalArgumentException(
                    "A ação não pode ser nula."
            );
        }

        this.text = text;
        this.action = action;
    }

    public AdvancedTableAction withIcon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public AdvancedTableAction enabledWhen(IntPredicate enabledWhen) {
        this.enabledWhen = enabledWhen;
        return this;
    }

    public String getText() {
        return text;
    }

    public Icon getIcon() {
        return icon;
    }

    public BiConsumer<JTable, Integer> getAction() {
        return action;
    }

    public boolean isEnabledFor(int modelRow) {
        return enabledWhen == null || enabledWhen.test(modelRow);
    }

    /**
     * Executa a ação se ela estiver habilitada para a linha. Retorna
     * true se a ação foi de fato executada, false se foi ignorada
     * por estar desabilitada — o editor usa esse retorno pra só
     * encerrar a edição da célula quando algo realmente aconteceu.
     */
    public boolean execute(
            JTable table,
            int modelRow) {

        if (!isEnabledFor(modelRow)) {
            return false;
        }

        action.accept(
                table,
                modelRow
        );

        return true;
    }
}