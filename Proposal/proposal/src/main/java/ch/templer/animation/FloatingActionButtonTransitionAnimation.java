package ch.templer.animation;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;

import ch.templer.controls.reveallayout.RevealLayout;

/**
 * Created by Templer on 23.04.2016.
 */
public class FloatingActionButtonTransitionAnimation implements View.OnClickListener {

    private FloatingActionButton floatingActionButton;
    private View revealView;
    private RevealLayout revealLayout;
    private FragmentTransaction transaction;

    public FloatingActionButtonTransitionAnimation(FloatingActionButton floatingActionButton, View revealView, RevealLayout revealLayout, FragmentTransaction transaction) {
        this.floatingActionButton = floatingActionButton;
        this.revealLayout = revealLayout;
        this.revealView = revealView;
        this.transaction = transaction;
    }
    public static void sendViewToBack(final View child) {
        final ViewGroup parent = (ViewGroup)child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }

    @Override
    public void onClick(View v) {
        floatingActionButton.setClickable(false);
        sendViewToBack(floatingActionButton);
        //floatingActionButton.setVisibility(View.INVISIBLE);
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
}
