package exception;

/**
 * 用户未找到异常
 */
public class UserNotFoundException extends LibraryException {
    private String userId;

    /**
     * 构造方法
     * @param userId 未找到的用户ID
     */
    public UserNotFoundException(String userId) {
        super("未找到 ID 为 " + userId + " 的用户", "LIB_1002", "UserManager");
        this.userId = userId;
    }

    /**
     * 获取用户ID
     * @return 用户ID
     */
    public String getUserId() {
        return userId;
    }
}