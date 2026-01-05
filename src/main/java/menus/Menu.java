package menus;

import managers.*;
import models.*;
import java.util.Scanner;
import java.util.Date;

public class Menu {
    private Scanner scanner;
    private BookManager bookManager;
    private UserManager userManager;
    private User currentUser;

    public Menu(BookManager bookManager, UserManager userManager) {
        this.scanner = new Scanner(System.in);
        this.bookManager = bookManager;
        this.userManager = userManager;
        this.currentUser = null;
    }

    public void showMainMenu() {
        while (true) {
            System.out.println("\n=== 简易图书馆管理系统 ===");

            if (currentUser == null) {
                // 未登录状态
                System.out.println("1. 用户登录");
                System.out.println("2. 图书浏览");
                System.out.println("0. 退出系统");
            } else {
                // 已登录状态
                System.out.println("当前用户: " + currentUser.getName() +
                        " [" + getUserTypeString(currentUser.getUserType()) + "]");
                System.out.println("==========================");

                if (currentUser instanceof Librarian) {
                    showLibrarianMenu();
                } else {
                    showNormalUserMenu();
                }
            }

            System.out.print("请选择操作: ");
            int choice = getIntInput();

            if (currentUser == null) {
                handleMainMenuChoice(choice);
            } else {
                handleUserMenuChoice(choice);
            }
        }
    }

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

    private String getStringInput() {
        return scanner.nextLine().trim();
    }

    private void showLibrarianMenu() {
        System.out.println("=== 管理员功能 ===");
        System.out.println("1. 图书管理");
        System.out.println("2. 用户管理");
        System.out.println("3. 系统统计");
        System.out.println("0. 登出系统");
    }

    private void showNormalUserMenu() {
        System.out.println("=== 学生/教师功能 ===");
        System.out.println("1. 图书浏览");
        System.out.println("2. 借阅图书");
        System.out.println("3. 归还图书");
        System.out.println("4. 查看我的借阅");
        System.out.println("0. 登出系统");
    }

    private void handleMainMenuChoice(int choice) {
        switch (choice) {
            case 1:
                userLogin();
                break;
            case 2:
                bookManager.displayAllBooks();
                break;
            case 0:
                System.out.println("感谢使用图书馆管理系统，再见！");
                System.exit(0);
                break;
            default:
                System.out.println("无效的选择，请重新输入！");
        }
    }

    private void handleUserMenuChoice(int choice) {
        if (currentUser instanceof Librarian) {
            handleLibrarianChoice(choice);
        } else {
            handleNormalUserChoice(choice);
        }
    }

    private void handleLibrarianChoice(int choice) {
        switch (choice) {
            case 1:
                showBookManagementMenu();
                break;
            case 2:
                showUserManagementMenu();
                break;
            case 3:
                showStatisticsMenu();
                break;
            case 0:
                logout();
                break;
            default:
                System.out.println("无效的选择，请重新输入！");
        }
    }

    private void handleNormalUserChoice(int choice) {
        switch (choice) {
            case 1:
                bookManager.displayAllBooks();
                break;
            case 2:
                borrowBookMenu();
                break;
            case 3:
                returnBookMenu();
                break;
            case 4:
                showMyBorrowedBooks();
                break;
            case 0:
                logout();
                break;
            default:
                System.out.println("无效的选择，请重新输入！");
        }
    }

    // 用户登录功能
    public void userLogin() {
        System.out.println("\n=== 用户登录 ===");
        System.out.print("请输入用户ID: ");
        String userId = getStringInput();

        User user = userManager.findUserById(userId);
        if (user == null) {
            System.out.println("错误：用户ID不存在！");
            return;
        }

        currentUser = user;
        System.out.println("登录成功！欢迎 " + user.getName() +
                " [" + getUserTypeString(user.getUserType()) + "]");
    }

    private void logout() {
        System.out.println("\n用户 " + currentUser.getName() + " 已登出！");
        currentUser = null;
    }

    private void borrowBookMenu() {
        System.out.println("\n=== 借阅图书 ===");

        // 检查借阅限制
        if (currentUser.hasReachedBorrowLimit()) {
            System.out.println("错误：已达到借阅上限！");
            return;
        }

        // 显示可借图书
        Book[] availableBooks = bookManager.getAvailableBooks();
        if (availableBooks.length == 0) {
            System.out.println("当前没有可借阅的图书");
            return;
        }

        System.out.println("=== 可借图书列表 ===");
        for (int i = 0; i < availableBooks.length; i++) {
            System.out.println((i + 1) + ". " + formatBookShortInfo(availableBooks[i]));
        }

        System.out.print("请输入要借阅的图书编号: ");
        int choice = getIntInput();

        if (choice < 1 || choice > availableBooks.length) {
            System.out.println("错误：无效的图书编号！");
            return;
        }

        Book book = availableBooks[choice - 1];

        // 检查是否可以借阅
        interfaces.IBorrowable borrowable = (interfaces.IBorrowable) book;
        if (!borrowable.canBorrow(currentUser)) {
            System.out.println("错误：不能借阅该图书！");
            return;
        }

        // 执行借阅
        if (borrowable.borrow(currentUser, new Date())) {
            currentUser.borrowItem();
            System.out.println("借阅成功！");
        }
    }

