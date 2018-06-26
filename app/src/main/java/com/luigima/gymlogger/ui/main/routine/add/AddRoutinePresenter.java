package com.luigima.gymlogger.ui.main.routine.add;

import android.support.annotation.NonNull;

import com.luigima.gymlogger.data.DataManager;
import com.luigima.gymlogger.data.db.entities.Routine;
import com.luigima.gymlogger.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

class AddRoutinePresenter extends BasePresenter<AddRoutineMvpView> {

    @Inject
    DataManager dataManager;

    private Subscription subscription;

    @Inject
    public AddRoutinePresenter() {
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) subscription.unsubscribe();
    }

    public void unsubscribe() {
        if (subscription != null) subscription.unsubscribe();
    }

    public void createNewRoutine(@NonNull String title, String description) {
        checkViewAttached();
        Routine routine = Routine.newRoutine(0, title, description, 0, 0, 5, 0f, 1, null);
        dataManager.putRoutine(routine)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> getMvpView().onRoutineCreated(result.insertedId().intValue()),
                        throwable -> Timber.e(throwable.getMessage()));

    }

    public void removeExercise(long routineId, int split, int exerciseId) {
        checkViewAttached();
        //TODO
        Timber.e("Not implemented yet");
    }

    public void getRoutine(int routineId) {
        checkViewAttached();
        dataManager.getRoutineById(routineId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(routine -> getMvpView().onRoutineLoaded(routine),
                        throwable -> Timber.e(throwable.getMessage()));
    }

    public void saveRoutine(Routine routine) {
        checkViewAttached();
        dataManager.saveRoutine(routine)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(putResult -> {
                            getRoutine(routine.getRoutineID());
                            Timber.d("Routine update: %s, numberOfRowsUpdated: %s",
                                    putResult.wasUpdated(), putResult.numberOfRowsUpdated());
                        },
                        throwable -> Timber.e(throwable.getMessage()));
    }
}
