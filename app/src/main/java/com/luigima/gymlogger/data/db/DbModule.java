package com.luigima.gymlogger.data.db;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.luigima.gymlogger.dagger.PerApp;
import com.luigima.gymlogger.data.db.entities.Exercise;
import com.luigima.gymlogger.data.db.entities.MainMuscleGroup;
import com.luigima.gymlogger.data.db.entities.Routine;
import com.luigima.gymlogger.data.db.entities.Workout;
import com.luigima.gymlogger.data.db.entities.sql.DBMuscle;
import com.luigima.gymlogger.data.db.entities.sql.DBMuscleStorIOSQLiteDeleteResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBMuscleStorIOSQLiteGetResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBMuscleStorIOSQLitePutResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBRoutine;
import com.luigima.gymlogger.data.db.entities.sql.DBRoutineExerciseHasSets;
import com.luigima.gymlogger.data.db.entities.sql.DBRoutineExerciseHasSetsStorIOSQLiteDeleteResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBRoutineExerciseHasSetsStorIOSQLiteGetResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBRoutineExerciseHasSetsStorIOSQLitePutResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBRoutineHasSplits;
import com.luigima.gymlogger.data.db.entities.sql.DBRoutineHasSplitsStorIOSQLiteDeleteResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBRoutineHasSplitsStorIOSQLiteGetResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBRoutineHasSplitsStorIOSQLitePutResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBRoutineStorIOSQLiteDeleteResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBRoutineStorIOSQLiteGetResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBRoutineStorIOSQLitePutResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBRoutineTextTranslate;
import com.luigima.gymlogger.data.db.entities.sql.DBRoutineTextTranslateStorIOSQLiteDeleteResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBRoutineTextTranslateStorIOSQLiteGetResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBRoutineTextTranslateStorIOSQLitePutResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBSplit;
import com.luigima.gymlogger.data.db.entities.sql.DBSplitHasExercises;
import com.luigima.gymlogger.data.db.entities.sql.DBSplitHasExercisesStorIOSQLiteDeleteResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBSplitHasExercisesStorIOSQLiteGetResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBSplitHasExercisesStorIOSQLitePutResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBSplitStorIOSQLiteDeleteResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBSplitStorIOSQLiteGetResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBSplitStorIOSQLitePutResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBWorkout;
import com.luigima.gymlogger.data.db.entities.sql.DBWorkoutStorIOSQLiteDeleteResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBWorkoutStorIOSQLiteGetResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBWorkoutStorIOSQLitePutResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBWorkoutTrainsExercises;
import com.luigima.gymlogger.data.db.entities.sql.DBWorkoutTrainsExercisesStorIOSQLiteDeleteResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBWorkoutTrainsExercisesStorIOSQLiteGetResolver;
import com.luigima.gymlogger.data.db.entities.sql.DBWorkoutTrainsExercisesStorIOSQLitePutResolver;
import com.luigima.gymlogger.data.db.resolvers.ExerciseDeleteResolver;
import com.luigima.gymlogger.data.db.resolvers.ExerciseGetResolver;
import com.luigima.gymlogger.data.db.resolvers.ExercisePutResolver;
import com.luigima.gymlogger.data.db.resolvers.MainMuscleGroupDeleteResolver;
import com.luigima.gymlogger.data.db.resolvers.MainMuscleGroupGetResolver;
import com.luigima.gymlogger.data.db.resolvers.MainMuscleGroupPutResolver;
import com.luigima.gymlogger.data.db.resolvers.RoutineDeleteResolver;
import com.luigima.gymlogger.data.db.resolvers.RoutineGetResolver;
import com.luigima.gymlogger.data.db.resolvers.RoutinePutResolver;
import com.luigima.gymlogger.data.db.resolvers.WorkoutDeleteResolver;
import com.luigima.gymlogger.data.db.resolvers.WorkoutGetResolver;
import com.luigima.gymlogger.data.db.resolvers.WorkoutPutResolver;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;

