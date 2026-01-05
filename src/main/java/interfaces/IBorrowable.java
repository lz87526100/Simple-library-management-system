package interfaces;

import models.User;
import java.util.Date;
    /**
     *借书接口
     */
public interface IBorrowable {
    /**
     *借阅物品
     *@paramuser借阅用户
     *@paramborrowDate借阅日期
     *@return借阅是否成功
     */
    boolean borrow(User user, Date borrowDate);
    /**
     *检查是否可以借阅
     *@paramuser借阅用户
     *@return是否可以借阅
     */
    boolean canBorrow(User user);
    /**
     *获取应归还日期
     *@return应归还日期
     */
    Date getDueDate();
    /**
     *获取借阅者ID
     *@return当前借阅者ID（如果未被借出则返回null）
     */
    String getBorrowerId();
}