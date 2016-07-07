package ch.templer.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import ch.templer.animation.FloatingActionButtonTransitionAnimation;
import ch.templer.controls.reveallayout.RevealLayout;
import ch.templer.fragments.service.FragmentTransactionProcessingService;
import ch.templer.services.multimedia.SoundService;

/**
 * Created by Templer on 22.04.2016.
 */
public abstract class AbstractFragment extends Fragment {
    private static String FRAGMENT_FINISHED_ID = "FRAGMENT_FINISHED_ID";
    private static String FRAGMENT_FIRST_STARTED_ID = "FRAGMENT_FIRST_STARTED_ID";

    private boolean fragmentFinished = false;
    private boolean fragmentFirstStarted = false;
    private SoundService soundService;


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
        soundService = new SoundService(getContext());
        if (savedInstanceState != null) {
            fragmentFinished = savedInstanceState.getBoolean(FRAGMENT_FINISHED_ID);
            fragmentFirstStarted = savedInstanceState.getBoolean(FRAGMENT_FIRST_STARTED_ID);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        soundService.stop();
    }

    @Override
    public void onPause() {
        super.onPause();
        soundService.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!soundService.isPlaying()) {
            soundService.resume();
        }
    }

    public boolean isFragmentFinished() {
        return fragmentFinished;
    }

    public boolean isFragmentFirstStarted() {
        return fragmentFirstStarted;
    }

    public SoundService getSoundService(){
        return soundService;
    }
}
