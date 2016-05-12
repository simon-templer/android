package ch.templer.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import ch.templer.activities.R;
import ch.templer.animation.ColorTransitionAnimation;
import ch.templer.animation.FloatingActionButtonTransitionAnimation;
import ch.templer.animation.TextFadeInOutAnimation;
import ch.templer.animation.ViewAppearAnimation;
import ch.templer.controls.listener.AnimationFinishedListener;
import ch.templer.controls.reveallayout.RevealLayout;
import ch.templer.fragments.service.FragmentTransactionProcessingService;
import ch.templer.model.TextMessagesModel;
import ch.templer.services.multimedia.SoundService;


public class TextMessagesFragment extends AbstractFragment implements AnimationFinishedListener {
    private static final String TEXT_MESSAGES_MODEL_ID = "textMessageID";

    private FrameLayout frameLayout;
    private TextView content;
    private ColorTransitionAnimation colorTransitionAnimation;
    private TextFadeInOutAnimation textFadeInOutAnimation;

    private TextMessagesModel textMessagesModel;

    private FloatingActionButton floatingActionButton;
    private View mRevealView;
    private RevealLayout mRevealLayout;

    protected static final class MyNonConfig {
        // fill with public variables keeping references and other state info, set to null
    }

    private boolean isConfigChange;
    private MyNonConfig nonConf;


    private OnFragmentInteractionListener mListener;

    public TextMessagesFragment() {
        // Required empty public constructor
    }

    public static TextMessagesFragment newInstance(TextMessagesModel textMessagesModel) {
        TextMessagesFragment fragment = new TextMessagesFragment();
        Bundle args = new Bundle();
        args.putSerializable(TEXT_MESSAGES_MODEL_ID, textMessagesModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            textMessagesModel = (TextMessagesModel) getArguments().getSerializable(TEXT_MESSAGES_MODEL_ID);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("test", textFadeInOutAnimation.getFadeCount());
        outState.putInt("songPosition", SoundService.getSongPosition());
        SoundService.stop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text_messages, container, false);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingButton);
        floatingActionButton.setVisibility(View.INVISIBLE);
        mRevealLayout = (RevealLayout) view.findViewById(R.id.reveal_layout);
        mRevealView = view.findViewById(R.id.reveal_view);
        mRevealView.setBackgroundColor(textMessagesModel.getNextFragmentBackroundColor());

        frameLayout = (FrameLayout) view.findViewById(R.id.TextMessageFragment_FrameLayout);
        frameLayout.setBackgroundColor(textMessagesModel.getBackgroundColor());
        content = (TextView) view.findViewById(R.id.text_messages_fragment_content_textview);

        colorTransitionAnimation = new ColorTransitionAnimation(frameLayout, textMessagesModel.getBackgroundAnimationColors(), textMessagesModel.getBackgroundColorTransitionTime());
        int testcounter = 0;
        int songPosition = 0;
        if (savedInstanceState != null) {
            testcounter = savedInstanceState.getInt("test");
            songPosition = savedInstanceState.getInt("songPosition");
        }
        textFadeInOutAnimation = new TextFadeInOutAnimation(textMessagesModel.getMessages(), content, textMessagesModel.getTextViewShowTime(), textMessagesModel.getTextAnimationDuration(), testcounter);
        textFadeInOutAnimation.setAnimationFinishedListener(this);

        FragmentTransaction transaction = FragmentTransactionProcessingService.prepareNextFragmentTransaction(getFragmentManager().beginTransaction());

        FloatingActionButtonTransitionAnimation floatingActionButtonAnimationOnClickListener = new FloatingActionButtonTransitionAnimation(floatingActionButton, mRevealView, mRevealLayout, transaction);
        floatingActionButton.setOnClickListener(floatingActionButtonAnimationOnClickListener);
        if (!SoundService.isPlaying()) {
            SoundService.playSound(this.getContext(), textMessagesModel.getBackgroundMusicID(),songPosition);
        }
        return (view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SoundService.stop();
    }

    @Override
    public void onPause() {
        super.onPause();
        SoundService.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!SoundService.isPlaying()) {
            SoundService.resume();
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation anim = new Animation() {
        };
        if (enter) {
            if (nextAnim != 0) {
                anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
                anim.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        colorTransitionAnimation.runAnimation();
                        textFadeInOutAnimation.runAnimation();
                    }
                });
            } else {
                colorTransitionAnimation.runAnimation();
                textFadeInOutAnimation.runAnimation();
            }
        }
        return anim;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onAnimationFinished() {
        ViewAppearAnimation.runAnimation(floatingActionButton, textMessagesModel.getTextAnimationDuration());
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}