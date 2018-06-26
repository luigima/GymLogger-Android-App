package com.luigima.gymlogger.db;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.luigima.gymlogger.data.db.DbModule;
import com.luigima.gymlogger.data.db.GymLoggerSQLiteOpenHelper;
import com.luigima.gymlogger.data.db.entities.Exercise;
import com.luigima.gymlogger.data.db.entities.Routine;
import com.luigima.gymlogger.data.db.relations.RoutineRelation;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukma on 03.04.2016.
 */
public class RoutineTestCase extends InstrumentationTestCase {
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

    public static Routine createRoutine() {
        List<Routine.RoutineSplit> splits = new ArrayList<>(3);
        List<Routine.RoutineExercise> exercises = new ArrayList<>(3);
        List<Routine.RoutineExercise> exercises2 = new ArrayList<>(3);
        List<Routine.RoutineExercise> exercises3 = new ArrayList<>(3);
        List<Routine.RoutineSet> sets = new ArrayList<>(3);
        sets.add(Routine.RoutineSet.newSet(12, 50, 0));
        sets.add(Routine.RoutineSet.newSet(12, 100, 0));
        sets.add(Routine.RoutineSet.newSet(12, 150, 0));

        Exercise exercise = new Exercise(0, 0, null, null, 0, 0, 0, "", "", null, null);
        exercises.add(Routine.RoutineExercise.newExercise(exercise, 0, sets));
        exercises.add(Routine.RoutineExercise.newExercise(exercise, 1, sets));
        exercises.add(Routine.RoutineExercise.newExercise(exercise, 2, sets));
        splits.add(Routine.RoutineSplit.newSplit(exercises, "Split 1"));

        exercises2.add(Routine.RoutineExercise.newExercise(exercise, 0, sets));
        exercises2.add(Routine.RoutineExercise.newExercise(exercise, 1, sets));
        exercises2.add(Routine.RoutineExercise.newExercise(exercise, 2, sets));
        splits.add(Routine.RoutineSplit.newSplit(exercises2, "Split 2"));

        exercises3.add(Routine.RoutineExercise.newExercise(exercise, 0, sets));
        exercises3.add(Routine.RoutineExercise.newExercise(exercise, 1, sets));
        exercises3.add(Routine.RoutineExercise.newExercise(exercise, 2, sets));
        splits.add(Routine.RoutineSplit.newSplit(exercises3, "Split 3"));

        return Routine.newRoutine(5, "testroutine", "description", 0, 1, 2, 3, 4, splits);
    }

    public void testRoutineGet() throws Exception {
        List<Routine> routineList = storIOSQLite
                .get()
                .listOfObjects(Routine.class)
                .withQuery(RoutineRelation.getAll().build())
                .prepare()
                .executeAsBlocking();

        assertEquals(0, routineList.size());

        Routine newRoutineWithNullSplits = new Routine(null, 0, "TestRoutine", "", 0, 0, 0, 0, 0, null);

        storIOSQLite
                .put()
                .object(newRoutineWithNullSplits)
                .prepare()
                .executeAsBlocking();

        routineList = storIOSQLite
                .get()
                .listOfObjects(Routine.class)
                .withQuery(RoutineRelation.getAll().build())
                .prepare()
                .executeAsBlocking();

        assertEquals(1, routineList.size());
        assertEquals("TestRoutine", routineList.get(0).getTitle());
    }

    public void testRoutinePut() throws Exception {
        Routine routine = createRoutine();
        storIOSQLite.put().object(routine).prepare().executeAsBlocking();

        List<Routine> routineList = storIOSQLite
                .get()
                .listOfObjects(Routine.class)
                .withQuery(RoutineRelation.getAll().build())
                .prepare()
                .executeAsBlocking();

        Routine getResult = routineList.get(0);
        assertEquals(1, routineList.size());
        assertEquals("testroutine", getResult.getTitle());
        assertEquals("description", getResult.getDescription());

        assertEquals(0, getResult.getCreatorId());
        assertEquals(1, getResult.getCreationDate());
        assertEquals(2, getResult.getDifficulty());
        assertEquals(3.0f, getResult.getRating());
        assertEquals(4, getResult.isLocal());
        assertEquals(5, getResult.getRoutineCategoryId());

        assertEquals(3, getResult.getSplits().size());
        assertEquals("Bench Press", getResult.getSplits().get(0).getExercises().get(0).getExercise().getTitle());
    }

    public void testRoutineMoveExercise() throws Exception {
        Routine routine = createRoutine();
        storIOSQLite.put().object(routine).prepare().executeAsBlocking();

        List<Routine> routineList = storIOSQLite
                .get()
                .listOfObjects(Routine.class)
                .withQuery(RoutineRelation.getAll().build())
                .prepare()
                .executeAsBlocking();

        Routine getResult = routineList.get(0);

        for (int k = 0; k < 2; k++) {
            for (Routine.RoutineSplit split :
                    getResult.getSplits()) {
                for (int i = 0; i < split.getExercises().size(); i++) {
                    assertEquals(split.getExercises().get(i).getOrderNr(), i);
                }
            }
            //Now we change the orderNr of some exercises
            int fromSplit = getResult.getSplits().get(0).getSplitId();
            int toSplit = fromSplit;
            int fromOrderNr = getResult.getSplits().get(fromSplit)
                    .getExercises()
                    .get(getResult.getSplits().get(fromSplit).getExercises().size() - 1).getOrderNr();
            int toOrderNr = getResult.getSplits().get(toSplit)
                    .getExercises()
                    .get(0).getOrderNr();

//            getResult.moveExercise(fromSplit, toSplit, fromOrderNr, toOrderNr);

            storIOSQLite.put().object(getResult).prepare().executeAsBlocking();
            getResult = storIOSQLite
                    .get()
                    .listOfObjects(Routine.class)
                    .withQuery(RoutineRelation.getAll().build())
                    .prepare()
                    .executeAsBlocking().get(0);
        }
    }

    public void testAddSplit() throws Exception {
        Routine routine = createRoutine();
        routine = saveRoutine(routine);

        assertEquals(3, routine.getSplits().size());

        routine.getSplits().add(Routine.RoutineSplit.newSplit(new ArrayList<>(), "new split"));
        routine = saveRoutine(routine);
        assertEquals("new split", routine.getSplits().get(routine.getSplits().size() - 1).getTitle());

        routine.getSplits().add(Routine.RoutineSplit.newSplit(new ArrayList<>(), "new split2"));
        routine = saveRoutine(routine);
        assertEquals("new split2", routine.getSplits().get(routine.getSplits().size() - 1).getTitle());
    }


    public void testRoutineDeleteExercise() {
        Routine routine = createRoutine();

        for (int i = 0; i < 2; i = (i + 1) * 3) {
            routine = saveRoutine(routine);

            assertEquals(routine.getSplits().get(0).getExercises().size(), 3 - i);
            /*for (int k = i; k < 2; k++) {
                assertEquals(routine.getSplits().get(0).getExercises().get(k).getExercise().getId().intValue(), k);
            }*/

            routine.getSplits().get(0).getExercises().remove(i);

        }
    }

    private Routine saveRoutine(Routine routine) {
        storIOSQLite.put().object(routine).prepare().executeAsBlocking();
        routine = storIOSQLite
                .get()
                .listOfObjects(Routine.class)
                .withQuery(RoutineRelation.getAll().build())
                .prepare()
                .executeAsBlocking()
                .get(0);

        return routine;
    }
}
