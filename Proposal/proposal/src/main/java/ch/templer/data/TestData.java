package ch.templer.data;

import java.util.ArrayList;
import java.util.List;

import ch.templer.activities.R;
import ch.templer.model.MapLocationModel;
import ch.templer.model.MessageData;
import ch.templer.model.MultipleChoiceModel;
import ch.templer.model.PictureSlideshowModel;
import ch.templer.model.QuestionAndAnswers;
import ch.templer.model.TextMessagesModel;
import ch.templer.utils.Colors;

/**
 * Created by Templer on 04.04.2016.
 */
public class TestData {
    private List<MessageData> messages;

    private static TestData instance = null;

    public static TestData getInstance() {
        if(instance == null) {
            instance = new TestData();
        }
        return instance;
    }

    protected TestData() {
        messages = new ArrayList<MessageData>();
        generateTestData();
    }

    private void generateTestData() {

        MultipleChoiceModel multipleChoiceModel = new MultipleChoiceModel();
        List<QuestionAndAnswers> questionAndAnswerses = new ArrayList<>();
        ArrayList<String> answers = new ArrayList<String>();
        answers.add("Text MessageData 1");
        answers.add("Text MessageData 2");
        answers.add("Text MessageData 3");
        answers.add("Text MessageData 4");
        QuestionAndAnswers questionAndAnswers = QuestionAndAnswers.build().setQuestion("Question 1").setAnswers(answers).build();
        multipleChoiceModel.setQAndA(questionAndAnswers);
        multipleChoiceModel.setBackgroundColor(Colors.Lime);
        multipleChoiceModel.setNextFragmentBackroundColor(Colors.Amber);
        this.messages.add(multipleChoiceModel);

        TextMessagesModel textMessagesModel = new TextMessagesModel();
        ArrayList<String> texts = new ArrayList<String>();
        texts.add("Hallo Mein Schatz");
//        texts.add("Ich wollte dir einmal mein neues Handy präsentieren!");
//        texts.add("Wie gefäält es dir?");
//        texts.add("Ich dachte, dich auf eine kleine Reise zu nehmen...");
//        texts.add("Ich möchte dir einiges zeigen und hoffe das du etwas Zeit hast");
        texts.add("auf gehts...");
        textMessagesModel.setMessages(texts);
        textMessagesModel.setTextViewShowTime(3000);
        textMessagesModel.setBackgrountColorTransitionTime(3000);
        textMessagesModel.setTextAnimationDuration(3000);
        textMessagesModel.setBackgroundColor(Colors.Amber);
        textMessagesModel.setNextFragmentBackroundColor(Colors.Black);
        int[] colors = new int[]{Colors.Amber, Colors.Emerald, Colors.Indigo, Colors.Crimson, Colors.Yellow, Colors.Mauve};
        textMessagesModel.setBackgroundAnimationColors(colors);
        this.messages.add(textMessagesModel);

        PictureSlideshowModel pictureSlideshowModel = new PictureSlideshowModel();
        int[] imageIds = {R.drawable.img_6786, R.drawable.img_6787, R.drawable.img_6788, R.drawable.img_6792, R.drawable.img_6797, R.drawable.img_6812, R.drawable.img_6869, R.drawable.img_6870 };
        pictureSlideshowModel.setImageIDs(imageIds);
        pictureSlideshowModel.setBackgroundColor(Colors.Black);
        pictureSlideshowModel.setNextFragmentBackroundColor(Colors.White);
        this.messages.add(pictureSlideshowModel);

        MapLocationModel mapLocationModel = new MapLocationModel();
        mapLocationModel.setLatitude(46.979631);
        mapLocationModel.setLongitude(7.456857);
        mapLocationModel.setRadius(10);
        mapLocationModel.setMinDistance(2);
        mapLocationModel.setTimeInterval(2000);
        mapLocationModel.setBackgroundColor(Colors.White);
        mapLocationModel.setNextFragmentBackroundColor(Colors.White);
        this.messages.add(mapLocationModel);

    }

    public List<MessageData> getMessages() {
        return messages;
    }
}
