package menus;

import managers.*;
import models.*;
import java.util.Scanner;
import java.util.Date;

/**
 * 菜单系统类
 * 负责处理用户界面交互，根据用户类型显示不同菜单
 * 集成了所有系统功能的用户界面操作
 */
public class Menu {
    // ========== 成员变量 ==========
    private Scanner scanner;          // 输入扫描器，用于接收用户输入
    private BookManager bookManager;  // 图书管理器依赖
    private UserManager userManager;  // 用户管理器依赖
    private User currentUser;         // 当前登录用户（null表示未登录）

    /**
     * 菜单系统构造方法
     * @param bookManager 图书管理器实例
     * @param userManager 用户管理器实例
     */
    public Menu(BookManager bookManager, UserManager userManager) {
        this.scanner = new Scanner(System.in);        // 初始化输入扫描器
        this.bookManager = bookManager;               // 注入图书管理器
        this.userManager = userManager;               // 注入用户管理器
        this.currentUser = null;                      // 初始状态：未登录
    }

    /**
     * 显示主菜单（程序主循环）
     * 根据用户登录状态显示不同的菜单选项
     */
    public void showMainMenu() {
        while (true) {
            System.out.println("\n=== 简易图书馆管理系统 ===");

            if (currentUser == null) {
                // 未登录状态菜单
                System.out.println("1. 用户登录");
                System.out.println("2. 图书浏览");
                System.out.println("0. 退出系统");
            } else {
                // 已登录状态菜单
                System.out.println("当前用户: " + currentUser.getName() +
                        " [" + getUserTypeString(currentUser.getUserType()) + "]");
                System.out.println("==========================");

                // 根据用户类型显示不同菜单
                if (currentUser instanceof Librarian) {
                    showLibrarianMenu();      // 管理员菜单
                } else {
                    showNormalUserMenu();     // 普通用户（学生/教师）菜单
                }
            }

            System.out.print("请选择操作: ");
            int choice = getIntInput();       // 获取用户选择

            // 根据登录状态处理选择
            if (currentUser == null) {
                handleMainMenuChoice(choice);
            } else {
                handleUserMenuChoice(choice);
            }
        }
    }

