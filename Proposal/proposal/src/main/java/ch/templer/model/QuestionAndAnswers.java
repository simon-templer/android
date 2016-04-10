package ch.templer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Templer on 4/8/2016.
 */
public class QuestionAndAnswers {
    private ArrayList<String> answers;
    private String Question;

    private QuestionAndAnswers(){}

    public static Builder build(){
        return new Builder();
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public static class Builder {
        QuestionAndAnswers questionAndAnswers;

        public Builder(){
            questionAndAnswers = new QuestionAndAnswers();
        }

        public Builder setQuestion(String question){
            questionAndAnswers.setQuestion(question);
            return this;
        }

        public Builder setAnswers(ArrayList<String> answers){
            questionAndAnswers.setAnswers(answers);
            return this;
        }

        public QuestionAndAnswers build() {
            return questionAndAnswers;
        }
    }
}
