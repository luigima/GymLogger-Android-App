package com.luigima.gymlogger.ui.main.workout.overview;

import com.luigima.gymlogger.data.DataManager;
import com.luigima.gymlogger.data.db.entities.Workout;
import com.luigima.gymlogger.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

class ActiveWorkoutListPresenter extends BasePresenter<ActiveWorkoutListMvpView> {

    @Inject
    DataManager dataManager;

    private Subscription subscription;

    @Inject
    public ActiveWorkoutListPresenter() {
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) subscription.unsubscribe();
    }

    public void loadWorkout(int workoutId) {
        checkViewAttached();
        subscription = dataManager.getWorkout(workoutId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(workout -> getMvpView().onWorkoutLoaded(workout),
                        throwable -> Timber.e(throwable.getMessage()));
    }

    public void getWorkoutExercise(int workoutId, int splitHasExercisesId) {
        checkViewAttached();
        dataManager.getWorkoutExercise(workoutId, splitHasExercisesId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(exercise -> getMvpView().onWorkoutExerciseLoaded(exercise),
                        throwable -> Timber.e(throwable.getMessage()));

    }

    public void saveWorkout(Workout workout) {
        checkViewAttached();
        dataManager.saveWorkout(workout)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(putResult -> {
                            loadWorkout(workout.getWorkoutId());
                            Timber.d("Workout update: %s, numberOfRowsUpdated: %s",
                                    putResult.wasUpdated(), putResult.numberOfRowsUpdated());
                        },
                        throwable -> Timber.e(throwable.getMessage()));
    }

    public void saveWorkoutSet(int workoutId, Workout.WorkoutExercise exercise, Workout.WorkoutSet set) {
        checkViewAttached();
        dataManager.saveWorkoutSet(workoutId, exercise, set)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(putResult -> {
                            Timber.d("Workout (set) update: %s, numberOfRowsUpdated: %s. ",
                                    putResult.wasUpdated(), putResult.numberOfRowsUpdated());
                        },
                        throwable -> Timber.e(throwable.getMessage()));
    }
}
