package ch.templer.model;

import android.content.res.Resources;

import ch.templer.fragments.service.ResourceQueryService;

/**
 * Created by Templer on 6/16/2016.
 */
public class VideoFragmentModel extends AbstractMessageModel {

    private Integer videoResourceID;
    private int fabAppearAnimationTime;

    public Integer getVideoResourceID() {
        return videoResourceID;
    }

    public void setVideoResourceID(String videoResourceID) {
        try {
            this.videoResourceID = ResourceQueryService.getRawByName(videoResourceID);
        } catch (Resources.NotFoundException e) {
            this.videoResourceID = null;
        }
    }

    public void setFabAppearAnimationTime(int fabAppearAnimationTime) {
        this.fabAppearAnimationTime = fabAppearAnimationTime;
    }

    public int getFabAppearAnimationTime() {
        return fabAppearAnimationTime;
    }
}
