package com.takealookcat.project_demo;

import android.graphics.drawable.Drawable;

public class ListViewItem {

    private Drawable iconDrawable ;
    public String key;
    public String title ;
    public String content ;
    public String file ;

    public void setTitle(String title) {
        this.title = title ;
    }
    public void setContent(String content) {
        this.content = content ;
    }
    public void setFile(String file) {
        this.file = file ;
    }
    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setDesc(String con) { content = con ; }
    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getTitle() {
        return this.title ;
    }
    public String getDesc() {
        return this.content ;
    }
    public String getFile() {
        return this.file ;
    }

}
