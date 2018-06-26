package com.luigima.gymlogger.db;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.luigima.gymlogger.data.db.DbModule;
import com.luigima.gymlogger.data.db.GymLoggerSQLiteOpenHelper;
import com.luigima.gymlogger.data.db.entities.Exercise;
import com.luigima.gymlogger.data.db.entities.Routine;
import com.luigima.gymlogger.data.db.entities.Workout;
import com.luigima.gymlogger.data.db.tables.WorkoutTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.List;

public class WorkoutTestCase extends InstrumentationTestCase {
    private static final String TEST_FILE_PREFIX = "test_";
    private GymLoggerSQLiteOpenHelper databaseHelper;
    private StorIOSQLite storIOSQLite;
    private Context context;

    protected void setUp() throws Exception {
        super.setUp();
        assertNotNull(getInstrumentation());
        Context c = getInstrumentation().getTargetContext();
        assertNotNull(c);

        context = c;//new RenamingDelegatingContext(c, TEST_FILE_PREFIX);
        assertNotNull(context.getAssets());

        context.deleteDatabase("gymlogger.db");

        databaseHelper = new GymLoggerSQLiteOpenHelper(context);
        assertNotNull(databaseHelper);
        storIOSQLite = (new DbModule()).provideStorIOSQLite(databaseHelper);
        assertNotNull(databaseHelper);
        assertNotNull(storIOSQLite);
    }

    public static Workout createWorkout() {
        List<Workout.WorkoutExercise> exercises = new ArrayList<>(3);
        List<Workout.WorkoutSet> sets = new ArrayList<>(3);
        sets.add(new Workout.WorkoutSet(1, 12, 0, 50, 0, 0, 0, 111));
        sets.add(new Workout.WorkoutSet(2, 12, 0, 100, 0, 0, 0, 112));
        sets.add(new Workout.WorkoutSet(3, 12, 0, 150, 0, 0, 0, 113));

        Exercise exercise = new Exercise(0, 0, null, null, 0, 0, 0, "", "", null, null);

        exercises.add(new Workout.WorkoutExercise(1, exercise, 1, "note1", sets));
        exercises.add(new Workout.WorkoutExercise(2, exercise, 2, "note2", sets));
        exercises.add(new Workout.WorkoutExercise(3, exercise, 3, "note3", sets));
        return Workout.newWorkout(1, 111, "testnote", exercises, 1);
    }

    public void testWorkoutPut() throws Exception {
        //easy way to insert a workout
        //each workout need a routine!
        Routine routine = RoutineTestCase.createRoutine();
        storIOSQLite.put().object(routine).prepare().executeAsBlocking();


        Workout workout = WorkoutTestCase.createWorkout();
        PutResult putResult = storIOSQLite.put().object(workout).prepare().executeAsBlocking();
        assertEquals(true, putResult.wasInserted());

        workout = saveWorkout(workout);
        //it shouldnt get inserted two times!
        putResult = storIOSQLite.put().object(workout).prepare().executeAsBlocking();
        assertEquals(false, putResult.wasInserted());
    }

    public void testWorkoutGet() throws Exception {
        testWorkoutPut();

        List<Workout> workouts =
                storIOSQLite.get()
                        .listOfObjects(Workout.class)
                        .withQuery(Query.builder().table(WorkoutTable.TABLE).build())
                        .prepare()
                        .executeAsBlocking();

        assertEquals(1, workouts.size());
        Workout workout = workouts.get(0);
        assertEquals(1, (int) workout.getWorkoutId());
        assertEquals(0, workout.getDuration());
        assertEquals(111, workout.getDate());
        assertEquals("testnote", workout.getNotes());
        assertEquals(1, workout.getSplitId());
        assertEquals("Split 1", workout.getSplitTitle());
        assertEquals(3, workout.getExercises().size());
        assertEquals("note3", workout.getExercises().get(2).getNotes());
    }

    private Workout saveWorkout(Workout workout) {
        storIOSQLite.put().object(workout).prepare().executeAsBlocking();
        List<Workout> list = storIOSQLite
                .get()
                .listOfObjects(Workout.class)
                .withQuery(Query.builder().table(WorkoutTable.TABLE).build())
                .prepare()
                .executeAsBlocking();

        workout = list.get(0);

        return workout;
    }

}
