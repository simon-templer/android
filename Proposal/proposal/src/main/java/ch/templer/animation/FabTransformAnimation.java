package ch.templer.animation;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

/**
 * Created by Templer on 5/25/2016.
 */
public class FabTransformAnimation extends AbstractAnimation {

    FloatingActionButton floatingActionButton;
    private ColorStateList colorStateList;
    private Drawable newFabDrawable;

    public FabTransformAnimation(FloatingActionButton floatingActionButton, ColorStateList colorStateList, Drawable newFabDrawable) {
        this.floatingActionButton = floatingActionButton;
        this.colorStateList = colorStateList;
        this.newFabDrawable = newFabDrawable;
    }

    public void runAnimation() {
        floatingActionButton.clearAnimation();
        floatingActionButton.setClickable(false);
        // Scale down animation
        ScaleAnimation shrink = new ScaleAnimation(1f, 0.2f, 1f, 0.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shrink.setDuration(150);     // animation duration in milliseconds
        shrink.setInterpolator(new DecelerateInterpolator());
        shrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Change FAB color and icon
                floatingActionButton.setBackgroundTintList(colorStateList);
                floatingActionButton.setImageDrawable(newFabDrawable);

                // Scale up animation
                ScaleAnimation expand = new ScaleAnimation(0.2f, 1f, 0.2f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                expand.setDuration(100);     // animation duration in milliseconds
                expand.setInterpolator(new AccelerateInterpolator());
                floatingActionButton.startAnimation(expand);
                floatingActionButton.setClickable(true);
                if (animationFinishedListener != null) {
                    animationFinishedListener.onAnimationFinished();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        floatingActionButton.startAnimation(shrink);
    }
}

