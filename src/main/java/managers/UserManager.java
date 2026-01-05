package managers;

import models.*;

public class UserManager {
    private User[] users;
    private int userCount;
    private final int MAX_USERS = 50;

    public UserManager() {
        users = new User[MAX_USERS];
        userCount = 0;
        initSampleUsers();
    }

    private void initSampleUsers() {
        System.out.println("---批量添加一些用户---");
        addUser(new Librarian("L001", "管理员", "EMP001"));
        addUser(new Teacher("T001", "张老师", "TEA001"));
        addUser(new Teacher("T002", "李教授", "TEA002"));
        addUser(new Student("S001", "王小明", "STU001"));
        addUser(new Student("S002", "李小红", "STU002"));
        addUser(new Student("S003", "赵小刚", "STU003"));
    }

    public boolean addUser(User user) {
        if (userCount >= MAX_USERS) {
            System.out.println("错误：用户数量已达上限！");
            return false;
        }

        if (findUserIndexById(user.getId()) != -1) {
            System.out.println("错误：用户ID已存在！");
            return false;
        }

        users[userCount] = user;
        userCount++;
        System.out.println(formatUserInfo(user));
        return true;
    }

    public boolean deleteUser(String userId) {
        int index = findUserIndexById(userId);
        if (index == -1) {
            System.out.println("错误：未找到 ID 为 " + userId + " 的用户");
            return false;
        }

        // 将后面的元素前移
        for (int i = index; i < userCount - 1; i++) {
            users[i] = users[i + 1];
        }

        users[userCount - 1] = null;
        userCount--;
        System.out.println("成功删除用户 ID：" + userId);
        return true;
    }

    private int findUserIndexById(String userId) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].getId().equals(userId)) {
                return i;
            }
        }
        return -1;
    }

    public User findUserById(String userId) {
        int index = findUserIndexById(userId);
        return index != -1 ? users[index] : null;
    }

    public void displayAllUsers() {
        System.out.println("\n=== 所有用户列表 ===");
        for (int i = 0; i < userCount; i++) {
            System.out.println(formatUserInfo(users[i]));
        }
        System.out.println("共 " + userCount + " 个用户");
    }

    private String formatUserInfo(User user) {
        String userType = getUserTypeString(user.getUserType());
        return String.format("ID: %-6s | 姓名: %-10s | 类型: %-8s | 已借数量: %d/%d",
                user.getId(),
                user.getName(),
                userType,
                user.getBorrowedCount(),
                user.getBorrowLimit());
    }

    private String getUserTypeString(String userType) {
        switch (userType) {
            case "STUDENT": return "学生";
            case "TEACHER": return "教师";
            case "LIBRARIAN": return "管理员";
            default: return "未知";
        }
    }

    public void displayStatistics() {
        int studentCount = 0;
        int teacherCount = 0;
        int librarianCount = 0;

        for (int i = 0; i < userCount; i++) {
            String type = users[i].getUserType();
            switch (type) {
                case "STUDENT": studentCount++; break;
                case "TEACHER": teacherCount++; break;
                case "LIBRARIAN": librarianCount++; break;
            }
        }

        System.out.println("\n--- 用户统计信息 ---");
        System.out.println("总用户数量: " + userCount);
        System.out.println("学生用户: " + studentCount);
        System.out.println("教师用户: " + teacherCount);
        System.out.println("管理员: " + librarianCount);
        System.out.println("可用容量: " + (MAX_USERS - userCount));
    }

    public int getUserCount() {
        return userCount;
    }
}