    private void returnBookMenu() {
        System.out.println("\n=== 归还图书 ===");

        // 获取用户借阅的图书
        Book[] borrowedBooks = bookManager.getBooksByUser(currentUser.getId());
        if (borrowedBooks.length == 0) {
            System.out.println("您当前没有借阅任何图书");
            return;
        }

        System.out.println("=== 您的借阅列表 ===");
        for (int i = 0; i < borrowedBooks.length; i++) {
            System.out.println((i + 1) + ". " + formatBookShortInfo(borrowedBooks[i]));
        }

        System.out.print("请输入要归还的图书编号: ");
        int choice = getIntInput();

        if (choice < 1 || choice > borrowedBooks.length) {
            System.out.println("错误：无效的图书编号！");
            return;
        }

        Book book = borrowedBooks[choice - 1];

        // 执行归还
        interfaces.IReturnable returnable = (interfaces.IReturnable) book;
        if (returnable.returnItem(new Date())) {
            currentUser.returnItem();
            System.out.println("归还成功！");
        }
    }

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
                case 1:
                    addBookMenu();
                    break;
                case 2:
                    deleteBookMenu();
                    break;
                case 3:
                    bookManager.displayAllBooks();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效的选择！");
            }
        }
    }

    private void addBookMenu() {
        System.out.println("\n=== 添加图书 ===");

        System.out.print("请输入图书ID: ");
        String id = getStringInput();

        System.out.print("请输入图书标题: ");
        String title = getStringInput();

        System.out.print("请输入作者: ");
        String author = getStringInput();

        System.out.print("请输入ISBN: ");
        String isbn = getStringInput();

        System.out.println("请选择图书类型:");
        System.out.println("1. 教材");
        System.out.println("2. 参考书");
        System.out.println("3. 小说");
        System.out.println("4. 期刊");
        System.out.println("5. 通用");

        System.out.print("请选择: ");
        int typeChoice = getIntInput();

        enums.BookCategory category;
        switch (typeChoice) {
            case 1: category = enums.BookCategory.TEXTBOOK; break;
            case 2: category = enums.BookCategory.REFERENCE_BOOK; break;
            case 3: category = enums.BookCategory.FICTION; break;
            case 4: category = enums.BookCategory.PERIODICAL; break;
            case 5: category = enums.BookCategory.GENERAL; break;
            default: category = enums.BookCategory.GENERAL;
        }

        Book book = new Book(id, title, author, isbn, category);
        bookManager.addBook(book);
    }

    private void deleteBookMenu() {
        System.out.println("\n=== 删除图书 ===");
        bookManager.displayAllBooks();

        System.out.print("请输入要删除的图书ID: ");
        String bookId = getStringInput();

        bookManager.deleteBook(bookId);
    }

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
                case 1:
                    addUserMenu();
                    break;
                case 2:
                    deleteUserMenu();
                    break;
                case 3:
                    userManager.displayAllUsers();
                    break;
                case 4:
                    userManager.displayStatistics();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效的选择！");
            }
        }
    }

    private void addUserMenu() {
        System.out.println("\n=== 添加用户 ===");

        System.out.println("请选择用户类型:");
        System.out.println("1. 学生");
        System.out.println("2. 教师");
        System.out.println("3. 管理员");

        System.out.print("请选择: ");
        int typeChoice = getIntInput();

        System.out.print("请输入用户ID: ");
        String id = getStringInput();

        System.out.print("请输入姓名: ");
        String name = getStringInput();

        User user;
        switch (typeChoice) {
            case 1:
                System.out.print("请输入学号: ");
                String studentId = getStringInput();
                user = new Student(id, name, studentId);
                break;
            case 2:
                System.out.print("请输入工号: ");
                String teacherId = getStringInput();
                user = new Teacher(id, name, teacherId);
                break;
            case 3:
                System.out.print("请输入员工号: ");
                String employedd = getStringInput();
                user = new Librarian(id, name, employedd);
                break;
            default:
                System.out.println("无效的用户类型！");
                return;
        }

        userManager.addUser(user);
    }

    private void deleteUserMenu() {
        System.out.println("\n=== 删除用户 ===");
        userManager.displayAllUsers();

        System.out.print("请输入要删除的用户ID: ");
        String userId = getStringInput();

        userManager.deleteUser(userId);
    }

    private void showStatisticsMenu() {
        System.out.println("\n=== 系统统计 ===");
        bookManager.displayStatistics();
        userManager.displayStatistics();
    }

    private String formatBookShortInfo(Book book) {
        return String.format("ID: %-6s | 《%s》 %s",
                book.getId(), book.getTitle(), book.getAuthor());
    }

    private String getUserTypeString(String userType) {
        switch (userType) {
            case "STUDENT": return "学生";
            case "TEACHER": return "教师";
            case "LIBRARIAN": return "管理员";
            default: return "未知";
        }
    }
}