package com.enes.chattapp.models;

public class message  {


    private String from;
    private Boolean seen;
    private String text;
    private String time;
    private String type ;


    public message(){

    }

    public message(String from, Boolean seen, String text, String time, String type) {
        this.from = from;
        this.seen = seen;
        this.text = text;
        this.time = time;
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
