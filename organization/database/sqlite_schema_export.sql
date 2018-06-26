-- Creator:       MySQL Workbench 6.3.6/ExportSQLite Plugin 0.1.0
-- Author:        lukma
-- Caption:       New Model
-- Project:       Name of the project
-- Changed:       2016-03-20 16:57
-- Created:       2016-03-20 00:38
PRAGMA foreign_keys = OFF;

CREATE TABLE "routine_text_translate"(
  "routine_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "language_id" INTEGER NOT NULL,
  "title" TEXT NOT NULL,
  "description" TEXT DEFAULT NULL
);
CREATE TABLE "equipment"(
  "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
);
CREATE INDEX "equipment._id" ON "equipment" ("_id");
CREATE TABLE "exercise_has_equipment"(
  "exercise_id" INTEGER NOT NULL,
  "equipment_id" INTEGER NOT NULL,
  PRIMARY KEY("exercise_id","equipment_id"),
  CONSTRAINT "fk_exercise_has_equipment_equipment1"
    FOREIGN KEY("equipment_id")
    REFERENCES "equipment"("_id")
);
CREATE INDEX "exercise_has_equipment.fk_exercise_has_equipment_equipment1_idx" ON "exercise_has_equipment" ("equipment_id");
CREATE TABLE "exercise"(
  "_id" INTEGER PRIMARY KEY NOT NULL,
  "creator_id" INTEGER NOT NULL DEFAULT '0',
  "image1" TEXT DEFAULT NULL,
  "image2" TEXT DEFAULT NULL,
  "difficulty" INTEGER DEFAULT '5',
  "type" INTEGER DEFAULT NULL,
  "is_bodyweight" INTEGER DEFAULT '0',
  "is_local" INTEGER DEFAULT NULL,
  CONSTRAINT "fk_exercise_exercise_has_equipment1"
    FOREIGN KEY("_id")
    REFERENCES "exercise_has_equipment"("exercise_id")
);
CREATE TABLE "workout"(
  "_id" INTEGER PRIMARY KEY NOT NULL,
  "routine_id" INTEGER NOT NULL,
  "duration" INTEGER DEFAULT NULL,
  "date" INTEGER DEFAULT NULL,
  "notes" TEXT DEFAULT NULL
);
CREATE TABLE "muscle_text_translate"(
  "muscle_id" INTEGER NOT NULL,
  "language_id" INTEGER NOT NULL,
  "title" TEXT NOT NULL,
  PRIMARY KEY("muscle_id","language_id")
);
CREATE TABLE "routine_category_text_translate"(
  "routine_category_id" INTEGER NOT NULL,
  "language_id" INTEGER NOT NULL,
  "title" TEXT NOT NULL,
  PRIMARY KEY("routine_category_id","language_id")
);
CREATE TABLE "routine_has_splits"(
  "routine_id" INTEGER NOT NULL,
  "split_id" INTEGER NOT NULL,
  "title" TEXT NOT NULL,
  PRIMARY KEY("routine_id","split_id")
);
CREATE TABLE "muscle"(
  "_id" INTEGER PRIMARY KEY NOT NULL,
  CONSTRAINT "fk_muscle_muscle_text_translate1"
    FOREIGN KEY("_id")
    REFERENCES "muscle_text_translate"("muscle_id")
);
CREATE TABLE "exercise_text_translate"(
  "exercise_id" INTEGER NOT NULL,
  "language_id" INTEGER NOT NULL,
  "title" TEXT NOT NULL,
  "steps" TEXT DEFAULT NULL,
  PRIMARY KEY("exercise_id","language_id"),
  CONSTRAINT "fk_exercise_text_translate_exercise1"
    FOREIGN KEY("exercise_id")
    REFERENCES "exercise"("_id")
);
CREATE TABLE "muscle_belongsto_main_muscle_group"(
  "muscle_id" INTEGER NOT NULL,
  "main_muscle_group_id" INTEGER NOT NULL,
  CONSTRAINT "fk_muscle_belongsto_main_muscle_group_muscle1"
    FOREIGN KEY("muscle_id")
    REFERENCES "muscle"("_id")
);
CREATE INDEX "muscle_belongsto_main_muscle_group.fk_muscle_belongsto_main_muscle_group_muscle1_idx" ON "muscle_belongsto_main_muscle_group" ("muscle_id");
CREATE TABLE "workout_trains_exercise"(
  "workout_id" INTEGER NOT NULL,
  "routine_has_exercise_id" INTEGER NOT NULL,
  "weight" INTEGER DEFAULT NULL,
  "repeats" INTEGER DEFAULT NULL,
  "date" INTEGER DEFAULT NULL,
  "duration" INTEGER DEFAULT NULL,
  "notes" TEXT DEFAULT NULL,
  PRIMARY KEY("workout_id","routine_has_exercise_id"),
  CONSTRAINT "workout_trains_exercise_ibfk_1"
    FOREIGN KEY("workout_id")
    REFERENCES "workout"("_id")
);
CREATE TABLE "main_muscle_group"(
  "_id" INTEGER PRIMARY KEY NOT NULL,
  CONSTRAINT "fk_main_muscle_group_muscle_belongsto_main_muscle_group1"
    FOREIGN KEY("_id")
    REFERENCES "muscle_belongsto_main_muscle_group"("main_muscle_group_id")
);
CREATE TABLE "exercise_has_muscle"(
  "exercise_id" INTEGER NOT NULL,
  "muscle_id" INTEGER NOT NULL,
  "is_primary" INTEGER DEFAULT '1',
  PRIMARY KEY("exercise_id","muscle_id"),
  CONSTRAINT "fk_exercise_has_muscle_exercise1"
    FOREIGN KEY("exercise_id")
    REFERENCES "exercise"("_id"),
  CONSTRAINT "fk_exercise_has_muscle_muscle1"
    FOREIGN KEY("muscle_id")
    REFERENCES "muscle"("_id")
);
CREATE INDEX "exercise_has_muscle.fk_exercise_has_muscle_muscle1_idx" ON "exercise_has_muscle" ("muscle_id");
CREATE TABLE "main_muscle_group_text_translate"(
  "main_muscle_group_id" INTEGER NOT NULL,
  "language_id" INTEGER NOT NULL,
  "title" TEXT NOT NULL,
  CONSTRAINT "fk_main_muscle_group_text_translate_main_muscle_group1"
    FOREIGN KEY("main_muscle_group_id")
    REFERENCES "main_muscle_group"("_id")
);
CREATE INDEX "main_muscle_group_text_translate.fk_main_muscle_group_text_translate_main_muscle_group1_idx" ON "main_muscle_group_text_translate" ("main_muscle_group_id");
CREATE TABLE "routine_has_exercises"(
  "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "routine_id" INTEGER NOT NULL,
  "exercise_id" INTEGER NOT NULL,
  "weight" INTEGER DEFAULT NULL,
  "order_nr" INTEGER NOT NULL,
  "repeats" INTEGER DEFAULT NULL,
  "duration" INTEGER DEFAULT NULL,
  "split_id" INTEGER DEFAULT NULL,
  CONSTRAINT "fk_routine_has_exercises_exercise1"
    FOREIGN KEY("exercise_id")
    REFERENCES "exercise"("_id"),
  CONSTRAINT "fk_routine_has_exercises_routine_has_splits1"
    FOREIGN KEY("split_id")
    REFERENCES "routine_has_splits"("split_id"),
  CONSTRAINT "fk_routine_has_exercises_workout_trains_exercise1"
    FOREIGN KEY("_id")
    REFERENCES "workout_trains_exercise"("routine_has_exercise_id")
);
CREATE INDEX "routine_has_exercises.fk_routine_has_exercises_exercise1_idx" ON "routine_has_exercises" ("exercise_id");
CREATE INDEX "routine_has_exercises.fk_routine_has_exercises_routine_has_splits1_idx" ON "routine_has_exercises" ("split_id");
CREATE TABLE "bodymetric"(
  "_id" INTEGER NOT NULL,
  "user_id" INTEGER NOT NULL,
  "height" INTEGER DEFAULT NULL,
  "weight" INTEGER DEFAULT NULL,
  PRIMARY KEY("_id","user_id"),
  CONSTRAINT "fk_bodymetric_user1"
    FOREIGN KEY("user_id")
    REFERENCES "user"("_id")
);
CREATE INDEX "bodymetric._id" ON "bodymetric" ("_id","user_id","height","weight");
CREATE INDEX "bodymetric.fk_bodymetric_user1_idx" ON "bodymetric" ("user_id");
CREATE TABLE "statistics"(
  "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "max1rep" INTEGER DEFAULT NULL,
  CONSTRAINT "fk_statistics_user_statistic1"
    FOREIGN KEY("_id")
    REFERENCES "user_statistic"("statistics_id")
);
CREATE TABLE "user_statistic"(
  "user_id" INTEGER NOT NULL,
  "statistics_id" INTEGER NOT NULL,
  "exercise_id" INTEGER DEFAULT NULL,
  "date" INTEGER DEFAULT NULL,
  PRIMARY KEY("user_id","statistics_id"),
  CONSTRAINT "fk_user_statistic_user1"
    FOREIGN KEY("user_id")
    REFERENCES "user"("_id")
);
CREATE TABLE "routine"(
  "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "routine_category_id" INTEGER DEFAULT NULL,
  "creator_id" INTEGER DEFAULT NULL,
  "creation_date" INTEGER DEFAULT NULL,
  "difficulty" INTEGER DEFAULT NULL,
  "rating" INTEGER DEFAULT NULL,
  "is_local" INTEGER DEFAULT NULL,
  CONSTRAINT "fk_routine_routine_has_exercises1"
    FOREIGN KEY("_id")
    REFERENCES "routine_has_exercises"("routine_id"),
  CONSTRAINT "fk_routine_routine_has_splits1"
    FOREIGN KEY("_id")
    REFERENCES "routine_has_splits"("routine_id"),
  CONSTRAINT "fk_routine_workout1"
    FOREIGN KEY("_id")
    REFERENCES "workout"("routine_id"),
  CONSTRAINT "fk_routine_routine_text_translate1"
    FOREIGN KEY("_id")
    REFERENCES "routine_text_translate"("routine_id")
);
CREATE INDEX "routine._id" ON "routine" ("_id","routine_category_id","creator_id","creation_date","difficulty","rating","is_local");
CREATE TABLE "equipment_text_translate"(
  "equipment_id" INTEGER NOT NULL,
  "language_id" INTEGER NOT NULL,
  "title" TEXT NOT NULL,
  PRIMARY KEY("equipment_id","language_id"),
  CONSTRAINT "equipment_text_translate_ibfk_1"
    FOREIGN KEY("language_id")
    REFERENCES "language"("_id"),
  CONSTRAINT "fk_equipment_text_translate_equipment1"
    FOREIGN KEY("equipment_id")
    REFERENCES "equipment"("_id")
);
CREATE INDEX "equipment_text_translate.language_id" ON "equipment_text_translate" ("language_id");
CREATE TABLE "routine_category"(
  "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  CONSTRAINT "fk_routine_category_routine1"
    FOREIGN KEY("_id")
    REFERENCES "routine"("routine_category_id"),
  CONSTRAINT "fk_routine_category_routine_category_text_translate1"
    FOREIGN KEY("_id")
    REFERENCES "routine_category_text_translate"("routine_category_id")
);
CREATE TABLE "user"(
  "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "username" TEXT DEFAULT NULL,
  "session" TEXT DEFAULT NULL,
  "email" TEXT DEFAULT NULL,
  "first_name" TEXT DEFAULT NULL,
  "last_name" TEXT DEFAULT NULL,
  "birthday" INTEGER DEFAULT NULL,
  "gender" INTEGER DEFAULT NULL,
  CONSTRAINT "fk_user_user_bodymetric1"
    FOREIGN KEY("_id")
    REFERENCES "user_bodymetric"("user_id")
);
CREATE TABLE "user_bodymetric"(
  "user_id" INTEGER NOT NULL,
  "bodymetric_id" INTEGER NOT NULL,
  "date" INTEGER DEFAULT NULL,
  PRIMARY KEY("user_id","bodymetric_id"),
  CONSTRAINT "fk_user_bodymetric_bodymetric1"
    FOREIGN KEY("bodymetric_id")
    REFERENCES "bodymetric"("_id")
);
CREATE INDEX "user_bodymetric.fk_user_bodymetric_bodymetric1_idx" ON "user_bodymetric" ("bodymetric_id");
CREATE TABLE "language"(
  "_id" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  "name" TEXT DEFAULT NULL,
  CONSTRAINT "fk_language_exercise_text_translate1"
    FOREIGN KEY("_id")
    REFERENCES "exercise_text_translate"("language_id"),
  CONSTRAINT "fk_language_routine_category_text_translate1"
    FOREIGN KEY("_id")
    REFERENCES "routine_category_text_translate"("language_id"),
  CONSTRAINT "fk_language_routine_text_translate1"
    FOREIGN KEY("_id")
    REFERENCES "routine_text_translate"("language_id"),
  CONSTRAINT "fk_language_muscle_text_translate1"
    FOREIGN KEY("_id")
    REFERENCES "muscle_text_translate"("language_id"),
  CONSTRAINT "fk_language_main_muscle_group_text_translate1"
    FOREIGN KEY("_id")
    REFERENCES "main_muscle_group_text_translate"("language_id")
);
