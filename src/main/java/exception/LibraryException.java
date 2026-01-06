package exception;

/**
 * 图书馆系统基础异常类
 * 所有自定义异常的基类
 */
public class LibraryException extends Exception {
    private String errorCode;
    private String moduleName;

    /**
     * 构造方法
     * @param message 异常信息
     */
    public LibraryException(String message) {
        super(message);
        this.errorCode = "LIB_0001";
        this.moduleName = "LibrarySystem";
    }

    /**
     * 构造方法
     * @param message 异常信息
     * @param errorCode 错误代码
     */
    public LibraryException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.moduleName = "LibrarySystem";
    }

    /**
     * 构造方法
     * @param message 异常信息
     * @param errorCode 错误代码
     * @param moduleName 模块名称
     */
    public LibraryException(String message, String errorCode, String moduleName) {
        super(message);
        this.errorCode = errorCode;
        this.moduleName = moduleName;
    }

    /**
     * 获取错误代码
     * @return 错误代码
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 获取模块名称
     * @return 模块名称
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * 获取完整的异常信息
     * @return 格式化后的异常信息
     */
    @Override
    public String toString() {
        return String.format("[%s][%s] %s",
                moduleName, errorCode, getMessage());
    }
}