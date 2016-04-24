package ch.templer.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import ch.templer.activities.R;
import ch.templer.animation.listener.AnimationFinishedListener;
import ch.templer.animation.ColorTransitionAnimation;
import ch.templer.animation.FloatingActionButtonTransitionAnimation;
import ch.templer.animation.reveallayout.RevealLayout;
import ch.templer.animation.TextFadeInOutAnimation;
import ch.templer.animation.ViewAppearAnimation;
import ch.templer.fragments.service.FragmentTransactionProcessingService;
import ch.templer.model.TextMessagesModel;


public class TextMessagesFragment extends AbstractFragment implements AnimationFinishedListener {
    private static final String TEXT_MESSAGES_ID = "textMessageID";
    private static final String BACKGROUND_COLOR_TRANSITION_TIME_ID = "BACKGROUND_COLOR_TRANSITION_TIME_ID";
    private static final String TEXT_VIEW_SHOW_TIME_ID = "BACKGROUND_COLOR_TRANSITION_TIME_ID";
    private static final String ANIMATION_DURATION_ID = "BACKGROUND_COLOR_TRANSITION_TIME_ID";
    private static final String BACKGROUND_COLOR_ID = "BACKGROUND_COLOR_ID";
    private static final String NEXT_FRAGMENT_BACKGROUND_COLOR_ID = "NEXT_FRAGMENT_BACKGROUND_COLOR_ID";
    private static final String BACKGROUND_ANIMATION_COLORS_ID = "BACKGROUND_ANIMATION_COLORS_ID";
    private FrameLayout frameLayout;
    private TextView content;
    private List<String> textMessages;
    private int backgroundColorTransitionTime;
    private int textViewShowTime;
    private int animationDuration;
    private ColorTransitionAnimation colorTransitionAnimation;
    private TextFadeInOutAnimation textFadeInOutAnimation;

    int[] backgroundAnimationColors;
    private int backgroundColor;
    private int nextFragmentBackgroundColor;

    private FloatingActionButton floatingActionButton;
    private View mRevealView;
    private RevealLayout mRevealLayout;


    private OnFragmentInteractionListener mListener;

    public TextMessagesFragment() {
        // Required empty public constructor
    }

    public static TextMessagesFragment newInstance(TextMessagesModel textMessagesModel) {
        TextMessagesFragment fragment = new TextMessagesFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(TEXT_MESSAGES_ID, textMessagesModel.getMessages());
        args.putInt(BACKGROUND_COLOR_TRANSITION_TIME_ID, textMessagesModel.getBackgrountColorTransitionTime());
        args.putInt(ANIMATION_DURATION_ID, textMessagesModel.getTextAnimationDuration());
        args.putInt(TEXT_VIEW_SHOW_TIME_ID, textMessagesModel.getTextViewShowTime());
        args.putInt(BACKGROUND_COLOR_ID, textMessagesModel.getBackgroundColor());
        args.putInt(NEXT_FRAGMENT_BACKGROUND_COLOR_ID, textMessagesModel.getNextFragmentBackroundColor());
        args.putIntArray(BACKGROUND_ANIMATION_COLORS_ID, textMessagesModel.getBackgroundAnimationColors());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            textMessages = getArguments().getStringArrayList(TEXT_MESSAGES_ID);
            animationDuration = getArguments().getInt(ANIMATION_DURATION_ID);
            backgroundColorTransitionTime = getArguments().getInt(BACKGROUND_COLOR_TRANSITION_TIME_ID);
            textViewShowTime = getArguments().getInt(TEXT_VIEW_SHOW_TIME_ID);
            backgroundColor  = getArguments().getInt(BACKGROUND_COLOR_ID);
            nextFragmentBackgroundColor =  getArguments().getInt(NEXT_FRAGMENT_BACKGROUND_COLOR_ID);
            backgroundAnimationColors = getArguments().getIntArray(BACKGROUND_ANIMATION_COLORS_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text_messages, container, false);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingButton);
        floatingActionButton.setVisibility(View.INVISIBLE);
        mRevealLayout = (RevealLayout) view.findViewById(R.id.reveal_layout);
        mRevealView = view.findViewById(R.id.reveal_view);
        mRevealView.setBackgroundColor(nextFragmentBackgroundColor);

        frameLayout = (FrameLayout) view.findViewById(R.id.TextMessageFragment_FrameLayout);
        frameLayout.setBackgroundColor(backgroundColor);
        content = (TextView) view.findViewById(R.id.text_messages_fragment_content_textview);

        colorTransitionAnimation = new ColorTransitionAnimation(frameLayout, backgroundAnimationColors, backgroundColorTransitionTime);
        textFadeInOutAnimation = new TextFadeInOutAnimation(textMessages, content, textViewShowTime, animationDuration);
        textFadeInOutAnimation.setAnimationFinishedListener(this);

        FragmentTransaction transaction = FragmentTransactionProcessingService.prepareNextFragmentTransaction(getFragmentManager().beginTransaction());

        FloatingActionButtonTransitionAnimation floatingActionButtonAnimationOnClickListener = new FloatingActionButtonTransitionAnimation(floatingActionButton,mRevealView, mRevealLayout, transaction);
        floatingActionButton.setOnClickListener(floatingActionButtonAnimationOnClickListener);

        return (view);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation anim = new Animation() {
        };

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
        ViewAppearAnimation.runAnimation(floatingActionButton,animationDuration);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}