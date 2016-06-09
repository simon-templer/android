package ch.templer.fragments.quizfragment;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
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
import ch.templer.animation.ViewAppearAnimation;
import ch.templer.controls.answerbutton.AnswerButton;
import ch.templer.controls.listener.AnimationFinishedListener;
import ch.templer.controls.listener.AnimationStartListener;
import ch.templer.controls.reveallayout.RevealLayout;
import ch.templer.fragments.AbstractFragment;
import ch.templer.fragments.service.FragmentTransactionProcessingService;
import ch.templer.model.QuizModel;
import ch.templer.model.QuestionAndAnswers;
import ch.templer.services.multimedia.SoundService;
import ch.templer.utils.Colors;

public class QuizFragment extends AbstractFragment {
    private static final String MULTIPLE_CHOICE_FRAGMENT_MODEL_ID = "MULTIPLE_CHOICE_FRAGMENT_MODEL_ID";
    private static final String QUIZ_POSITION_ID = "QUIZ_POSITION_ID";
    private static final String QUIZ_CORRECT_ANSWER_COUNTER_ID = "QUIZ_CORRECT_ANSWER_COUNTER_ID";
    private static final String ANSWER_PRESSED_ID = "ANSWER_PRESSED_ID";

    private static int BLINK_ANIMATION_TIME = 300;
    private static int BLINK_AMOUNT = 2;

    private FloatingActionButton floatingActionButton;
    private View mRevealView;
    private RevealLayout mRevealLayout;
    private QuizModel quizModel;
    private LinearLayout buttonContainer;
    private TextView correctAnswers;
    private CoordinatorLayout coordinatorLayout;

    private OnFragmentInteractionListener mListener;
    private int correctAnswersCounter = 0;
    TextSwitcher textSwitcher;
    private List<AnswerButton> answerButtons = new ArrayList<>();
    private int questionCounter = 0;
    private boolean answerPressed = false;

    private QuizAnswerClickListener quizClickListener = new QuizAnswerClickListener();

    public QuizFragment() {
        // Required empty public constructor
    }

    public static QuizFragment newInstance(QuizModel quizModel) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putSerializable(MULTIPLE_CHOICE_FRAGMENT_MODEL_ID, quizModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        QuestionAndAnswers questionAndAnswers;
        // Restoring State if necessary
        if (savedInstanceState != null) {
            questionCounter = savedInstanceState.getInt(QUIZ_POSITION_ID);
            correctAnswersCounter = savedInstanceState.getInt(QUIZ_CORRECT_ANSWER_COUNTER_ID);
            if (savedInstanceState.getBoolean(ANSWER_PRESSED_ID)) {
                // The AnswerPressed flag indicates if a user already pressed an answer.
                // If a rotation occurs between seeing the result and moving on to the next question
                // it is necessary to increase the questionCounter by one
                questionCounter++;
            }
            questionAndAnswers = getCurrentQuestion();

        } else {
            questionAndAnswers = getNextQuestion();
        }

        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        buttonContainer = (LinearLayout) view.findViewById(R.id.ButtonContainer);

        RelativeLayout topBar = (RelativeLayout) view.findViewById(R.id.topBar);
        topBar.setBackgroundColor(quizModel.getFragmentColors().getColorTheme().getToolBarBackgroundColor());

        correctAnswers = (TextView) view.findViewById(R.id.correct_answers_text_view);
        correctAnswers.setTextColor(quizModel.getFragmentColors().getColorTheme().getButtonTextColor());
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingButton);
        mRevealLayout = (RevealLayout) view.findViewById(R.id.reveal_layout);
        mRevealView = view.findViewById(R.id.reveal_view);
        mRevealView.setBackgroundColor(quizModel.getFragmentColors().getNextFragmentBackroundColor());

        AnswerButton answerButtonOne = (AnswerButton) view.findViewById(R.id.answerOne);
        AnswerButton answerButtonTwo = (AnswerButton) view.findViewById(R.id.answerTwo);
        AnswerButton answerButtonThree = (AnswerButton) view.findViewById(R.id.answerThree);
        AnswerButton answerButtonFour = (AnswerButton) view.findViewById(R.id.answerFour);
        answerButtons.add(answerButtonOne);
        answerButtons.add(answerButtonTwo);
        answerButtons.add(answerButtonThree);
        answerButtons.add(answerButtonFour);
        setupButtons(questionAndAnswers);

