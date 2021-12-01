package com.example.koroboandroidapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Log {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "access_time")
    private String logTime;

    public Log(String logTime) {
        this.logTime = logTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setAccessTime(String accessTime) {
        this.logTime = accessTime;
    }

    public String getAccessTime() {
        return logTime;
    }

}
