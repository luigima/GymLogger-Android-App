package com.luigima.gymlogger.data.db.entities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Exercise {
    @NonNull
    Integer id;

    @NonNull
    long creatorId;

    @Nullable
    String image1;

    @Nullable
    String image2;

    int difficulty;

    int type;

    @NonNull
    int isLocal;

    @NonNull
    String steps;

    @NonNull
    String title;

    HashMap<Integer, String> equipments;
    ArrayList<ExerciseHasMuscle> categories;

    public Exercise(@Nullable Integer id, long creatorId, @Nullable String image1, @Nullable String image2,
                    int type, int difficulty, int isLocal, @NonNull String steps, @NonNull
                            String title, HashMap<Integer, String> equipments, ArrayList<ExerciseHasMuscle> categories) {
        this.id = id;
        this.creatorId = creatorId;
        this.image1 = image1;
        this.image2 = image2;
        this.type = type;
        this.difficulty = difficulty;
        this.steps = steps;
        this.isLocal = isLocal;
        this.equipments = equipments;
        this.title = title;
        this.categories = categories;
    }

    public Integer getId() {
        return id;
    }

    public long getCreatorId() {
        return creatorId;
    }

    @Nullable
    public String getImage1() {
        return image1;
    }

    @Nullable
    public String getImage2() {
        return image2;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getType() {
        return type;
    }

    public int getIsLocal() {
        return isLocal;
    }

    @NonNull
    public String getSteps() {
        return steps;
    }

    public String getTitle() {
        return title;
    }

    public HashMap<Integer, String> getEquipments() {
        return equipments;
    }

    public ArrayList<ExerciseHasMuscle> getCategories() {
        return categories;
    }

    public String getEquipmentListString() {
        String res = "";
        if (getEquipments().isEmpty()) {
            return res;
        }
        for (String equipment : getEquipments().values()) {
            res += equipment + ", ";
        }
        res = res.substring(0, res.length() - 2);

        return res;
    }

    public Boolean containsSearchQuery(String searchQuery) {
        String dataStrings = getTitle() +
                getEquipmentListString() +
                getMuscleListString();
        dataStrings = dataStrings.toLowerCase();

        return dataStrings.contains(searchQuery);
    }

    public String getMuscleListString() {
        String res = "";
        if (getCategories().isEmpty()) {
            return res;
        }
        for (ExerciseHasMuscle muscle :
                getCategories()) {
            res += muscle.getTitle() + ", ";
        }
        res = res.substring(0, res.length() - 2);

        return res;
    }

    public List<Integer> getMuscleIds() {
        ArrayList<Integer> ids = new ArrayList<>();
        if (getCategories().isEmpty()) {
            return ids;
        }
        for (ExerciseHasMuscle muscle :
                getCategories()) {
            ids.add(muscle.getId());
        }

        return ids;
    }

    public String test() {
        String a1 = "{";
        for (ExerciseHasMuscle e : categories) {
            a1 += e.title + ";";
        }
        a1 += "}";

        String a2 = "{";
        for (Map.Entry<Integer, String> entry : equipments.entrySet()) {
            a2 += entry.getValue() + ";";
        }
        a2 += "}";

        return "id : " + id + "\n" +
                "title : " + title + "\n" +
                "image1 : " + image1 + "\n" +
                "image2 : " + image2 + "\n" +
                //"type : " + type + "\n" +
                "difficulty : " + difficulty + "\n" +
                "steps : " + steps + "\n" +
                "isLocal : " + isLocal + "\n" +
                "equipments : " + a2 + "\n" +
                "categories : " + a1 + "\n";
    }
}