package library;

public class BookData {
    private long id;
    private String title;
    private String author;
    private int year;
    private boolean isAvailable;

    public BookData(long id, String title, String author, int year, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.isAvailable = isAvailable;
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public boolean isAvailable() { return isAvailable; }
}