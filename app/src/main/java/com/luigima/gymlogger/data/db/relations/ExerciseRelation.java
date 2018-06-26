package com.luigima.gymlogger.data.db.relations;

import com.luigima.gymlogger.data.db.tables.EquipmentTextTranslateTable;
import com.luigima.gymlogger.data.db.tables.MuscleTextTranslateTable;
import com.luigima.gymlogger.data.db.tables.ExerciseHasMuscleTable;
import com.luigima.gymlogger.data.db.tables.ExerciseHasEquipmentTable;
import com.luigima.gymlogger.data.db.tables.ExerciseTable;
import com.luigima.gymlogger.data.db.tables.ExerciseTextTranslateTable;
import com.luigima.gymlogger.data.db.tables.LanguageTable;
import com.luigima.gymlogger.data.db.tables.MainMuscleGroupTable;
import com.luigima.gymlogger.data.db.tables.MainMuscleGroupTextTranslateTable;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;

public class ExerciseRelation {
    public ExerciseRelation() {
    }

    private static final String EXERCISE_TEXT =
            ExerciseTextTranslateTable.TABLE +
                    " LEFT JOIN " + LanguageTable.TABLE +
                    " ON " + ExerciseTextTranslateTable.TABLE + "." + ExerciseTextTranslateTable.COLUMN_LANGUAGE_ID + "=" +
                    LanguageTable.TABLE + "." + LanguageTable.COLUMN_ID;

    private static final String EXERCISE_CATEGORY =
            ExerciseHasMuscleTable.TABLE +
                    " LEFT JOIN " + MuscleTextTranslateTable.TABLE +
                    " ON " + ExerciseHasMuscleTable.TABLE + "." + ExerciseHasMuscleTable.COLUMN_MUSCLE_ID + "=" +
                    MuscleTextTranslateTable.TABLE + "." + MuscleTextTranslateTable.COLUMN_MUSCLE_ID;

    private static final String EXERCISE_EQUIPMENT =
            ExerciseHasEquipmentTable.TABLE +
                    " LEFT JOIN " + EquipmentTextTranslateTable.TABLE +
                    " ON " + ExerciseHasEquipmentTable.TABLE + "." + ExerciseHasEquipmentTable.COLUMN_EQUIPMENT_ID + "=" +
                    EquipmentTextTranslateTable.TABLE + "." + EquipmentTextTranslateTable.COLUMN_EQUIPMENT_ID;

    private static final String EXERCISE =
            "SELECT " +
                    ExerciseTable.TABLE + "." + ExerciseTable.COLUMN_ID + "," +
                    ExerciseTable.TABLE + "." + ExerciseTable.COLUMN_CREATOR_ID + "," +
                    ExerciseTable.TABLE + "." + ExerciseTable.COLUMN_DIFFICULTY + "," +
                    ExerciseTable.TABLE + "." + ExerciseTable.COLUMN_IMAGE1 + "," +
                    ExerciseTable.TABLE + "." + ExerciseTable.COLUMN_IMAGE2 + "," +
                    ExerciseTable.TABLE + "." + ExerciseTable.COLUMN_IS_LOCAL + "," +
                    ExerciseTable.TABLE + "." + ExerciseTable.COLUMN_TYPE + "," +
                    "tr." + ExerciseTextTranslateTable.COLUMN_STEPS + "," +
                    "tr." + ExerciseTextTranslateTable.COLUMN_TITLE +
                    " FROM " + ExerciseTable.TABLE +
                    " LEFT JOIN ( " + EXERCISE_TEXT + " ) AS tr " +
                    " ON " + ExerciseTable.TABLE + "." + ExerciseTable.COLUMN_ID + "=" +
                    "tr." + ExerciseTextTranslateTable.COLUMN_EXERCISE_ID;

    public static RawQuery getAll() {
        return RawQuery.builder()
                .query(EXERCISE + ";")
                .build();
    }

    public static RawQuery getById(int id) {
        return RawQuery.builder()
                .query(EXERCISE +
                        " WHERE " + ExerciseTable.TABLE + "." + ExerciseTable.COLUMN_ID +
                        " = " + id + ";")
                .build();
    }

    public static RawQuery getByCategory(int id) {
        return RawQuery.builder()
                .query(EXERCISE + " LEFT JOIN ( " + EXERCISE_CATEGORY + " ) AS cat ON " +
                        ExerciseTable.TABLE + "." + ExerciseTable.COLUMN_ID + "=cat." + ExerciseHasMuscleTable.COLUMN_EXERCISE_ID +
                        " WHERE " + "cat." + MuscleTextTranslateTable.COLUMN_MUSCLE_ID +
                        " = " + id + ";")
                .build();
    }

    public static RawQuery getMainCategories() {
        return RawQuery.builder()
                .query("SELECT * FROM " + MainMuscleGroupTable.TABLE + "," + MainMuscleGroupTextTranslateTable.TABLE +
                        " WHERE " + MainMuscleGroupTable.TABLE + "." + MainMuscleGroupTable.COLUMN_ID +
                        " = " + MainMuscleGroupTextTranslateTable.TABLE + "." + MainMuscleGroupTextTranslateTable.COLUMN_MAIN_CATEGORY_ID)
                .build();
    }
}
