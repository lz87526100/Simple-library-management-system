package exception;

/**
 * 存储已满异常
 */
public class StorageFullException extends LibraryException {
    private String storageType;
    private int maxCapacity;

    /**
     * 构造方法
     * @param storageType 存储类型
     * @param maxCapacity 最大容量
     */
    public StorageFullException(String storageType, int maxCapacity) {
        super(storageType + " 存储已满，最大容量: " + maxCapacity, "LIB_4001", "StorageManager");
        this.storageType = storageType;
        this.maxCapacity = maxCapacity;
    }

    /**
     * 获取存储类型
     * @return 存储类型
     */
    public String getStorageType() {
        return storageType;
    }

    /**
     * 获取最大容量
     * @return 最大容量
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }
}