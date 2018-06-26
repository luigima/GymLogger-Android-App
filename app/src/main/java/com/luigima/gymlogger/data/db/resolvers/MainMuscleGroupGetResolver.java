package com.luigima.gymlogger.data.db.resolvers;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.luigima.gymlogger.data.db.entities.sql.DBMuscle;
import com.luigima.gymlogger.data.db.entities.MainMuscleGroup;
import com.luigima.gymlogger.data.db.tables.MuscleBelongsToMainMuscleGroupTable;
import com.luigima.gymlogger.data.db.tables.MuscleTable;
import com.luigima.gymlogger.data.db.tables.MainMuscleGroupTable;
import com.luigima.gymlogger.data.db.tables.MainMuscleGroupTextTranslateTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.get.GetResolver;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;

import java.util.List;


public class MainMuscleGroupGetResolver extends GetResolver<MainMuscleGroup> {

    @NonNull
    private final ThreadLocal<StorIOSQLite> storIOSQLiteFromPerformGet =
            new ThreadLocal<>();

    public MainMuscleGroupGetResolver() {
    }

    @NonNull
    @Override
    public MainMuscleGroup mapFromCursor(Cursor cursor) {
        final StorIOSQLite storIOSQLite = storIOSQLiteFromPerformGet.get();

        int id = cursor.getInt(cursor.getColumnIndex(MainMuscleGroupTable.COLUMN_ID));

        Cursor titleCursor = storIOSQLite.get().cursor().withQuery(Query.builder()
                .table(MainMuscleGroupTextTranslateTable.TABLE)
                .where(MainMuscleGroupTextTranslateTable.COLUMN_MAIN_CATEGORY_ID + "= ?")
                .whereArgs(id)
                .build())
                .prepare()
                .executeAsBlocking();

        String title = "-";
        if(titleCursor.moveToNext()) {
            title = titleCursor.getString(titleCursor.getColumnIndex(MainMuscleGroupTextTranslateTable.COLUMN_TITLE));
        }

        List<DBMuscle> exerciseCategories = storIOSQLite.get()
                .listOfObjects(DBMuscle.class)
                .withQuery(Query.builder()
                        .table(MuscleBelongsToMainMuscleGroupTable.TABLE)
                        .columns(MuscleBelongsToMainMuscleGroupTable.TABLE +
                                "." + MuscleBelongsToMainMuscleGroupTable.COLUMN_MUSCLE_ID +
                                " AS " + MuscleTable.COLUMN_ID)
                        .where(MuscleBelongsToMainMuscleGroupTable.COLUMN_MAIN_MUSCLE_GROUP_ID + "= ?")
                        .whereArgs(id)
                        .build())
                .prepare()
                .executeAsBlocking();

        return new MainMuscleGroup(id, title, exerciseCategories);
    }

    @NonNull
    @Override
    public Cursor performGet(@NonNull StorIOSQLite storIOSQLite, @NonNull RawQuery rawQuery) {
        storIOSQLiteFromPerformGet.set(storIOSQLite);
        return storIOSQLite.internal().rawQuery(rawQuery);
    }

    @NonNull
    @Override
    public Cursor performGet(@NonNull StorIOSQLite storIOSQLite, @NonNull Query query) {
        storIOSQLiteFromPerformGet.set(storIOSQLite);
        return storIOSQLite.internal().query(query);
    }
}
