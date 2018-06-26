package com.luigima.gymlogger.data.db.entities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.luigima.gymlogger.data.db.entities.sql.DBMuscle;

import java.util.List;

public class MainMuscleGroup {
    @Nullable
    Integer id;

    @NonNull
    String title;

    @Nullable
    List<DBMuscle> muscles;

    public MainMuscleGroup(Integer id, @NonNull String titel, List<DBMuscle> muscles) {
        this.id = id;
        this.title = titel;
        this.muscles = muscles;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Nullable
    public List<DBMuscle> getMuscles() {
        return muscles;
    }
}
