package com.takealookcat.project_demo;

//git push from notebook
//git push from desktop
//test brc 2
//after merge
//test on dummy account
import android.graphics.drawable.Drawable;

public class catitem {

    public String key;
    public String info ;
    public String content ;
    public String file ;
    public String email ;
    public String latitude ;
    public String longitude ;

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
    public void setLatitude(String latitude) {
        this.latitude = latitude ;
    }
    public void setLongitude(String longitude) { this.longitude = longitude ; }

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
    public String getInfo() { return this.info; }
}
