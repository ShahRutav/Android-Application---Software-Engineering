package com.example.weddingplanner.Models;

public class Checklist {
   private String task,status;

    public Checklist() {
    }

    public Checklist(String task, String status) {
        this.task = task;
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
