package ch.templer.controls.alertdialog;

import android.view.animation.Interpolator;

/**
 * Created by Templer on 6/14/2016.
 */
class HesitateInterpolator implements Interpolator {

    private static double POW = 1.0/2.0;

    @Override
    public float getInterpolation(float input) {
        return input < 0.5
                ? (float) Math.pow(input * 2, POW) * 0.5f
                : (float) Math.pow((1 - input) * 2, POW) * -0.5f + 1;
    }
}
