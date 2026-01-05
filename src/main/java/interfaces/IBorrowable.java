package interfaces;

import models.User;
import java.util.Date;

public interface IBorrowable {
    boolean borrow(User user, Date borrowDate);
    boolean canBorrow(User user);
    Date getDueDate();
    String getBorrowerId();
}