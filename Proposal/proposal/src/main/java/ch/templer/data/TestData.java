package ch.templer.data;

import java.util.ArrayList;
import java.util.List;

import ch.templer.activities.R;
import ch.templer.fragments.MultipleChoiceFragment;
import ch.templer.model.Message;
import ch.templer.model.MultipleChoiceModel;
import ch.templer.model.PictureSlideshowModel;
import ch.templer.model.QuestionAndAnswers;
import ch.templer.model.TextMessagesModel;

/**
 * Created by Templer on 04.04.2016.
 */
public class TestData {
    private List<Message> messages;

    private static TestData instance = null;

    public static TestData getInstance() {
        if(instance == null) {
            instance = new TestData();
        }
        return instance;
    }

    protected TestData() {
        messages = new ArrayList<Message>();
        generateTestData();
    }

    private void generateTestData() {

        MultipleChoiceModel multipleChoiceModel = new MultipleChoiceModel();
        List<QuestionAndAnswers> questionAndAnswerses = new ArrayList<>();
        ArrayList<String> answers = new ArrayList<String>();
        answers.add("Text Message 1");
        answers.add("Text Message 2");
        answers.add("Text Message 3");
        answers.add("Text Message 4");
        QuestionAndAnswers questionAndAnswers = QuestionAndAnswers.build().setQuestion("Question 1").setAnswers(answers).build();
        multipleChoiceModel.setQAndA(questionAndAnswers);
        this.messages.add(multipleChoiceModel);

        PictureSlideshowModel pictureSlideshowModel = new PictureSlideshowModel();
        int[] imageIds = {R.drawable.img_6786, R.drawable.img_6787, R.drawable.img_6788, R.drawable.img_6792, R.drawable.img_6797, R.drawable.img_6812, R.drawable.img_6869, R.drawable.img_6870 };
        pictureSlideshowModel.setImageIDs(imageIds);
        this.messages.add(pictureSlideshowModel);

        TextMessagesModel textMessagesModel = new TextMessagesModel();
        ArrayList<String> texts = new ArrayList<String>();
        texts.add("Text Message 1");
        texts.add("Text Message 2");
        texts.add("Text Message 3");
        texts.add("Text Message 4");
        textMessagesModel.setMessages(texts);
        textMessagesModel.setShowTime(3000);
        this.messages.add(textMessagesModel);
    }

    public List<Message> getMessages() {
        return messages;
    }
}
