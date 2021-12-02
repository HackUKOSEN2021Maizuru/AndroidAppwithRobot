package com.example.koroboandroidapp.db;


import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.ColumnInfo;

import androidx.room.Entity;

import androidx.room.PrimaryKey;
import androidx.room.OnConflictStrategy;

@Dao
public interface LogDao {
    @Query("SELECT * FROM log")
    List<Log> getAll();

    @Query("SELECT * FROM log WHERE id IN (:ids)")
    List<Log> loadAllByIds(int[] ids);

    @Insert
    void insertAll(Log... log);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Log log);

    @Delete
    void delete(Log log);
}