package advancedswingtable.core;

import java.util.function.BiConsumer;
import javax.swing.JTable;

public class TableAction {

    private final String text;
    private final BiConsumer<JTable, Integer> action;

    public TableAction(
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

    public String getText() {
        return text;
    }

    public BiConsumer<JTable, Integer> getAction() {
        return action;
    }

    public void execute(
            JTable table,
            int modelRow) {

        action.accept(
                table,
                modelRow
        );
    }
}