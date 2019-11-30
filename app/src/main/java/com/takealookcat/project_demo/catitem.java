package com.takealookcat.project_demo;

import android.graphics.drawable.Drawable;

public class catitem {

    public String title ;
    public String content ;
    public String file;

    public catitem() {

    }
    public catitem(String title, String content) {
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
