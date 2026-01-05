package models;

public class Student extends User {
    private String studentId;

    public Student(String id, String name, String studentId) {
        this.id = id;
        this.name = name;
        this.studentId = studentId;
        this.maxBorrowLimit = 5;
        this.borrowedCount = 0;
    }

    @Override
    public String getUserType() {
        return "STUDENT";
    }

    @Override
    public boolean canBorrow() {
        return borrowedCount < maxBorrowLimit;
    }

    @Override
    public int getBorrowLimit() {
        return maxBorrowLimit;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}