package com.luigima.gymlogger.data.db.entities.sql;

import android.support.annotation.Nullable;

import com.luigima.gymlogger.data.db.tables.RoutineHasSplitsTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = RoutineHasSplitsTable.TABLE)
public class DBRoutineHasSplits {
    @StorIOSQLiteColumn(name = RoutineHasSplitsTable.COLUMN_ROUTINE_ID, key = true)
    @Nullable
    Integer routineId;

    @StorIOSQLiteColumn(name = RoutineHasSplitsTable.COLUMN_SPLIT_ID, key = true)
    @Nullable
    Integer splitId;

    public DBRoutineHasSplits() {
    }

    public DBRoutineHasSplits(Integer routineId, Integer splitId) {
        this.routineId = routineId;
        this.splitId = splitId;
    }

    @Nullable
    public Integer getRoutineId() {
        return routineId;
    }

    @Nullable
    public Integer getSplitId() {
        return splitId;
    }
}
