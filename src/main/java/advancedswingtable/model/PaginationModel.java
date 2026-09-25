package advancedswingtable.model;

public class PaginationModel {

    private int currentPage = 1;
    private int pageSize = 10;
    private int totalItems = 0;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {

        if (currentPage < 1) {
            currentPage = 1;
        }

        if (currentPage > getTotalPages()) {
            currentPage = getTotalPages();
        }

        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {

        if (pageSize < 1) {
            pageSize = 1;
        }

        this.pageSize = pageSize;

        if (currentPage > getTotalPages()) {
            currentPage = getTotalPages();
        }
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {

        if (totalItems < 0) {
            totalItems = 0;
        }

        this.totalItems = totalItems;

        if (currentPage > getTotalPages()) {
            currentPage = getTotalPages();
        }
    }

    public int getTotalPages() {

        if (totalItems == 0) {
            return 1;
        }

        return (int) Math.ceil(
                (double) totalItems / pageSize
        );
    }

    public boolean hasPreviousPage() {
        return currentPage > 1;
    }

    public boolean hasNextPage() {
        return currentPage < getTotalPages();
    }

    public void nextPage() {

        if (hasNextPage()) {
            currentPage++;
        }
    }

    public void previousPage() {

        if (hasPreviousPage()) {
            currentPage--;
        }
    }

    public int getStartIndex() {
        return (currentPage - 1) * pageSize;
    }

    public int getEndIndex() {
        return Math.min(
                getStartIndex() + pageSize,
                totalItems
        );
    }
}