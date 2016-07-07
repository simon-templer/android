package ch.templer.model;

import android.content.res.Resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.android.gms.maps.MapFragment;

import java.io.Serializable;

import ch.templer.fragments.service.ResourceQueryService;
import ch.templer.fragments.videofragment.VideoFragment;

/**
 * Created by Templer on 04.04.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = VideoFragmentModel.class, name = "VideoFragmentModel"),
        @JsonSubTypes.Type(value = FragmentColors.class, name = "FragmentColors"),
        @JsonSubTypes.Type(value = MapLocationModel.class, name = "MapLocationModel"),
        @JsonSubTypes.Type(value = PictureSlideshowModel.class, name = "PictureSlideshowModel"),
        @JsonSubTypes.Type(value = QuizModel.class, name = "QuizModel"),
        @JsonSubTypes.Type(value = SelfieModel.class, name = "SelfieModel"),
        @JsonSubTypes.Type(value = TextMessagesModel.class, name = "TextMessagesModel"),
        @JsonSubTypes.Type(value = ColorTheme.class, name = "ColorTheme")}
)
public abstract class AbstractMessageModel implements Serializable {

    @JsonProperty("FragmentColors")
    private FragmentColors fragmentColors;
    private Integer backgroundMusicID;

    public FragmentColors getFragmentColors() {
        return fragmentColors;
    }

    public void setFragmentColors(FragmentColors fragmentColors) {
        this.fragmentColors = fragmentColors;
    }

    public Integer getBackgroundMusicID() {
        return backgroundMusicID;
    }

    public void setBackgroundMusicID(String backgroundMusicID) {
        try {
            this.backgroundMusicID = ResourceQueryService.getRawByName(backgroundMusicID);
        } catch (Resources.NotFoundException e) {
            this.backgroundMusicID = null;
        }
    }
}
