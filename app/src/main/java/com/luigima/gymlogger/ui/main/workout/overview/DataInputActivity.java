package com.luigima.gymlogger.ui.main.workout.overview;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;

import com.commit451.elasticdragdismisslayout.ElasticDragDismissFrameLayout;
import com.commit451.elasticdragdismisslayout.ElasticDragDismissListener;
import com.luigima.gymlogger.GymLoggerApp;
import com.luigima.gymlogger.R;
import com.luigima.gymlogger.data.db.entities.Workout;
import com.luigima.gymlogger.ui.base.SystemChromeFader;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import timber.log.Timber;

public class DataInputActivity extends AppCompatActivity implements ActiveWorkoutListMvpView, WorkoutExerciseSetDataAdapter.OnChangeListener {
    public static final String ARG_SPLIT_HAS_EXERCISE_ID = "splitHasExercisesId";
    public static final String ARG_WORKOUT_ID = "workoutId";

    @Inject
    ActiveWorkoutListPresenter presenter;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private ElasticDragDismissFrameLayout draggableFrame;
    private WorkoutExerciseSetDataAdapter dataAdapter;
    private int splitHasExercisesId, workoutId;
    private Workout.WorkoutExercise workoutExercise;

    public static Intent newIntent(Context context, int workoutId, int setId) {
        Intent intent = new Intent(context, DataInputActivity.class);
        intent.putExtra(ARG_SPLIT_HAS_EXERCISE_ID, setId);
        intent.putExtra(ARG_WORKOUT_ID, workoutId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_input);
        GymLoggerApp.getComponent(this).inject(this);
        ButterKnife.bind(this);
        presenter.attachView(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            workoutId = bundle.getInt(ARG_WORKOUT_ID, -1);
            splitHasExercisesId = bundle.getInt(ARG_SPLIT_HAS_EXERCISE_ID, -1);
        } else {
            workoutId = -1;
            splitHasExercisesId = -1;
        }

        draggableFrame = (ElasticDragDismissFrameLayout) findViewById(R.id.draggable_frame);

        draggableFrame.addListener(new ElasticDragDismissListener() {
            @Override
            public void onDrag(float elasticOffset, float elasticOffsetPixels, float rawOffset, float rawOffsetPixels) {
            }

            @Override
            public void onDragDismissed() {
                // if we drag dismiss downward then the default reversal of the enter
                // transition would slide content upward which looks weird. So reverse it.
                if (draggableFrame.getTranslationY() > 0 && Build.VERSION.SDK_INT >= 21) {
                    getWindow().setReturnTransition(
                            TransitionInflater.from(DataInputActivity.this)
                                    .inflateTransition(R.transition.about_return_downward));
                }
                if (Build.VERSION.SDK_INT >= 21) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });
        if (Build.VERSION.SDK_INT >= 21) {
            draggableFrame.addListener(new SystemChromeFader(getWindow()));
        }

        dataAdapter = new WorkoutExerciseSetDataAdapter(new ArrayList<>());
        recyclerView.setHasFixedSize(false);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(dataAdapter);
        presenter.getWorkoutExercise(workoutId, splitHasExercisesId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // the adapter will notice that it got detached and
        // will unsubscribe all its rxbinding subscriptions
        recyclerView.setAdapter(null);
    }

    @Override
    public void showError() {
        Timber.e("Error!");
    }

    @Override
    public void onWorkoutLoaded(Workout workout) {
        Timber.d("Workout loaded: %s", workout.getSplitTitle());
    }

    @Override
    public void onWorkoutExerciseLoaded(Workout.WorkoutExercise exercise) {
        Timber.d("WorkoutExercise loaded: %s", exercise.getExercise().getTitle());
        this.workoutExercise = exercise;
        dataAdapter.addOnChangeListener(this);
        dataAdapter.setData(exercise.getSets());
    }

    @Override
    @DebugLog
    public void onSetChanged(int setId, int reps, float weight) {
        Workout.WorkoutSet set = getSetById(setId);
        if(set == null) {
            set = new Workout.WorkoutSet(
                    setId,
                    reps, reps,
                    weight, weight,
                    0, 0,
                    123
            );
        }
       if (workoutExercise != null) {
            set.setRepeatsIs(reps);
            set.setWeightIs(weight);
            presenter.saveWorkoutSet(workoutId, workoutExercise, set);
        }
    }

    @Override
    public void onNewSet() {
        if (workoutExercise != null) {
            Workout.WorkoutSet set = Workout.newWorkoutSet();
            set.setSetId(workoutExercise.getSets().size());
            presenter.saveWorkoutSet(workoutId, workoutExercise, set);
        }

    }

    private Workout.WorkoutSet getSetById(int setId) {
        if (workoutExercise != null) {
            for (Workout.WorkoutSet set :
                    workoutExercise.getSets()) {
                if (set.getSetId() == setId) {
                    return set;
                }
            }
        }
        return null;
    }
}
