package com.luigima.gymlogger.ui.main.exercise;

import com.luigima.gymlogger.data.db.entities.MainMuscleGroup;
import com.luigima.gymlogger.ui.base.MvpView;

import java.util.List;

interface MuscleCategoryMvpView extends MvpView {
    void showError();
    void showCategories(List<MainMuscleGroup> mData);
}
