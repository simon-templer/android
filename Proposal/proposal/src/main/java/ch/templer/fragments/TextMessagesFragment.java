package ch.templer.fragments;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;

import ch.templer.activities.R;
import ch.templer.model.TextMessagesModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TextMessagesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TextMessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TextMessagesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TEXT_MESSAGES_ID = "textMessageID";
    private static final String SHOW_TIME_ID = "showTimeID";
    private FrameLayout layout;
    private TextView content;
    private Timer timer = new Timer();

    // TODO: Rename and change types of parameters
    private List<String> textMessages;
    private int showTime;

    private OnFragmentInteractionListener mListener;

    public TextMessagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TextMessagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TextMessagesFragment newInstance(TextMessagesModel textMessagesModel) {
        TextMessagesFragment fragment = new TextMessagesFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(TEXT_MESSAGES_ID, textMessagesModel.getMessages());
        args.putInt(SHOW_TIME_ID, textMessagesModel.getShowTime());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            textMessages = getArguments().getStringArrayList(TEXT_MESSAGES_ID);
            showTime = getArguments().getInt(SHOW_TIME_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_text_messages, container, false);
        layout = (FrameLayout) view.findViewById(R.id.TextMessageFragment_FrameLayout);
        content = (TextView) view.findViewById(R.id.text_messages_fragment_content_textview);

        new CountDownTimer(textMessages.size() * showTime , showTime) {

            public void onTick(long millisUntilFinished) {
                content.setText(textMessages.get(0));
                textMessages.remove(0);
            }

            public void onFinish() {
                content.setText(textMessages.get(0));
                textMessages.remove(0);
                //content.setText("done!");
            }
        }.start();


        return (view);
    }
    int[] colors = new int[]{Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN};
    int counter = 0;
    int prevalue = 0;

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {

        Animation anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
        //anim.setRepeatCount(Animation.INFINITE);
        //anim.setRepeatMode(Animation.RESTART);
        anim.setAnimationListener(new Animation.AnimationListener() {


            @Override
            public void onAnimationStart(Animation animation) {
                // additional functionality
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                runNextAnimation();
            }
        });

        return anim;
    }

    private void runNextAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofInt(layout, "backgroundColor", colors[prevalue], colors[counter]).setDuration(3000);
        animator.setEvaluator(new ArgbEvaluator());
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                prevalue = counter;
                counter++;
                if (counter >= colors.length - 1){
                    counter = 0;
                    prevalue = 3;
                }
                runNextAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();

    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
