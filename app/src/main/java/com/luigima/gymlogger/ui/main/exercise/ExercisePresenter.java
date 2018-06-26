package com.luigima.gymlogger.ui.main.exercise;

import com.luigima.gymlogger.data.DataManager;
import com.luigima.gymlogger.data.db.entities.Exercise;
import com.luigima.gymlogger.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import timber.log.Timber;

class ExercisePresenter extends BasePresenter<ExerciseMvpView> {

    @Inject
    DataManager dataManager;

    private Subscription subscription;

    @Inject
    public ExercisePresenter() {

    }

    @Override
    public void detachView() {
        super.detachView();
        if (subscription != null) subscription.unsubscribe();
    }

    //TODO delete
    public void test(List<Exercise> exercises) {
        for (Exercise exercise : exercises) {
            Timber.d(exercise.test());
        }
        Timber.d(String.valueOf(exercises.size()));
    }

    public void loadExercisesByCategoryIds(List<Integer> exerciseCategoryIds) {
        checkViewAttached();

        subscription = Observable.from(exerciseCategoryIds)
                .flatMap(id -> dataManager.getExercisesByCategory(id))
                .collect((Func0<ArrayList<Exercise>>) ArrayList::new, ArrayList::addAll)
                .distinct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> getMvpView().addExercises(data),
                        throwable -> Timber.e(throwable.getMessage()));
    }

    public void search(String searchQuery) {
        checkViewAttached();
        subscription = Observable.just(searchQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(query -> getMvpView().searchExercises(query),
                        throwable -> Timber.e(throwable.getMessage()));
    }
}
