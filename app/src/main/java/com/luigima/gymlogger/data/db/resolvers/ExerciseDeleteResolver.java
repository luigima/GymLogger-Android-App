package com.luigima.gymlogger.data.db.resolvers;

import android.support.annotation.NonNull;

import com.luigima.gymlogger.data.db.entities.Exercise;
import com.luigima.gymlogger.data.db.tables.ExerciseHasEquipmentTable;
import com.luigima.gymlogger.data.db.tables.ExerciseTable;
import com.luigima.gymlogger.data.db.tables.ExerciseTextTranslateTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;

import java.util.HashSet;
import java.util.Set;

public class ExerciseDeleteResolver extends DeleteResolver<Exercise> {
    @NonNull
    @Override
    public DeleteResult performDelete(@NonNull StorIOSQLite storIOSQLite, @NonNull Exercise exercise) {
        storIOSQLite
                .delete()
                .object(exercise)
                .prepare()
                .executeAsBlocking();

        final Set<String> affectedTables = new HashSet<>(3);
        affectedTables.add(ExerciseTextTranslateTable.TABLE);
        affectedTables.add(ExerciseTable.TABLE);
        affectedTables.add(ExerciseHasEquipmentTable.TABLE);
        //TODO alle anderen betroffenen Tabellen hinzufügen
        return DeleteResult.newInstance(1, affectedTables);
    }
}
