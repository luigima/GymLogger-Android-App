package com.luigima.gymlogger.data.db.entities.sql;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.luigima.gymlogger.data.db.tables.RoutineTextTranslateTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = RoutineTextTranslateTable.TABLE)
public class DBRoutineTextTranslate {
    @StorIOSQLiteColumn(name = RoutineTextTranslateTable.COLUMN_ROUTINE_ID, key = true)
    @Nullable
    Integer routineId;

    @StorIOSQLiteColumn(name = RoutineTextTranslateTable.COLUMN_LANGUAGE_ID, key = true)
    @Nullable
    Integer languageId;

    @StorIOSQLiteColumn(name = RoutineTextTranslateTable.COLUMN_TITLE)
    @NonNull
    String title;

    @StorIOSQLiteColumn(name = RoutineTextTranslateTable.COLUMN_DESCRIPTION)
    @Nullable
    String description;

    public DBRoutineTextTranslate(Integer routineId, Integer languageId, @NonNull String title, String description) {
        this.routineId = routineId;
        this.languageId = languageId;
        this.title = title;
        this.description = description;
    }

    public DBRoutineTextTranslate() {
    }
}
