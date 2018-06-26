package com.luigima.gymlogger.ui.main.exercise;

import com.luigima.gymlogger.data.DataManager;
import com.luigima.gymlogger.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

class DetailsPresenter extends BasePresenter<DetailsMvpView> {

    @Inject
    DataManager dataManager;

    private Subscription subscription;

    @Inject
    public DetailsPresenter() {
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) subscription.unsubscribe();
    }

    public void loadWorkoutExercise(int exerciseId) {
        checkViewAttached();
        subscription = dataManager.getExercise(exerciseId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(exercise -> getMvpView().showExercise(exercise),
                        throwable -> Timber.e(throwable.getMessage()));
    }
}
