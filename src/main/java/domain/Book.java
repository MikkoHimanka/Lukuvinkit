package domain;

public class Book {
    private String link;
    private String title;
    private int id;
    private String description;

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

    public Book(String link, String title, int id, String description) {
        this(link, title);
        this.id = id;
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
