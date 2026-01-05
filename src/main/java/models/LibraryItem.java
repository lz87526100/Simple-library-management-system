package models;

/**
 * 图书馆物品抽象基类
 * 定义所有图书馆物品（如书籍、杂志、光盘等）的共同属性和行为
 * 采用抽象类设计，强制子类实现特定方法，确保多态性
 */
public abstract class LibraryItem {

    // 静态变量：统计所有图书馆物品的总数
    // 属于类级别，所有实例共享
    private static int totalItems = 0;

    // 实例变量：每个物品特有的属性
    protected String id;          // 物品唯一标识符（如条码号、ISBN等）
    protected String title;       // 物品标题/名称
    protected boolean available;  // 借阅状态，true表示可借，false表示已借出
    protected String location;    // 物品在图书馆内的存放位置（如书架编号）

    /**
     * 图书馆物品构造方法
     * @param id 物品唯一标识符
     * @param title 物品标题
     * 说明：新物品默认状态为可借阅，位置未指定，同时更新物品总数
     */
    public LibraryItem(String id, String title) {
        this.id = id;
        this.title = title;
        this.available = true;    // 新物品默认可借
        this.location = "未指定"; // 默认位置
        totalItems++;             // 每创建一个物品，总数加1
    }

    // ============ 抽象方法 ============
    // 子类必须实现这些方法，体现不同物品类型的特性

    /**
     * 获取物品类型
     * @return 返回具体物品类型（如"图书"、"杂志"、"光盘"等）
     */
    public abstract String getType();

    /**
     * 获取物品详细信息
     * @return 返回物品的完整描述信息（包含所有特有属性）
     */
    public abstract String getDetails();

    // ============ 具体方法 ============
    // 子类可以直接继承使用的方法

    /**
     * 检查物品当前是否可借阅
     * @return true-可借，false-已借出
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * 设置物品的借阅状态
     * @param available 新的借阅状态
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    // ============ 静态方法 ============
    // 类级别方法，不依赖于具体实例

    /**
     * 获取图书馆物品总数
     * @return 当前已创建的物品总数
     */
    public static int getTotalItems() {
        return totalItems;
    }

    // ============ Getter和Setter方法 ============
    // 提供对私有属性的安全访问

    /**
     * 获取物品ID
     * @return 物品唯一标识符
     */
    public String getId() {
        return id;
    }

    /**
     * 获取物品标题
     * @return 物品标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 获取物品存放位置
     * @return 物品位置信息
     */
    public String getLocation() {
        return location;
    }

    /**
     * 设置物品存放位置
     * @param location 新的存放位置
     */
    public void setLocation(String location) {
        this.location = location;
    }
}