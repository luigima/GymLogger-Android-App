package com.luigima.gymlogger.data.db.entities.sql;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.luigima.gymlogger.data.db.tables.RoutineExerciseHasSetsTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = RoutineExerciseHasSetsTable.TABLE)
public class DBRoutineExerciseHasSets {
    @StorIOSQLiteColumn(name = RoutineExerciseHasSetsTable.COLUMN_SET_ID, key = true)
    @Nullable
    Integer setId;

    @StorIOSQLiteColumn(name = RoutineExerciseHasSetsTable.COLUMN_SPLIT_HAS_EXERCISES_ID, key = true)
    @NonNull
    Integer setHasExercisesId;

    @StorIOSQLiteColumn(name = RoutineExerciseHasSetsTable.COLUMN_REPEATS)
    @Nullable
    Integer repeats;

    @StorIOSQLiteColumn(name = RoutineExerciseHasSetsTable.COLUMN_WEIGHT)
    @Nullable
    Float weight;

    @StorIOSQLiteColumn(name = RoutineExerciseHasSetsTable.COLUMN_DURATION)
    @Nullable
    Integer duration;

    public DBRoutineExerciseHasSets() {
    }

    public DBRoutineExerciseHasSets(@Nullable Integer setId, @NonNull Integer setHasExercisesId, Integer repeats, Float weight, Integer duration) {
        this.setId = setId;
        this.setHasExercisesId = setHasExercisesId;
        this.repeats = repeats;
        this.weight = weight;
        this.duration = duration;
    }

    @NonNull
    public Integer getSetId() {
        return setId;
    }

    @NonNull
    public Integer getSetHasExercisesId() {
        return setHasExercisesId;
    }

    @Nullable
    public Integer getRepeats() {
        return repeats;
    }

    @Nullable
    public Float getWeight() {
        return weight;
    }

    @Nullable
    public Integer getDuration() {
        return duration;
    }
}
