package com.luigima.gymlogger.data.db.entities.sql;

import android.support.annotation.Nullable;

import com.luigima.gymlogger.data.db.tables.MuscleTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = MuscleTable.TABLE)
public class DBMuscle {
    @StorIOSQLiteColumn(name = MuscleTable.COLUMN_ID, key = true)
    @Nullable
    Integer id;

    public DBMuscle() {
    }

    public DBMuscle(Integer id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
