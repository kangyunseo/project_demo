package com.takealookcat.project_demo;

import android.graphics.drawable.Drawable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class DonationItem {
    public String key;
    public String title ;
    public String content ;
    public String startDate ;
    public String dueDate ;
    public String targetAmount;
    public String curAmount;
    public String file;

    public DonationItem() {

    }
    public DonationItem(String title, String content) {
        this.title=title;
        this.content=content;
    }
    public void setTitle(String title) {
        this.title = title ;
    }
    public void setContent(String content) {
        this.content = content ;
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
    public void setFile(String file) {
        this.file = file ;
    }


    public String getTitle() {
        return this.title ;
    }
    public String getContent() {
        return this.content ;
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
    public String getFile() {
        return this.file ;
    }


}
