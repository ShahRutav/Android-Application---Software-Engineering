package com.example.weddingplanner.Models;

public class Blog {
    private String title, description, image, pid, date, time, contact_address, contact_email, contact_name;

    public Blog() {
    }

    public Blog(String title, String description, String image, String pid, String date, String time, String contact_address, String contact_email, String contact_name) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.pid = pid;
        this.date = date;
        this.time = time;
        this.contact_address = contact_address;
        this.contact_email = contact_email;
        this.contact_name = contact_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContact_address() {
        return contact_address;
    }

    public void setContact_address(String contact_address) {
        this.contact_address = contact_address;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }
}
