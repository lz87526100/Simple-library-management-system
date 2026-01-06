package exception;

/**
 * 借阅上限超出异常
 */
public class BorrowLimitExceededException extends LibraryException {
    private String userId;
    private int limit;

    /**
     * 构造方法
     * @param userId 用户ID
     * @param limit 借阅上限
     */
    public BorrowLimitExceededException(String userId, int limit) {
        super("用户 " + userId + " 借阅数量已达上限（" + limit + "本）", "LIB_2002", "BorrowService");
        this.userId = userId;
        this.limit = limit;
    }

    /**
     * 获取用户ID
     * @return 用户ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 获取借阅上限
     * @return 上限值
     */
    public int getLimit() {
        return limit;
    }
}