    /**
     * 获取整数输入（带异常处理）
     * @return 用户输入的整数
     */
    private int getIntInput() {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("请输入有效的数字: ");
            }
        }
    }

    /**
     * 获取字符串输入
     * @return 用户输入的字符串（去除首尾空格）
     */
    private String getStringInput() {
        return scanner.nextLine().trim();
    }

    /**
     * 显示管理员专属菜单
     */
    private void showLibrarianMenu() {
        System.out.println("=== 管理员功能 ===");
        System.out.println("1. 图书管理");   // 图书增删改查
        System.out.println("2. 用户管理");   // 用户增删改查
        System.out.println("3. 系统统计");   // 查看系统统计数据
        System.out.println("0. 登出系统");   // 退出登录
    }

    /**
     * 显示普通用户（学生/教师）菜单
     */
    private void showNormalUserMenu() {
        System.out.println("=== 学生/教师功能 ===");
        System.out.println("1. 图书浏览");   // 查看所有图书
        System.out.println("2. 借阅图书");   // 借阅图书
        System.out.println("3. 归还图书");   // 归还图书
        System.out.println("4. 查看我的借阅"); // 查看个人借阅记录
        System.out.println("0. 登出系统");   // 退出登录
    }

    /**
     * 处理未登录状态的主菜单选择
     * @param choice 用户选择
     */
    private void handleMainMenuChoice(int choice) {
        switch (choice) {
            case 1:  // 用户登录
                userLogin();
                break;
            case 2:  // 图书浏览
                bookManager.displayAllBooks();
                break;
            case 0:  // 退出系统
                System.out.println("感谢使用图书馆管理系统，再见！");
                System.exit(0);
                break;
            default: // 无效选择
                System.out.println("无效的选择，请重新输入！");
        }
    }

    /**
     * 处理已登录状态的菜单选择
     * @param choice 用户选择
     */
    private void handleUserMenuChoice(int choice) {
        // 根据用户类型分发到不同的处理逻辑
        if (currentUser instanceof Librarian) {
            handleLibrarianChoice(choice);   // 管理员操作
        } else {
            handleNormalUserChoice(choice);  // 普通用户操作
        }
    }

    /**
     * 处理管理员菜单选择
     * @param choice 管理员选择
     */
    private void handleLibrarianChoice(int choice) {
        switch (choice) {
            case 1:  // 图书管理
                showBookManagementMenu();
                break;
            case 2:  // 用户管理
                showUserManagementMenu();
                break;
            case 3:  // 系统统计
                showStatisticsMenu();
                break;
            case 0:  // 登出
                logout();
                break;
            default: // 无效选择
                System.out.println("无效的选择，请重新输入！");
        }
    }

    /**
     * 处理普通用户菜单选择
     * @param choice 用户选择
     */
    private void handleNormalUserChoice(int choice) {
        switch (choice) {
            case 1:  // 图书浏览
                bookManager.displayAllBooks();
                break;
            case 2:  // 借阅图书
                borrowBookMenu();
                break;
            case 3:  // 归还图书
                returnBookMenu();
                break;
            case 4:  // 查看我的借阅
                showMyBorrowedBooks();
                break;
            case 0:  // 登出
                logout();
                break;
            default: // 无效选择
                System.out.println("无效的选择，请重新输入！");
        }
    }

    /**
     * 用户登录功能
     */
    public void userLogin() {
        System.out.println("\n=== 用户登录 ===");
        System.out.print("请输入用户ID: ");
        String userId = getStringInput();

        // 查找用户
        User user = userManager.findUserById(userId);
        if (user == null) {
            System.out.println("错误：用户ID不存在！");
            return;
        }

        // 登录成功
        currentUser = user;
        System.out.println("登录成功！欢迎 " + user.getName() +
                " [" + getUserTypeString(user.getUserType()) + "]");
    }

    /**
     * 用户登出功能
     */
    private void logout() {
        System.out.println("\n用户 " + currentUser.getName() + " 已登出！");
        currentUser = null;  // 清除当前用户
    }

    /**
     * 借阅图书菜单
     */
    private void borrowBookMenu() {
        System.out.println("\n=== 借阅图书 ===");

        // 1. 检查借阅限制
        if (currentUser.hasReachedBorrowLimit()) {
            System.out.println("错误：已达到借阅上限！");
            return;
        }

        // 2. 获取可借图书
        Book[] availableBooks = bookManager.getAvailableBooks();
        if (availableBooks.length == 0) {
            System.out.println("当前没有可借阅的图书");
            return;
        }

        // 3. 显示可借图书列表
        System.out.println("=== 可借图书列表 ===");
        for (int i = 0; i < availableBooks.length; i++) {
            System.out.println((i + 1) + ". " + formatBookShortInfo(availableBooks[i]));
        }

        // 4. 用户选择
        System.out.print("请输入要借阅的图书编号: ");
        int choice = getIntInput();

        if (choice < 1 || choice > availableBooks.length) {
            System.out.println("错误：无效的图书编号！");
            return;
        }

        Book book = availableBooks[choice - 1];

        // 5. 检查借阅资格
        interfaces.IBorrowable borrowable = (interfaces.IBorrowable) book;
        if (!borrowable.canBorrow(currentUser)) {
            System.out.println("错误：不能借阅该图书！");
            return;
        }

        // 6. 执行借阅
        if (borrowable.borrow(currentUser, new Date())) {
            currentUser.borrowItem();  // 更新用户借阅计数
            System.out.println("借阅成功！");
        }
    }

    /**
     * 归还图书菜单
     */
    private void returnBookMenu() {
        System.out.println("\n=== 归还图书 ===");

        // 1. 获取用户借阅的图书
        Book[] borrowedBooks = bookManager.getBooksByUser(currentUser.getId());
        if (borrowedBooks.length == 0) {
            System.out.println("您当前没有借阅任何图书");
            return;
        }

        // 2. 显示借阅列表
        System.out.println("=== 您的借阅列表 ===");
        for (int i = 0; i < borrowedBooks.length; i++) {
            System.out.println((i + 1) + ". " + formatBookShortInfo(borrowedBooks[i]));
        }

        // 3. 用户选择
        System.out.print("请输入要归还的图书编号: ");
        int choice = getIntInput();

        if (choice < 1 || choice > borrowedBooks.length) {
            System.out.println("错误：无效的图书编号！");
            return;
        }

        Book book = borrowedBooks[choice - 1];

        // 4. 执行归还
        interfaces.IReturnable returnable = (interfaces.IReturnable) book;
        if (returnable.returnItem(new Date())) {
            currentUser.returnItem();  // 更新用户借阅计数
            System.out.println("归还成功！");
        }
    }

    /**
     * 显示我的借阅记录
     */
    private void showMyBorrowedBooks() {
        System.out.println("\n=== 我的借阅记录 ===");

        Book[] borrowedBooks = bookManager.getBooksByUser(currentUser.getId());
        if (borrowedBooks.length == 0) {
            System.out.println("您当前没有借阅任何图书");
            return;
        }

        for (Book book : borrowedBooks) {
            System.out.println(formatBookShortInfo(book));
        }

        System.out.println("共借阅 " + borrowedBooks.length + " 本图书");
    }

    /**
     * 图书管理子菜单
     */
    private void showBookManagementMenu() {
        while (true) {
            System.out.println("\n=== 图书管理 ===");
            System.out.println("1. 添加图书");
            System.out.println("2. 删除图书");
            System.out.println("3. 显示所有图书");
            System.out.println("0. 返回上级");

            System.out.print("请选择操作: ");
            int choice = getIntInput();

            switch (choice) {
                case 1:  // 添加图书
                    addBookMenu();
                    break;
                case 2:  // 删除图书
                    deleteBookMenu();
                    break;
                case 3:  // 显示所有图书
                    bookManager.displayAllBooks();
                    break;
                case 0:  // 返回上级
                    return;
                default: // 无效选择
                    System.out.println("无效的选择！");
            }
        }
    }

    /**
     * 添加图书菜单
     */
    private void addBookMenu() {
        System.out.println("\n=== 添加图书 ===");

        // 收集图书信息
        System.out.print("请输入图书ID: ");
        String id = getStringInput();

        System.out.print("请输入图书标题: ");
        String title = getStringInput();

        System.out.print("请输入作者: ");
        String author = getStringInput();

        System.out.print("请输入ISBN: ");
        String isbn = getStringInput();

        // 选择图书类型
        System.out.println("请选择图书类型:");
        System.out.println("1. 教材");
        System.out.println("2. 参考书");
        System.out.println("3. 小说");
        System.out.println("4. 期刊");
        System.out.println("5. 通用");

        System.out.print("请选择: ");
        int typeChoice = getIntInput();

        // 映射类型选择到枚举
        enums.BookCategory category;
        switch (typeChoice) {
            case 1: category = enums.BookCategory.TEXTBOOK; break;
            case 2: category = enums.BookCategory.REFERENCE_BOOK; break;
            case 3: category = enums.BookCategory.FICTION; break;
            case 4: category = enums.BookCategory.PERIODICAL; break;
            case 5: category = enums.BookCategory.GENERAL; break;
            default: category = enums.BookCategory.GENERAL;
        }

        // 创建图书对象并添加
        Book book = new Book(id, title, author, isbn, category);
        bookManager.addBook(book);
    }

    /**
     * 删除图书菜单
     */
    private void deleteBookMenu() {
        System.out.println("\n=== 删除图书 ===");
        bookManager.displayAllBooks();  // 先显示所有图书

        System.out.print("请输入要删除的图书ID: ");
        String bookId = getStringInput();

        bookManager.deleteBook(bookId);  // 执行删除
    }

    /**
     * 用户管理子菜单
     */
    private void showUserManagementMenu() {
        while (true) {
            System.out.println("\n=== 用户管理 ===");
            System.out.println("1. 添加用户");
            System.out.println("2. 删除用户");
            System.out.println("3. 显示所有用户");
            System.out.println("4. 用户统计");
            System.out.println("0. 返回上级");

            System.out.print("请选择操作: ");
            int choice = getIntInput();

            switch (choice) {
                case 1:  // 添加用户
                    addUserMenu();
                    break;
                case 2:  // 删除用户
                    deleteUserMenu();
                    break;
                case 3:  // 显示所有用户
                    userManager.displayAllUsers();
                    break;
                case 4:  // 用户统计
                    userManager.displayStatistics();
                    break;
                case 0:  // 返回上级
                    return;
                default: // 无效选择
                    System.out.println("无效的选择！");
            }
        }
    }

    /**
     * 添加用户菜单
     */
    private void addUserMenu() {
        System.out.println("\n=== 添加用户 ===");

        // 选择用户类型
        System.out.println("请选择用户类型:");
        System.out.println("1. 学生");
        System.out.println("2. 教师");
        System.out.println("3. 管理员");

        System.out.print("请选择: ");
        int typeChoice = getIntInput();

        // 收集基本信息
        System.out.print("请输入用户ID: ");
        String id = getStringInput();

        System.out.print("请输入姓名: ");
        String name = getStringInput();

        User user;
        switch (typeChoice) {
            case 1:  // 创建学生用户
                System.out.print("请输入学号: ");
                String studentId = getStringInput();
                user = new Student(id, name, studentId);
                break;
            case 2:  // 创建教师用户
                System.out.print("请输入工号: ");
                String teacherId = getStringInput();
                user = new Teacher(id, name, teacherId);
                break;
            case 3:  // 创建管理员用户
                System.out.print("请输入员工号: ");
                String employedd = getStringInput();
                user = new Librarian(id, name, employedd);
                break;
            default: // 无效类型
                System.out.println("无效的用户类型！");
                return;
        }

        // 添加用户到系统
        userManager.addUser(user);
    }

    /**
     * 删除用户菜单
     */
    private void deleteUserMenu() {
        System.out.println("\n=== 删除用户 ===");
        userManager.displayAllUsers();  // 先显示所有用户

        System.out.print("请输入要删除的用户ID: ");
        String userId = getStringInput();

        userManager.deleteUser(userId);  // 执行删除
    }

    /**
     * 显示系统统计菜单
     */
    private void showStatisticsMenu() {
        System.out.println("\n=== 系统统计 ===");
        bookManager.displayStatistics();  // 图书统计
        userManager.displayStatistics();  // 用户统计
    }

    /**
     * 格式化图书简短信息
     * @param book 图书对象
     * @return 格式化后的图书信息字符串
     */
    private String formatBookShortInfo(Book book) {
        return String.format("ID: %-6s | 《%s》 %s",
                book.getId(), book.getTitle(), book.getAuthor());
    }

    /**
     * 用户类型代码转中文
     * @param userType 用户类型代码
     * @return 中文用户类型
     */
    private String getUserTypeString(String userType) {
        switch (userType) {
            case "STUDENT": return "学生";
            case "TEACHER": return "教师";
            case "LIBRARIAN": return "管理员";
            default: return "未知";
        }
    }
}