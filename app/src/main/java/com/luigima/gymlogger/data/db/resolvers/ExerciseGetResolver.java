package com.luigima.gymlogger.data.db.resolvers;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.luigima.gymlogger.data.db.entities.Exercise;
import com.luigima.gymlogger.data.db.entities.ExerciseHasMuscle;
import com.luigima.gymlogger.data.db.tables.EquipmentTextTranslateTable;
import com.luigima.gymlogger.data.db.tables.MuscleTextTranslateTable;
import com.luigima.gymlogger.data.db.tables.ExerciseHasMuscleTable;
import com.luigima.gymlogger.data.db.tables.ExerciseHasEquipmentTable;
import com.luigima.gymlogger.data.db.tables.ExerciseTable;
import com.luigima.gymlogger.data.db.tables.ExerciseTextTranslateTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.get.GetResolver;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;

import java.util.ArrayList;
import java.util.HashMap;


public class ExerciseGetResolver extends GetResolver<Exercise> {

    @NonNull
    private final ThreadLocal<StorIOSQLite> storIOSQLiteFromPerformGet =
            new ThreadLocal<>();

    public ExerciseGetResolver() {
    }


    @NonNull
    @Override
    public Exercise mapFromCursor(@NonNull Cursor cursor) {
        final StorIOSQLite storIOSQLite = storIOSQLiteFromPerformGet.get();

        final ArrayList<ExerciseHasMuscle> categories = new ArrayList<>();
        final HashMap<Integer, String> equipments = new HashMap<>();
        final int exerciseId = cursor.getInt(cursor.getColumnIndex(ExerciseTable.COLUMN_ID));

        Cursor categoryCursor = storIOSQLite
                .get().cursor()
                .withQuery(RawQuery.builder().query(
                        "SELECT * FROM " +
                                ExerciseHasMuscleTable.TABLE +
                                " LEFT JOIN " + MuscleTextTranslateTable.TABLE +
                                " ON " + ExerciseHasMuscleTable.TABLE + "." + ExerciseHasMuscleTable.COLUMN_MUSCLE_ID +
                                "=" + MuscleTextTranslateTable.TABLE + "." + MuscleTextTranslateTable.COLUMN_MUSCLE_ID +
                                " WHERE " +
                                ExerciseHasMuscleTable.TABLE + "." + ExerciseHasMuscleTable.COLUMN_EXERCISE_ID +
                                "=" + exerciseId
                ).build())
                .prepare()
                .executeAsBlocking();

        Cursor equipmentCursor = storIOSQLite
                .get().cursor()
                .withQuery(RawQuery.builder().query(
                        "SELECT * FROM " +
                                ExerciseHasEquipmentTable.TABLE +
                                " LEFT JOIN " + EquipmentTextTranslateTable.TABLE +
                                " ON " + ExerciseHasEquipmentTable.TABLE + "." + ExerciseHasEquipmentTable.COLUMN_EQUIPMENT_ID +
                                "=" + EquipmentTextTranslateTable.TABLE + "." + EquipmentTextTranslateTable.COLUMN_EQUIPMENT_ID +
                                " WHERE " +
                                ExerciseHasEquipmentTable.TABLE + "." + ExerciseHasEquipmentTable.COLUMN_EXERCISE_ID +
                                "=" + exerciseId
                ).build())
                .prepare()
                .executeAsBlocking();

        for (equipmentCursor.moveToFirst(); !equipmentCursor.isAfterLast(); equipmentCursor.moveToNext()) {
            equipments.put(
                    equipmentCursor.getInt(equipmentCursor.getColumnIndex(ExerciseHasEquipmentTable.COLUMN_EQUIPMENT_ID)),
                    equipmentCursor.getString(equipmentCursor.getColumnIndex(EquipmentTextTranslateTable.COLUMN_TITLE))
            );
        }

        for (categoryCursor.moveToFirst(); !categoryCursor.isAfterLast(); categoryCursor.moveToNext()) {
            categories.add(new ExerciseHasMuscle(
                    categoryCursor.getInt(categoryCursor.getColumnIndex(ExerciseHasMuscleTable.COLUMN_MUSCLE_ID)),
                    categoryCursor.getInt(categoryCursor.getColumnIndex(ExerciseHasMuscleTable.COLUMN_IS_PRIMARY)),
                    categoryCursor.getString(categoryCursor.getColumnIndex(MuscleTextTranslateTable.COLUMN_TITLE))
            ));
        }

        categoryCursor.close();
        equipmentCursor.close();

        return new Exercise(
                exerciseId,
                cursor.getInt(cursor.getColumnIndex(ExerciseTable.COLUMN_CREATOR_ID)),
                cursor.getString(cursor.getColumnIndex(ExerciseTable.COLUMN_IMAGE1)),
                cursor.getString(cursor.getColumnIndex(ExerciseTable.COLUMN_IMAGE2)),
                cursor.getInt(cursor.getColumnIndex(ExerciseTable.COLUMN_TYPE)),
                cursor.getInt(cursor.getColumnIndex(ExerciseTable.COLUMN_DIFFICULTY)),
                cursor.getInt(cursor.getColumnIndex(ExerciseTable.COLUMN_IS_LOCAL)),
                cursor.getString(cursor.getColumnIndex(ExerciseTextTranslateTable.COLUMN_STEPS)),
                cursor.getString(cursor.getColumnIndex(ExerciseTextTranslateTable.COLUMN_TITLE)),
                equipments,
                categories
        );
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
