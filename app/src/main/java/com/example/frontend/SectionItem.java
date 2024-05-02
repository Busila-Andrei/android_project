package com.example.frontend;

import androidx.fragment.app.Fragment;

public class SectionItem {
    private String text;
    private Fragment fragment;

    public SectionItem(String text, Fragment fragment) {
        this.text = text;
        this.fragment = fragment;
    }

    public String getText() {
        return text;
    }

    public Fragment getFragment() {
        return fragment;
    }
}
