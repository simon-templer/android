package ch.templer.animation;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import ch.templer.controls.listener.AnimationFinishedListener;

/**
 * Created by Templer on 5/6/2016.
 */
public class BlinkAnimation extends AbstractAnimation {
    private View view;
    private int blinkDuration;
    private int blinkAmount;

    public BlinkAnimation(View view, int blinkDuration, int blinkAmount){
        this.view = view;
        this.blinkAmount = blinkAmount;
        this.blinkDuration=blinkDuration;
    }

    public void runAnimation() {
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (animationFinishedListener != null){
                    animationFinishedListener.onAnimationFinished();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setDuration(blinkDuration);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(blinkAmount);
        animation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(animation);
    }
}
