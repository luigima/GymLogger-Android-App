package com.luigima.gymlogger.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.facebook.stetho.Stetho;
import com.luigima.gymlogger.GymLoggerApp;
import com.luigima.gymlogger.R;
import com.luigima.gymlogger.data.db.entities.Exercise;
import com.luigima.gymlogger.ui.main.exercise.ExerciseFragment;
import com.luigima.gymlogger.ui.main.exercise.ExerciseMainFragment;
import com.luigima.gymlogger.ui.main.exercise.MuscleCategoryFragment;
import com.luigima.gymlogger.ui.main.routine.add.AddRoutineActivity;
import com.luigima.gymlogger.ui.main.routine.details.RoutineDetailsFragment;
import com.luigima.gymlogger.ui.main.routine.list.RoutineFragment;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ExerciseFragment.OnFragmentInteractionListener,
        com.luigima.gymlogger.ui.main.exercise.DetailsFragment.OnFragmentInteractionListener,
        MuscleCategoryFragment.OnFragmentInteractionListener,
        ExerciseMainFragment.OnFragmentInteractionListener,
        RoutineFragment.OnFragmentInteractionListener,
        RoutineDetailsFragment.OnFragmentInteractionListener{

    public static final int ACTIVITY_RESULT_CODE = 1234;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @Inject
    StorIOSQLite storIOSQLite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        GymLoggerApp.getComponent(this).inject(this);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Stetho.initializeWithDefaults(this);
    }

    @Override
    public void onBackPressed() {
        ExerciseFragment fragment = (ExerciseFragment) getSupportFragmentManager()
                .findFragmentByTag(ExerciseFragment.OnFragmentInteractionListener.FRAGMENT_TAG);
        if (fragment != null && fragment.isVisible()) {
            // add your code here
        }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_workouts) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, RoutineFragment.newInstance(floatingActionButton)).commit();
        } else if (id == R.id.nav_exercises) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, ExerciseMainFragment.newInstance()).commit();
            fragmentManager.beginTransaction()
                    .replace(R.id.container_recyclerview, MuscleCategoryFragment.newInstance(),
                            MuscleCategoryFragment.OnFragmentInteractionListener.FRAGMENT_TAG).commit();

        } else if (id == R.id.nav_share) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, ExerciseMainFragment.newInstance()).commit();

        } else if (id == R.id.nav_send) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, ExerciseMainFragment.newInstance()).commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void addOrEditWorkout(int workoutId) {
        Intent intent = new Intent(this, AddRoutineActivity.class);
        intent.putExtra(AddRoutineActivity.ARG_ROUTINE_ID, workoutId);
        startActivityForResult(intent, ACTIVITY_RESULT_CODE);
    }

    @Override
    public void onExerciseCategoryFragmentShowExercisesInteraction(List<Integer> exerciseCategories) {
        // Code to run on older devices
        Fragment fragment = ExerciseFragment.newInstance(exerciseCategories, ExerciseFragment.VIEW_TYPE_NORMAL);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                //.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.container_recyclerview, fragment, ExerciseFragment.OnFragmentInteractionListener.FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onExerciseMainFragmentInteraction(Uri uri) {

    }

    @Override
    public void onExerciseFragmentShowDetailsInteraction(ImageView imageView, int id) {

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition changeTransform = TransitionInflater.from(this).
                    inflateTransition(R.transition.change_image_transform);
            Transition explodeTransform = TransitionInflater.from(this).
                    inflateTransition(android.R.transition.explode);
            Transition slideRightTransform = TransitionInflater.from(this).
                    inflateTransition(android.R.transition.slide_right);
            Transition slideLeftTransform = TransitionInflater.from(this).
                    inflateTransition(android.R.transition.slide_left);

            // Setup exit transition on first fragment
            Fragment fragmentTwo = com.luigima.gymlogger.ui.main.exercise.DetailsFragment.newInstance(id);

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
            Fragment fragment = com.luigima.gymlogger.ui.main.exercise.DetailsFragment.newInstance(id);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container_recyclerview, fragment)
                    .addToBackStack(null)
                    .commit();
        //}
    }

    @Override
    public void onAddRoutineButtonClicked() {
        // We do not have any workoutId yet
        addOrEditWorkout(-1);

    }

    @Override
    public void onRoutineFragmentShowDetailsInteraction(ImageView imageView, int id) {
        // Code to run on older devices
        Fragment fragment = RoutineDetailsFragment.newInstance(id);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment, RoutineDetailsFragment.OnFragmentInteractionListener.FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onEditRoutineButtonClicked(int routineId) {
        addOrEditWorkout(routineId);
    }

    @Override
    public void onAddButtonClicked(Exercise exercise) {
        Timber.d("Exercise added: %s", exercise.getTitle());
    }

    @Override
    public void onRoutineDeleted(int routineId) {
        // TODO Currently workouts only can get deleted from the RoutineDetailsFragment.
        // popBackStack maight be the wrong choice if deleting the workout is allowed from somewhere else
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
