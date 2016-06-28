package ch.templer.model;

import android.content.res.Resources;

import ch.templer.activities.R;
import ch.templer.fragments.service.ResourceQueryService;

/**
 * Created by Templer on 04.04.2016.
 */
public class PictureSlideshowModel extends AbstractMessageModel {

    public PictureSlideshowModel() {

    }

    private int[] imageIDs;

    public int[] getImageIDs() {
        return imageIDs;
    }

    public void setImageIDs(String[] imageIDs) {

        int[] convertedImageIds = new int[imageIDs.length];
        for (int i = 0; i < imageIDs.length; i++) {
            try {
                convertedImageIds[i] = ResourceQueryService.getDrawableByName(imageIDs[i]);
            } catch (Resources.NotFoundException e) {
                convertedImageIds[i] = R.drawable.unknown_icon;
            }
        }
        this.imageIDs = convertedImageIds;
    }
}
