// src/main/java/LibrarySystem.java
import managers.BookManager;
import managers.UserManager;
import menus.Menu;

public class LibrarySystem {
    private BookManager bookManager;
    private UserManager userManager;
    private Menu menu;

    public LibrarySystem() {
        // 初始化管理器
        bookManager = new BookManager();
        userManager = new UserManager();

        // 创建菜单系统
        menu = new Menu(bookManager, userManager);
    }

    public void start() {
        System.out.println("---------------------------------------");
        System.out.println("    简易图书馆管理系统 - 实训 2");
        System.out.println("---------------------------------------");
        System.out.println("系统初始化完成！");
        System.out.println("已加载 " + bookManager.getBookCount() + " 本图书");
        System.out.println("已加载 " + userManager.getUserCount() + " 个用户");
        System.out.println("\n欢迎使用图书馆管理系统！");

        // 显示主菜单
        menu.showMainMenu();
    }

    public static void main(String[] args) {
        LibrarySystem system = new LibrarySystem();
        system.start();
    }
}