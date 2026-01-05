package interfaces;

import java.util.Date;
    /**
            *还书接口
    */
public interface IReturnable {
    /**
     *归还物品
     *@paramreturnDate归还日期
     *@return归还是否成功
     */
    boolean returnItem(Date returnDate);
    /**
                *计算逾期罚款
    *@paramcurrentDate当前日期（计算到该日期的罚款）
                    *@return罚款金额，如果未逾期返回0
    */
    double calculateFine(Date currentDate);
    /**
     *检查是否逾期
     *@paramcurrentDate当前日期
     *@return是否已逾期
     */
    boolean isOverdue(Date currentDate);
    /**
     *获取逾期天数
     *@paramcurrentDate当前日期
     *@return逾期天数，如果未逾期返回0
     */
    int getOverdueDays(Date currentDate);
}