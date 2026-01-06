package exception;

/**
 * 图书不可借异常
 */
public class BookNotAvailableException extends LibraryException {
    private String bookId;

    /**
     * 构造方法
     * @param bookId 不可借的图书ID
     */
    public BookNotAvailableException(String bookId) {
        super("图书 " + bookId + " 当前不可借阅", "LIB_2001", "BookManager");
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