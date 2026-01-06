package managers;

import models.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 增强版用户管理器 - 使用集合框架
 */
public class UserManagerEnhanced {
    // 主数据源：使用ArrayList存储所有用户
    private final List<User> userList;

    // 索引：使用HashMap实现ID到User对象的快速查找
    private final Map<String, User> userIdIndex;

    // 可选：泛型容器
    private GenericContainer<User> userContainer;

    // 统计信息
    private int totalOperations;

    /**
     * 构造方法
     */
    public UserManagerEnhanced() {
        userList = new ArrayList<>();
        userIdIndex = new HashMap<>();
        userContainer = new GenericContainer<>("用户容器");
        totalOperations = 0;

        // 初始化示例数据
        initSampleUsers();
    }

    /**
     * 初始化示例用户
     */
    private void initSampleUsers() {
        System.out.println("--- 增强版用户管理器初始化 ---");
        addUser(new Librarian("L001", "管理员", "EMP001"));
        addUser(new Teacher("T001", "张老师", "TEA001"));
        addUser(new Teacher("T002", "李教授", "TEA002"));
        addUser(new Student("S001", "王小明", "STU001"));
        addUser(new Student("S002", "李小红", "STU002"));
        addUser(new Student("S003", "赵小刚", "STU003"));
    }

    /**
     * 添加用户
     * @param user 用户对象
     * @return 添加是否成功
     */
    public boolean addUser(User user) {
        // 检查ID是否已存在
        if (userIdIndex.containsKey(user.getId())) {
            System.out.println("添加失败：用户ID " + user.getId() + " 已存在！");
            return false;
        }

        // 添加到主列表
        userList.add(user);

        // 更新索引
        userIdIndex.put(user.getId(), user);

        // 可选：添加到泛型容器
        userContainer.add(user);

        totalOperations++;
        System.out.println("成功添加用户：" + user.getName() + " (" + user.getUserType() + ")");
        return true;
    }

    /**
     * 根据ID查找用户（O(1)时间复杂度）
     * @param userId 用户ID
     * @return 用户对象，未找到返回null
     */
    public User findUserById(String userId) {
        totalOperations++;
        return userIdIndex.get(userId);
    }

    /**
     * 删除用户
     * @param userId 用户ID
     * @return 删除是否成功
     */
    public boolean deleteUser(String userId) {
        User user = userIdIndex.get(userId);
        if (user == null) {
            System.out.println("删除失败：未找到ID为 " + userId + " 的用户");
            return false;
        }

        // 从主列表删除
        userList.remove(user);

        // 从索引删除
        userIdIndex.remove(userId);

        // 可选：从泛型容器删除
        userContainer.remove(user);

        totalOperations++;
        System.out.println("成功删除用户：" + user.getName());
        return true;
    }

    /**
     * 显示所有用户
     */
    public void displayAllUsers() {
        displayUsers(userList, "所有用户列表");
    }

    /**
     * 通用显示用户方法
     * @param users 用户列表
     * @param title 显示标题
     */
    private void displayUsers(List<User> users, String title) {
        if (users.isEmpty()) {
            System.out.println("\n" + title + "：当前没有用户");
            return;
        }

        System.out.println("\n=== " + title + " ===");
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            System.out.println((i + 1) + ". " + formatUserInfo(user));
        }
        System.out.println("总计: " + users.size() + " 个用户");
    }

    /**
     * 格式化用户信息
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
     * 转换用户类型为中文
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
     * 显示统计信息
     */
    public void displayStatistics() {
        // 使用流API统计各类用户数量
        long studentCount = userList.stream()
                .filter(user -> "STUDENT".equals(user.getUserType()))
                .count();
        long teacherCount = userList.stream()
                .filter(user -> "TEACHER".equals(user.getUserType()))
                .count();
        long librarianCount = userList.stream()
                .filter(user -> "LIBRARIAN".equals(user.getUserType()))
                .count();

        System.out.println("\n=== 用户统计信息 ===");
        System.out.println("总用户数量: " + userList.size());
        System.out.println("学生用户: " + studentCount);
        System.out.println("教师用户: " + teacherCount);
        System.out.println("管理员: " + librarianCount);
        System.out.println("总操作次数: " + totalOperations);
        System.out.println("泛型容器中的用户数量: " + userContainer.size());
    }

    /**
     * 演示TreeSet排序功能（可选功能）
     */
    public void demonstrateTreeSet() {
        System.out.println("\n=== TreeSet 用户排序演示 ===");

        // 创建TreeSet，按姓名排序
        Set<User> treeSetByName = new TreeSet<>(Comparator.comparing(User::getName));
        treeSetByName.addAll(userList);

        System.out.println("TreeSet 按姓名排序（自动去重，基于姓名）：");
        int count = 1;
        for (User user : treeSetByName) {
            System.out.println(count++ + ". " + user.getName() + " (" + user.getUserType() + ")");
        }

        // 按用户ID排序
        Set<User> treeSetById = new TreeSet<>(Comparator.comparing(User::getId));
        treeSetById.addAll(userList);

        System.out.println("\nTreeSet 按ID排序：");
        count = 1;
        for (User user : treeSetById) {
            System.out.println(count++ + ". " + user.getId() + " - " + user.getName());
        }
    }

    /**
     * 搜索用户
     * @param keyword 关键词（可以是姓名的一部分）
     * @return 匹配的用户列表
     */
    public List<User> searchUsers(String keyword) {
        return userList.stream()
                .filter(user -> user.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * 获取用户数量
     * @return 用户总数
     */
    public int getUserCount() {
        return userList.size();
    }
}