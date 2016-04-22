package ch.templer.animation;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.view.View;

import ch.templer.utils.Colors;

/**
 * Created by Templer on 4/14/2016.
 */
public class ColorTransitionAnimation {
    View view;
    private int[] colors;
    private int colorCounter = 0;
    private int duration;

    public ColorTransitionAnimation(View view, int[] colors, int duration){
        this.view = view;
        this.colors = colors;
        this.duration = duration;
    }

    public void runAnimation() {

        ObjectAnimator animator;
        if (colorCounter +1 < colors.length){
            animator = ObjectAnimator.ofInt(view, "backgroundColor", colors[colorCounter], colors[colorCounter + 1]).setDuration(duration);
            colorCounter++;
        }else{
            animator = ObjectAnimator.ofInt(view, "backgroundColor", colors[colorCounter], colors[0]).setDuration(duration);
            colorCounter = 0;

        }
        animator.setEvaluator(new ArgbEvaluator());
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                runAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();

    }
}