import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {
    @Provides
    @PerApp
    public SQLiteOpenHelper gymLoggerSQLiteOpenHelper(Application app) {
        return new GymLoggerSQLiteOpenHelper(app.getBaseContext());
    }

    @Provides
    @PerApp
    @NonNull
    public StorIOSQLite provideStorIOSQLite(@NonNull SQLiteOpenHelper sqLiteOpenHelper) {
        return DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(Exercise.class, SQLiteTypeMapping.<Exercise>builder()
                        .putResolver(new ExercisePutResolver())
                        .getResolver(new ExerciseGetResolver())
                        .deleteResolver(new ExerciseDeleteResolver())
                        .build())
                .addTypeMapping(DBMuscle.class, SQLiteTypeMapping.<DBMuscle>builder()
                        .putResolver(new DBMuscleStorIOSQLitePutResolver())
                        .getResolver(new DBMuscleStorIOSQLiteGetResolver())
                        .deleteResolver(new DBMuscleStorIOSQLiteDeleteResolver())
                        .build())
                .addTypeMapping(MainMuscleGroup.class, SQLiteTypeMapping.<MainMuscleGroup>builder()
                        .putResolver(new MainMuscleGroupPutResolver())
                        .getResolver(new MainMuscleGroupGetResolver())
                        .deleteResolver(new MainMuscleGroupDeleteResolver())
                        .build())
                .addTypeMapping(DBRoutineTextTranslate.class, SQLiteTypeMapping.<DBRoutineTextTranslate>builder()
                        .putResolver(new DBRoutineTextTranslateStorIOSQLitePutResolver())
                        .getResolver(new DBRoutineTextTranslateStorIOSQLiteGetResolver())
                        .deleteResolver(new DBRoutineTextTranslateStorIOSQLiteDeleteResolver())
                        .build())
                .addTypeMapping(DBRoutine.class, SQLiteTypeMapping.<DBRoutine>builder()
                        .putResolver(new DBRoutineStorIOSQLitePutResolver())
                        .getResolver(new DBRoutineStorIOSQLiteGetResolver())
                        .deleteResolver(new DBRoutineStorIOSQLiteDeleteResolver())
                        .build())
                .addTypeMapping(DBSplitHasExercises.class, SQLiteTypeMapping.<DBSplitHasExercises>builder()
                        .putResolver(new DBSplitHasExercisesStorIOSQLitePutResolver())
                        .getResolver(new DBSplitHasExercisesStorIOSQLiteGetResolver())
                        .deleteResolver(new DBSplitHasExercisesStorIOSQLiteDeleteResolver())
                        .build())
                .addTypeMapping(Routine.class, SQLiteTypeMapping.<Routine>builder()
                        .putResolver(new RoutinePutResolver())
                        .getResolver(new RoutineGetResolver())
                        .deleteResolver(new RoutineDeleteResolver())
                        .build())
                .addTypeMapping(DBRoutineHasSplits.class, SQLiteTypeMapping.<DBRoutineHasSplits>builder()
                        .putResolver(new DBRoutineHasSplitsStorIOSQLitePutResolver())
                        .getResolver(new DBRoutineHasSplitsStorIOSQLiteGetResolver())
                        .deleteResolver(new DBRoutineHasSplitsStorIOSQLiteDeleteResolver())
                        .build())
                .addTypeMapping(DBWorkout.class, SQLiteTypeMapping.<DBWorkout>builder()
                        .putResolver(new DBWorkoutStorIOSQLitePutResolver())
                        .getResolver(new DBWorkoutStorIOSQLiteGetResolver())
                        .deleteResolver(new DBWorkoutStorIOSQLiteDeleteResolver())
                        .build())
                .addTypeMapping(Workout.class, SQLiteTypeMapping.<Workout>builder()
                        .putResolver(new WorkoutPutResolver())
                        .getResolver(new WorkoutGetResolver())
                        .deleteResolver(new WorkoutDeleteResolver())
                        .build())
                .addTypeMapping(DBWorkoutTrainsExercises.class, SQLiteTypeMapping.<DBWorkoutTrainsExercises>builder()
                        .putResolver(new DBWorkoutTrainsExercisesStorIOSQLitePutResolver())
                        .getResolver(new DBWorkoutTrainsExercisesStorIOSQLiteGetResolver())
                        .deleteResolver(new DBWorkoutTrainsExercisesStorIOSQLiteDeleteResolver())
                        .build())
                .addTypeMapping(DBRoutineExerciseHasSets.class, SQLiteTypeMapping.<DBRoutineExerciseHasSets>builder()
                        .putResolver(new DBRoutineExerciseHasSetsStorIOSQLitePutResolver())
                        .getResolver(new DBRoutineExerciseHasSetsStorIOSQLiteGetResolver())
                        .deleteResolver(new DBRoutineExerciseHasSetsStorIOSQLiteDeleteResolver())
                        .build())
                .addTypeMapping(DBSplit.class, SQLiteTypeMapping.<DBSplit>builder()
                        .putResolver(new DBSplitStorIOSQLitePutResolver())
                        .getResolver(new DBSplitStorIOSQLiteGetResolver())
                        .deleteResolver(new DBSplitStorIOSQLiteDeleteResolver())
                        .build())
                .build();
    }

    @Provides
    @PerApp
    @NonNull
    public DatabaseHelper provideDataBaseHelper(StorIOSQLite storIOSQLite) {
        return new DatabaseHelper(storIOSQLite);
    }
}
