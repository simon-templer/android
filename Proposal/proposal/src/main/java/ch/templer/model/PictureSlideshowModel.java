package ch.templer.model;

import android.content.res.Resources;

import java.util.List;

import ch.templer.activities.R;
import ch.templer.fragments.service.ResourceQueryService;

/**
 * Created by Templer on 04.04.2016.
 */
public class PictureSlideshowModel extends AbstractMessageModel {

    public PictureSlideshowModel() {

    }

    private List<ImageData> images;

    public List<ImageData> getImages() {
        return images;
    }

    public void setImages(List<ImageData> images) {
        this.images = images;
    }
}
