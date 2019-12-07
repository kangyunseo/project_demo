package com.takealookcat.project_demo;
//
public class BoardItem {
    public String title ;
    public String content ;
    public String file;
    public String key;
    public String email;

    public BoardItem() {

    }
    public BoardItem(String title, String content) {
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
    public void setemail(String email) {
        this.email = email ;
    }

    public String getTitle() {
        return this.title ;
    }
    public String getContent() {
        return this.content ;
    }
    public String getFile() {
        return this.file ;
    }
    public String getemail() {
        return this.email ;
    }
}
