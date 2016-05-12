package ch.templer.model;

import java.io.Serializable;

/**
 * Created by Templer on 5/6/2016.
 */
public class Answer implements Serializable{
    String answer;
    boolean isCorrectAnswer;

    public Answer(String answer, boolean isCorrectAnswer){
        this.answer = answer;
        this.isCorrectAnswer = isCorrectAnswer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrectAnswer() {
        return isCorrectAnswer;
    }

    public void setIsCorrectAnswer(boolean isCorrectAnswer) {
        this.isCorrectAnswer = isCorrectAnswer;
    }
}
