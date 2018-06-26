package com.luigima.gymlogger.data.db.resolvers;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.luigima.gymlogger.data.db.entities.Exercise;
import com.luigima.gymlogger.data.db.entities.Workout;
import com.luigima.gymlogger.data.db.entities.sql.DBSplit;
import com.luigima.gymlogger.data.db.entities.sql.DBSplitHasExercises;
import com.luigima.gymlogger.data.db.entities.sql.DBWorkoutTrainsExercises;
import com.luigima.gymlogger.data.db.relations.ExerciseRelation;
import com.luigima.gymlogger.data.db.tables.SplitHasExercisesTable;
import com.luigima.gymlogger.data.db.tables.SplitTable;
import com.luigima.gymlogger.data.db.tables.WorkoutTable;
import com.luigima.gymlogger.data.db.tables.WorkoutTrainsExerciseTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.get.GetResolver;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;

import java.util.ArrayList;
import java.util.List;

public class WorkoutGetResolver extends GetResolver<Workout> {

    @NonNull
    private final ThreadLocal<StorIOSQLite> storIOSQLiteFromPerformGet =
            new ThreadLocal<>();

    @NonNull
    @Override
    public Workout mapFromCursor(@NonNull Cursor cursor) {
        final StorIOSQLite storIOSQLite = storIOSQLiteFromPerformGet.get();
        final int workoutId = cursor.getInt(cursor.getColumnIndex(WorkoutTable.COLUMN_ID));
        int splitId = cursor.getInt(cursor.getColumnIndex(WorkoutTable.COLUMN_SPLIT_ID));


        List<Workout.WorkoutExercise> exercises = new ArrayList<>(7);

        List<DBWorkoutTrainsExercises> workoutExercises =
                storIOSQLite.get()
                        .listOfObjects(DBWorkoutTrainsExercises.class)
                        .withQuery(Query.builder().table(WorkoutTrainsExerciseTable.TABLE)
                                .where(WorkoutTrainsExerciseTable.COLUMN_WORKOUT_ID + "=" + workoutId).build())
                        .prepare()
                        .executeAsBlocking();

        for (DBWorkoutTrainsExercises workoutExercise :
                workoutExercises) {
            int splitHasExerciseId = workoutExercise.getSetHasExerciseId();
            List<Workout.WorkoutSet> sets = new ArrayList<>(3);

            Workout.WorkoutExercise wExercise = getExerciseBySplitHasExerciseId(exercises, splitHasExerciseId);
            int repeatsShould = 0, durationshould = 0;
            float weightShould = 0;

            if (wExercise != null) {
                //TODO add routineHasSets information

                wExercise.getSets().add(new Workout.WorkoutSet(workoutExercise.getSetID(),
                        repeatsShould,workoutExercise.getRepeats(),
                        weightShould, workoutExercise.getWeight(),
                        durationshould, workoutExercise.getDuration(),
                        workoutExercise.getDate()));
            } else {
                DBSplitHasExercises splitExerciseInformation =
                        storIOSQLite.get()
                                .listOfObjects(DBSplitHasExercises.class)
                                .withQuery(Query.builder().table(SplitHasExercisesTable.TABLE)
                                        .where(SplitHasExercisesTable.COLUMN_ID + "=" + workoutExercise.getSetHasExerciseId()).build())
                                .prepare()
                                .executeAsBlocking()
                                .get(0); //TODO dirty dirty dirty..

                Exercise exercise = storIOSQLite.get()
                        .listOfObjects(Exercise.class)
                        .withQuery(ExerciseRelation.getById(splitExerciseInformation.getExerciseId()))
                        .prepare()
                        .executeAsBlocking()
                        .get(0);

                sets.add(new Workout.WorkoutSet(workoutExercise.getSetID(),
                        repeatsShould,workoutExercise.getRepeats(),
                        weightShould, workoutExercise.getWeight(),
                        durationshould, workoutExercise.getDuration(),
                        workoutExercise.getDate()));

                exercises.add(new Workout.WorkoutExercise(
                        workoutExercise.getSetHasExerciseId(),
                        exercise,
                        splitExerciseInformation.getOrderNr(),
                        workoutExercise.getNotes(),
                        sets));
            }
        }

        DBSplit split = storIOSQLite.get().listOfObjects(DBSplit.class)
                .withQuery(Query.builder().table(SplitTable.TABLE).where(SplitTable.COLUMN_ID + "=" + splitId).build())
                .prepare().executeAsBlocking()
                .get(0); //TODO MUCH DIRTY MUCH SHIT
        
        return new Workout(
                workoutId,
                cursor.getInt(cursor.getColumnIndex(WorkoutTable.COLUMN_ROUTINE_ID)),
                cursor.getInt(cursor.getColumnIndex(WorkoutTable.COLUMN_DURATION)),
                cursor.getLong(cursor.getColumnIndex(WorkoutTable.COLUMN_DATE)),
                cursor.getString(cursor.getColumnIndex(WorkoutTable.COLUMN_NOTES)),
                exercises,
                splitId,
                split.getTitle());
    }

    private Workout.WorkoutExercise getExerciseBySplitHasExerciseId(List<Workout.WorkoutExercise> exercises, int splitHasExerciseId) {
        for (Workout.WorkoutExercise exercise :
                exercises) {
            if (exercise.getSplitHasExercisesId() == splitHasExerciseId) {
                return exercise;
            }
        }
        return null;
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
