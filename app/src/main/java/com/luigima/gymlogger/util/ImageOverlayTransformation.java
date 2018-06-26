package com.luigima.gymlogger.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


public class ImageOverlayTransformation implements Transformation {
    private List<String> imageList;
    private Context context;

    public ImageOverlayTransformation(Context context) {
        this.context = context;
        imageList = new ArrayList<>();
    }

    public void addImage(String imagePath) {
        imageList.add(imagePath);
    }

    @Override
    public Bitmap transform(Bitmap source) {
        if (imageList.size() == 0) {
            return source;
        }

        Bitmap mutableBitmap = source.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);

        Bitmap image = null;
        for (String imagePath : imageList) {
            try {
                image = getBitmapFromAsset(context, imagePath);

                // image should be semi transparent
                Integer transparency = 100 / imageList.size();
                if (transparency < 20) {
                    transparency = 40;
                }
                Paint paint = new Paint();
                paint.setAlpha(transparency);
                canvas.drawBitmap(image, 0, 0, paint);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (image != null) image.recycle();
            }
        }

        source.recycle();
        return mutableBitmap;
    }

    private Bitmap getBitmapFromAsset(Context context, String filePath) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream istr = assetManager.open(filePath);

        return BitmapFactory.decodeStream(istr);
    }

    @Override
    public String key() {
        return "SimpleImageDecoratorTransformation";
    }
}
