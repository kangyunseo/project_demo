package com.takealookcat.project_demo;

import android.graphics.drawable.Drawable;

public class FeedItem {
    private Drawable iconDrawable ;
    public String key;
    public String info ;
    public String content ;
    public String file ;
    public String email;
    public String latitude ;
    public String longitude ;

    public void setinfo(String info) {
        this.info = info ;
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
    public void setEmail(String email) {
        this.email = email ;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude ;
    }
    public void setLongitude(String longitude) { this.longitude = longitude ; }

    public Drawable getIcon() { return this.iconDrawable ; }
    public String getTitle() {
        return this.info ;
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
    public String getLatitude() {
        return this.latitude;
    }
    public String getLongitude() { return this.longitude; }
}
