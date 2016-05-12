package ch.templer.animation;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

/**
 * Created by Templer on 5/6/2016.
 */
public class BlinkAnimation {
    public static void runAnimation(View view, int blinkDuration, int blinkAmount) {
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(blinkDuration);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(blinkAmount);
        animation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(animation);
    }
}
