package models;

import interfaces.IBorrowable;
import interfaces.IReturnable;
import enums.BookCategory;
import java.util.Date;
import java.util.Calendar;

/**
 * 图书类
 * 继承自LibraryItem，实现IBorrowable和IReturnable接口
 * 表示图书馆中的图书资源，包含借阅、归还等完整生命周期管理
 */
public class Book extends LibraryItem implements IBorrowable, IReturnable {
    // ========== 图书基本属性 ==========
    private String author;           // 作者
    private String ISBN;             // 国际标准书号，图书唯一标识
    private BookCategory category;   // 图书分类
    private int pageCount;           // 页数
    private int year;                // 出版年份

    // ========== 借阅相关属性 ==========
    private String borrowerId;       // 当前借阅者ID（null表示未被借出）
    private Date borrowDate;         // 借阅日期
    private Date dueDate;            // 应归还日期

    /**
     * 完整参数构造方法
     * @param id 图书唯一标识符
     * @param title 图书标题
     * @param author 作者
     * @param ISBN 国际标准书号
     * @param category 图书分类
     */
    public Book(String id, String title, String author, String ISBN, BookCategory category) {
        super(id, title);                    // 调用父类构造方法
        this.author = author;
        this.ISBN = ISBN;
        this.category = category;
        this.pageCount = 0;                  // 默认页数为0
        this.year = 2024;                    // 默认出版年份为2024
        this.borrowerId = null;              // 初始状态：未被借出
        this.borrowDate = null;
        this.dueDate = null;
    }

    /**
     * 简化构造方法（重载）
     * 默认分类为BookCategory.GENERAL（通用类）
     * @param id 图书唯一标识符
     * @param title 图书标题
     * @param author 作者
     * @param ISBN 国际标准书号
     */
    public Book(String id, String title, String author, String ISBN) {
        this(id, title, author, ISBN, BookCategory.GENERAL);
    }

    // ========== 实现 LibraryItem 抽象方法 ==========

    /**
     * 获取物品类型
     * @return "Book" - 表示图书类型
     */
    @Override
    public String getType() {
        return "Book";
    }

    /**
     * 获取图书详细信息
     * @return 格式化的图书信息字符串
     */
    @Override
    public String getDetails() {
        return String.format("图书: %s (作者: %s, ISBN: %s, 出版年: %d)",
                title, author, ISBN, year);
    }

    // ========== 实现 IBorrowable 接口方法 ==========

    /**
     * 借阅图书
     * @param user 借阅用户
     * @param borrowDate 借阅日期
     * @return true-借阅成功, false-借阅失败
     */
    @Override
    public boolean borrow(User user, Date borrowDate) {
        // 1. 检查是否可以借阅
        if (!canBorrow(user)) {
            System.out.println("借阅失败：不满足借阅条件");
            return false;
        }

        // 2. 设置借阅信息
        this.borrowerId = user.getId();
        this.borrowDate = borrowDate;
        this.available = false;  // 标记为不可借状态

        // 3. 计算应归还日期
        calculateDueDate(user);
        System.out.println("借阅成功！应还日期：" + dueDate);
        return true;
    }

    /**
     * 检查图书是否可以被指定用户借阅
     * @param user 借阅用户
     * @return true-可以借阅, false-不能借阅
     */
    @Override
    public boolean canBorrow(User user) {
        // 1. 检查图书是否已被借出
        if (!available) {
            System.out.println("图书已被借出");
            return false;
        }

        // 2. 检查学生是否可以借阅参考书（特殊规则）
        if (category == BookCategory.REFERENCE_BOOK && user instanceof Student) {
            System.out.println("学生不能借阅参考书");
            return false;
        }

        return true;
    }

    /**
     * 获取应归还日期
     * @return 应归还日期
     */
    @Override
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * 获取当前借阅者ID
     * @return 借阅者ID（null表示未被借出）
     */
    @Override
    public String getBorrowerId() {
        return borrowerId;
    }

