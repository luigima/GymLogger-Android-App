package com.luigima.gymlogger.data.db.resolvers;

import android.support.annotation.NonNull;

import com.luigima.gymlogger.data.db.entities.Routine;
import com.luigima.gymlogger.data.db.entities.sql.DBRoutine;
import com.luigima.gymlogger.data.db.entities.sql.DBRoutineExerciseHasSets;
import com.luigima.gymlogger.data.db.entities.sql.DBRoutineHasSplits;
import com.luigima.gymlogger.data.db.entities.sql.DBRoutineTextTranslate;
import com.luigima.gymlogger.data.db.entities.sql.DBSplit;
import com.luigima.gymlogger.data.db.entities.sql.DBSplitHasExercises;
import com.luigima.gymlogger.data.db.tables.RoutineExerciseHasSetsTable;
import com.luigima.gymlogger.data.db.tables.RoutineHasSplitsTable;
import com.luigima.gymlogger.data.db.tables.RoutineTable;
import com.luigima.gymlogger.data.db.tables.RoutineTextTranslateTable;
import com.luigima.gymlogger.data.db.tables.SplitHasExercisesTable;
import com.luigima.gymlogger.data.db.tables.SplitTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoutinePutResolver extends PutResolver<Routine> {
    @NonNull
    @Override
    public PutResult performPut(@NonNull StorIOSQLite storIOSQLite, @NonNull Routine routine) {
        final List<Object> objectsToPut = new ArrayList<>(1);

        PutResult putResult = putRoutine(storIOSQLite, routine);
        if (putResult.wasInserted()) {
            routine.setRoutineID(putResult.insertedId().intValue());
        }

        if (routine.getSplits() != null) {
            for (Routine.RoutineSplit split : routine.getSplits()) {
                split = putSplit(storIOSQLite, split);
                objectsToPut.add(new DBRoutineHasSplits(routine.getRoutineID(), split.getSplitId()));

                for (Routine.RoutineExercise exercise :
                        split.getExercises()) {

                    exercise = putSplitExercise(storIOSQLite, exercise, routine.getRoutineID(), split.getSplitId());

                    for (Routine.RoutineSet set :
                            exercise.getSets()) {
                        if (set.getSetId() == null) {
                            int setId = storIOSQLite.get().listOfObjects(DBRoutineExerciseHasSets.class).withQuery(
                                    Query.builder().table(RoutineExerciseHasSetsTable.TABLE)
                                            .where(RoutineExerciseHasSetsTable.COLUMN_SPLIT_HAS_EXERCISES_ID
                                                    + "=" + exercise.getSplitHasExerciseId()).build()
                            ).prepare().executeAsBlocking().size();
                            set.setSetId(setId);
                        }
                        storIOSQLite.put().object(
                                new DBRoutineExerciseHasSets(set.getSetId(), exercise.getSplitHasExerciseId(), set.getRepeatsShould(), set.getWeightShould(), set.getDurationShould())
                        ).prepare().executeAsBlocking();
                    }
                }
            }
        }


        objectsToPut.add(
                new DBRoutineTextTranslate(
                        routine.getRoutineID(),
                        0, //TODO change languageID
                        routine.getTitle(),
                        routine.getDescription()));
        storIOSQLite
                .put()
                .objects(objectsToPut)
                .prepare()
                .executeAsBlocking();

        final Set<String> affectedTables = new HashSet<>(6);
        affectedTables.add(RoutineTable.TABLE);
        affectedTables.add(RoutineHasSplitsTable.TABLE);
        affectedTables.add(SplitHasExercisesTable.TABLE);
        affectedTables.add(RoutineExerciseHasSetsTable.TABLE);
        affectedTables.add(RoutineTextTranslateTable.TABLE);
        affectedTables.add(SplitTable.TABLE);

        if (putResult.insertedId() != null &&
                putResult.wasInserted()) {
            //new Routine got inserted!
            return PutResult.newInsertResult(routine.getRoutineID(), affectedTables);
        }
        return PutResult.newUpdateResult(affectedTables.size(), affectedTables);
    }

    private Routine.RoutineSplit putSplit(StorIOSQLite storIOSQLite, Routine.RoutineSplit split) {
        DBSplit dbSplit = new DBSplit(split.getSplitId(), split.getTitle());
        PutResult putResult = storIOSQLite.put().object(dbSplit).prepare().executeAsBlocking();
        if (putResult.wasInserted()) {
            split.setSplitId(putResult.insertedId().intValue());
        }
        return split;
    }

    private PutResult putRoutine(StorIOSQLite storIOSQLite, Routine routine) {
        DBRoutine dbRoutine = new DBRoutine(
                routine.getRoutineID(),
                routine.getRoutineCategoryId(),
                routine.getCreatorId(),
                routine.getCreationDate(),
                routine.getDifficulty(),
                routine.getRating(),
                routine.isLocal());
        PutResult putResult = storIOSQLite.put().object(dbRoutine).prepare().executeAsBlocking();

        return putResult;
    }

    private Routine.RoutineExercise putSplitExercise(StorIOSQLite storIOSQLite, Routine.RoutineExercise exercise, int routineId, int splitId) {
        DBSplitHasExercises dbSplitHasExercises =
                new DBSplitHasExercises(exercise.getSplitHasExerciseId(), routineId,
                        exercise.getExercise().getId(), splitId, exercise.getOrderNr());
        PutResult putResult = storIOSQLite.put().object(dbSplitHasExercises).prepare().executeAsBlocking();
        if (putResult.wasInserted()) {
            exercise.setSplitHasExerciseId(putResult.insertedId().intValue());
        }
        return exercise;
    }
}
