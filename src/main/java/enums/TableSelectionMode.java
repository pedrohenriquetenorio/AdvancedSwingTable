package advancedswingtable.enums;

import javax.swing.ListSelectionModel;

public enum TableSelectionMode {

    SINGLE(ListSelectionModel.SINGLE_SELECTION),
    SINGLE_INTERVAL(ListSelectionModel.SINGLE_INTERVAL_SELECTION),
    MULTIPLE_INTERVAL(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    private final int swingMode;

    TableSelectionMode(int swingMode) {
        this.swingMode = swingMode;
    }

    public int getSwingMode() {
        return swingMode;
    }
}