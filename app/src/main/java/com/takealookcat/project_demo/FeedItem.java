package com.takealookcat.project_demo;

public class FeedItem {
    public String title ;
    public String content ;
    public String file;
    public String key;

    public FeedItem() {

    }
    public FeedItem(String title, String content) {
        this.title=title;
        this.content=content;
    }

    public void setTitle(String title) {
        this.title = title ;
    }

    public void setContent(String content) {
        this.content = content ;
    }

    public void setFile(String file) {
        this.file = file ;
    }
}
