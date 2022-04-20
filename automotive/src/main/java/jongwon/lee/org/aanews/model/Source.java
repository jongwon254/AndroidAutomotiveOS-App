package jongwon.lee.org.aanews.model;

public class Source {

    // JSON response object from NewsAPI
    private String name;

    public Source(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
