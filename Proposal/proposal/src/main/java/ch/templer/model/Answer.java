package ch.templer.model;

import java.io.Serializable;

/**
 * Created by Templer on 5/6/2016.
 */
public class Answer implements Serializable{
    String answer;
    boolean CorrectAnswer;

    public Answer(){

    }

    public Answer(String answer, boolean isCorrectAnswer){
        this.answer = answer;
        this.CorrectAnswer = isCorrectAnswer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean getCorrectAnswer() {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(boolean isCorrectAnswer) {
        this.CorrectAnswer = isCorrectAnswer;
    }
}
