package com.luigima.gymlogger.ui.main.routine.details;

import com.luigima.gymlogger.data.DataManager;
import com.luigima.gymlogger.data.db.entities.Routine;
import com.luigima.gymlogger.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

class RoutineDetailsPresenter extends BasePresenter<RoutineDetailsMvpView> {

    @Inject
    DataManager dataManager;

    private Subscription subscription;

    @Inject
    public RoutineDetailsPresenter() {
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) subscription.unsubscribe();
    }

    public void loadRoutine(int routineId) {
        checkViewAttached();
        subscription = dataManager.getRoutineById(routineId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(routine -> getMvpView().onRoutineLoaded(routine),
                        throwable -> Timber.e(throwable.getMessage()));

    }

    public void deleteRoutine(int routineId) {
        checkViewAttached();
        subscription = dataManager.getRoutineById(routineId)
                .flatMap(routine -> dataManager.deleteRoutine(routine))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result.numberOfRowsDeleted() > 0) {
                                Timber.d("Routine with id %d got deleted!", routineId);
                                getMvpView().onRoutineDeleted(routineId);
                            } else Timber.e("Routine with id %d did not get deleted!", routineId);
                        },
                        throwable -> Timber.e(throwable.getMessage()));

    }

    public void startNewWorkout(Routine routine, int splitId) {
        checkViewAttached();
        subscription = dataManager.addWorkout(routine, splitId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result.wasInserted()) {
                                getMvpView().onNewWorkoutStarted(result.insertedId().intValue(), splitId);
                            } else Timber.e("Workout not created!");
                        },
                        throwable -> Timber.e(throwable.getMessage()));
    }

}
