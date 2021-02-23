package domain;

public class Book {
    private String link;
    private String title;

    public Book(String link) {
        this.link = link;
    }

    public Book(String link, String title) {
        this.link = link;
        this.title = title;
    }

    public String getLink() {
        return this.link;
    }

    public String getTitle() {
        return this.title;
    }
}
