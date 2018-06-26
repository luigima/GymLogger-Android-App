package com.luigima.gymlogger.data.db.resolvers;

import android.support.annotation.NonNull;

import com.luigima.gymlogger.data.db.entities.Workout;
import com.luigima.gymlogger.data.db.entities.sql.DBWorkout;
import com.luigima.gymlogger.data.db.entities.sql.DBWorkoutTrainsExercises;
import com.luigima.gymlogger.data.db.tables.WorkoutTable;
import com.luigima.gymlogger.data.db.tables.WorkoutTrainsExerciseTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorkoutPutResolver extends PutResolver<Workout> {
    @NonNull
    @Override
    public PutResult performPut(@NonNull StorIOSQLite storIOSQLite, @NonNull Workout workout) {
        final List<Object> objectsToPut = new ArrayList<>(1);

        boolean wasInserted = false;

        PutResult putResult = putWorkout(storIOSQLite, workout);
        if (putResult.wasInserted()) {
            workout.setWorkoutId(putResult.insertedId().intValue());
            wasInserted = true;
        }

        if(workout.getExercises() != null) {
            for (Workout.WorkoutExercise exercise :
                    workout.getExercises()) {
                for (Workout.WorkoutSet set :
                        exercise.getSets()) {
                    putExerciseSet(storIOSQLite, workout.getWorkoutId(), exercise, set);
                }
            }
        }

        //TODO remove routine if it is flagged as a copy and isnt used anymore
        final Set<String> affectedTables = new HashSet<>(3);
        affectedTables.add(WorkoutTable.TABLE);
        affectedTables.add(WorkoutTrainsExerciseTable.TABLE);

        if(wasInserted) {
            return PutResult.newInsertResult(workout.getWorkoutId(), affectedTables);
        }
        return PutResult.newUpdateResult(affectedTables.size(), affectedTables);
    }

    private PutResult putWorkout(StorIOSQLite storIOSQLite, Workout workout) {
        DBWorkout dbWorkout = new DBWorkout(
                workout.getWorkoutId(),
                workout.getRoutineId(),
                workout.getSplitId(),
                workout.getDate(),
                workout.getDuration(),
                workout.getNotes());

        PutResult putResult = storIOSQLite.put().object(dbWorkout).prepare().executeAsBlocking();
        return putResult;
    }

    private Workout.WorkoutSet putExerciseSet(StorIOSQLite storIOSQLite, int workoutId, Workout.WorkoutExercise exercise, Workout.WorkoutSet set) {
        DBWorkoutTrainsExercises dbWorkoutTrainsExercises =
                new DBWorkoutTrainsExercises(
                        workoutId,
                        set.getSetId(),
                        exercise.getSplitHasExercisesId(),
                        set.getWeightIs(),
                        set.getRepeatsIs(),
                        set.getDurationIs(),
                        set.getDate(),
                        exercise.getNotes());
        storIOSQLite.put().object(dbWorkoutTrainsExercises).prepare().executeAsBlocking();
        return set;
    }
}