        //necessary for last question iteration
        if (questionCounter > questionAndAnswers.getAnswers().size()){
            freezeButtons();
        }

        FragmentTransaction transaction = FragmentTransactionProcessingService.prepareNextFragmentTransaction(getFragmentManager().beginTransaction());

        FloatingActionButtonTransitionAnimation floatingActionButtonAnimationOnClickListener = new FloatingActionButtonTransitionAnimation(floatingActionButton, mRevealView, mRevealLayout, transaction);
        floatingActionButtonAnimationOnClickListener.setAnimationStartListener(new AnimationStartListener() {
            @Override
            public void onAnimationStarted() {

                //ViewVanishAnimation.runAnimation(floatingActionButton, 1000);
             //  floatingActionButton.setVisibility(View.GONE);
            }
        });
        floatingActionButton.setOnClickListener(floatingActionButtonAnimationOnClickListener);
        floatingActionButton.setVisibility(View.INVISIBLE);

        textSwitcher = (TextSwitcher) view.findViewById(R.id.multiple_choice_fragment_question_text_switcher);
        textSwitcher.setInAnimation(this.getContext(), R.anim.slide_in_left);
        textSwitcher.setOutAnimation(this.getContext(), R.anim.slide_out_right);
        textSwitcher.setBackgroundColor(quizModel.getFragmentColors().getColorTheme().getTextViewBackgroundColor());
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                TextView textView = new TextView(getContext());
                textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                textView.setLayoutParams(layoutParams);
                textView.setTypeface(null, Typeface.BOLD);
                textView.setTextSize(18);
                textView.setBackgroundColor(quizModel.getFragmentColors().getColorTheme().getTextViewBackgroundColor());
                textView.setTextColor(quizModel.getFragmentColors().getColorTheme().getTextViewTextColor());
                return textView;
            }
        });
        textSwitcher.setText(questionAndAnswers.getQuestion());
        textSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processNextQuestion();
            }
        });
        textSwitcher.setClickable(false);
        updateTopBar();

        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.CoordinatorLayout);
        coordinatorLayout.setBackgroundColor(quizModel.getFragmentColors().getColorTheme().getViewBackgroundColor());

        return (view);
    }

    public static void sendViewToBack(final View child) {
        final ViewGroup parent = (ViewGroup) child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            quizModel = (QuizModel) getArguments().getSerializable(MULTIPLE_CHOICE_FRAGMENT_MODEL_ID);
        }
    }

    private void showSnackbar(String message, int lenght) {
        Snackbar mySnackbar = Snackbar.make(coordinatorLayout, message, lenght);
        View snackbarView = mySnackbar.getView();
        snackbarView.setBackgroundColor(quizModel.getFragmentColors().getColorTheme().getToolBarBackgroundColor());
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(4);
        mySnackbar.show();
    }


    private void setupButtons(QuestionAndAnswers questionAndAnswers) {
        for (int i = 0; i < questionAndAnswers.getAnswers().size(); i++) {
            AnswerButton answerButton = answerButtons.get(i);
            answerButton.setBackgroundColor(quizModel.getFragmentColors().getColorTheme().getButtonBackgroundColor());
            answerButton.setTextColor(quizModel.getFragmentColors().getColorTheme().getButtonTextColor());
            answerButton.setText(questionAndAnswers.getAnswers().get(i).getAnswer());
            answerButton.setIsCorrectAnswer(questionAndAnswers.getAnswers().get(i).isCorrectAnswer());
            answerButton.setOnClickListener(quizClickListener);
        }
    }

    private QuestionAndAnswers getNextQuestion() {
        QuestionAndAnswers questionAndAnswers = quizModel.getQAndAs().get(questionCounter);
        if (quizModel.getQAndAs().size() > questionCounter) {
            questionAndAnswers = quizModel.getQAndAs().get(questionCounter);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(QUIZ_POSITION_ID, questionCounter);
        outState.putInt(QUIZ_CORRECT_ANSWER_COUNTER_ID, correctAnswersCounter);
        outState.putBoolean(ANSWER_PRESSED_ID,answerPressed);
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

    private QuestionAndAnswers getCurrentQuestion() {
        QuestionAndAnswers questionAndAnswers = null;
        if (questionCounter - 1 < quizModel.getQAndAs().size()) {
            questionAndAnswers = quizModel.getQAndAs().get(questionCounter - 1);
        }else {
            questionAndAnswers = quizModel.getQAndAs().get(quizModel.getQAndAs().size() - 1);
        }
        return questionAndAnswers;
    }

    private void processNextQuestion() {
        if (quizModel.getQAndAs().size() > questionCounter) {
            textSwitcher.setClickable(false);
            QuestionAndAnswers questionAndAnswers = getNextQuestion();
            textSwitcher.setText(questionAndAnswers.getQuestion());
            for (int i = 0; i < answerButtons.size(); i++) {
                AnswerButton answerButton = answerButtons.get(i);
                answerButton.setText(questionAndAnswers.getAnswers().get(i).getAnswer());
                answerButton.setIsCorrectAnswer(questionAndAnswers.getAnswers().get(i).isCorrectAnswer());
                answerButton.setBackgroundColor(quizModel.getFragmentColors().getColorTheme().getButtonBackgroundColor());
                answerButton.setOnClickListener(quizClickListener);
                SoundService.playSound(this.getContext(), R.raw.swoosh);

            }
            FlipAnimation flipAnimation = new FlipAnimation(buttonContainer, buttonContainer);
            flipAnimation.setAnimationFinishedListener(new AnimationFinishedListener() {
                @Override
                public void onAnimationFinished() {
                    answerPressed = false;
                    unfreezeButtons();
                }
            });
            buttonContainer.startAnimation(flipAnimation);

        } else {
            textSwitcher.setClickable(false);
            ViewAppearAnimation.runAnimation(floatingActionButton, quizModel.getFabAppearAnimationTime());
        }

    }

    private void processCorrectAnswer(AnswerButton answerButton) {
        correctAnswersCounter++;
        showSnackbar(getCurrentQuestion().getCorrectMessage(), Snackbar.LENGTH_LONG);
        SoundService.playSound(this.getContext(), R.raw.correct);
        updateTopBar();
        answerButton.setBackgroundColor(Colors.Green);
        BlinkAnimation blinkAnimation = new BlinkAnimation(answerButton, BLINK_ANIMATION_TIME, BLINK_AMOUNT);
        blinkAnimation.setAnimationFinishedListener(new AnimationFinishedListener() {
            @Override
            public void onAnimationFinished() {
                textSwitcher.setClickable(true);
            }
        });
        blinkAnimation.runAnimation();

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
        answerButton.setBackgroundColor(Colors.Red);

        AnswerButton correctAnswerButton = getCorrectAnswerButton();
        showSnackbar(getCurrentQuestion().getWrongMessage(), Snackbar.LENGTH_LONG);

        correctAnswerButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                AnswerButton correctAnswerButton = getCorrectAnswerButton();
                correctAnswerButton.setBackgroundColor(Colors.Green);
                BlinkAnimation blinkAnimation = new BlinkAnimation(correctAnswerButton, BLINK_ANIMATION_TIME, BLINK_AMOUNT);
                blinkAnimation.setAnimationFinishedListener(new AnimationFinishedListener() {
                    @Override
                    public void onAnimationFinished() {
                        textSwitcher.setClickable(true);
                    }
                });
                blinkAnimation.runAnimation();
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

    private class QuizAnswerClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v instanceof AnswerButton) {
                answerPressed = true;
                freezeButtons();
                textSwitcher.setClickable(false);
                AnswerButton answerButton = (AnswerButton) v;
                if (answerButton.isCorrectAnswer()) {
                    processCorrectAnswer(answerButton);
                } else {
                    processWrongAnswer(answerButton);
                }
            }
        }
    }

    private void freezeButtons() {
        for (AnswerButton answerButton : answerButtons) {
            answerButton.setClickable(false);
        }
    }

    private void unfreezeButtons() {
        for (AnswerButton answerButton : answerButtons) {
            answerButton.setClickable(true);
        }
    }
}
