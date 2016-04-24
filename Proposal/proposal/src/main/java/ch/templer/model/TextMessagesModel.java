package ch.templer.model;

import java.util.ArrayList;

import ch.templer.utils.Colors;

/**
 * Created by Templer on 04.04.2016.
 */
public class TextMessagesModel extends MessageData {
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

    public void setBackgrountColorTransitionTime(int backgrountColorTransitionTime) {
        this.backgrountColorTransitionTime = backgrountColorTransitionTime;
    }

    public int getBackgrountColorTransitionTime() {
        return backgrountColorTransitionTime;
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

    public void setBackgroundAnimationColors(int[] backgroundAnimationColors) {
        this.backgroundAnimationColors = backgroundAnimationColors;
    }
}
