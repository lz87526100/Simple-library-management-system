package models;

public class Teacher extends User {
    private String teacherId;

    public Teacher(String id, String name, String teacherId) {
        this.id = id;
        this.name = name;
        this.teacherId = teacherId;
        this.maxBorrowLimit = 20;
        this.borrowedCount = 0;
    }

    @Override
    public String getUserType() {
        return "TEACHER";
    }

    @Override
    public boolean canBorrow() {
        return borrowedCount < maxBorrowLimit;
    }

    @Override
    public int getBorrowLimit() {
        return maxBorrowLimit;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }
}