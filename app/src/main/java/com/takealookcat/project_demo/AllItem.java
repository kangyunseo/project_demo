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
    //donation
    public String startDate ;
    public String dueDate ;
    public String targetAmount;
    public String curAmount;

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
    public void setStartDate(String startDate) {
        this.startDate = startDate ;
    }
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate ;
    }
    public void setTargetAmount(String targetAmount) {
        this.targetAmount = targetAmount ;
    }
    public void setCurAmount(String curAmount) {
        this.curAmount = curAmount ;
    }
    public void setinfo(String info) {
        this.info = info ;
    }
    public void setDesc(String con) { content = con ; }
    public void setEmail(String email) { this.email = email ; }
    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
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
    public String getDesc() {
        return this.content ;
    }
    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getStartDate() {
        return this.startDate ;
    }
    public String getDueDate() {
        return this.dueDate ;
    }
    public String getTargetAmount() {
        return this.targetAmount ;
    }
    public String getCurAmount() {
        return this.curAmount ;
    }
}
