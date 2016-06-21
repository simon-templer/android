package ch.templer.model;

/**
 * Created by Templer on 6/16/2016.
 */
public class VideoFragmentModel extends AbstractMessageModel {

    private int videoResourceID;
    private int fabAppearAnimationTime;

    public int getVideoResourceID() {
        return videoResourceID;
    }

    public void setVideoResourceID(int videoResourceID) {
        this.videoResourceID = videoResourceID;
    }

    public void setFabAppearAnimationTime(int fabAppearAnimationTime) {
        this.fabAppearAnimationTime = fabAppearAnimationTime;
    }

    public int getFabAppearAnimationTime() {
        return fabAppearAnimationTime;
    }
}
