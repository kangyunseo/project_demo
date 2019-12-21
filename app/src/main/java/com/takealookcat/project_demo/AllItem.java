package com.takealookcat.project_demo;

import android.graphics.drawable.Drawable;

public class AllItem {
    public String title ;
    public String content ;
    public String file;
    public String key;
    public String email;
    public String info ;
    public String type ;
    private Drawable iconDrawable ;

    public void setType(String type) {
        this.type = type ;
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
    public void setinfo(String info) {
        this.info = info ;
    }
    public void setEmail(String email) {
        this.email = email ;
    }
    public void setDesc(String con) { content = con ; }
    public String getDesc() {
        return this.content ;
    }
    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public Drawable getIcon() {
        return this.iconDrawable ;
    }
}
