package ch.templer.animation;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import ch.templer.controls.listener.AnimationStartListener;
import ch.templer.controls.reveallayout.RevealLayout;

/**
 * Created by Templer on 23.04.2016.
 */
public class FloatingActionButtonTransitionAnimation implements View.OnClickListener {

    private View floatingActionButton;
    private View revealView;
    private RevealLayout revealLayout;
    private FragmentTransaction transaction;
    private Snackbar snackbar;

    private AnimationStartListener animationStartListener;

    public FloatingActionButtonTransitionAnimation(View floatingActionButton, View revealView, RevealLayout revealLayout, FragmentTransaction transaction) {
        this(floatingActionButton,revealView,revealLayout,null, transaction);
    }

    public FloatingActionButtonTransitionAnimation(View floatingActionButton, View revealView, RevealLayout revealLayout,Snackbar snackbar, FragmentTransaction transaction) {
        this.floatingActionButton = floatingActionButton;
        this.revealLayout = revealLayout;
        this.revealView = revealView;
        this.snackbar = snackbar;
        this.transaction = transaction;
    }

    public static void sendViewToBack(final View child) {
        final ViewGroup parent = (ViewGroup) child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }

    public void runAnimation(){
        NextViewAnimation();
    }

    private void NextViewAnimation(){
        if (animationStartListener != null){
            animationStartListener.onAnimationStarted();
        }

        Animation fadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);
        fadeOutAnimation.setDuration(500);
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                floatingActionButton.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        floatingActionButton.startAnimation(fadeOutAnimation);

        floatingActionButton.setClickable(false);
        int[] location = new int[2];
        floatingActionButton.getLocationOnScreen(location);
        location[0] += floatingActionButton.getWidth() / 2;
        location[1] += floatingActionButton.getHeight() / 2;
        revealView.setVisibility(View.VISIBLE);
        revealLayout.setVisibility(View.VISIBLE);


        revealLayout.show(location[0], location[1]);
        floatingActionButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Navigate to new Fragment
                transaction.commit();
            }
        }, 1000);
        floatingActionButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                floatingActionButton.setClickable(true);
                revealLayout.setVisibility(View.INVISIBLE);
                revealLayout.setVisibility(View.INVISIBLE);
            }
        }, 1360);
    }

    @Override
    public void onClick(View v) {
        if (snackbar!= null){
            snackbar.dismiss();
        }
        NextViewAnimation();
    }

    public void setAnimationStartListener(AnimationStartListener animationStartListener) {
        this.animationStartListener = animationStartListener;
    }
}
