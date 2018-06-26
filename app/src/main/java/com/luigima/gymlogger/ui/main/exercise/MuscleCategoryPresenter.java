package com.luigima.gymlogger.ui.main.exercise;

import com.luigima.gymlogger.data.DataManager;
import com.luigima.gymlogger.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

class MuscleCategoryPresenter extends BasePresenter<MuscleCategoryMvpView> {

    @Inject
    DataManager dataManager;

    private Subscription subscription;

    @Inject
    public MuscleCategoryPresenter() {

    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) subscription.unsubscribe();
    }

    public void loadCategories() {
        checkViewAttached();

        subscription = dataManager.getMainExercisesCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> getMvpView().showCategories(data),
                        throwable -> Timber.e(throwable.getMessage()));
    }
}
