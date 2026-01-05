package models;

import interfaces.IBorrowable;
import interfaces.IReturnable;

import java.util.Date;

/**
 * 图书馆管理员类
 * 继承自User，代表图书馆管理人员，拥有特殊权限和管理功能
 * 管理员可以执行图书管理、罚款处理等特权操作
 */
public class Librarian extends User {

    // 管理员特有属性：入职日期（格式应为"yyyy-MM-dd"）
    // 注意：属性名有拼写错误，应为employedDate
    private String employedd; // 建议重命名为employedDate

    /**
     * 管理员构造方法
     * @param id 管理员系统ID
     * @param name 管理员姓名
     * @param employedd 入职日期（格式示例："2023-01-15"）
     *
     * 说明：管理员借阅上限设为999，象征性表示不受限制
     *       已借数量初始化为0
     */
    public Librarian(String id, String name, String employedd) {
        // 注意：此处直接访问父类protected属性
        // 建议使用super()调用父类构造方法
        this.id = id;              // 系统ID
        this.name = name;          // 管理员姓名
        this.employedd = employedd; // 入职日期
        this.borrowedCount = 0;    // 初始已借数量
        this.maxBorrowLimit = 999; // 管理员特权：极高的借阅上限（实际不受限制）
    }

    // ============ 父类抽象方法实现 ============

    /**
     * 获取用户类型
     * @return 固定返回"LIBRARIAN"字符串，标识管理员用户类型
     */
    @Override
    public String getUserType() {
        return "LIBRARIAN";
    }

    /**
     * 检查管理员是否可以借阅
     * 管理员总是可以借阅（不受常规限制）
     * @return 总是返回true
     */
    @Override
    public boolean canBorrow() {
        return true; // 管理员拥有无限借阅权限
    }

    /**
     * 获取管理员借阅上限
     * @return 管理员的最大借阅数量（999本，象征性数值）
     */
    @Override
    public int getBorrowLimit() {
        return maxBorrowLimit; // 返回999，表示特权
    }

    // ============ 管理员特有管理功能 ============

    /**
     * 添加图书（管理功能）
     * 注意：此方法仅为演示，实际应集成到BookManager中
     * @param book 要添加的图书对象
     */
    public void addBook(Book book) {
        System.out.println("管理员 " + name + " 添加了图书: " + book.getTitle());
        // 实际实现中应调用BookManager.addBook()
    }

    /**
     * 移除图书（管理功能）
     * 注意：此方法仅为演示，实际应集成到BookManager中
     * @param bookId 要移除的图书ID
     */
    public void removeBook(String bookId) {
        System.out.println("管理员 " + name + " 移除了图书ID: " + bookId);
        // 实际实现中应调用BookManager.deleteBook()
    }

    /**
     * 处理用户罚款（核心管理功能）
     * 计算用户归还逾期物品的罚款金额
     * @param user 需要处理罚款的用户
     * @param item 逾期的图书馆物品
     * @return 计算的罚款金额，单位：元
     *
     * 说明：只对实现了IReturnable接口的物品计算罚款
     *       使用当前日期作为计算基准
     */
    public double processFine(User user, LibraryItem item) {
        // 检查物品是否支持罚款计算
        if (item instanceof IReturnable) {
            IReturnable returnable = (IReturnable) item;

            // 使用当前日期计算罚款
            double fine = returnable.calculateFine(new Date());

            System.out.println("处理用户 " + user.getName() + " 的罚款: " + fine + "元");
            return fine;
        }

        // 物品不支持罚款计算
        System.out.println("物品 " + item.getTitle() + " 不支持罚款计算");
        return 0.0;
    }

    // ============ Getter和Setter ============

    /**
     * 获取管理员入职日期
     * @return 入职日期字符串
     */
    public String getEmployedd() {
        return employedd;
    }

    /**
     * 设置管理员入职日期
     * @param employedd 新的入职日期
     */
    public void setEmployedd(String employedd) {
        this.employedd = employedd;
    }


}
