package com.luigima.gymlogger.ui.main.exercise;

import com.luigima.gymlogger.data.db.entities.Exercise;
import com.luigima.gymlogger.ui.base.MvpView;


interface DetailsMvpView extends MvpView {
    void showError();

    void showExercise(Exercise exercise);
}
