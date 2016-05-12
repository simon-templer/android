package ch.templer.model;

/**
 * Created by Templer on 04.04.2016.
 */
public class PictureSlideshowModel extends AbstractMessageModel {

    public PictureSlideshowModel(){

    }

    private int[] imageIDs;

    public int[] getImageIDs() {
        return imageIDs;
    }

    public void setImageIDs(int[] imageIDs) {
        this.imageIDs = imageIDs;
    }
}
