package domain;

public class Book {
    private String link;
    private String title;
    private int id;

    public Book(String link) {
        this.link = link;
    }

    public Book(String link, String title) {
        this.link = link;
        this.title = title;
    }

    public Book(String link, String title, int id) {
        this(link, title);
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return this.link;
    }

    public String getTitle() {
        return this.title;
    }

    public int getId() {
        return this.id;
    }
}
