package ch.templer.animation;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * Created by Templer on 23.04.2016.
 */
public class ViewAppearAnimation {
    public static void runAnimation(View view, int animationDuration) {
        Animation fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimation.setDuration(animationDuration);
        view.setVisibility(View.VISIBLE);
        view.startAnimation(fadeInAnimation);
    }
}
