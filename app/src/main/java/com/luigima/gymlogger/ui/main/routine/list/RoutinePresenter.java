package com.luigima.gymlogger.ui.main.routine.list;

import com.luigima.gymlogger.data.DataManager;
import com.luigima.gymlogger.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

class RoutinePresenter extends BasePresenter<RoutineMvpView> {

    @Inject
    DataManager dataManager;

    private Subscription subscription;

    @Inject
    public RoutinePresenter() {
    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) subscription.unsubscribe();
    }

    public void loadRoutines() {
        checkViewAttached();

        subscription = dataManager.getRoutines()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> getMvpView().setData(data),
                        throwable -> Timber.e(throwable.getMessage()));
    }

    public void loadRoutineById(int id) {
        checkViewAttached();

        subscription = dataManager.getRoutineById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(routine -> getMvpView().addRoutine(routine),
                        throwable -> Timber.e(throwable.getMessage()));
    }
}
