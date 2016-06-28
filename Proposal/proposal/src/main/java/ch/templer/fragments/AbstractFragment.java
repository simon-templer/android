package ch.templer.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import ch.templer.animation.FloatingActionButtonTransitionAnimation;
import ch.templer.controls.reveallayout.RevealLayout;
import ch.templer.fragments.service.FragmentTransactionProcessingService;

/**
 * Created by Templer on 22.04.2016.
 */
public abstract class AbstractFragment extends Fragment {
    private static String FRAGMENT_FINISHED_ID = "FRAGMENT_FINISHED_ID";
    private static String FRAGMENT_FIRST_STARTED_ID = "FRAGMENT_FIRST_STARTED_ID";

    private FloatingActionButton navigationFab;
    protected boolean fragmentFinished = false;
    private boolean fragmentFirstStarted = false;


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FRAGMENT_FINISHED_ID, fragmentFinished);
        outState.putBoolean(FRAGMENT_FIRST_STARTED_ID, true);
    }

    protected void fragmentFinished(final FloatingActionButton navigationFab, final View mRevealView, final RevealLayout mRevealLayout) {
        fragmentFinished(navigationFab, mRevealView, mRevealLayout, null);
    }

    protected void fragmentFinished(final FloatingActionButton navigationFab, final View mRevealView, final RevealLayout mRevealLayout, TransactionCallback listener) {
        navigationFab.setOnClickListener(new CustomOnClickListener(navigationFab, mRevealView, mRevealLayout, listener));
        fragmentFinished = true;
    }

    protected interface TransactionCallback {
        void onClick();
    }

    private class CustomOnClickListener implements View.OnClickListener {

        private FloatingActionButton navigationFab;
        private View mRevealView;
        private RevealLayout mRevealLayout;
        private TransactionCallback callback;

        public CustomOnClickListener(final FloatingActionButton navigationFab, final View mRevealView, final RevealLayout mRevealLayout) {
            this(navigationFab, mRevealView, mRevealLayout, null);
        }

        public CustomOnClickListener(final FloatingActionButton navigationFab, final View mRevealView, final RevealLayout mRevealLayout, TransactionCallback callback) {
            this.navigationFab = navigationFab;
            this.mRevealView = mRevealView;
            this.mRevealLayout = mRevealLayout;
            this.callback = callback;
        }

        @Override
        public void onClick(View v) {
            FragmentTransaction transaction = FragmentTransactionProcessingService.prepareNextFragmentTransaction(getFragmentManager().beginTransaction(), getContext());
            FragmentTransactionProcessingService.transactionStarted();
            FloatingActionButtonTransitionAnimation floatingActionButtonAnimationOnClickListener = new FloatingActionButtonTransitionAnimation(navigationFab, mRevealView, mRevealLayout, transaction);
            floatingActionButtonAnimationOnClickListener.runAnimation();
            mRevealLayout.bringToFront();
            if (callback != null) {
                callback.onClick();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            fragmentFinished = savedInstanceState.getBoolean(FRAGMENT_FINISHED_ID);
            fragmentFirstStarted = savedInstanceState.getBoolean(FRAGMENT_FIRST_STARTED_ID);
        }
    }

    public boolean isFragmentFinished() {
        return fragmentFinished;
    }

    public boolean isFragmentFirstStarted() {
        return fragmentFirstStarted;
    }
}
