package managers;

import models.Book;
import enums.BookCategory;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 增强版图书管理器 - 使用集合框架
 * 替换数组为ArrayList和HashMap，提供更高效的数据管理
 */
public class BookManagerEnhanced {
    // 主数据源：使用ArrayList存储所有图书
    private final List<Book> bookList;

    // 索引：使用HashMap实现ID到Book对象的快速查找
    private final Map<String, Book> bookIdIndex;

    // 可选：泛型容器
    private GenericContainer<Book> bookContainer;

    // 统计信息
    private int totalOperations;

    /**
     * 构造方法
     */
    public BookManagerEnhanced() {
        bookList = new ArrayList<>();
        bookIdIndex = new HashMap<>();
        bookContainer = new GenericContainer<>("图书容器");
        totalOperations = 0;

        // 初始化示例数据
        initSampleBooks();
    }

    /**
     * 初始化示例图书
     */
    private void initSampleBooks() {
        System.out.println("--- 增强版图书管理器初始化 ---");
        addBook(new Book("B001", "Java编程思想", "Bruce Eckel", "978-7-111-21382-6", BookCategory.TEXTBOOK));
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
     * 添加图书（保证所有索引同步更新）
     * @param book 要添加的图书
     * @return 添加是否成功
     */
    public boolean addBook(Book book) {
        // 检查ID是否已存在
        if (bookIdIndex.containsKey(book.getId())) {
            System.out.println("添加失败：图书ID " + book.getId() + " 已存在！");
            return false;
        }

        // 添加到主列表
        bookList.add(book);

        // 更新索引
        bookIdIndex.put(book.getId(), book);

        // 可选：添加到泛型容器
        bookContainer.add(book);

        totalOperations++;
        System.out.println("成功添加图书：" + book.getTitle());
        return true;
    }

    /**
     * 根据ID查找图书（O(1)时间复杂度）
     * @param bookId 图书ID
     * @return 图书对象，未找到返回null
     */
    public Book findBookById(String bookId) {
        totalOperations++;
        return bookIdIndex.get(bookId);
    }

    /**
     * 删除图书
     * @param bookId 图书ID
     * @return 删除是否成功
     */
    public boolean deleteBook(String bookId) {
        Book book = bookIdIndex.get(bookId);
        if (book == null) {
            System.out.println("删除失败：未找到ID为 " + bookId + " 的图书");
            return false;
        }

        // 从主列表删除
        bookList.remove(book);

        // 从索引删除
        bookIdIndex.remove(bookId);

        // 可选：从泛型容器删除
        bookContainer.remove(book);

        totalOperations++;
        System.out.println("成功删除图书：" + book.getTitle());
        return true;
    }

    /**
     * 获取所有可借阅的图书（使用Stream API过滤）
     * @return 可借阅图书列表
     */
    public List<Book> getAvailableBooks() {
        totalOperations++;
        return bookList.stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }

    /**
     * 获取所有已借出的图书
     * @return 已借出图书列表
     */
    public List<Book> getBorrowedBooks() {
        totalOperations++;
        return bookList.stream()
                .filter(book -> !book.isAvailable())
                .collect(Collectors.toList());
    }

    /**
     * 获取指定用户借阅的图书，并按应归还日期排序
     * @param userId 用户ID
     * @return 排序后的图书列表
     */
    public List<Book> getBooksBorrowedByUser(String userId) {
        totalOperations++;
        return bookList.stream()
                .filter(book -> !book.isAvailable()) // 已被借出
                .filter(book -> userId.equals(book.getBorrowerId()))
                .sorted(Comparator.comparing(book -> {
                    // 注意：这里需要Book实现getDueDate方法
                    return book.getDueDate() != null ? book.getDueDate() : new Date(0);
                }))
                .collect(Collectors.toList());
    }

    /**
     * 显示所有图书
     */
    public void displayAllBooks() {
        displayBooks(bookList, "所有图书列表");
    }

    /**
     * 通用显示图书方法
     * @param books 图书列表
     * @param title 显示标题
     */
    private void displayBooks(List<Book> books, String title) {
        if (books.isEmpty()) {
            System.out.println("\n" + title + "：当前没有图书");
            return;
        }

        System.out.println("\n=== " + title + " ===");
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            System.out.println((i + 1) + ". " + formatBookInfo(book));
        }
        System.out.println("总计: " + books.size() + " 本图书");
    }

    /**
     * 格式化图书信息
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
     * 显示统计信息（使用Stream API）
     */
    public void displayStatistics() {
        // 使用流API进行统计
        long totalCount = bookList.size();
        long availableCount = bookList.stream().filter(Book::isAvailable).count();
        long borrowedCount = totalCount - availableCount;

        System.out.println("\n=== 图书统计信息 ===");
        System.out.println("总图书数量: " + totalCount);
        System.out.println("可借阅图书: " + availableCount);
        System.out.println("已借出图书: " + borrowedCount);
        System.out.println("总操作次数: " + totalOperations);

        // 可选：显示泛型容器信息
        System.out.println("泛型容器中的图书数量: " + bookContainer.size());
    }

    /**
     * 根据标题关键词搜索图书
     * @param keyword 关键词
     * @return 匹配的图书列表
     */
    public List<Book> searchByTitle(String keyword) {
        return bookList.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * 根据作者搜索图书
     * @param author 作者名
     * @return 匹配的图书列表
     */
    public List<Book> searchByAuthor(String author) {
        return bookList.stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * 获取图书数量
     * @return 图书总数
     */
    public int getBookCount() {
        return bookList.size();
    }
}