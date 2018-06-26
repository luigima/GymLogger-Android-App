package com.luigima.gymlogger.ui.main.workout.overview;

import com.luigima.gymlogger.data.db.entities.Workout;
import com.luigima.gymlogger.ui.base.MvpView;


interface ActiveWorkoutListMvpView extends MvpView {
    void showError();

    void onWorkoutLoaded(Workout workout);
    void onWorkoutExerciseLoaded(Workout.WorkoutExercise exercise);
}
