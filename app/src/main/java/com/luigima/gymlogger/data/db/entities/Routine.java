package com.luigima.gymlogger.data.db.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import timber.log.Timber;

public class Routine {
    private Integer routineID;
    private int routineCategoryId;
    private String title;
    private String description;

    private int creatorId;
    private long creationDate;
    private int difficulty;
    private float rating;
    private int isLocal;

    private List<RoutineSplit> splits;

    public static class RoutineSplit {
        private Integer splitId;
        private List<RoutineExercise> exercises;
        private String title;

        public RoutineSplit(Integer splitId, List<RoutineExercise> exercises, String title) {
            this.splitId = splitId;
            this.exercises = exercises;
            this.title = title;
        }

        public static RoutineSplit newSplit(List<RoutineExercise> exercises, String title) {
            return new RoutineSplit(null, exercises, title);
        }

        public Integer getSplitId() {
            return splitId;
        }

        public List<RoutineExercise> getExercises() {
            return exercises;
        }

        public String getTitle() {
            return title;
        }

        public void setSplitId(Integer splitId) {
            this.splitId = splitId;
        }
    }

    public static class RoutineExercise {
        private Integer splitHasExerciseId;
        private Exercise exercise;
        private int orderNr;
        private List<RoutineSet> sets;

        public RoutineExercise(Integer splitHasExerciseId, Exercise exercise, int orderNr, List<RoutineSet> sets) {
            this.splitHasExerciseId = splitHasExerciseId;
            this.exercise = exercise;
            this.orderNr = orderNr;
            this.sets = sets;
        }

        public static RoutineExercise newExercise(Exercise exercise, int orderNr, List<RoutineSet> sets) {
            return new RoutineExercise(null, exercise, orderNr, sets);
        }


        public int getOrderNr() {
            return orderNr;
        }

        public Integer getSplitHasExerciseId() {
            return splitHasExerciseId;
        }

        public Exercise getExercise() {
            return exercise;
        }

        public List<RoutineSet> getSets() {
            return sets;
        }

        public void setSplitHasExerciseId(Integer splitHasExerciseId) {
            this.splitHasExerciseId = splitHasExerciseId;
        }

        public void setOrderNr(int orderNr) {
            this.orderNr = orderNr;
        }
    }

    public static class RoutineSet {
        private Integer setId;
        private int repeatsShould;
        private float weightShould;
        private int durationShould;

        public RoutineSet(Integer setId, int repeatsShould, float weightShould, int durationShould) {
            this.setId = setId;
            this.repeatsShould = repeatsShould;
            this.weightShould = weightShould;
            this.durationShould = durationShould;
        }

        public static RoutineSet newSet(int repeatsShould, float weightShould, int durationShould) {
            return new RoutineSet(null, repeatsShould, weightShould, durationShould);
        }

        public Integer getSetId() {
            return setId;
        }

        public int getRepeatsShould() {
            return repeatsShould;
        }

        public float getWeightShould() {
            return weightShould;
        }

        public void setSetId(Integer setId) {
            this.setId = setId;
        }

        public int getDurationShould() {
            return durationShould;
        }
    }

    public Routine(Integer routineID, int routineCategoryId, String title, String description, int creatorId,
                   long creationDate, int difficulty, float rating, int isLocal, List<RoutineSplit> splits) {
        this.routineID = routineID;
        this.routineCategoryId = routineCategoryId;
        this.title = title;
        this.description = description;
        this.creatorId = creatorId;
        this.creationDate = creationDate;
        this.difficulty = difficulty;
        this.rating = rating;
        this.isLocal = isLocal;
        this.splits = splits;
    }

    public static Routine newRoutine(int routineCategoryId, String title, String description, int creatorId,
                                     long creationDate, int difficulty, float rating, int isLocal, List<RoutineSplit> splits) {
        return new Routine(null, routineCategoryId, title, description, creatorId,
                creationDate, difficulty, rating, isLocal, splits);
    }

    public Integer getRoutineID() {
        return routineID;
    }

    public int getRoutineCategoryId() {
        return routineCategoryId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public float getRating() {
        return rating;
    }

    public int isLocal() {
        return isLocal;
    }

    public List<RoutineSplit> getSplits() {
        return splits;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRoutineID(Integer routineID) {
        this.routineID = routineID;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public RoutineSplit getSplitbyId(int id) {
        for (RoutineSplit split :
                getSplits()) {
            if (split.getSplitId().equals(id)) {
                return split;
            }
        }
        return null;
    }

    public List<ExerciseHasMuscle> getMuscles(){
        ArrayList<ExerciseHasMuscle> ids = new ArrayList<>();
        for (RoutineSplit split : getSplits()) {
            for (RoutineExercise exercise : split.getExercises()) {
                ids.addAll(exercise.getExercise().getCategories());
            }
        }
        Timber.d(ids.toString());
        return ids;
    }

    public void addNewExerciseWithOneSet(int splitId, Exercise exercise) {
        Routine.RoutineSplit split = getSplitbyId(splitId);
        if (split != null) {
            List<RoutineSet> sets = new ArrayList<>(1);
            sets.add(RoutineSet.newSet(0, 0, 0));
            Routine.RoutineExercise routineExercise =
                    Routine.RoutineExercise.newExercise(exercise, split.getExercises().size(), sets);
            split.getExercises().add(routineExercise);
        }
    }
}