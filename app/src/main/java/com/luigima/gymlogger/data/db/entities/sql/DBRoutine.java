package com.luigima.gymlogger.data.db.entities.sql;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.luigima.gymlogger.data.db.tables.RoutineTable;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = RoutineTable.TABLE)
public class DBRoutine {
    @StorIOSQLiteColumn(name = RoutineTable.COLUMN_ID, key = true)
    @Nullable
    Integer id;

    @StorIOSQLiteColumn(name = RoutineTable.COLUMN_CATEGORY_ID)
    @NonNull
    Integer routineCategoryId;

    @StorIOSQLiteColumn(name = RoutineTable.COLUMN_CREATOR_ID)
    @NonNull
    Integer creatorId;

    @StorIOSQLiteColumn(name = RoutineTable.COLUMN_CREATION_DATE)
    @NonNull
    Long creationDate;

    @StorIOSQLiteColumn(name = RoutineTable.COLUMN_DIFFICULTY)
    @NonNull
    Integer difficulty;

    @StorIOSQLiteColumn(name = RoutineTable.COLUMN_RATING)
    @Nullable
    Float rating;

    @StorIOSQLiteColumn(name = RoutineTable.COLUMN_IS_LOCAL)
    @NonNull
    Integer isLocal;

    public DBRoutine(){}

    public DBRoutine(Integer id, @NonNull Integer routineCategoryId, @NonNull Integer creatorId,
                     @NonNull Long creationDate, @NonNull Integer difficulty,
                     @Nullable Float rating, @NonNull Integer isLocal) {
        this.id = id;
        this.routineCategoryId = routineCategoryId;
        this.creatorId = creatorId;
        this.creationDate = creationDate;
        this.difficulty = difficulty;
        this.rating = rating;
        this.isLocal = isLocal;
    }

    @Nullable
    public Integer getId() {
        return id;
    }

    @NonNull
    public Integer getRoutineCategoryId() {
        return routineCategoryId;
    }

    @NonNull
    public Integer getCreatorId() {
        return creatorId;
    }

    @NonNull
    public Long getCreationDate() {
        return creationDate;
    }

    @NonNull
    public Integer getDifficulty() {
        return difficulty;
    }

    @Nullable
    public Float getRating() {
        return rating;
    }

    @NonNull
    public Integer getIsLocal() {
        return isLocal;
    }
}
