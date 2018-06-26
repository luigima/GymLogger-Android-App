package com.luigima.gymlogger.data.db.entities.sql;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.luigima.gymlogger.data.db.tables.WorkoutTrainsExerciseTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = WorkoutTrainsExerciseTable.TABLE)
public class DBWorkoutTrainsExercises {
    @StorIOSQLiteColumn(name = WorkoutTrainsExerciseTable.COLUMN_WORKOUT_ID, key = true)
    @NonNull
    Integer workoutId;

    @StorIOSQLiteColumn(name = WorkoutTrainsExerciseTable.COLUMN_SET_ID, key = true)
    @NonNull
    Integer setID;

    @StorIOSQLiteColumn(name = WorkoutTrainsExerciseTable.COLUMN_SET_HAS_EXERCISE_ID, key = true)
    @NonNull
    Integer setHasExerciseId;

    @StorIOSQLiteColumn(name = WorkoutTrainsExerciseTable.COLUMN_WEIGHT)
    @Nullable
    Float weight;

    @StorIOSQLiteColumn(name = WorkoutTrainsExerciseTable.COLUMN_REPEATS)
    @NonNull
    int repeats;

    @StorIOSQLiteColumn(name = WorkoutTrainsExerciseTable.COLUMN_DURATION)
    @NonNull
    int duration;

    @StorIOSQLiteColumn(name = WorkoutTrainsExerciseTable.COLUMN_DATE)
    @Nullable
    Long date;

    @StorIOSQLiteColumn(name = WorkoutTrainsExerciseTable.COLUMN_NOTES)
    @NonNull
    String notes;

    public DBWorkoutTrainsExercises() {
    }


    public DBWorkoutTrainsExercises(@NonNull Integer workoutId, @NonNull Integer setID, @NonNull Integer routineHasExerciseId, Float weight, @NonNull int repeats, @NonNull int duration, Long date, @NonNull String notes) {
        this.workoutId = workoutId;
        this.setID = setID;
        this.setHasExerciseId = routineHasExerciseId;
        this.weight = weight;
        this.repeats = repeats;
        this.duration = duration;
        this.date = date;
        this.notes = notes;
    }

    @NonNull
    public Integer getWorkoutId() {
        return workoutId;
    }

    @NonNull
    public Integer getSetID() {
        return setID;
    }

    @NonNull
    public Integer getSetHasExerciseId() {
        return setHasExerciseId;
    }

    @Nullable
    public Float getWeight() {
        return weight;
    }

    @NonNull
    public int getRepeats() {
        return repeats;
    }

    @NonNull
    public int getDuration() {
        return duration;
    }

    @Nullable
    public Long getDate() {
        return date;
    }

    @NonNull
    public String getNotes() {
        return notes;
    }
}
