package models;

import interfaces.IBorrowable;
import interfaces.IReturnable;
import enums.BookCategory;
import java.util.Date;
import java.util.Calendar;

public class Book extends LibraryItem implements IBorrowable, IReturnable {
    private String author;
    private String ISBN;
    private BookCategory category;
    private int pageCount;
    private int year;

    // 借阅相关属性
    private String borrowerId;
    private Date borrowDate;
    private Date dueDate;

    public Book(String id, String title, String author, String ISBN, BookCategory category) {
        super(id, title);
        this.author = author;
        this.ISBN = ISBN;
        this.category = category;
        this.pageCount = 0;
        this.year = 2024;
        this.borrowerId = null;
        this.borrowDate = null;
        this.dueDate = null;
    }

    // 重载构造方法
    public Book(String id, String title, String author, String ISBN) {
        this(id, title, author, ISBN, BookCategory.GENERAL);
    }

    // ========== 实现 LibraryItem 抽象方法 ==========
    @Override
    public String getType() {
        return "Book";
    }

    @Override
    public String getDetails() {
        return String.format("图书: %s (作者: %s, ISBN: %s, 出版年: %d)",
                title, author, ISBN, year);
    }

    // ========== 实现 IBorrowable 接口方法 ==========
    @Override
    public boolean borrow(User user, Date borrowDate) {
        if (!canBorrow(user)) {
            System.out.println("借阅失败：不满足借阅条件");
            return false;
        }

        this.borrowerId = user.getId();
        this.borrowDate = borrowDate;
        this.available = false;

        calculateDueDate(user);
        System.out.println("借阅成功！应还日期：" + dueDate);
        return true;
    }

    @Override
    public boolean canBorrow(User user) {
        if (!available) {
            System.out.println("图书已被借出");
            return false;
        }

        if (category == BookCategory.REFERENCE_BOOK &&
                user instanceof Student) {
            System.out.println("学生不能借阅参考书");
            return false;
        }

        return true;
    }

    @Override
    public Date getDueDate() {
        return dueDate;
    }

    @Override
    public String getBorrowerId() {
        return borrowerId;
    }

    private void calculateDueDate(User user) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(borrowDate);
        int borrowDays = getBorrowDaysByUserAndCategory(user);
        calendar.add(Calendar.DAY_OF_YEAR, borrowDays);
        this.dueDate = calendar.getTime();
    }

    private int getBorrowDaysByUserAndCategory(User user) {
        if (category == null) return user instanceof Teacher ? 60 : 30;

        switch (category) {
            case TEXTBOOK:
                return user instanceof Teacher ? 90 : 60;
            case REFERENCE_BOOK:
                return user instanceof Teacher ? 30 : 0;
            case FICTION:
                return 30;
            case PERIODICAL:
                return 14;
            default:
                return user instanceof Teacher ? 60 : 30;
        }
    }

    // ========== 实现 IReturnable 接口方法 ==========
    @Override
    public boolean returnItem(Date returnDate) {
        if (available) {
            System.out.println("图书未被借出，无需归还");
            return false;
        }

        if (isOverdue(returnDate)) {
            double fine = calculateFine(returnDate);
            System.out.println("图书已逾期，罚款金额：" + fine + "元");
        }

        this.borrowerId = null;
        this.borrowDate = null;
        this.dueDate = null;
        this.available = true;

        System.out.println("归还成功！");
        return true;
    }

    @Override
    public double calculateFine(Date currentDate) {
        if (!isOverdue(currentDate)) {
            return 0.0;
        }

        int overdueDays = getOverdueDays(currentDate);
        double dailyFine = getDailyFineRate();
        return overdueDays * dailyFine;
    }

    @Override
    public boolean isOverdue(Date currentDate) {
        if (dueDate == null || available) {
            return false;
        }
        return currentDate.after(dueDate);
    }

    @Override
    public int getOverdueDays(Date currentDate) {
        if (!isOverdue(currentDate)) {
            return 0;
        }

        long diff = currentDate.getTime() - dueDate.getTime();
        int days = (int) (diff / (1000 * 60 * 60 * 24));
        return Math.max(1, days);
    }

    private double getDailyFineRate() {
        if (category == null) return 0.5;

        switch (category) {
            case TEXTBOOK: return 0.3;
            case REFERENCE_BOOK: return 1.0;
            case FICTION: return 0.5;
            case PERIODICAL: return 0.8;
            default: return 0.5;
        }
    }

    // ========== Getter 和 Setter ==========
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public BookCategory getCategory() {
        return category;
    }

    public void setCategory(BookCategory category) {
        this.category = category;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}