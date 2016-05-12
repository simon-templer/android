package ch.templer.fragments.quizfragment;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;

import ch.templer.activities.R;
import ch.templer.animation.BlinkAnimation;
import ch.templer.animation.FlipAnimation;
import ch.templer.animation.FloatingActionButtonTransitionAnimation;
import ch.templer.controls.answerbutton.AnswerButton;
import ch.templer.controls.reveallayout.RevealLayout;
import ch.templer.fragments.AbstractFragment;
import ch.templer.fragments.service.FragmentTransactionProcessingService;
import ch.templer.model.MultipleChoiceModel;
import ch.templer.model.QuestionAndAnswers;
import ch.templer.services.multimedia.SoundService;
import ch.templer.utils.Colors;

public class MultipleChoiceFragment extends AbstractFragment {
    private static final String MULTIPLE_CHOICE_FRAGMENT_MODEL_ID = "MULTIPLE_CHOICE_FRAGMENT_MODEL_ID";

    private FloatingActionButton floatingActionButton;
    private View mRevealView;
    private RevealLayout mRevealLayout;
    private MultipleChoiceModel multipleChoiceModel;
    private LinearLayout buttonContainer;
    private TextView correctAnswers;
    private CoordinatorLayout coordinatorLayout;

    private OnFragmentInteractionListener mListener;
    private int correctAnswersCounter = 0;
    TextSwitcher textSwitcher;
    private List<AnswerButton> answerButtons = new ArrayList<>();
    private int questionCounter = 0;

    private QuizClickListener quizClickListener = new QuizClickListener();

    public MultipleChoiceFragment() {
        // Required empty public constructor
    }

    public static MultipleChoiceFragment newInstance(MultipleChoiceModel multipleChoiceModel) {
        MultipleChoiceFragment fragment = new MultipleChoiceFragment();
        Bundle args = new Bundle();
        args.putSerializable(MULTIPLE_CHOICE_FRAGMENT_MODEL_ID, multipleChoiceModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            multipleChoiceModel = (MultipleChoiceModel) getArguments().getSerializable(MULTIPLE_CHOICE_FRAGMENT_MODEL_ID);
        }
    }

    private void showSnackbar(String message, int lenght) {
        Snackbar mySnackbar = Snackbar.make(coordinatorLayout, message, lenght);
        View snackbarView = mySnackbar.getView();
        snackbarView.setBackgroundColor(multipleChoiceModel.getFragmentColors().getColorTheme().getToolBarBackgroundColor());
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(4);
        mySnackbar.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multiple_choice, container, false);
        buttonContainer = (LinearLayout) view.findViewById(R.id.ButtonContainer);

        RelativeLayout topBar = (RelativeLayout) view.findViewById(R.id.topBar);
        topBar.setBackgroundColor(multipleChoiceModel.getFragmentColors().getColorTheme().getToolBarBackgroundColor());

