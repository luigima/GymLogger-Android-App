package com.luigima.gymlogger.data.db.entities.sql;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.luigima.gymlogger.data.db.tables.WorkoutTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = WorkoutTable.TABLE)
public class DBWorkout {
    @StorIOSQLiteColumn(name = WorkoutTable.COLUMN_ID, key = true)
    @Nullable
    Integer workoutId;

    @StorIOSQLiteColumn(name = WorkoutTable.COLUMN_ROUTINE_ID)
    @NonNull
    Integer routineId;


    @StorIOSQLiteColumn(name = WorkoutTable.COLUMN_SPLIT_ID)
    @NonNull
    Integer splitId;

    @StorIOSQLiteColumn(name = WorkoutTable.COLUMN_DATE)
    @Nullable
    Long date;

    @StorIOSQLiteColumn(name = WorkoutTable.COLUMN_DURATION)
    @NonNull
    int duration;

    @StorIOSQLiteColumn(name = WorkoutTable.COLUMN_NOTES)
    @Nullable
    String notes;

    @Nullable
    public Integer getWorkoutId() {
        return workoutId;
    }

    public DBWorkout(Integer workoutId, @NonNull Integer routineId, @NonNull Integer splitId, Long date, @NonNull int duration, String notes) {
        this.workoutId = workoutId;
        this.routineId = routineId;
        this.splitId = splitId;
        this.date = date;
        this.duration = duration;
        this.notes = notes;
    }

    @NonNull
    public Integer getSplitId() {
        return splitId;
    }

    @NonNull
    public Integer getRoutineId() {
        return routineId;
    }


    public DBWorkout() {
    }

    @Nullable
    public Long getDate() {
        return date;
    }

    @NonNull
    public int getDuration() {
        return duration;
    }

    public String getNotes() {
        return notes;
    }
}
