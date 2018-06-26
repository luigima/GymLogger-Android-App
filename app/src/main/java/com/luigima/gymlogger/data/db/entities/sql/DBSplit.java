package com.luigima.gymlogger.data.db.entities.sql;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.luigima.gymlogger.data.db.tables.SplitTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = SplitTable.TABLE)
public class DBSplit {
    @StorIOSQLiteColumn(name = SplitTable.COLUMN_ID, key = true)
    @Nullable
    Integer id;

    @StorIOSQLiteColumn(name = SplitTable.COLUMN_TITLE)
    @NonNull
    String title;

    public DBSplit() {
    }

    public DBSplit(Integer id, @NonNull String title) {
        this.id = id;
        this.title = title;
    }

    @Nullable
    public Integer getId() {
        return id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }
}
