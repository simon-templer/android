package ch.templer.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import ch.FragmentFinishedListener;
import ch.templer.activities.R;
import ch.templer.animation.AnimationFinishedListener;
import ch.templer.animation.ColorTransitionAnimation;
import ch.templer.animation.TextFadeInOutAnimation;
import ch.templer.model.TextMessagesModel;
import ch.templer.utils.Colors;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TextMessagesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TextMessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TextMessagesFragment extends Fragment implements AnimationFinishedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TEXT_MESSAGES_ID = "textMessageID";
    private static final String BACKGROUND_COLOR_TRANSITION_TIME_ID = "BACKGROUND_COLOR_TRANSITION_TIME_ID";
    private static final String TEXT_VIEW_SHOW_TIME_ID = "BACKGROUND_COLOR_TRANSITION_TIME_ID";
    private static final String ANIMATION_DURATION_ID = "BACKGROUND_COLOR_TRANSITION_TIME_ID";
    private FrameLayout frameLayout;
    private TextView content;
    private FloatingActionButton floatingActionButton;
    private FragmentFinishedListener fragmentFinishedListener;
    private int[] colors = new int[]{Colors.Emerald, Colors.Indigo, Colors.Crimson, Colors.Yellow, Colors.Mauve};
    private List<String> textMessages;
    private int backgroundColorTransitionTime;
    private int textViewShowTime;
    private int animationDuration;
    private ColorTransitionAnimation colorTransitionAnimation;
    private TextFadeInOutAnimation textFadeInOutAnimation;

    private OnFragmentInteractionListener mListener;

    public void setFragmentFinishedListener(FragmentFinishedListener fragmentFinishedListener) {
        this.fragmentFinishedListener = fragmentFinishedListener;
    }

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_text_messages, container, false);
        frameLayout = (FrameLayout) view.findViewById(R.id.TextMessageFragment_FrameLayout);
        frameLayout.setBackgroundColor(colors[0]);
        content = (TextView) view.findViewById(R.id.text_messages_fragment_content_textview);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentFinishedListener != null) {
                    fragmentFinishedListener.onFragmentFinished();
                }
            }
        });
        floatingActionButton.setVisibility(View.INVISIBLE);
        colorTransitionAnimation = new ColorTransitionAnimation(frameLayout, colors, backgroundColorTransitionTime);
        textFadeInOutAnimation = new TextFadeInOutAnimation(textMessages, content, textViewShowTime, animationDuration);
        textFadeInOutAnimation.setAnimationFinishedListener(this);

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
                    //colorTransitionAnimation.runAnimation();
                    textFadeInOutAnimation.runAnimation();
                }
            });
        } else {
            //colorTransitionAnimation.runAnimation();
            textFadeInOutAnimation.runAnimation();
        }

        return anim;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        Animation fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimation.setDuration(animationDuration);
        floatingActionButton.setVisibility(View.VISIBLE);
        floatingActionButton.startAnimation(fadeInAnimation);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