    /**
     * 计算应归还日期（私有方法）
     * @param user 借阅用户
     */
    private void calculateDueDate(User user) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(borrowDate);                     // 设置基准日期
        int borrowDays = getBorrowDaysByUserAndCategory(user); // 获取可借天数
        calendar.add(Calendar.DAY_OF_YEAR, borrowDays);   // 计算应还日期
        this.dueDate = calendar.getTime();
    }

    /**
     * 根据用户类型和图书类型确定借阅天数（私有方法）
     * @param user 借阅用户
     * @return 可借天数
     */
    private int getBorrowDaysByUserAndCategory(User user) {
        if (category == null) {
            return user instanceof Teacher ? 60 : 30; // 默认规则
        }

        // 根据图书分类和用户类型确定借阅天数
        switch (category) {
            case TEXTBOOK:      // 教科书：教师90天，学生60天
                return user instanceof Teacher ? 90 : 60;
            case REFERENCE_BOOK: // 参考书：教师30天，学生不能借（返回0天）
                return user instanceof Teacher ? 30 : 0;
            case FICTION:       // 小说：统一30天
                return 30;
            case PERIODICAL:    // 期刊：统一14天
                return 14;
            default:            // 其他分类：教师60天，学生30天
                return user instanceof Teacher ? 60 : 30;
        }
    }

    // ========== 实现 IReturnable 接口方法 ==========

    /**
     * 归还图书
     * @param returnDate 归还日期
     * @return true-归还成功, false-归还失败
     */
    @Override
    public boolean returnItem(Date returnDate) {
        // 1. 检查图书是否已被借出
        if (available) {
            System.out.println("图书未被借出，无需归还");
            return false;
        }

        // 2. 检查是否逾期并计算罚款
        if (isOverdue(returnDate)) {
            double fine = calculateFine(returnDate);
            System.out.println("图书已逾期，罚款金额：" + fine + "元");
        }

        // 3. 重置借阅信息
        this.borrowerId = null;
        this.borrowDate = null;
        this.dueDate = null;
        this.available = true;  // 标记为可借状态

        System.out.println("归还成功！");
        return true;
    }

    /**
     * 计算逾期罚款金额
     * @param currentDate 当前日期（归还日期）
     * @return 罚款金额（单位：元）
     */
    @Override
    public double calculateFine(Date currentDate) {
        if (!isOverdue(currentDate)) {
            return 0.0; // 未逾期，无罚款
        }

        int overdueDays = getOverdueDays(currentDate); // 计算逾期天数
        double dailyFine = getDailyFineRate();         // 获取每日罚款费率
        return overdueDays * dailyFine;                // 计算总罚款
    }

    /**
     * 检查图书是否逾期
     * @param currentDate 当前日期
     * @return true-已逾期, false-未逾期
     */
    @Override
    public boolean isOverdue(Date currentDate) {
        if (dueDate == null || available) {
            return false; // 无应还日期或未被借出，不算逾期
        }
        return currentDate.after(dueDate); // 当前日期晚于应还日期
    }

    /**
     * 计算逾期天数
     * @param currentDate 当前日期
     * @return 逾期天数（至少1天）
     */
    @Override
    public int getOverdueDays(Date currentDate) {
        if (!isOverdue(currentDate)) {
            return 0; // 未逾期，天数为0
        }

        // 计算毫秒差并转换为天数
        long diff = currentDate.getTime() - dueDate.getTime();
        int days = (int) (diff / (1000 * 60 * 60 * 24));
        return Math.max(1, days); // 至少算1天逾期
    }

    /**
     * 获取每日罚款费率（私有方法）
     * 不同分类的图书有不同的罚款费率
     * @return 每日罚款费率（单位：元/天）
     */
    private double getDailyFineRate() {
        if (category == null) return 0.5; // 默认费率

        switch (category) {
            case TEXTBOOK:       // 教科书：0.3元/天
                return 0.3;
            case REFERENCE_BOOK: // 参考书：1.0元/天（较贵）
                return 1.0;
            case FICTION:        // 小说：0.5元/天
                return 0.5;
            case PERIODICAL:     // 期刊：0.8元/天
                return 0.8;
            default:             // 其他分类：0.5元/天
                return 0.5;
        }
    }

    // ========== Getter 和 Setter 方法 ==========

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public BookCategory getCategory() {
        return category;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}