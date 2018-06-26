package com.luigima.gymlogger.data.db.relations;

import com.luigima.gymlogger.data.db.tables.LanguageTable;
import com.luigima.gymlogger.data.db.tables.RoutineTable;
import com.luigima.gymlogger.data.db.tables.RoutineTextTranslateTable;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;

import timber.log.Timber;

public class RoutineRelation {
    public RoutineRelation() {
    }

    private static final String ROUTINE_TEXT =
            RoutineTextTranslateTable.TABLE +
                    " LEFT JOIN " + LanguageTable.TABLE +
                    " ON " + RoutineTextTranslateTable.TABLE + "." + RoutineTextTranslateTable.COLUMN_LANGUAGE_ID + "=" +
                    LanguageTable.TABLE + "." + LanguageTable.COLUMN_ID;

    private static final String ROUTINE =
            "SELECT " +
                    RoutineTable.TABLE + "." + RoutineTable.COLUMN_ID + "," +
                    RoutineTable.TABLE + "." + RoutineTable.COLUMN_CATEGORY_ID + "," +
                    RoutineTable.TABLE + "." + RoutineTable.COLUMN_CREATOR_ID + "," +
                    RoutineTable.TABLE + "." + RoutineTable.COLUMN_CREATION_DATE + "," +
                    RoutineTable.TABLE + "." + RoutineTable.COLUMN_DIFFICULTY + "," +
                    RoutineTable.TABLE + "." + RoutineTable.COLUMN_RATING + "," +
                    RoutineTable.TABLE + "." + RoutineTable.COLUMN_IS_LOCAL + "," +
                    "tr." + RoutineTextTranslateTable.COLUMN_TITLE + "," +
                    "tr." + RoutineTextTranslateTable.COLUMN_DESCRIPTION +
                    " FROM " + RoutineTable.TABLE +
                    " LEFT JOIN ( " + ROUTINE_TEXT + " ) AS tr " +
                    " ON " + RoutineTable.TABLE + "." + RoutineTable.COLUMN_ID + "=" +
                    "tr." + RoutineTextTranslateTable.COLUMN_ROUTINE_ID;

    public static RawQuery.CompleteBuilder getAll() {
        Timber.d(ROUTINE);
        return RawQuery.builder()
                .query(ROUTINE + ";");
    }

    public static RawQuery.CompleteBuilder getById(int id) {
        return RawQuery.builder()
                .query(ROUTINE +
                        " WHERE " + RoutineTable.TABLE + "." + RoutineTable.COLUMN_ID +
                        " = " + id + ";");
    }

    public static RawQuery getByCategory(int id) {
        return RawQuery.builder()
                .query(ROUTINE +
                        " WHERE " + RoutineTable.TABLE + "." + RoutineTable.COLUMN_CATEGORY_ID +
                        " = " + id + ";")
                .build();
    }

}
