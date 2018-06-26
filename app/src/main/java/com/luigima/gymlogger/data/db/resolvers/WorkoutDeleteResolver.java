package com.luigima.gymlogger.data.db.resolvers;

import android.support.annotation.NonNull;

import com.luigima.gymlogger.data.db.entities.Workout;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;

import java.util.HashSet;
import java.util.Set;

public class WorkoutDeleteResolver extends DeleteResolver<Workout> {
    @NonNull
    @Override
    public DeleteResult performDelete(@NonNull StorIOSQLite storIOSQLite, @NonNull Workout workout) {

        //TODO
        final Set<String> affectedTables = new HashSet<>(4);

        //TODO alle anderen betroffenen Tabellen hinzufügen
        return DeleteResult.newInstance(4, affectedTables);
    }
}
