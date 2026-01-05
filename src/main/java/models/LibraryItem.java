package models;

public abstract class LibraryItem {
    private static int totalItems = 0;
    protected String id;
    protected String title;
    protected boolean available;
    protected String location;

    public LibraryItem(String id, String title) {
        this.id = id;
        this.title = title;
        this.available = true;
        this.location = "未指定";
        totalItems++;
    }

    // 抽象方法
    public abstract String getType();
    public abstract String getDetails();

    // 具体方法
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public static int getTotalItems() {
        return totalItems;
    }

    // Getter 和 Setter
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}