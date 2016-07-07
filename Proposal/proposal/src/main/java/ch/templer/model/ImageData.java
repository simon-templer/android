package ch.templer.model;

import java.io.Serializable;

import ch.templer.fragments.service.ResourceQueryService;

/**
 * Created by Templer on 6/30/2016.
 */
public class ImageData implements Serializable{
    private int imageID;
    private String description;

    public int getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = ResourceQueryService.getDrawableByName(imageID);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
