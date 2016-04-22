package ch.templer.animation;

import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Templer on 4/14/2016.
 */
public class TextFadeInOutAnimation {

    private List<String> textMessages;
    private TextView textView;
    private Handler handler;
    private Animation fadeInAnimation;
    private Animation fadeOutAnimation;
    private int fadeCount;
    private int textViewShowTime;
    private int animationDuration;
    private AnimationFinishedListener animationFinishedListener;

    public TextFadeInOutAnimation(List<String> textMessages, TextView textView, int textViewShowTime, int animationDuration){
        this.textMessages = textMessages;
        this.textView = textView;
        this.textViewShowTime = textViewShowTime;
        this.animationDuration = animationDuration;
    }

    public void runAnimation() {
        fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimation.setDuration(animationDuration);

        fadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);
        fadeOutAnimation.setDuration(animationDuration);
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                fadeCount++;
                if (fadeCount >= textMessages.size()) {
                    textView.setText("");
                    if (animationFinishedListener != null){
                        animationFinishedListener.onAnimationFinished();
                    }
                    return;
                }
                textView.setText(textMessages.get(fadeCount));
                textView.startAnimation(fadeInAnimation);
                handler.postDelayed(mFadeOut, textViewShowTime);

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }
        });

        fadeCount = 0;

        handler = new Handler();

        textView.setText(textMessages.get(fadeCount));

        textView.startAnimation(fadeInAnimation);

        handler.postDelayed(mFadeOut, textViewShowTime);

    }

    private Runnable mFadeOut = new Runnable() {

        @Override
        public void run() {
            textView.startAnimation(fadeOutAnimation);
        }
    };

    public void setAnimationFinishedListener(AnimationFinishedListener animationFinishedListener) {
        this.animationFinishedListener = animationFinishedListener;
    }
}
