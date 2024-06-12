package com.example.frontend.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SubcategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Subcategory> subcategories);

    @Query("SELECT * FROM subcategories WHERE categoryId = :categoryId")
    List<Subcategory> getSubcategoriesByCategoryId(int categoryId);
}
