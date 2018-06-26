package com.luigima.gymlogger.data;

import com.luigima.gymlogger.dagger.PerApp;
import com.luigima.gymlogger.data.db.DatabaseHelper;
import com.luigima.gymlogger.data.db.entities.Exercise;
import com.luigima.gymlogger.data.db.entities.MainMuscleGroup;
import com.luigima.gymlogger.data.db.entities.Routine;
import com.luigima.gymlogger.data.db.entities.Workout;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

@PerApp
public class DataManager {
    private final DatabaseHelper databaseHelper;

    @Inject
    public DataManager(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public Observable<List<Exercise>> getExercises() {
        return databaseHelper.getExercises();
    }

    public Observable<Exercise> getExercise(int id) {
        return databaseHelper.getExercise(id);
    }


    public Observable<List<Exercise>> getExercisesByCategory(int id) {
        return databaseHelper.getExercisesByCategory(id);
    }

    public Observable<List<MainMuscleGroup>> getMainExercisesCategories() {
        return databaseHelper.getMainExercisesCategories();
    }

    public Observable<List<Routine>> getRoutines() {
        return databaseHelper.getRoutines();
    }

    public Observable<Routine> getRoutineById(int id) {
        return databaseHelper.getRoutine(id);
    }

    public Observable<DeleteResult> deleteRoutine(Routine routine) {
        return databaseHelper.deleteRoutine(routine);
    }

    public Observable<PutResult> putRoutine(Routine routine) {
        return databaseHelper.putRoutine(routine);
    }

    public Observable<PutResult> putRoutineExercise(int routineId, int split, int exerciseId) {
        return databaseHelper.putRoutineExercise(routineId, split, exerciseId);
    }

    public Observable<PutResult> saveRoutine(Routine routine) {
        return databaseHelper.saveRoutine(routine);
    }

    public Observable<PutResult> saveWorkout(Workout workout) {
        return databaseHelper.saveWorkout(workout);
    }

    public Observable<PutResult> saveWorkoutSet(int workoutId, Workout.WorkoutExercise exercise, Workout.WorkoutSet set) {
        return databaseHelper.saveWorkoutSet(workoutId, exercise, set);
    }

    public Observable<PutResult> addWorkout(Routine routine, int splitId) {
        return databaseHelper.addWorkout(routine, splitId);
    }

    public Observable<Workout> getWorkout(int workoutId) {
        return databaseHelper.getWorkout(workoutId);
    }

    public Observable<Workout.WorkoutExercise> getWorkoutExercise(int workoutId, int splitHasExercisesId) {
        return databaseHelper.getWorkoutExercise(workoutId, splitHasExercisesId);
    }
}
