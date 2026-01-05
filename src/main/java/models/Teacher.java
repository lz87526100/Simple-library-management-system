package models;

/**
 * 教师用户类
 * 继承自User抽象类，代表图书馆的教师用户类型
 * 教师用户享有比学生更高的借阅权限
 */
public class Teacher extends User {

    // 教师特有属性：教师工号（区别于用户ID）
    private String teacherId;

    /**
     * 教师用户构造方法
     * @param id 用户唯一标识符（系统ID）
     * @param name 教师姓名
     * @param teacherId 教师工号（如T2023001）
     *
     * 说明：教师用户拥有较高的借阅上限（20本）
     *       新教师用户的已借数量初始化为0
     */
    public Teacher(String id, String name, String teacherId) {
        // 注意：此处直接访问了父类的protected属性
        // 更好的实践是通过super()调用父类构造方法
        this.id = id;                  // 用户系统ID
        this.name = name;              // 教师姓名
        this.teacherId = teacherId;    // 教师工号
        this.maxBorrowLimit = 20;      // 教师可借20本书
        this.borrowedCount = 0;        // 初始已借数量为0
    }

    // ============ 父类抽象方法实现 ============

    /**
     * 获取用户类型
     * @return 固定返回"TEACHER"字符串，标识教师用户类型
     */
    @Override
    public String getUserType() {
        return "TEACHER";
    }

    /**
     * 检查教师是否可以继续借阅
     * 判断条件：已借数量是否小于最大借阅限制
     * @return true-可以继续借阅，false-已达借阅上限
     */
    @Override
    public boolean canBorrow() {
        return borrowedCount < maxBorrowLimit;
    }

    /**
     * 获取教师的借阅上限
     * @return 最大借阅数量（教师为20本）
     */
    @Override
    public int getBorrowLimit() {
        return maxBorrowLimit;
    }

    // ============ 教师特有方法 ============

    /**
     * 获取教师工号
     * @return 教师工号
     */
    public String getTeacherId() {
        return teacherId;
    }

    /**
     * 设置教师工号
     * @param teacherId 新的教师工号
     */
    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }


}