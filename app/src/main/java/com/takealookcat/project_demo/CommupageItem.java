package com.takealookcat.project_demo;

public class CommupageItem {

    public String key;
    public String info ;
    public String content ;
    public String file ;
    public String email ;

    public void setinfo(String info) {
        this.info = info ;
    }
    public void setEmail(String email) {
        this.email = email ;
    }
    public void setContent(String content) {
        this.content = content ;
    }
    public void setFile(String file) {
        this.file = file ;
    }
    public void setDesc(String con) { content = con ; }
    public String getTitle() {
        return this.info ;
    }
    public String getDesc() {
        return this.content ;
    }
    public String getFile() {
        return this.file ;
    }
    public String getemail() {
        return this.email ;
    }
}
