package com.luigima.gymlogger.data.db.entities;

import java.util.List;

public class Workout {
    private Integer workoutId;
    private int routineId;
    private int duration;
    private long date;
    private String notes;
    private List<WorkoutExercise> exercises;
    private int splitId;
    private String splitTitle;

    public static class WorkoutExercise {
        private Integer splitHasExercisesId;
        private Exercise exercise;
        private int orderNr;
        private String notes;
        private List<WorkoutSet> sets;

        public WorkoutExercise(Integer splitHasExercisesId, Exercise exercise, int orderNr, String notes, List<WorkoutSet> sets) {
            this.splitHasExercisesId = splitHasExercisesId;
            this.exercise = exercise;
            this.orderNr = orderNr;
            this.notes = notes;
            this.sets = sets;
        }

        public void setSplitHasExercisesId(Integer splitHasExercisesId) {
            this.splitHasExercisesId = splitHasExercisesId;
        }

        public Integer getSplitHasExercisesId() {
            return splitHasExercisesId;
        }

        public Exercise getExercise() {
            return exercise;
        }

        public int getOrderNr() {
            return orderNr;
        }

        public String getNotes() {
            return notes;
        }

        public List<WorkoutSet> getSets() {
            return sets;
        }

        public WorkoutSet getSetById(int setId) {
            for (Workout.WorkoutSet set :
                    getSets()) {
                if (set.getSetId() == setId) {
                    return set;
                }
            }
            return null;
        }
    }

    public static class WorkoutSet {
        private Integer setId;
        private int repeatsShould;
        private int repeatsIs;

        private float weightShould;
        private float weightIs;

        private int durationShould;
        private int durationIs;

        private long date;


        public WorkoutSet(Integer setId, int repeatsShould, int repeatsIs, float weightShould, float weightIs, int durationShould, int durationIs, long date) {
            this.setId = setId;
            this.repeatsShould = repeatsShould;
            this.repeatsIs = repeatsIs;
            this.weightShould = weightShould;
            this.weightIs = weightIs;
            this.durationShould = durationShould;
            this.durationIs = durationIs;
            this.date = date;
        }

        public long getDate() {
            return date;
        }

        public void setSetId(Integer setId) {
            this.setId = setId;
        }

        public Integer getSetId() {
            return setId;
        }

        public int getRepeatsShould() {
            return repeatsShould;
        }

        public int getRepeatsIs() {
            return repeatsIs;
        }

        public float getWeightShould() {
            return weightShould;
        }

        public float getWeightIs() {
            return weightIs;
        }

        public int getDurationShould() {
            return durationShould;
        }

        public int getDurationIs() {
            return durationIs;
        }

        public void setRepeatsShould(int repeatsShould) {
            this.repeatsShould = repeatsShould;
        }

        public void setRepeatsIs(int repeatsIs) {
            this.repeatsIs = repeatsIs;
        }

        public void setWeightShould(float weightShould) {
            this.weightShould = weightShould;
        }

        public void setWeightIs(float weightIs) {
            this.weightIs = weightIs;
        }

        public void setDurationShould(int durationShould) {
            this.durationShould = durationShould;
        }

        public void setDurationIs(int durationIs) {
            this.durationIs = durationIs;
        }

        public void setDate(long date) {
            this.date = date;
        }
    }

    public Workout(Integer workoutId, int routineId, int duration, long date, String notes, List<WorkoutExercise> exercises, int splitId, String splitTitle) {
        this.workoutId = workoutId;
        this.routineId = routineId;
        this.duration = duration;
        this.date = date;
        this.notes = notes;
        this.exercises = exercises;
        this.splitId = splitId;
        this.splitTitle = splitTitle;
    }

    public static Workout newWorkout(int routineId, long date, String notes, List<WorkoutExercise> exercises, int splitId) {
        return new Workout(null, routineId, 0, date, notes, exercises, splitId, null);
    }

    public static WorkoutSet newWorkoutSet() {
        return new WorkoutSet(null, 0, 0, 0, 0, 0, 0, 0);
    }

    public Integer getWorkoutId() {
        return workoutId;
    }

    public int getRoutineId() {
        return routineId;
    }

    public int getDuration() {
        return duration;
    }

    public long getDate() {
        return date;
    }

    public String getNotes() {
        return notes;
    }

    public int getSplitId() {
        return splitId;
    }

    public void setSplitId(int splitId) {
        this.splitId = splitId;
    }

    public String getSplitTitle() {
        return splitTitle;
    }

    public void setSplitTitle(String splitTitle) {
        this.splitTitle = splitTitle;
    }

    public List<WorkoutExercise> getExercises() {
        return exercises;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public void setRoutineId(int routineId) {
        this.routineId = routineId;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setExercises(List<WorkoutExercise> exercises) {
        this.exercises = exercises;
    }

    public WorkoutExercise getExerciseBySplitHasExerciseId(int id) {
        for (WorkoutExercise exercise :
                getExercises()) {
            if (exercise.getSplitHasExercisesId().equals(id)) {
                return exercise;
            }
        }
        return null;
    }

}