package com.luigima.gymlogger;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.luigima.gymlogger.data.db.DbModule;
import com.luigima.gymlogger.data.db.GymLoggerSQLiteOpenHelper;
import com.luigima.gymlogger.data.db.entities.Exercise;
import com.luigima.gymlogger.data.db.relations.ExerciseRelation;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import java.util.List;

public class GymLoggerSQLiteOpenHelperTestCase extends InstrumentationTestCase {

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

        databaseHelper = new GymLoggerSQLiteOpenHelper(context);
        assertNotNull(databaseHelper);
        storIOSQLite = (new DbModule()).provideStorIOSQLite(databaseHelper);
        assertNotNull(databaseHelper);
        assertNotNull(storIOSQLite);

    }

    public void testPreConditions() {

        List<Exercise> exerciseList = storIOSQLite
                .get()
                .listOfObjects(Exercise.class)
                .withQuery(ExerciseRelation.getAll())
                .prepare()
                .executeAsBlocking();

        assertEquals(286, exerciseList.size());
    }

}