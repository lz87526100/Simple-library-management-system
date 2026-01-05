package managers;

import models.Book;
import enums.BookCategory;

/**
 * 图书管理类
 * 负责图书的增删改查、状态管理和统计功能
 * 使用数组存储图书数据，支持最多100本图书
 */
public class BookManager {

    // 图书数据存储数组
    private Book[] books;

    // 当前图书数量
    private int bookCount;

    // 最大图书容量（常量）
    private final int MAX_BOOKS = 100;

    /**
     * 图书管理器构造方法
     * 初始化数组并添加示例数据
     */
    public BookManager() {
        books = new Book[MAX_BOOKS];  // 创建容量为100的图书数组
        bookCount = 0;                // 初始图书数量为0
        initSampleBooks();           // 初始化示例图书
    }

    /**
     * 初始化示例图书（测试用）
     * 在系统启动时添加一些示例图书数据
     */
    private void initSampleBooks() {
        System.out.println("---批量添加一些示例图书---");
        // 添加各类图书：教材、参考书、小说、期刊
        addBook(new Book("B001", "Java 编程思想", "Bruce Eckel", "978-7-111-21382-6", BookCategory.TEXTBOOK));
        addBook(new Book("B002", "算法导论", "Erich Gamma", "978-7-111-07575-2", BookCategory.TEXTBOOK));
        addBook(new Book("B003", "代码大全", "Steve McConnell", "978-7-111-18777-6", BookCategory.TEXTBOOK));
        addBook(new Book("B004", "JavaEE", "Steve McConnell", "978-7-111-15847-2", BookCategory.REFERENCE_BOOK));
        addBook(new Book("B005", "设计模式", "Erich Gamma", "978-7-111-12575-8", BookCategory.REFERENCE_BOOK));
        addBook(new Book("B006", "三体", "刘慈欣", "978-7-5366-9293-0", BookCategory.FICTION));
        addBook(new Book("B007", "活着", "余华", "978-7-5063-8649-8", BookCategory.FICTION));
        addBook(new Book("B008", "计算机科学", "计算机科学杂志社", "1002-137X", BookCategory.PERIODICAL));
        addBook(new Book("B009", "昭通学院学报", "昭通学院出版社", "1005-1805", BookCategory.PERIODICAL));
    }

    /**
     * 添加图书
     * @param book 要添加的图书对象
     * @return 添加成功返回true，失败返回false
     * 失败原因：1. 已达容量上限 2. 图书ID已存在
     */
    public boolean addBook(Book book) {
        // 检查容量
        if (bookCount >= MAX_BOOKS) {
            System.out.println("错误：图书数量已达上限！");
            return false;
        }

        // 检查ID是否重复
        if (findBookIndexById(book.getId()) != -1) {
            System.out.println("错误：图书ID已存在！");
            return false;
        }

        // 添加图书到数组末尾
        books[bookCount] = book;
        bookCount++;
        System.out.println("成功添加图书：" + book.getTitle());
        return true;
    }

    /**
     * 删除图书
     * @param bookId 要删除的图书ID
     * @return 删除成功返回true，失败返回false
     * 注意：删除后需要移动数组元素以填补空缺
     */
    public boolean deleteBook(String bookId) {
        int index = findBookIndexById(bookId);
        if (index == -1) {
            System.out.println("错误：未找到 ID 为 " + bookId + " 的图书");
            return false;
        }

        // 将后面的元素前移，填补空缺
        for (int i = index; i < bookCount - 1; i++) {
            books[i] = books[i + 1];
        }

        // 清理最后一个元素并减少计数
        books[bookCount - 1] = null;
        bookCount--;
        System.out.println("成功删除图书 ID：" + bookId);
        return true;
    }

    /**
     * 根据图书ID查找索引（内部方法）
     * @param bookId 图书ID
     * @return 找到返回索引，未找到返回-1
     */
    private int findBookIndexById(String bookId) {
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getId().equals(bookId)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 根据图书ID查找图书对象
     * @param bookId 图书ID
     * @return 找到返回图书对象，未找到返回null
     */
    public Book findBookById(String bookId) {
        int index = findBookIndexById(bookId);
        return index != -1 ? books[index] : null;
    }

    /**
     * 获取所有可借阅的图书
     * @return 可借阅图书数组（无null元素）
     */
    public Book[] getAvailableBooks() {
        Book[] available = new Book[bookCount];
        int count = 0;

        // 筛选可借阅图书
        for (int i = 0; i < bookCount; i++) {
            if (books[i].isAvailable()) {
                available[count++] = books[i];
            }
        }

        // 创建大小合适的数组返回
        Book[] result = new Book[count];
        System.arraycopy(available, 0, result, 0, count);
        return result;
    }

    /**
     * 获取所有已借出的图书
     * @return 已借出图书数组（无null元素）
     */
    public Book[] getBorrowedBooks() {
        Book[] borrowed = new Book[bookCount];
        int count = 0;

        for (int i = 0; i < bookCount; i++) {
            if (!books[i].isAvailable()) {
                borrowed[count++] = books[i];
            }
        }

        Book[] result = new Book[count];
        System.arraycopy(borrowed, 0, result, 0, count);
        return result;
    }

    /**
     * 获取指定用户借阅的所有图书
     * @param userId 用户ID
     * @return 该用户借阅的图书数组
     * 注意：需要通过IBorrowable接口获取借阅者信息
     */
    public Book[] getBooksByUser(String userId) {
        Book[] userBooks = new Book[bookCount];
        int count = 0;

        for (int i = 0; i < bookCount; i++) {
            if (!books[i].isAvailable()) {
                // 强制转换以访问借阅者ID（需要Book实现IBorrowable接口）
                String borrowerId = ((interfaces.IBorrowable) books[i]).getBorrowerId();
                if (borrowerId != null && borrowerId.equals(userId)) {
                    userBooks[count++] = books[i];
                }
            }
        }

        Book[] result = new Book[count];
        System.arraycopy(userBooks, 0, result, 0, count);
        return result;
    }

    /**
     * 显示所有图书列表（控制台输出）
     */
    public void displayAllBooks() {
        System.out.println("\n=== 所有图书列表 ===");
        for (int i = 0; i < bookCount; i++) {
            System.out.println(formatBookInfo(books[i]));
        }
        System.out.println("共 " + bookCount + " 本图书");
    }

    /**
     * 格式化图书信息（内部方法）
     * @param book 图书对象
     * @return 格式化后的字符串
     */
    private String formatBookInfo(Book book) {
        return String.format("ID: %-6s | 状态: %s | ISBN: %-18s | 《%s》 %s",
                book.getId(),
                book.isAvailable() ? "可借" : "借出",
                book.getISBN(),
                book.getTitle(),
                book.getAuthor());
    }

    /**
     * 显示图书统计信息（控制台输出）
     */
    public void displayStatistics() {
        int availableCount = 0;
        int borrowedCount = 0;

        // 统计可借和已借数量
        for (int i = 0; i < bookCount; i++) {
            if (books[i].isAvailable()) {
                availableCount++;
            } else {
                borrowedCount++;
            }
        }

        System.out.println("\n--- 图书统计信息 ---");
        System.out.println("总图书数量: " + bookCount);
        System.out.println("可借阅图书: " + availableCount);
        System.out.println("已借出图书: " + borrowedCount);
        System.out.println("可用容量: " + (MAX_BOOKS - bookCount));
    }

    /**
     * 获取当前图书数量
     * @return 图书数量
     */
    public int getBookCount() {
        return bookCount;
    }


}