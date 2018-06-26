package com.luigima.gymlogger.ui.main.routine.add;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.luigima.gymlogger.GymLoggerApp;
import com.luigima.gymlogger.R;
import com.luigima.gymlogger.data.DataManager;
import com.luigima.gymlogger.data.db.entities.Exercise;
import com.luigima.gymlogger.data.db.entities.Routine;
import com.luigima.gymlogger.ui.base.advancedrecyclerview.AbstractExpandableDataProvider;
import com.luigima.gymlogger.ui.main.exercise.DetailsFragment;
import com.luigima.gymlogger.ui.main.exercise.ExerciseFragment;
import com.luigima.gymlogger.ui.main.exercise.ExerciseMainFragment;
import com.luigima.gymlogger.ui.main.exercise.MuscleCategoryFragment;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class AddRoutineActivity extends AppCompatActivity
        implements AddTitleFragment.OnFragmentInteractionListener,
        AddExercisesFragment.OnFragmentInteractionListener,
        ExerciseMainFragment.OnFragmentInteractionListener,
        MuscleCategoryFragment.OnFragmentInteractionListener,
        ExerciseFragment.OnFragmentInteractionListener,
        DetailsFragment.OnFragmentInteractionListener,
        AddRoutineMvpView {

    public static final String ARG_ROUTINE_ID = "routineId";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Inject
    DataManager dataManager;

    @Inject
    AddRoutinePresenter presenter;

    private int routineId;
    private FragmentManager fragmentManager;
    private RoutineConfigDataProvider dataProvider;
    private int currentSplitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_routine);

        ButterKnife.bind(this);
        GymLoggerApp.getComponent(this).inject(this);
        presenter.attachView(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.new_routine);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            routineId = bundle.getInt(ARG_ROUTINE_ID, -1);
        } else {
            routineId = -1;
        }

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content, AddTitleFragment.newInstance(routineId)).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackCount > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public void onExerciseAddButtonClicked(int groupPosition) {
        this.currentSplitId = getDataProvider().getGroupItem(groupPosition).getDataId();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, ExerciseMainFragment.newInstance())
                .addToBackStack(null)
                .commit();

        fragmentManager.beginTransaction()
                .replace(R.id.container_recyclerview, MuscleCategoryFragment.newInstance(),
                        MuscleCategoryFragment.OnFragmentInteractionListener.FRAGMENT_TAG).commit();
    }

    @Override
    public void onRoutineCreated(int routineId) {
        Timber.d("onRoutineCreated(%d)", routineId);

    }


    @Override
    public void onTitleAndDescriptionSet(int routineId) {
        Timber.d("onTitleAndDescriptionSet()");
        this.routineId = routineId;
        fragmentManager.beginTransaction()
                .replace(R.id.content, AddExercisesFragment.newInstance(routineId), AddExercisesFragment.FRAGMENT_TAG)
                .addToBackStack(null).commit();
    }

    @Override
    public void onExerciseCategoryFragmentShowExercisesInteraction(List<Integer> exerciseCategories) {
        // Code to run on older devices
        Fragment fragment = ExerciseFragment.newInstance(exerciseCategories, ExerciseFragment.VIEW_TYPE_ADD);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.container_recyclerview, fragment, ExerciseFragment.OnFragmentInteractionListener.FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onExerciseFragmentShowDetailsInteraction(ImageView imageView, int id) {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition changeTransform = TransitionInflater.from(this).
                    inflateTransition(R.transition.change_image_transform);
            Transition explodeTransform = TransitionInflater.from(this).
                    inflateTransition(android.R.transition.explode);
            Transition slideRightTransform = TransitionInflater.from(this).
                    inflateTransition(android.R.transition.slide_right);
            Transition slideLeftTransform = TransitionInflater.from(this).
                    inflateTransition(android.R.transition.slide_left);

            // Setup exit transition on first fragment
            Fragment fragmentTwo = DetailsFragment.newInstance(id);

            getSupportFragmentManager().findFragmentById(R.id.container_recyclerview)
                    .setSharedElementReturnTransition(slideRightTransform);
            getSupportFragmentManager().findFragmentById(R.id.container_recyclerview)
                    .setExitTransition(slideLeftTransform);

            // Setup enter transition on second fragment
            fragmentTwo.setSharedElementEnterTransition(slideLeftTransform);
            fragmentTwo.setEnterTransition(slideRightTransform);

            // Add second fragment by replacing first
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_recyclerview, fragmentTwo)
                    .addSharedElement(imageView, "exerciseImage")
                    .addToBackStack(null)
                    .commit();
        } else {*/
        // Code to run on older devices
        Fragment fragment = DetailsFragment.newInstance(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_recyclerview, fragment)
                .addToBackStack(null)
                .commit();
        //}
    }


    public long getRoutineId() {
        return routineId;
    }

    public RoutineConfigDataProvider getDataProvider() {
        if (dataProvider == null) {
            List<Pair<AbstractExpandableDataProvider.GroupData, List<AbstractExpandableDataProvider.ChildData>>> data
                    = new LinkedList<>();
            dataProvider = new RoutineConfigDataProvider(data);
        }
        return dataProvider;
    }

    @Override
    public void showError() {

    }


    @Override
    public void onExerciseMainFragmentInteraction(Uri uri) {

    }

    @Override
    public void onAddButtonClicked(Exercise exercise) {
        Timber.d("Exercise added: %s", exercise.getTitle());
        AddExercisesFragment fragment = (AddExercisesFragment) fragmentManager
                .findFragmentByTag(AddExercisesFragment.FRAGMENT_TAG);

        if (fragment != null) {
            fragment.addExercise(currentSplitId, exercise);
        }
    }

    @Override
    public void onRoutineLoaded(Routine routine) {
        Timber.d("Nothing here");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSplitRenamed(boolean wasUpdated) {

    }
}
