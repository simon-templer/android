package ch.templer.model;

import java.util.List;

/**
 * Created by Templer on 4/8/2016.
 */
public class QuizModel extends AbstractMessageModel {

    private List<QuestionAndAnswers> QAndAs;
    private String title;
    private int fabAppearAnimationTime;

    public List<QuestionAndAnswers> getQAndAs() {
        return QAndAs;
    }

    public void setQAndAs(List<QuestionAndAnswers> QAndAs) {
        this.QAndAs = QAndAs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFabAppearAnimationTime() {
        return fabAppearAnimationTime;
    }

    public void setFabAppearAnimationTime(int fabAppearAnimationTime) {
        this.fabAppearAnimationTime = fabAppearAnimationTime;
    }
}
