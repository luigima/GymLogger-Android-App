package com.luigima.gymlogger.ui.main.exercise;

import com.luigima.gymlogger.data.db.entities.Exercise;
import com.luigima.gymlogger.ui.base.MvpView;

import java.util.List;

interface ExerciseMvpView extends MvpView {
    void showError();
    void showExercises(List<Exercise> mData);
    void addExercises(List<Exercise> mData);
    void searchExercises(String searchQuery);
}
