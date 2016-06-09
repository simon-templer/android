package ch.templer.controls.fab.fabtoolbarlayout;

/**
 * Created by Templer on 6/9/2016.
 */

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar.SnackbarLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import ch.templer.controls.fab.fabtoolbarlayout.FABToolbarLayout;

public class FABToolbarLayoutBehavior extends CoordinatorLayout.Behavior<FABToolbarLayout> {

    public FABToolbarLayoutBehavior(Context context, AttributeSet attrs) {
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FABToolbarLayout child, View dependency) {
        return dependency instanceof SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FABToolbarLayout child, View dependency) {
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }
    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, FABToolbarLayout child, View dependency) {
        ViewCompat.animate(child).translationY(0.0F).scaleX(1.0F).scaleY(1.0F).alpha(1.0F).setInterpolator(new AccelerateDecelerateInterpolator()).start();
    }
}