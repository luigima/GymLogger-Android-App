package com.luigima.gymlogger;

import android.app.Application;

import com.luigima.gymlogger.dagger.PerApp;
import com.luigima.gymlogger.data.db.DbModule;
import com.luigima.gymlogger.module.NetworkModule;
import com.luigima.gymlogger.ui.main.MainActivity;
import com.luigima.gymlogger.ui.main.exercise.ExerciseDataAdapter;
import com.luigima.gymlogger.ui.main.exercise.ExerciseFragment;
import com.luigima.gymlogger.ui.main.exercise.MuscleCategoryDataAdapter;
import com.luigima.gymlogger.ui.main.exercise.MuscleCategoryFragment;
import com.luigima.gymlogger.ui.main.routine.add.AddExercisesFragment;
import com.luigima.gymlogger.ui.main.routine.add.AddRoutineActivity;
import com.luigima.gymlogger.ui.main.routine.add.AddTitleFragment;
import com.luigima.gymlogger.ui.main.routine.details.RoutineDetailsFragment;
import com.luigima.gymlogger.ui.main.routine.list.RoutineDataAdapter;
import com.luigima.gymlogger.ui.main.routine.list.RoutineFragment;
import com.luigima.gymlogger.ui.main.workout.ActiveWorkoutActivity;
import com.luigima.gymlogger.ui.main.workout.overview.ActiveWorkoutListFragment;
import com.luigima.gymlogger.ui.main.workout.overview.DataInputActivity;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;

import dagger.Component;

@PerApp
@Component(
    modules = {
        GymLoggerAppModule.class,
        NetworkModule.class,
        DbModule.class
    }
)
public interface GymLoggerAppComponent {

    void inject(MainActivity activity);
    void inject(AddRoutineActivity activity);
    void inject(ActiveWorkoutActivity activity);
    void inject(DataInputActivity activity);

    void inject(ExerciseDataAdapter.ViewHolder viewHolder);
    void inject(MuscleCategoryDataAdapter.ViewHolder viewHolder);
    void inject(ExerciseFragment fragment);
    void inject(com.luigima.gymlogger.ui.main.exercise.DetailsFragment fragment);
    void inject(MuscleCategoryFragment fragment);

    void inject(RoutineFragment fragment);
    void inject(AddTitleFragment fragment);
    void inject(AddExercisesFragment fragment);
    void inject(RoutineDetailsFragment fragment);
    void inject(ActiveWorkoutListFragment fragment);

    void inject(RoutineDataAdapter.ViewHolder viewHolder);

    Application application();
    Picasso providePicasso();
    OkHttpClient provideOkHttpClient();
}
