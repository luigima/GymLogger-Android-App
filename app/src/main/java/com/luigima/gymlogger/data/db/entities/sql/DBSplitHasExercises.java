package com.luigima.gymlogger.data.db.entities.sql;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.luigima.gymlogger.data.db.tables.SplitHasExercisesTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = SplitHasExercisesTable.TABLE)
public class DBSplitHasExercises {
    @StorIOSQLiteColumn(name = SplitHasExercisesTable.COLUMN_ID, key = true)
    @Nullable
    Integer id;

    @StorIOSQLiteColumn(name = SplitHasExercisesTable.COLUMN_ROUTINE_ID)
    @NonNull
    Integer routineId;

    @StorIOSQLiteColumn(name = SplitHasExercisesTable.COLUMN_EXERCISE_ID)
    @NonNull
    Integer exerciseId;

    @StorIOSQLiteColumn(name = SplitHasExercisesTable.COLUMN_SPLIT_ID)
    @NonNull
    Integer splitId;

    @StorIOSQLiteColumn(name = SplitHasExercisesTable.COLUMN_ORDER_NUMBER)
    @NonNull
    Integer orderNr;

    public DBSplitHasExercises() {
    }


    public DBSplitHasExercises(Integer id, @NonNull Integer routineId, @NonNull Integer exerciseId, @NonNull Integer splitId, @NonNull Integer orderNr) {
        this.id = id;
        this.routineId = routineId;
        this.exerciseId = exerciseId;
        this.splitId = splitId;
        this.orderNr = orderNr;
    }

    @Nullable
    public Integer getId() {
        return id;
    }

    @NonNull
    public Integer getRoutineId() {
        return routineId;
    }

    @NonNull
    public Integer getExerciseId() {
        return exerciseId;
    }

    @NonNull
    public Integer getSplitId() {
        return splitId;
    }

    @NonNull
    public Integer getOrderNr() {
        return orderNr;
    }
}
