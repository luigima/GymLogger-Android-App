package com.luigima.gymlogger.ui.main.routine.add;

import com.luigima.gymlogger.data.db.entities.Routine;
import com.luigima.gymlogger.ui.base.MvpView;


interface AddRoutineMvpView extends MvpView {
    void showError();

    void onRoutineCreated(int id);
    void onRoutineLoaded(Routine routine);
    void onSplitRenamed(boolean wasUpdated);
}
