package com.luigima.gymlogger.ui.main.routine.details;

import com.luigima.gymlogger.data.db.entities.Routine;
import com.luigima.gymlogger.ui.base.MvpView;


interface RoutineDetailsMvpView extends MvpView {
    void showError();

    void onRoutineLoaded(Routine routine);

    void onRoutineDeleted(int routineId);

    void onNewWorkoutStarted(int workoutId, int split); //TODO CHANGE THIS!
}
