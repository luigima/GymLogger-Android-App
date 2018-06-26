package com.luigima.gymlogger.data.db.resolvers;

import android.support.annotation.NonNull;

import com.luigima.gymlogger.data.db.entities.MainMuscleGroup;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;

public class MainMuscleGroupDeleteResolver extends DeleteResolver<MainMuscleGroup> {

    //TODO Implement this
    @NonNull
    @Override
    public DeleteResult performDelete(StorIOSQLite storIOSQLite, MainMuscleGroup object) {
        return null;
    }
}
