package com.luigima.gymlogger.data.db.entities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ExerciseHasMuscle {
    @Nullable
    Integer id;

    @NonNull
    Integer isPrimary;

    @NonNull
    String title;

    public ExerciseHasMuscle() {
    }

    public ExerciseHasMuscle(Integer id, @NonNull Integer isPrimary, @NonNull String title) {
        this.id = id;
        this.isPrimary = isPrimary;
        this.title = title;
    }

    @Nullable
    public Integer getId() {
        return id;
    }

    @NonNull
    public Integer getIsPrimary() {
        return isPrimary;
    }

    @NonNull
    public String getTitle() {
        return title;
    }
}
