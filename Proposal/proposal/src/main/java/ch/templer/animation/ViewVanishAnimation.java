package ch.templer.animation;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * Created by Templer on 5/13/2016.
 */
public class ViewVanishAnimation {
    public static void runAnimation(View view, int animationDuration) {
        Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(animationDuration);
        view.startAnimation(fadeOut);
    }
}
