package ch.templer.model;

/**
 * Created by Templer on 4/8/2016.
 */
public class MultipleChoiceModel extends MessageData {

    QuestionAndAnswers QAndA;

    public QuestionAndAnswers getQAndA() {
        return QAndA;
    }

    public void setQAndA(QuestionAndAnswers QAndA) {
        this.QAndA = QAndA;
    }
}
