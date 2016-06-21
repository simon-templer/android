package ch.templer.model.data;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ch.templer.activities.R;
import ch.templer.model.AbstractMessageModel;
import ch.templer.model.ColorTheme;
import ch.templer.model.FragmentColors;
import ch.templer.model.MapLocationModel;
import ch.templer.model.SelfieModel;
import ch.templer.model.VideoFragmentModel;
import ch.templer.utils.Colors;

/**
 * Created by Templer on 04.04.2016.
 */
public class TestData {
    private List<AbstractMessageModel> messages;

    private static TestData instance = null;
    private Context context;

    public static TestData getInstance(Context context) {
        if (instance == null) {
            instance = new TestData(context);
        }
        return instance;
    }

    protected TestData(Context context) {
        messages = new ArrayList<AbstractMessageModel>();
        this.context = context;
        generateTestData();
    }

    private void generateTestData() {

        FragmentColors fragmentColors = new FragmentColors();
        fragmentColors.setColorTheme(ColorTheme.constructColorTheme(ColorTheme.ColorThemeType.DEEPPURPLE, context));
        fragmentColors.setNextFragmentBackroundColor(Colors.Amber);

//        QuizModel quizModel = new QuizModel();
//        List<QuestionAndAnswers> qAndAs =  new ArrayList<>();
//        for (int i = 0; i <4; i++){
//            ArrayList<Answer> answers = new ArrayList<Answer>();
//            answers.add(new Answer("Carrot orange test test long test long " + i, false));
//            answers.add(new Answer("Blue " + i, true));
//            answers.add(new Answer("Brown " + i, false));
//            answers.add(new Answer("Grey " + i, false));
//            QuestionAndAnswers questionAndAnswers = new QuestionAndAnswers();
//            questionAndAnswers.setQuestion("You surly know the color of my eyes You surly know the color of my eyes? " + i);
//            questionAndAnswers.setAnswers(answers);
//            questionAndAnswers.setCorrectMessage("That is Correct");
//            questionAndAnswers.setWrongMessage("That is incorrect");
//            qAndAs.add(questionAndAnswers);
//        }
//
//        quizModel.setFragmentColors(fragmentColors);
//        quizModel.setBackgroundColor(Colors.White);
//        quizModel.setQAndA(qAndAs);
//        quizModel.setFabAppearAnimationTime(1000);
//        quizModel.setTitle("Quiz 1 - Easy");
//        this.messages.add(quizModel);
//
//        TextMessagesModel textMessagesModel = new TextMessagesModel();
//        ArrayList<String> texts = new ArrayList<String>();
//        texts.add("Hallo Mein Schatz");
////        texts.add("Ich wollte dir einmal mein neues Handy präsentieren!");
////        texts.add("Wie gefäält es dir?");
////        texts.add("Ich dachte, dich auf eine kleine Reise zu nehmen...");
//        //texts.add("Ich möchte dir einiges zeigen und hoffe das du etwas Zeit hast");
//        texts.add("auf gehts...");
//        textMessagesModel.setMessages(texts);
//        textMessagesModel.setTextViewShowTime(3000);
//        textMessagesModel.setBackgrountColorTransitionTime(3000);
//        textMessagesModel.setTextAnimationDuration(3000);
//        textMessagesModel.setBackgroundColor(Colors.Amber);
//        textMessagesModel.setBackgroundMusicID(R.raw.adelle_hello);
//        textMessagesModel.setNextFragmentBackroundColor(Colors.ImageSliderBackground);
//        int[] colors = new int[]{Colors.Amber, Colors.Emerald, Colors.Indigo, Colors.Crimson, Colors.Yellow, Colors.Mauve};
//        textMessagesModel.setBackgroundAnimationColors(colors);
//        this.messages.add(textMessagesModel);
//
//        PictureSlideshowModel pictureSlideshowModel = new PictureSlideshowModel();
//        //int[] imageIds = {R.drawable.img_6786, R.drawable.img_6787, R.drawable.img_6788, R.drawable.img_6792, R.drawable.img_6797, R.drawable.img_6812, R.drawable.img_6869, R.drawable.img_6870 };
//        int[] imageIds = {R.drawable.wallpaper, R.drawable.wallpaper,R.drawable.wallpaper,R.drawable.wallpaper,R.drawable.wallpaper, R.drawable.wallpaper};
//        pictureSlideshowModel.setImageIDs(imageIds);
//        pictureSlideshowModel.setBackgroundColor(Colors.ImageSliderBackground);
//        pictureSlideshowModel.setNextFragmentBackroundColor(Colors.White);
//        this.messages.add(pictureSlideshowModel);
//
//        SelfieModel selfieModel = new SelfieModel();
//        selfieModel.setBackgroundColor(Colors.ImageSliderBackground);
//        selfieModel.setNextFragmentBackroundColor(Colors.White);
//        selfieModel.setFragmentColors(fragmentColors);
//        selfieModel.setDescription("Test Description to take a selfie!!!");
//        this.messages.add(selfieModel);
//
//        MapLocationModel mapLocationModel = new MapLocationModel();
//
//        mapLocationModel.setLatitude(46.521930);
//        mapLocationModel.setLongitude(30.614898);

//        mapLocationModel.setLatitude(48.660463);
//        mapLocationModel.setLongitude(8.943870);

//        mapLocationModel.setLatitude(48.834002);
//        mapLocationModel.setLongitude(9.151623);

//        mapLocationModel.setLatitude(48.657370);
//        mapLocationModel.setLongitude(8.937986);

//        mapLocationModel.setLatitude(46.979631);
//        mapLocationModel.setLongitude(7.456857);
//        mapLocationModel.setRadius(3);
//        mapLocationModel.setBackgroundColor(Colors.White);
//        mapLocationModel.setNextFragmentBackroundColor(Colors.White);
//        mapLocationModel.setFragmentColors(fragmentColors);
//        this.messages.add(mapLocationModel);

        VideoFragmentModel videoFragmentModel = new VideoFragmentModel();
        videoFragmentModel.setBackgroundColor(Colors.ImageSliderBackground);
        videoFragmentModel.setNextFragmentBackroundColor(Colors.White);
        videoFragmentModel.setFabAppearAnimationTime(1000);
        videoFragmentModel.setFragmentColors(fragmentColors);
        videoFragmentModel.setVideoResourceID(R.raw.video1);
        this.messages.add(videoFragmentModel);
    }

    public List<AbstractMessageModel> getMessages() {
        return messages;
    }
}
