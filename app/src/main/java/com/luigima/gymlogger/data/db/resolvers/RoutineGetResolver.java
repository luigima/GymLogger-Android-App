package com.luigima.gymlogger.data.db.resolvers;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.luigima.gymlogger.data.db.entities.Exercise;
import com.luigima.gymlogger.data.db.entities.Routine;
import com.luigima.gymlogger.data.db.entities.sql.DBRoutineExerciseHasSets;
import com.luigima.gymlogger.data.db.entities.sql.DBRoutineHasSplits;
import com.luigima.gymlogger.data.db.entities.sql.DBSplit;
import com.luigima.gymlogger.data.db.entities.sql.DBSplitHasExercises;
import com.luigima.gymlogger.data.db.relations.ExerciseRelation;
import com.luigima.gymlogger.data.db.tables.RoutineExerciseHasSetsTable;
import com.luigima.gymlogger.data.db.tables.RoutineHasSplitsTable;
import com.luigima.gymlogger.data.db.tables.RoutineTable;
import com.luigima.gymlogger.data.db.tables.RoutineTextTranslateTable;
import com.luigima.gymlogger.data.db.tables.SplitHasExercisesTable;
import com.luigima.gymlogger.data.db.tables.SplitTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.get.GetResolver;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;

import java.util.ArrayList;
import java.util.List;

public class RoutineGetResolver extends GetResolver<Routine> {

    @NonNull
    private final ThreadLocal<StorIOSQLite> storIOSQLiteFromPerformGet =
            new ThreadLocal<>();

    public RoutineGetResolver() {
    }


    @NonNull
    @Override
    public Routine mapFromCursor(@NonNull Cursor cursor) {
        final StorIOSQLite storIOSQLite = storIOSQLiteFromPerformGet.get();
        final int routineId = cursor.getInt(cursor.getColumnIndex(RoutineTable.COLUMN_ID));

        //TODO DIRTY!
        List<Routine.RoutineSplit> splits = new ArrayList<>(3);

        List<DBRoutineHasSplits> routineSplits =
                storIOSQLite
                        .get()
                        .listOfObjects(DBRoutineHasSplits.class)
                        .withQuery(Query.builder()
                                .table(RoutineHasSplitsTable.TABLE)
                                .where(RoutineHasSplitsTable.COLUMN_ROUTINE_ID + "=" + routineId)
                                .build())
                        .prepare()
                        .executeAsBlocking();

        for (int i = 0; i < routineSplits.size(); i++) {
            List<DBSplitHasExercises> splitExercises =
                    storIOSQLite
                            .get()
                            .listOfObjects(DBSplitHasExercises.class)
                            .withQuery(Query.builder()
                                    .table(SplitHasExercisesTable.TABLE)
                                    .where(SplitHasExercisesTable.COLUMN_ROUTINE_ID + "=" + routineId + " AND " +
                                            SplitHasExercisesTable.COLUMN_SPLIT_ID + "=" + routineSplits.get(i).getSplitId())
                                    .orderBy(SplitHasExercisesTable.COLUMN_ORDER_NUMBER)
                                    .build())
                            .prepare()
                            .executeAsBlocking();

            List<Routine.RoutineExercise> exercises = new ArrayList<>();
            for (int k = 0; k < splitExercises.size(); k++) {
                List<DBRoutineExerciseHasSets> exerciseSet =
                        storIOSQLite
                                .get()
                                .listOfObjects(DBRoutineExerciseHasSets.class)
                                .withQuery(Query.builder()
                                        .table(RoutineExerciseHasSetsTable.TABLE)
                                        .where(RoutineExerciseHasSetsTable.COLUMN_SPLIT_HAS_EXERCISES_ID + "=" + splitExercises.get(k).getId())
                                        .build())
                                .prepare()
                                .executeAsBlocking();


                //TODO does it handle other languages?
                Exercise exercise = storIOSQLite.get()
                        .listOfObjects(Exercise.class)
                        .withQuery(ExerciseRelation.getById(splitExercises.get(k).getExerciseId()))
                        .prepare()
                        .executeAsBlocking()
                        .get(0);

                List<Routine.RoutineSet> sets = new ArrayList<>(exerciseSet.size());
                for (DBRoutineExerciseHasSets set :
                        exerciseSet) {
                    sets.add(new Routine.RoutineSet(set.getSetId(), set.getRepeats(), set.getWeight(), set.getDuration()));
                }
                exercises.add(new Routine.RoutineExercise(splitExercises.get(k).getId(), exercise, splitExercises.get(k).getOrderNr(), sets));
            }

            DBSplit split = storIOSQLite.get()
                    .listOfObjects(DBSplit.class)
                    .withQuery(Query.builder().table(SplitTable.TABLE)
                            .where(SplitTable.COLUMN_ID + "=" + routineSplits.get(i).getSplitId())
                            .build())
                    .prepare()
                    .executeAsBlocking()
                    .get(0);
            splits.add(new Routine.RoutineSplit(routineSplits.get(i).getSplitId(), exercises, split.getTitle()));
        }

        return new Routine(
                routineId,
                cursor.getInt(cursor.getColumnIndex(RoutineTable.COLUMN_CATEGORY_ID)),
                cursor.getString(cursor.getColumnIndex(RoutineTextTranslateTable.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(RoutineTextTranslateTable.COLUMN_DESCRIPTION)),
                cursor.getInt(cursor.getColumnIndex(RoutineTable.COLUMN_CREATOR_ID)),
                cursor.getLong(cursor.getColumnIndex(RoutineTable.COLUMN_CREATION_DATE)),
                cursor.getInt(cursor.getColumnIndex(RoutineTable.COLUMN_DIFFICULTY)),
                cursor.getFloat(cursor.getColumnIndex(RoutineTable.COLUMN_RATING)),
                cursor.getInt(cursor.getColumnIndex(RoutineTable.COLUMN_IS_LOCAL)),
                splits
        );
    }

    @NonNull
    @Override
    public Cursor performGet(@NonNull StorIOSQLite storIOSQLite, @NonNull RawQuery rawQuery) {
        storIOSQLiteFromPerformGet.set(storIOSQLite);
        return storIOSQLite.internal().rawQuery(rawQuery);
    }

    @NonNull
    @Override
    public Cursor performGet(@NonNull StorIOSQLite storIOSQLite, @NonNull Query query) {
        storIOSQLiteFromPerformGet.set(storIOSQLite);
        return storIOSQLite.internal().query(query);
    }
}
