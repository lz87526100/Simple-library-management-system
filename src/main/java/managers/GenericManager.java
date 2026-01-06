package managers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通用泛型管理器基类
 * 提供基础的CRUD操作和统计功能
 * @param <T> 管理的数据类型
 */
public abstract class GenericManager<T> {
    protected List<T> items;
    protected int totalOperations;
    protected String managerName;

    /**
     * 构造方法
     * @param managerName 管理器名称
     */
    public GenericManager(String managerName) {
        this.managerName = managerName;
        this.items = new ArrayList<>();
        this.totalOperations = 0;
    }

    /**
     * 添加项目
     * @param item 要添加的项目
     * @return 是否添加成功
     */
    public abstract boolean add(T item);

    /**
     * 删除项目
     * @param id 项目ID
     * @return 是否删除成功
     */
    public abstract boolean delete(String id);

    /**
     * 根据ID查找项目
     * @param id 项目ID
     * @return 项目对象
     */
    public abstract T findById(String id);

    /**
     * 获取所有项目
     * @return 所有项目的列表
     */
    public List<T> getAllItems() {
        totalOperations++;
        return new ArrayList<>(items);
    }

    /**
     * 获取项目数量
     * @return 项目总数
     */
    public int getCount() {
        return items.size();
    }

    /**
     * 获取总操作次数
     * @return 操作次数
     */
    public int getTotalOperations() {
        return totalOperations;
    }

    /**
     * 显示统计信息
     */
    public void displayStatistics() {
        System.out.println("\n=== " + managerName + " 统计信息 ===");
        System.out.println("总项目数量: " + getCount());
        System.out.println("总操作次数: " + totalOperations);
    }

    /**
     * 清空所有项目
     */
    public void clear() {
        items.clear();
        totalOperations++;
        System.out.println(managerName + " 已清空");
    }

    /**
     * 检查是否包含指定ID的项目
     * @param id 项目ID
     * @return 是否包含
     */
    public boolean contains(String id) {
        return findById(id) != null;
    }
}