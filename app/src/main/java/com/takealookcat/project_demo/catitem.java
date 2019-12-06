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

    public void setinfo(String info) {
        this.info = info ;
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
}
