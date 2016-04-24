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
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import ch.templer.activities.R;
import ch.templer.animation.reveallayout.RevealLayout;
import ch.templer.fragments.service.FragmentTransactionProcessingService;
import ch.templer.model.MultipleChoiceModel;
import ch.templer.animation.FloatingActionButtonTransitionAnimation;

public class MultipleChoiceFragment extends AbstractFragment {
    private static final String QUESTION_ID = "question_id";
    private static final String ANSWERS_ID = "answers_id";
    private static final String BACKGROUND_COLOR_ID = "BACKGROUND_COLOR_ID";
    private static final String NEXT_FRAGMENT_BACKGROUND_COLOR_ID = "NEXT_FRAGMENT_BACKGROUND_COLOR_ID";

    private FloatingActionButton floatingActionButton;
    private View mRevealView;
    private RevealLayout mRevealLayout;

    private int backgroundColor;
    private int nextFragmentBackgroundColor;

    private String question;
    private List<String> answers;

    private OnFragmentInteractionListener mListener;

    public MultipleChoiceFragment() {
        // Required empty public constructor
    }

    public static MultipleChoiceFragment newInstance(MultipleChoiceModel multipleChoiceModel) {
        MultipleChoiceFragment fragment = new MultipleChoiceFragment();
        Bundle args = new Bundle();
        args.putString(QUESTION_ID, multipleChoiceModel.getQAndA().getQuestion());
        args.putStringArrayList(ANSWERS_ID, multipleChoiceModel.getQAndA().getAnswers());
        args.putInt(BACKGROUND_COLOR_ID, multipleChoiceModel.getBackgroundColor());
        args.putInt(NEXT_FRAGMENT_BACKGROUND_COLOR_ID, multipleChoiceModel.getNextFragmentBackroundColor());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = getArguments().getString(QUESTION_ID);
            answers = getArguments().getStringArrayList(ANSWERS_ID);
            backgroundColor  = getArguments().getInt(BACKGROUND_COLOR_ID);
            nextFragmentBackgroundColor =  getArguments().getInt(NEXT_FRAGMENT_BACKGROUND_COLOR_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multiple_choice, container, false);
        TextView questionTextView =(TextView)view.findViewById(R.id.multiple_choice_fragment_question_text_view);
        questionTextView.setText(question);
        RadioButton answerOneRadioButton =(RadioButton)view.findViewById(R.id.multiple_choice_fragment_answer_one_radio_button);
        answerOneRadioButton.setText(answers.get(0));
        RadioButton answerTwoRadioButton =(RadioButton)view.findViewById(R.id.multiple_choice_fragment_answer_two_radio_button);
        answerTwoRadioButton.setText(answers.get(1));
        RadioButton answerThreeRadioButton =(RadioButton)view.findViewById(R.id.multiple_choice_fragment_answer_three_radio_button);
        answerThreeRadioButton.setText(answers.get(2));
        RadioButton answerFourRadioButton =(RadioButton)view.findViewById(R.id.multiple_choice_fragment_answer_four_radio_button);

        FrameLayout frameLayout =(FrameLayout)view.findViewById(R.id.multiple_choice_fragment_frame_layout);
        frameLayout.setBackgroundColor(backgroundColor);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingButton);
        mRevealLayout = (RevealLayout) view.findViewById(R.id.reveal_layout);
        mRevealView = view.findViewById(R.id.reveal_view);
        mRevealView.setBackgroundColor(nextFragmentBackgroundColor);

        FragmentTransaction transaction = FragmentTransactionProcessingService.prepareNextFragmentTransaction(getFragmentManager().beginTransaction());

        FloatingActionButtonTransitionAnimation floatingActionButtonAnimationOnClickListener = new FloatingActionButtonTransitionAnimation(floatingActionButton,mRevealView, mRevealLayout, transaction);
        floatingActionButton.setOnClickListener(floatingActionButtonAnimationOnClickListener);

        return(view);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
