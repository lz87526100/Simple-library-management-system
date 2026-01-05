package interfaces;

import java.util.Date;

public interface IReturnable {
    boolean returnItem(Date returnDate);
    double calculateFine(Date currentDate);
    boolean isOverdue(Date currentDate);
    int getOverdueDays(Date currentDate);
}