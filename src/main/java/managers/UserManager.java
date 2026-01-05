package managers;

import models.*;

/**
 * 用户管理器类
 * 负责管理图书馆系统中的所有用户，包括用户的增删改查和统计功能
 * 使用数组存储用户数据，支持最多50个用户
 */
public class UserManager {
    // ========== 成员变量 ==========
    private User[] users;           // 用户数组，用于存储所有用户对象
    private int userCount;          // 当前用户数量
    private final int MAX_USERS = 50; // 最大用户容量，常量

    /**
     * 用户管理器构造方法
     * 初始化用户数组，创建示例用户数据
     */
    public UserManager() {
        users = new User[MAX_USERS]; // 创建容量为MAX_USERS的用户数组
        userCount = 0;               // 初始用户数量为0
        initSampleUsers();           // 初始化示例用户数据
    }

    /**
     * 初始化示例用户数据（私有方法）
     * 系统启动时自动创建一些默认用户，便于测试和演示
     */
    private void initSampleUsers() {
        System.out.println("---批量添加一些用户---");
        // 添加管理员用户
        addUser(new Librarian("L001", "管理员", "EMP001"));
        // 添加教师用户
        addUser(new Teacher("T001", "张老师", "TEA001"));
        addUser(new Teacher("T002", "李教授", "TEA002"));
        // 添加学生用户
        addUser(new Student("S001", "王小明", "STU001"));
        addUser(new Student("S002", "李小红", "STU002"));
        addUser(new Student("S003", "赵小刚", "STU003"));
    }

    /**
     * 添加新用户
     * @param user 要添加的用户对象
     * @return true-添加成功, false-添加失败
     */
    public boolean addUser(User user) {
        // 1. 检查容量限制
        if (userCount >= MAX_USERS) {
            System.out.println("错误：用户数量已达上限！");
            return false;
        }

        // 2. 检查用户ID是否已存在
        if (findUserIndexById(user.getId()) != -1) {
            System.out.println("错误：用户ID已存在！");
            return false;
        }

        // 3. 添加用户到数组
        users[userCount] = user;
        userCount++;

        // 4. 显示添加成功的用户信息
        System.out.println(formatUserInfo(user));
        return true;
    }

    /**
     * 删除指定ID的用户
     * @param userId 要删除的用户ID
     * @return true-删除成功, false-删除失败（未找到用户）
     */
    public boolean deleteUser(String userId) {
        // 1. 查找用户索引
        int index = findUserIndexById(userId);
        if (index == -1) {
            System.out.println("错误：未找到 ID 为 " + userId + " 的用户");
            return false;
        }

        // 2. 删除用户：将后面的元素前移
        for (int i = index; i < userCount - 1; i++) {
            users[i] = users[i + 1];
        }

        // 3. 清理最后一个元素并减少计数
        users[userCount - 1] = null;
        userCount--;

        System.out.println("成功删除用户 ID：" + userId);
        return true;
    }

    /**
     * 根据用户ID查找用户在数组中的索引（私有方法）
     * @param userId 要查找的用户ID
     * @return 用户索引，-1表示未找到
     */
    private int findUserIndexById(String userId) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].getId().equals(userId)) {
                return i;
            }
        }
        return -1; // 未找到
    }

    /**
     * 根据用户ID查找用户对象
     * @param userId 要查找的用户ID
     * @return 用户对象，null表示未找到
     */
    public User findUserById(String userId) {
        int index = findUserIndexById(userId);
        return index != -1 ? users[index] : null;
    }

    /**
     * 显示所有用户信息
     * 以表格形式展示所有用户的详细信息
     */
    public void displayAllUsers() {
        System.out.println("\n=== 所有用户列表 ===");
        for (int i = 0; i < userCount; i++) {
            System.out.println(formatUserInfo(users[i]));
        }
        System.out.println("共 " + userCount + " 个用户");
    }

    /**
     * 格式化用户信息为字符串（私有方法）
     * @param user 要格式化的用户对象
     * @return 格式化后的用户信息字符串
     */
    private String formatUserInfo(User user) {
        String userType = getUserTypeString(user.getUserType());
        return String.format("ID: %-6s | 姓名: %-10s | 类型: %-8s | 已借数量: %d/%d",
                user.getId(),
                user.getName(),
                userType,
                user.getBorrowedCount(),
                user.getBorrowLimit());
    }

    /**
     * 将用户类型代码转换为中文描述（私有方法）
     * @param userType 用户类型代码（如："STUDENT"）
     * @return 中文类型描述
     */
    private String getUserTypeString(String userType) {
        switch (userType) {
            case "STUDENT": return "学生";
            case "TEACHER": return "教师";
            case "LIBRARIAN": return "管理员";
            default: return "未知";
        }
    }

    /**
     * 显示用户统计信息
     * 统计各类用户数量，显示系统容量信息
     */
    public void displayStatistics() {
        int studentCount = 0;
        int teacherCount = 0;
        int librarianCount = 0;

        // 统计各类用户数量
        for (int i = 0; i < userCount; i++) {
            String type = users[i].getUserType();
            switch (type) {
                case "STUDENT": studentCount++; break;
                case "TEACHER": teacherCount++; break;
                case "LIBRARIAN": librarianCount++; break;
            }
        }

        // 输出统计信息
        System.out.println("\n--- 用户统计信息 ---");
        System.out.println("总用户数量: " + userCount);
        System.out.println("学生用户: " + studentCount);
        System.out.println("教师用户: " + teacherCount);
        System.out.println("管理员: " + librarianCount);
        System.out.println("可用容量: " + (MAX_USERS - userCount));
    }

    /**
     * 获取当前用户数量
     * @return 用户数量
     */
    public int getUserCount() {
        return userCount;
    }
}