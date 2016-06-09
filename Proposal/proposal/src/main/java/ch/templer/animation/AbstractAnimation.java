package ch.templer.animation;

import android.view.animation.Animation;

import ch.templer.controls.listener.AnimationFinishedListener;

/**
 * Created by Templer on 5/22/2016.
 */
public abstract class AbstractAnimation extends Animation {
    protected AnimationFinishedListener animationFinishedListener;

    public void setAnimationFinishedListener(AnimationFinishedListener animationFinishedListener) {
        this.animationFinishedListener = animationFinishedListener;
    }
}