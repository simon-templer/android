package ch.templer.model;

import java.util.List;

/**
 * Created by Templer on 4/8/2016.
 */
public class MultipleChoiceModel extends AbstractMessageModel {

    private List<QuestionAndAnswers> QAndAs;
    private String title;

    public List<QuestionAndAnswers> getQAndAs() {
        return QAndAs;
    }

    public void setQAndA(List<QuestionAndAnswers> QAndAs) {
        this.QAndAs = QAndAs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
