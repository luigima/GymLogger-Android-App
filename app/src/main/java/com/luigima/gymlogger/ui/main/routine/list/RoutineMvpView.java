package com.luigima.gymlogger.ui.main.routine.list;

import com.luigima.gymlogger.data.db.entities.Routine;
import com.luigima.gymlogger.ui.base.MvpView;

import java.util.List;

interface RoutineMvpView extends MvpView {
    void showError();
    void showRoutines(List<Routine> routines);
    void setData(List<Routine> routines);
    void addRoutine(Routine routine);
    void clearRoutines();
}
