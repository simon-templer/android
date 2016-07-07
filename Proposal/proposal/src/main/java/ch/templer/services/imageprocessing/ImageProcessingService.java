package ch.templer.services.imageprocessing;

import android.content.Context;
import android.widget.ImageView;

import ch.templer.controls.photoview.PhotoView;

/**
 * Created by Templer on 6/30/2016.
 */
public class ImageProcessingService {
    public static void loadBitmap(int resId, ImageView imageView,Context context) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView, context);
        task.execute(resId);
    }
}
