package ch.templer.animation;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.lang.reflect.Field;

import ch.templer.activities.R;
import ch.templer.fragments.service.ResourceQueryService;

/**
 * Created by Templer on 4/14/2016.
 */
public class ColorTransitionAnimation {
    private View view;
    private int[] colors;
    private int colorCounter = 0;
    private int duration;
    private Context context;

    public ColorTransitionAnimation(Context context, View view, int[] colors, int duration) {
        this(context, view, colors, duration, 0);
    }

    public ColorTransitionAnimation(Context context, View view, int[] colors, int duration, int colorStartPosition) {
        this.view = view;
        this.context = context;
        this.colors = colors;
        this.duration = duration;
        if (colorStartPosition >= 0 && colorStartPosition < colors.length) {
            this.colorCounter = colorStartPosition;
        }
    }

    public int getColorByName(String name) {
        int colorId = 0;

        try {
            Class res = R.color.class;
            Field field = res.getField(name);
            colorId = field.getInt(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return colorId;
    }

    public int getCurrentPosition() {
        return colorCounter;
    }

    public void runAnimation() {

        ObjectAnimator animator;
        if (colorCounter + 1 < colors.length) {
            animator = ObjectAnimator.ofInt(view, "backgroundColor", colors[colorCounter], colors[colorCounter + 1]).setDuration(duration);
            colorCounter++;
        } else {
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
