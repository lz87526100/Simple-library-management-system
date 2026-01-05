// src/main/java/LibrarySystem.java

import managers.BookManager;
import managers.UserManager;
import menus.Menu;

/**
 * 图书馆系统主类
 * 这是整个图书馆管理系统的入口点和核心控制器
 * 负责初始化系统组件并启动应用程序
 */
public class LibrarySystem {
    // ========== 系统核心组件 ==========
    private BookManager bookManager; // 图书管理器：负责所有图书相关的操作
    private UserManager userManager; // 用户管理器：负责所有用户相关的操作
    private Menu menu;               // 菜单系统：负责用户交互界面

    /**
     * 图书馆系统构造方法
     * 在创建LibrarySystem对象时初始化所有系统组件
     */
    public LibrarySystem() {
        // 初始化管理器（组合关系：LibrarySystem拥有这些组件）
        bookManager = new BookManager(); // 创建图书管理器实例
        userManager = new UserManager(); // 创建用户管理器实例

        // 创建菜单系统，并注入管理器依赖（依赖注入模式）
        menu = new Menu(bookManager, userManager);
    }

    /**
     * 启动图书馆管理系统
     * 这是系统的启动入口，执行以下流程：
     * 1. 显示欢迎界面和系统信息
     * 2. 显示主菜单并开始用户交互
     */
    public void start() {
        // 1. 显示系统标题和分隔线
        System.out.println("---------------------------------------");
        System.out.println("    简易图书馆管理系统 - 实训 2");
        System.out.println("---------------------------------------");

        // 2. 显示系统初始化信息
        System.out.println("系统初始化完成！");
        System.out.println("已加载 " + bookManager.getBookCount() + " 本图书");
        System.out.println("已加载 " + userManager.getUserCount() + " 个用户");

        // 3. 显示欢迎信息
        System.out.println("\n欢迎使用图书馆管理系统！");

        // 4. 启动菜单系统，开始用户交互
        menu.showMainMenu(); // 控制权交给菜单系统
    }

    /**
     * 程序主入口方法
     * Java程序的执行起点，遵循标准Java应用程序结构
     * @param args 命令行参数（本系统未使用）
     */
    public static void main(String[] args) {
        // 1. 创建图书馆系统实例
        LibrarySystem system = new LibrarySystem();

        // 2. 启动系统
        system.start();
    }
}