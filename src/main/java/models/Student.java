package models;

/**
 * 学生类
 * 继承自User抽象类，表示图书馆系统中的学生用户
 * 学生有特定的借阅规则和限制
 */
public class Student extends User {
    // ========== 特有属性 ==========
    private String studentId;  // 学生学号，用于唯一标识学生身份

    /**
     * 学生用户构造方法
     * @param id 用户在系统中的唯一ID（如："S001"）
     * @param name 学生姓名
     * @param studentId 学生学号（如："20240001"）
     */
    public Student(String id, String name, String studentId) {
        this.id = id;                       // 设置用户ID
        this.name = name;                   // 设置姓名
        this.studentId = studentId;         // 设置学号
        this.maxBorrowLimit = 5;            // 学生最大借阅限制：5本
        this.borrowedCount = 0;             // 初始已借阅数量：0本
    }

    // ========== 实现User抽象方法 ==========

    /**
     * 获取用户类型标识
     * 用于区分不同类型的用户（如学生、教师）
     * @return "STUDENT" - 表示学生用户类型
     */
    @Override
    public String getUserType() {
        return "STUDENT";
    }

    /**
     * 检查学生是否满足借阅条件
     * 学生可以借阅的条件：当前借阅数量未达到上限
     * @return true-可以借阅, false-不能借阅（已达上限）
     */
    @Override
    public boolean canBorrow() {
        return borrowedCount < maxBorrowLimit;
    }

    /**
     * 获取学生的最大借阅限制
     * @return 最大借阅数量（5本）
     */
    @Override
    public int getBorrowLimit() {
        return maxBorrowLimit;
    }

    // ========== Getter和Setter方法 ==========

    /**
     * 获取学生学号
     * @return 学生学号
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * 设置学生学号
     * @param studentId 新的学生学号
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
