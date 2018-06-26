package com.luigima.gymlogger.data.db;

import com.luigima.gymlogger.data.db.entities.Exercise;
import com.luigima.gymlogger.data.db.entities.MainMuscleGroup;
import com.luigima.gymlogger.data.db.entities.Routine;
import com.luigima.gymlogger.data.db.entities.Workout;
import com.luigima.gymlogger.data.db.entities.sql.DBSplitHasExercises;
import com.luigima.gymlogger.data.db.entities.sql.DBWorkoutTrainsExercises;
import com.luigima.gymlogger.data.db.relations.ExerciseRelation;
import com.luigima.gymlogger.data.db.relations.RoutineRelation;
import com.luigima.gymlogger.data.db.tables.MainMuscleGroupTable;
import com.luigima.gymlogger.data.db.tables.WorkoutTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import timber.log.Timber;

public class DatabaseHelper {

    @Inject
    StorIOSQLite storIOSQLite;

    public DatabaseHelper(StorIOSQLite storIOSQLite) {
        this.storIOSQLite = storIOSQLite;
    }

    public Observable<List<Exercise>> getExercises() {
        return storIOSQLite
                .get()
                .listOfObjects(Exercise.class)
                .withQuery(ExerciseRelation.getAll())
                .prepare()
                .createObservable();
    }

    public Observable<Exercise> getExercise(int id) {
        return storIOSQLite
                .get()
                .listOfObjects(Exercise.class)
                .withQuery(ExerciseRelation.getById(id))
                .prepare()
                .createObservable()
                .flatMap(Observable::from)
                .first();
    }

    public Observable<List<Exercise>> getExercisesByCategory(int categoryId) {
        return storIOSQLite
                .get()
                .listOfObjects(Exercise.class)
                .withQuery(ExerciseRelation.getByCategory(categoryId))
                .prepare()
                .createObservable();
    }

    public Observable<List<MainMuscleGroup>> getMainExercisesCategories() {
        return storIOSQLite
                .get()
                .listOfObjects(MainMuscleGroup.class)
                .withQuery(Query.builder().table(MainMuscleGroupTable.TABLE).build())
                .prepare()
                .createObservable();
    }

    public Observable<List<Routine>> getRoutines() {
        return storIOSQLite
                .get()
                .listOfObjects(Routine.class)
                .withQuery(RoutineRelation.getAll().build())
                .prepare()
                .createObservable();
    }

    public Observable<Routine> getRoutine(int id) {
        return storIOSQLite
                .get()
                .listOfObjects(Routine.class)
                .withQuery(RoutineRelation.getById(id).build())
                .prepare()
                .createObservable()
                //only one
                .map(routines -> routines.get(0));
    }

    public Observable<PutResult> putRoutine(Routine routine) {
        return storIOSQLite
                .put()
                .object(routine)
                .prepare()
                .createObservable();
    }

    public Observable<DeleteResult> deleteRoutine(Routine routine) {
        return storIOSQLite
                .delete()
                .object(routine)
                .prepare()
                .createObservable();
    }

    public Observable<PutResult> saveRoutine(Routine routine) {
        return storIOSQLite
                .put()
                .object(routine)
                .prepare()
                .createObservable();
    }

    public Observable<PutResult> saveWorkout(Workout workout) {
        return storIOSQLite
                .put()
                .object(workout)
                .prepare()
                .createObservable();
    }

    public Observable<PutResult> saveWorkoutSet(int workoutId, Workout.WorkoutExercise exercise, Workout.WorkoutSet set) {
        DBWorkoutTrainsExercises dbSet = new DBWorkoutTrainsExercises(
                workoutId,
                set.getSetId(),
                exercise.getSplitHasExercisesId(),
                set.getWeightIs(),
                set.getRepeatsIs(),
                set.getDurationIs(),
                set.getDate(),
                exercise.getNotes()
        );
        return storIOSQLite
                .put()
                .object(dbSet)
                .prepare()
                .createObservable();
    }

    public Observable<PutResult> putRoutineExercise(int routineId, int split, int exerciseId) {
        DBSplitHasExercises putObject =
                new DBSplitHasExercises(
                        null,
                        routineId,   // routineId;
                        exerciseId,  // exerciseId;
                        split,
                        0/* TODO */        // orderNr;
                );
        return storIOSQLite
                .put()
                .object(putObject)
                .prepare()
                .createObservable();
    }

    public Observable<PutResult> addWorkout(Routine routine, int splitId) {
        Routine.RoutineSplit split = routine.getSplitbyId(splitId);
        List<Workout.WorkoutExercise> workoutExercises = new ArrayList<>();
        if (split == null) {
            Timber.e("invalid split");
        }

        for (Routine.RoutineExercise routineExercise : split.getExercises()) {
            List<Workout.WorkoutSet> sets = new ArrayList<>(3);
            for (Routine.RoutineSet set : routineExercise.getSets()) {
                sets.add(new Workout.WorkoutSet(
                        set.getSetId(),
                        set.getRepeatsShould(), 0,
                        set.getWeightShould(), 0,
                        set.getDurationShould(), 0,
                        111));
            }
            workoutExercises.add(new Workout.WorkoutExercise(
                    routineExercise.getSplitHasExerciseId(),
                    routineExercise.getExercise(),
                    routineExercise.getOrderNr(),
                    "",
                    sets));
        }

        Workout workout = Workout.newWorkout(routine.getRoutineID(), 0, "", workoutExercises, splitId);
        return storIOSQLite
                .put()
                .object(workout)
                .prepare()
                .createObservable();
    }

    public Observable<Workout> getWorkout(int workoutId) {
        return storIOSQLite
                .get()
                .listOfObjects(Workout.class)
                .withQuery(Query.builder()
                        .table(WorkoutTable.TABLE)
                        .where(WorkoutTable.COLUMN_ID + "=" + workoutId)
                        .build())
                .prepare()
                .createObservable()
                //only one
                .map(workouts -> workouts.get(0));
    }

    public Observable<Workout.WorkoutExercise> getWorkoutExercise(int workoutId, int splitHasExercisesId) {
        return getWorkout(workoutId)
                .map(workout -> workout.getExerciseBySplitHasExerciseId(splitHasExercisesId));
    }
}
