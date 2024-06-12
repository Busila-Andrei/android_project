package com.example.frontend.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Word.class, Question.class, Category.class, Subcategory.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract WordDao wordDao();
    public abstract QuestionDao questionDao();
    public abstract CategoryDao categoryDao();
    public abstract SubcategoryDao subcategoryDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "word-database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
