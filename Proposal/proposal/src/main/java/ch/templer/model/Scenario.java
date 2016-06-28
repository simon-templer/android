package ch.templer.model;

import android.content.res.Resources;

import java.io.Serializable;
import java.util.List;

import ch.templer.activities.R;
import ch.templer.fragments.service.ResourceQueryService;

/**
 * Created by Templer on 6/22/2016.
 */
public class Scenario implements Serializable {
    private long id;
    private String scenarioTitle;
    private String secondaryTitle;
    private String description;
    private int iconId = R.drawable.unknown_icon;
    private List<AbstractMessageModel> messages;

    public Scenario() {

    }

    public Scenario(List<AbstractMessageModel> messages) {
        this.messages = messages;
    }

    public List<AbstractMessageModel> getMessages() {
        return messages;
    }

    public void setMessages(List<AbstractMessageModel> messages) {
        this.messages = messages;
    }

    public String getScenarioTitle() {
        return scenarioTitle;
    }

    public void setScenarioTitle(String scenarioTitle) {
        this.scenarioTitle = scenarioTitle;
    }

    public String getSecondaryTitle() {
        return secondaryTitle;
    }

    public void setSecondaryTitle(String secondaryTitle) {
        this.secondaryTitle = secondaryTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIconId() {
        return this.iconId;
    }

    public void setIconId(String iconId) {
        try {
            this.iconId = ResourceQueryService.getDrawableByName(iconId);
        } catch (Resources.NotFoundException e) {
            this.iconId = R.drawable.unknown_icon;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
