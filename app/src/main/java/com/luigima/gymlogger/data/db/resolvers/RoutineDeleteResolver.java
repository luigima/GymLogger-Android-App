package com.luigima.gymlogger.data.db.resolvers;

import android.support.annotation.NonNull;

import com.luigima.gymlogger.data.db.entities.Routine;
import com.luigima.gymlogger.data.db.tables.RoutineHasSplitsTable;
import com.luigima.gymlogger.data.db.tables.RoutineTable;
import com.luigima.gymlogger.data.db.tables.RoutineTextTranslateTable;
import com.luigima.gymlogger.data.db.tables.SplitHasExercisesTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;

import java.util.HashSet;
import java.util.Set;

public class RoutineDeleteResolver extends DeleteResolver<Routine> {
    @NonNull
    @Override
    public DeleteResult performDelete(@NonNull StorIOSQLite storIOSQLite, @NonNull Routine routine) {

        /*
        DBRoutine dbRoutine = new DBRoutine(routine.getId(), routine.getRoutineCategoryId(),
                routine.getCreatorId(), routine.getCreationDate(), routine.getDifficulty(),
                routine.getRating(), routine.getIsLocal());

        storIOSQLite
                .delete()
                .object(dbRoutine)
                .prepare()
                .executeAsBlocking();

        storIOSQLite
                .delete()
                .byQuery(DeleteQuery.builder()
                        .table(SplitHasExercisesTable.TABLE)
                        .where(SplitHasExercisesTable.COLUMN_ROUTINE_ID + "=" + routine.getId())
                        .build())
                .prepare()
                .executeAsBlocking();

        storIOSQLite
                .delete()
                .byQuery(DeleteQuery.builder()
                        .table(RoutineTextTranslateTable.TABLE)
                        .where(RoutineTextTranslateTable.COLUMN_ROUTINE_ID + "=" + routine.getId())
                        .build())
                .prepare()
                .executeAsBlocking();

        storIOSQLite
                .delete()
                .byQuery(DeleteQuery.builder()
                        .table(RoutineHasSplitsTable.TABLE)
                        .where(RoutineHasSplitsTable.COLUMN_ROUTINE_ID + "=" + routine.getId())
                        .build())
                .prepare()
                .executeAsBlocking();
*/
        //TODO
        final Set<String> affectedTables = new HashSet<>(4);
        affectedTables.add(RoutineTextTranslateTable.TABLE);
        affectedTables.add(RoutineTable.TABLE);
        affectedTables.add(SplitHasExercisesTable.TABLE);
        affectedTables.add(RoutineHasSplitsTable.TABLE);

        //TODO alle anderen betroffenen Tabellen hinzufügen
        return DeleteResult.newInstance(4, affectedTables);
    }
}