        correctAnswers = (TextView) view.findViewById(R.id.correct_answers_text_view);
        correctAnswers.setTextColor(multipleChoiceModel.getFragmentColors().getColorTheme().getButtonTextColor());
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingButton);
        mRevealLayout = (RevealLayout) view.findViewById(R.id.reveal_layout);
        mRevealView = view.findViewById(R.id.reveal_view);
        mRevealView.setBackgroundColor(multipleChoiceModel.getFragmentColors().getNextFragmentBackroundColor());

        FragmentTransaction transaction = FragmentTransactionProcessingService.prepareNextFragmentTransaction(getFragmentManager().beginTransaction());

        FloatingActionButtonTransitionAnimation floatingActionButtonAnimationOnClickListener = new FloatingActionButtonTransitionAnimation(floatingActionButton, mRevealView, mRevealLayout, transaction);
        floatingActionButton.setOnClickListener(floatingActionButtonAnimationOnClickListener);
        floatingActionButton.setVisibility(View.INVISIBLE);

        textSwitcher = (TextSwitcher) view.findViewById(R.id.multiple_choice_fragment_question_text_switcher);
        textSwitcher.setInAnimation(this.getContext(), R.anim.slide_in_left);
        textSwitcher.setOutAnimation(this.getContext(), R.anim.slide_out_right);
        textSwitcher.setBackgroundColor(multipleChoiceModel.getFragmentColors().getColorTheme().getTextViewBackgroundColor());
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                TextView textView = new TextView(getContext());
                textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                textView.setLayoutParams(layoutParams);
                textView.setTypeface(null, Typeface.BOLD);
                textView.setTextSize(18);
                textView.setBackgroundColor(multipleChoiceModel.getFragmentColors().getColorTheme().getTextViewBackgroundColor());
                textView.setTextColor(multipleChoiceModel.getFragmentColors().getColorTheme().getTextViewTextColor());
                return textView;
            }
        });

        QuestionAndAnswers questionAndAnswers = getNextQuestion();
        textSwitcher.setText(questionAndAnswers.getQuestion());
        for (int i = 0; i < questionAndAnswers.getAnswers().size(); i++) {
            AnswerButton answerButton = new AnswerButton(this.getContext());
            answerButton.setBackgroundColor(multipleChoiceModel.getFragmentColors().getColorTheme().getButtonBackgroundColor());
            answerButton.setTextColor(multipleChoiceModel.getFragmentColors().getColorTheme().getButtonTextColor());
            answerButton.setText(questionAndAnswers.getAnswers().get(i).getAnswer());
            answerButton.setIsCorrectAnswer(questionAndAnswers.getAnswers().get(i).isCorrectAnswer());
            answerButton.setOnClickListener(quizClickListener);

            Drawable buttonDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.button_selector, null);
            answerButton.setBackground(buttonDrawable);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(20, 15, 20, 15);

            buttonContainer.addView(answerButton, layoutParams);

            answerButtons.add(answerButton);
        }
        updateTopBar();

        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.CoordinatorLayout);
        coordinatorLayout.setBackgroundColor(multipleChoiceModel.getFragmentColors().getColorTheme().getViewBackgroundColor());

        return (view);
    }

    private QuestionAndAnswers getNextQuestion() {
        QuestionAndAnswers questionAndAnswers = null;
        if (multipleChoiceModel.getQAndAs().size() > questionCounter) {
            questionAndAnswers = multipleChoiceModel.getQAndAs().get(questionCounter);
            questionCounter++;
        }
        return questionAndAnswers;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class QuizClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v instanceof AnswerButton) {
                AnswerButton answerButton = (AnswerButton) v;
                if (answerButton.isCorrectAnswer()) {
                    processCorrectAnswer(answerButton);
                } else {
                    processWrongAnswer(answerButton);
                }
                registerNextQuestionListener();
            }
        }
    }

    private void registerNextQuestionListener() {
        buttonContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processNextQuestion();
            }
        });
    }

    private QuestionAndAnswers getCurrentQuestion() {
        QuestionAndAnswers questionAndAnswers = null;
        if (questionCounter - 1 < multipleChoiceModel.getQAndAs().size()) {
            questionAndAnswers = multipleChoiceModel.getQAndAs().get(questionCounter - 1);
        }
        return questionAndAnswers;
    }

    private void processNextQuestion() {
        if (multipleChoiceModel.getQAndAs().size() > questionCounter) {
            buttonContainer.setOnClickListener(null);
            QuestionAndAnswers questionAndAnswers = getNextQuestion();
            textSwitcher.setText(questionAndAnswers.getQuestion());
            for (int i = 0; i < answerButtons.size(); i++) {
                AnswerButton answerButton = answerButtons.get(i);
                answerButton.setText(questionAndAnswers.getAnswers().get(i).getAnswer());
                answerButton.setIsCorrectAnswer(questionAndAnswers.getAnswers().get(i).isCorrectAnswer());
                answerButton.setBackgroundColor(multipleChoiceModel.getFragmentColors().getColorTheme().getButtonBackgroundColor());
                answerButton.setOnClickListener(quizClickListener);
                FlipAnimation flipAnimation = new FlipAnimation(answerButton, answerButton);

                answerButton.startAnimation(flipAnimation);
                SoundService.playSound(this.getContext(), R.raw.swoosh);
            }
        } else {
            floatingActionButton.setVisibility(View.VISIBLE);
        }

    }

    private void processCorrectAnswer(AnswerButton answerButton) {
        correctAnswersCounter++;
        SoundService.playSound(this.getContext(), R.raw.correct);
        updateTopBar();
        unregisterAnswerButtonsClickListener();
        answerButton.setBackgroundColor(Colors.Green);
        BlinkAnimation.runAnimation(answerButton, 300, 2);
        showSnackbar(getCurrentQuestion().getCorrectMessage(), Snackbar.LENGTH_LONG);
    }

    private void updateTopBar() {
        correctAnswers.setText("Correct Answers: " + correctAnswersCounter);
    }

    private void unregisterAnswerButtonsClickListener() {
        for (AnswerButton answerButton : answerButtons) {
            answerButton.setOnClickListener(null);
        }
    }

    private void registerAnswerButtonsClickListener() {
        for (AnswerButton answerButton : answerButtons) {
            answerButton.setOnClickListener(quizClickListener);
        }
    }

    private void processWrongAnswer(AnswerButton answerButton) {
        SoundService.playSound(this.getContext(), R.raw.wrong);
        unregisterAnswerButtonsClickListener();
        answerButton.setBackgroundColor(Colors.Red);

        AnswerButton correctAnswerButton = getCorrectAnswerButton();
        showSnackbar("fdsssssssssssssssssssssssssssssss fhdsfh sdfhosd fsdof sdofj sdofj sdof sdojf sdjof sdf sdof sdjfsod fsdjo fjsdof ojsd fosdf jsdof jsd ofsdojfj sdf jsfj sdjfosao fsd ofsd jfs aodfs ofj sdfosdfjoasfj osdfojsjo", Snackbar.LENGTH_LONG);

        correctAnswerButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                AnswerButton correctAnswerButton = getCorrectAnswerButton();
                correctAnswerButton.setBackgroundColor(Colors.Green);
                BlinkAnimation.runAnimation(correctAnswerButton, 300, 2);
            }
        }, 500);
    }

    private AnswerButton getCorrectAnswerButton() {
        AnswerButton correctAnswerButton = null;
        for (AnswerButton answerButton : answerButtons) {
            if (answerButton.isCorrectAnswer()) {
                correctAnswerButton = answerButton;
                break;
            }
        }
        return correctAnswerButton;
    }


}
