package com.example.frontend;

import java.util.ArrayList;
import java.util.List;

public class Section {
    private String title;
    private List<String> levels;

    public Section(String title) {
        this.title = title;
        this.levels = new ArrayList<>();
        // Adaugă niveluri aici
        initLevels();
    }

    private void initLevels() {
        for (int i = 1; i <= 5; i++) {
            levels.add("Nivel " + i);
        }
    }

    public String getTitle() {
        return title;
    }

    public List<String> getLevels() {
        return levels;
    }
}

