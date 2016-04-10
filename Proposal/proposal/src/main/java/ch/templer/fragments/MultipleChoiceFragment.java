package ch.templer.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import ch.templer.activities.R;
import ch.templer.model.MultipleChoiceModel;
import ch.templer.model.QuestionAndAnswers;
import ch.templer.viewpagger.InfinitePagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MultipleChoiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MultipleChoiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MultipleChoiceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String QUESTION_ID = "question_id";
    private static final String ANSWERS_ID = "answers_id";

    // TODO: Rename and change types of parameters
    private String question;
    private List<String> answers;

    private OnFragmentInteractionListener mListener;

    public MultipleChoiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MultipleChoiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MultipleChoiceFragment newInstance(MultipleChoiceModel multipleChoiceModel) {
        MultipleChoiceFragment fragment = new MultipleChoiceFragment();
        Bundle args = new Bundle();
        args.putString(QUESTION_ID, multipleChoiceModel.getQAndA().getQuestion());
        args.putStringArrayList(ANSWERS_ID, multipleChoiceModel.getQAndA().getAnswers());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = getArguments().getString(QUESTION_ID);
            answers = getArguments().getStringArrayList(ANSWERS_ID);
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
        answerFourRadioButton.setText(answers.get(3));

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
