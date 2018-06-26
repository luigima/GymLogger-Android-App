package com.luigima.gymlogger.util;

import android.content.Context;

import com.luigima.gymlogger.data.db.entities.ExerciseHasMuscle;

import java.util.List;

import timber.log.Timber;

public class ImageOverlayTransformationBuilder {
    private List<ExerciseHasMuscle> categoryList;
    private Context context;


    public ImageOverlayTransformationBuilder(Context context, List<ExerciseHasMuscle> categoryList) {
        this.categoryList = categoryList;
        this.context = context;
    }

    public ImageOverlayTransformation getTransformation() {
        ImageOverlayTransformation imageOverlayTransformation =
                new ImageOverlayTransformation(context);

        if(categoryList != null) {
            for (ExerciseHasMuscle category : categoryList) {
                imageOverlayTransformation.addImage("images/body/example/" + category.getTitle().replace(" ", "_") + ".png");
                Timber.d("Decorating with " + category.getTitle());
            }
        }

        return imageOverlayTransformation;
    }
}
