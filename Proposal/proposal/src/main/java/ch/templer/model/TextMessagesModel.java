package ch.templer.model;

/**
 * Created by Templer on 04.04.2016.
 */
public class TextMessagesModel extends Message {
    private String[] messages;
    private int showTime;

    public String[] getMessages() {
        return messages;
    }

    public void setMessages(String[] messages) {
        this.messages = messages;
    }

    public int getShowTime() {
        return showTime;
    }

    public void setShowTime(int showTime) {
        this.showTime = showTime;
    }
}
