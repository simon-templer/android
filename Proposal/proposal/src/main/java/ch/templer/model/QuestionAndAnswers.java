package ch.templer.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Templer on 4/8/2016.
 */
public class QuestionAndAnswers implements Serializable{
    private ArrayList<Answer> answers;
    private String Question;
    private String correctMessage;
    private String wrongMessage;

    public QuestionAndAnswers(){}

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getCorrectMessage() {
        return correctMessage;
    }

    public void setCorrectMessage(String correctMessage) {
        this.correctMessage = correctMessage;
    }

    public String getWrongMessage() {
        return wrongMessage;
    }

    public void setWrongMessage(String wrongMessage) {
        this.wrongMessage = wrongMessage;
    }
}
