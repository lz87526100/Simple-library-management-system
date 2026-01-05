package models;

import interfaces.IBorrowable;
import interfaces.IReturnable;

import java.util.Date;

public class Librarian extends User {
    private String employedd;

    public Librarian(String id, String name, String employedd) {
        this.id = id;
        this.name = name;
        this.employedd = employedd;
        this.borrowedCount = 0;
        this.maxBorrowLimit = 999; // 管理员不受限制
    }

    @Override
    public String getUserType() {
        return "LIBRARIAN";
    }

    @Override
    public boolean canBorrow() {
        return true; // 管理员总是可以借阅
    }

    @Override
    public int getBorrowLimit() {
        return maxBorrowLimit;
    }

    // 管理员特有方法
    public void addBook(Book book) {
        System.out.println("管理员 " + name + " 添加了图书: " + book.getTitle());
    }

    public void removeBook(String bookId) {
        System.out.println("管理员 " + name + " 移除了图书ID: " + bookId);
    }

    public double processFine(User user, LibraryItem item) {
        if (item instanceof IReturnable) {
            IReturnable returnable = (IReturnable) item;
            double fine = returnable.calculateFine(new Date());
            System.out.println("处理用户 " + user.getName() + " 的罚款: " + fine + "元");
            return fine;
        }
        return 0.0;
    }

    public String getEmployedd() {
        return employedd;
    }

    public void setEmployedd(String employedd) {
        this.employedd = employedd;
    }
}