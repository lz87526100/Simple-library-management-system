package models;

/**
 * 用户抽象基类
 * 表示图书馆系统的用户，定义了用户的基本属性和行为
 * 具体用户类型（如学生、教师）应继承此类并实现抽象方法
 */
public abstract class User {
    // ========== 成员变量 ==========
    protected String id;             // 用户唯一标识符
    protected String name;           // 用户姓名
    protected int borrowedCount;     // 当前已借阅物品数量
    protected int maxBorrowLimit;    // 最大借阅数量限制

    // ========== 抽象方法 ==========

    /**
     * 获取用户类型
     * @return 用户类型字符串（如："Student"、"Teacher"）
     */
    public abstract String getUserType();

    /**
     * 检查用户是否满足借阅条件
     * @return true-可以借阅, false-不能借阅
     */
    public abstract boolean canBorrow();

    /**
     * 获取用户的最大借阅限制
     * @return 最大借阅数量
     */
    public abstract int getBorrowLimit();

    // ========== 具体方法 ==========

    /**
     * 获取用户完整信息
     * @return 格式化的用户信息字符串
     */
    public String getInfo() {
        return String.format("用户: %s (ID: %s, 类型: %s)",
                name, id, getUserType());
    }

    /**
     * 检查用户是否已达到借阅上限
     * @return true-已达到上限, false-未达到上限
     */
    public boolean hasReachedBorrowLimit() {
        return borrowedCount >= getBorrowLimit();
    }

    /**
     * 用户借阅物品
     * 每次借阅成功时增加借阅计数
     * @return true-借阅成功, false-借阅失败（已达上限）
     */
    public boolean borrowItem() {
        if (hasReachedBorrowLimit()) {
            return false; // 借阅失败：已达上限
        }
        borrowedCount++;
        return true; // 借阅成功
    }

    /**
     * 用户归还物品
     * 每次归还时减少借阅计数（确保计数不为负）
     */
    public void returnItem() {
        if (borrowedCount > 0) {
            borrowedCount--;
        }
    }

    // ========== Getter 和 Setter 方法 ==========

    /**
     * 获取用户ID
     * @return 用户ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置用户ID
     * @param id 新的用户ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取用户姓名
     * @return 用户姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置用户姓名
     * @param name 新的用户姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取当前已借阅数量
     * @return 已借阅物品数量
     */
    public int getBorrowedCount() {
        return borrowedCount;
    }

    /**
     * 设置当前已借阅数量
     * @param borrowedCount 新的已借阅数量
     */
    public void setBorrowedCount(int borrowedCount) {
        this.borrowedCount = borrowedCount;
    }

    /**
     * 获取最大借阅限制
     * @return 最大借阅数量限制
     */
    public int getMaxBorrowLimit() {
        return maxBorrowLimit;
    }

    /**
     * 设置最大借阅限制
     * @param maxBorrowLimit 新的最大借阅限制
     */
    public void setMaxBorrowLimit(int maxBorrowLimit) {
        this.maxBorrowLimit = maxBorrowLimit;
    }
}