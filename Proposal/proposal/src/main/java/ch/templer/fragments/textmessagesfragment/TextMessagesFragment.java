package ch.templer.fragments.textmessagesfragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import ch.templer.activities.R;
import ch.templer.activities.settingsactivity.SettingsActivity;
import ch.templer.animation.ColorTransitionAnimation;
import ch.templer.animation.TextFadeInOutAnimation;
import ch.templer.animation.ViewAppearAnimation;
import ch.templer.controls.listener.AnimationFinishedListener;
import ch.templer.controls.reveallayout.RevealLayout;
import ch.templer.fragments.AbstractFragment;
import ch.templer.model.TextMessagesModel;
import ch.templer.services.SettingsService;
import ch.templer.services.multimedia.SoundService;


public class TextMessagesFragment extends AbstractFragment implements AnimationFinishedListener {
    private static final String TEXT_MESSAGES_MODEL_ID = "textMessageID";
    private static final String TEXT_MESSAGE_POSITION_ID = "TEXT_MESSAGE_POSITION_ID";
    private static final String COLOR_TRANSITION_POSITION_ID = "COLOR_TRANSITION_POSITION_ID";
    private static final String SONG_POSITION_ID = "SONG_POSITION_ID";
    private static final String TEXT_ANIMATION_FINISHED = "TEXT_ANIMATION_FINISHED";

    private FrameLayout frameLayout;
    private TextView content;
    private ColorTransitionAnimation colorTransitionAnimation;
    private TextFadeInOutAnimation textFadeInOutAnimation;

    private TextMessagesModel textMessagesModel;

    private FloatingActionButton nextFragmentFab;
    private FloatingActionButton restartTextAnimationFab;
    private View mRevealView;
    private RevealLayout mRevealLayout;
    private boolean textAnimationFinished = false;

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
        if (!textAnimationFinished) {
            outState.putInt(TEXT_MESSAGE_POSITION_ID, textFadeInOutAnimation.getFadeCount());
        }
        outState.putInt(COLOR_TRANSITION_POSITION_ID, colorTransitionAnimation.getCurrentPosition());
        outState.putInt(SONG_POSITION_ID, SoundService.getSongPosition());
        outState.putBoolean(TEXT_ANIMATION_FINISHED, textAnimationFinished);
        SoundService.stop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_text_messages, container, false);

        nextFragmentFab = (FloatingActionButton) view.findViewById(R.id.nextFragmentFab);
        restartTextAnimationFab = (FloatingActionButton) view.findViewById(R.id.restartTextAnimationFab);
        restartTextAnimationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartTextAnimationFab.setClickable(false);
                textFadeInOutAnimation.stop();
                textFadeInOutAnimation = new TextFadeInOutAnimation(textMessagesModel.getMessages(), content, textMessagesModel.getTextViewShowTime(), textMessagesModel.getTextAnimationDuration(), 0);
                textFadeInOutAnimation.runAnimation();
                textAnimationFinished = false;

                restartTextAnimationFab.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        restartTextAnimationFab.setClickable(true);
                    }
                }, textMessagesModel.getTextViewShowTime());
            }
        });

        mRevealLayout = (RevealLayout) view.findViewById(R.id.reveal_layout);
        mRevealView = view.findViewById(R.id.reveal_view);
        mRevealView.setBackgroundColor(textMessagesModel.getFragmentColors().getNextFragmentBackroundColor());

        frameLayout = (FrameLayout) view.findViewById(R.id.TextMessageFragment_FrameLayout);
        frameLayout.setBackgroundColor(textMessagesModel.getFragmentColors().getFragmentBackgroundColor());
        content = (TextView) view.findViewById(R.id.text_messages_fragment_content_textview);

        int currentTextMessagesPosition = 0;
        int currentColorTransitionPosition = 0;
        int songPosition = 0;
        if (savedInstanceState != null) {
            currentTextMessagesPosition = savedInstanceState.getInt(TEXT_MESSAGE_POSITION_ID);
            songPosition = savedInstanceState.getInt(SONG_POSITION_ID);
            currentColorTransitionPosition = savedInstanceState.getInt(COLOR_TRANSITION_POSITION_ID);
            textAnimationFinished = savedInstanceState.getBoolean(TEXT_ANIMATION_FINISHED);
        }
        if (!textAnimationFinished) {
            textFadeInOutAnimation = new TextFadeInOutAnimation(textMessagesModel.getMessages(), content, textMessagesModel.getTextViewShowTime(), textMessagesModel.getTextAnimationDuration(), currentTextMessagesPosition);
            textFadeInOutAnimation.setAnimationFinishedListener(this);
        }

        if (fragmentFinished || SettingsService.getInstance().getBooleanSetting(SettingsActivity.GENERAL_DEBUGGING_SWITCH, false)) {
            fragmentFinished(nextFragmentFab, mRevealView, mRevealLayout);
        } else {
            restartTextAnimationFab.setVisibility(View.INVISIBLE);
            nextFragmentFab.setVisibility(View.INVISIBLE);
        }

        colorTransitionAnimation = new ColorTransitionAnimation(getContext(), frameLayout, textMessagesModel.getBackgroundAnimationColors(), textMessagesModel.getBackgrountColorTransitionTime(), currentColorTransitionPosition);
        if (!SoundService.isPlaying()) {
            SoundService.playSound(this.getContext(), textMessagesModel.getBackgroundMusicID(), songPosition);
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
                        if (!textAnimationFinished) {
                            textFadeInOutAnimation.runAnimation();
                        }
                        colorTransitionAnimation.runAnimation();
                    }
                });
            } else {
                if (!textAnimationFinished) {
                    textFadeInOutAnimation.runAnimation();
                }
                colorTransitionAnimation.runAnimation();
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
        ViewAppearAnimation.runAnimation(nextFragmentFab, textMessagesModel.getTextAnimationDuration());
        ViewAppearAnimation.runAnimation(restartTextAnimationFab, textMessagesModel.getTextAnimationDuration());
        textAnimationFinished = true;
        fragmentFinished(nextFragmentFab, mRevealView, mRevealLayout);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}