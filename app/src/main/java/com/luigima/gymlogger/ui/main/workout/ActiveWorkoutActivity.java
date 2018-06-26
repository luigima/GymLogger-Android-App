package com.luigima.gymlogger.ui.main.workout;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.luigima.gymlogger.GymLoggerApp;
import com.luigima.gymlogger.R;
import com.luigima.gymlogger.data.db.entities.Workout;
import com.luigima.gymlogger.ui.base.advancedrecyclerview.AbstractExpandableDataProvider;
import com.luigima.gymlogger.ui.main.exercise.DetailsFragment;
import com.luigima.gymlogger.ui.main.workout.overview.ActiveWorkoutListFragment;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ActiveWorkoutActivity extends AppCompatActivity
        implements ActiveWorkoutListFragment.OnFragmentInteractionListener,
        DetailsFragment.OnFragmentInteractionListener {
    public static final String ARG_WORKOUT_ID = "workoutId";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private int workoutId;
    private Workout workout;
    private WorkoutExercisesProvider dataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_workout);

        ButterKnife.bind(this);
        GymLoggerApp.getComponent(this).inject(this);
        setSupportActionBar(toolbar);

        workoutId = -1;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            workoutId = bundle.getInt(ARG_WORKOUT_ID, -1);
        }
        if (workoutId == -1) {
            Timber.e("Something went wrong! workoutId = %d", workoutId);
        }

        Fragment fragment = ActiveWorkoutListFragment.newInstance(workoutId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onExerciseItemClicked(ActiveWorkoutListFragment fragment, Workout.WorkoutExercise exercise, ImageView imageView, TextView textView) {
        DetailsFragment fragmentNext =
                DetailsFragment.newInstance(exercise.getExercise().getId());
        Transition changeTransform = TransitionInflater.from(this).
                inflateTransition(R.transition.change_image_transform);
        Transition explodeTransform = TransitionInflater.from(this).
                inflateTransition(android.R.transition.explode);

        /*// Setup exit transition on first fragment
        fragment.setSharedElementReturnTransition(changeTransform);
        fragment.setExitTransition(explodeTransform);

        // Setup enter transition on second fragment
        fragmentNext.setSharedElementEnterTransition(changeTransform);
        fragmentNext.setEnterTransition(explodeTransform);

        // Find the shared element (in Fragment A)
*/
        // Add second fragment by replacing first
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragmentNext)
                .addToBackStack("transaction")
                //.addSharedElement(textView, "txtExerciseTitle")
                //.addSharedElement(imageView, "imgMuscles")
                .commit();
    }
    public WorkoutExercisesProvider getDataProvider() {
        if (dataProvider == null) {
            List<Pair<AbstractExpandableDataProvider.GroupData, List<AbstractExpandableDataProvider.ChildData>>> data
                    = new LinkedList<>();
            dataProvider = new WorkoutExercisesProvider(data);
        }
        return dataProvider;
    }


    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public Workout getWorkout() {
        return workout;
    }
}
