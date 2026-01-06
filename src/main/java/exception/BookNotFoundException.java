package exception;

/**
 * 图书未找到异常
 */
public class BookNotFoundException extends LibraryException {
    private String bookId;

    /**
     * 构造方法
     * @param bookId 未找到的图书ID
     */
    public BookNotFoundException(String bookId) {
        super("未找到 ID 为 " + bookId + " 的图书", "LIB_1001", "BookManager");
        this.bookId = bookId;
    }

    /**
     * 获取图书ID
     * @return 图书ID
     */
    public String getBookId() {
        return bookId;
    }
}