package ch.templer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Templer on 04.04.2016.
 */
public class TextMessagesModel extends Message {
    private ArrayList<String> messages;
    private int showTime;

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }

    public int getShowTime() {
        return showTime;
    }

    public void setShowTime(int showTime) {
        this.showTime = showTime;
    }
}
