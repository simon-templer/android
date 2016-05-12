package ch.templer.controls.answerbutton;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Templer on 5/6/2016.
 */
public class AnswerButton extends Button {
    private boolean isCorrectAnswer;

    public AnswerButton(Context context)
    {
        super(context);
    }
    public AnswerButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public AnswerButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setIsCorrectAnswer(boolean isCorrectAnswer) {
        this.isCorrectAnswer = isCorrectAnswer;
    }

    public boolean isCorrectAnswer() {
        return isCorrectAnswer;
    }
}
