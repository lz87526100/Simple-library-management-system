package models;

public abstract class User {
    protected String id;
    protected String name;
    protected int borrowedCount;
    protected int maxBorrowLimit;

    // 抽象方法
    public abstract String getUserType();
    public abstract boolean canBorrow();
    public abstract int getBorrowLimit();

    // 具体方法
    public String getInfo() {
        return String.format("用户: %s (ID: %s, 类型: %s)",
                name, id, getUserType());
    }

    public boolean hasReachedBorrowLimit() {
        return borrowedCount >= getBorrowLimit();
    }

    public boolean borrowItem() {
        if (hasReachedBorrowLimit()) return false;
        borrowedCount++;
        return true;
    }

    public void returnItem() {
        if (borrowedCount > 0) borrowedCount--;
    }

    // Getter 和 Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBorrowedCount() {
        return borrowedCount;
    }

    public void setBorrowedCount(int borrowedCount) {
        this.borrowedCount = borrowedCount;
    }

    public int getMaxBorrowLimit() {
        return maxBorrowLimit;
    }

    public void setMaxBorrowLimit(int maxBorrowLimit) {
        this.maxBorrowLimit = maxBorrowLimit;
    }
}