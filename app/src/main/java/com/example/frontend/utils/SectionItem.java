package com.example.frontend.utils;

public class SectionItem {
    private String text;
    private int id;

    public SectionItem(String text, int id) {
        this.text = text;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }
}
