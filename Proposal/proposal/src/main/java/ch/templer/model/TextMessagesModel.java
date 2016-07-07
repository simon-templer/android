package ch.templer.model;

import android.content.res.Resources;
import android.net.Uri;

import java.util.ArrayList;

import ch.templer.activities.R;
import ch.templer.fragments.service.ResourceQueryService;

/**
 * Created by Templer on 04.04.2016.
 */
public class TextMessagesModel extends AbstractMessageModel {
    private ArrayList<String> messages;
    private int textViewShowTime;
    private int backgrountColorTransitionTime;
    private int textAnimationDuration;
    private int[] backgroundAnimationColors;

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }

    public int getTextViewShowTime() {
        return textViewShowTime;
    }

    public void setTextViewShowTime(int textViewShowTime) {
        this.textViewShowTime = textViewShowTime;
    }

    public int getBackgrountColorTransitionTime() {
        return backgrountColorTransitionTime;
    }

    public void setBackgrountColorTransitionTime(int backgrountColorTransitionTime) {
        this.backgrountColorTransitionTime = backgrountColorTransitionTime;
    }

    public int getTextAnimationDuration() {
        return textAnimationDuration;
    }

    public void setTextAnimationDuration(int textAnimationDuration) {
        this.textAnimationDuration = textAnimationDuration;
    }

    public int[] getBackgroundAnimationColors() {
        return backgroundAnimationColors;
    }

    public void setBackgroundAnimationColors(String[] backgroundAnimationColors) {
        int[] convertedColors = new int[backgroundAnimationColors.length];
        for (int i = 0; i < backgroundAnimationColors.length; i++) {
            try {
                convertedColors[i] = ResourceQueryService.getColorByName(backgroundAnimationColors[i]);
            } catch (Resources.NotFoundException e) {
                convertedColors[i] = R.color.default_fallback_color;
            }
        }
        this.backgroundAnimationColors = convertedColors;
    }
}
