package exception;

/**
 * 无效输入异常
 */
public class InvalidInputException extends LibraryException {
    private String inputValue;
    private String expectedFormat;

    /**
     * 构造方法
     * @param inputValue 无效的输入值
     * @param expectedFormat 期望的格式
     */
    public InvalidInputException(String inputValue, String expectedFormat) {
        super("输入值 '" + inputValue + "' 无效，期望格式: " + expectedFormat, "LIB_3001", "InputValidator");
        this.inputValue = inputValue;
        this.expectedFormat = expectedFormat;
    }

    /**
     * 获取输入值
     * @return 输入值
     */
    public String getInputValue() {
        return inputValue;
    }

    /**
     * 获取期望格式
     * @return 期望格式
     */
    public String getExpectedFormat() {
        return expectedFormat;
    }
}