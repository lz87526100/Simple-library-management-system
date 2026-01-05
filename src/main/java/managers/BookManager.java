package managers;

import models.Book;
import enums.BookCategory;

public class BookManager {
    private Book[] books;
    private int bookCount;
    private final int MAX_BOOKS = 100;

    public BookManager() {
        books = new Book[MAX_BOOKS];
        bookCount = 0;
        initSampleBooks();
    }

    private void initSampleBooks() {
        System.out.println("---批量添加一些示例图书---");
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

    public boolean addBook(Book book) {
        if (bookCount >= MAX_BOOKS) {
            System.out.println("错误：图书数量已达上限！");
            return false;
        }

        if (findBookIndexById(book.getId()) != -1) {
            System.out.println("错误：图书ID已存在！");
            return false;
        }

        books[bookCount] = book;
        bookCount++;
        System.out.println("成功添加图书：" + book.getTitle());
        return true;
    }

    public boolean deleteBook(String bookId) {
        int index = findBookIndexById(bookId);
        if (index == -1) {
            System.out.println("错误：未找到 ID 为 " + bookId + " 的图书");
            return false;
        }

        // 将后面的元素前移
        for (int i = index; i < bookCount - 1; i++) {
            books[i] = books[i + 1];
        }

        books[bookCount - 1] = null;
        bookCount--;
        System.out.println("成功删除图书 ID：" + bookId);
        return true;
    }

    private int findBookIndexById(String bookId) {
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getId().equals(bookId)) {
                return i;
            }
        }
        return -1;
    }

    public Book findBookById(String bookId) {
        int index = findBookIndexById(bookId);
        return index != -1 ? books[index] : null;
    }

    public Book[] getAvailableBooks() {
        Book[] available = new Book[bookCount];
        int count = 0;

        for (int i = 0; i < bookCount; i++) {
            if (books[i].isAvailable()) {
                available[count++] = books[i];
            }
        }

        // 创建合适大小的数组
        Book[] result = new Book[count];
        System.arraycopy(available, 0, result, 0, count);
        return result;
    }

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

    public Book[] getBooksByUser(String userId) {
        Book[] userBooks = new Book[bookCount];
        int count = 0;

        for (int i = 0; i < bookCount; i++) {
            if (!books[i].isAvailable()) {
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

    public void displayAllBooks() {
        System.out.println("\n=== 所有图书列表 ===");
        for (int i = 0; i < bookCount; i++) {
            System.out.println(formatBookInfo(books[i]));
        }
        System.out.println("共 " + bookCount + " 本图书");
    }

    private String formatBookInfo(Book book) {
        return String.format("ID: %-6s | 状态: %s | ISBN: %-18s | 《%s》 %s",
                book.getId(),
                book.isAvailable() ? "可借" : "借出",
                book.getISBN(),
                book.getTitle(),
                book.getAuthor());
    }

    public void displayStatistics() {
        int availableCount = 0;
        int borrowedCount = 0;

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

    public int getBookCount() {
        return bookCount;
    }
}