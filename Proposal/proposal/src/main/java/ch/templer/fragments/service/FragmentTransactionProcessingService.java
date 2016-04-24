package ch.templer.fragments.service;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

import ch.templer.activities.R;
import ch.templer.data.TestData;
import ch.templer.fragments.FinishedFragment;
import ch.templer.fragments.MapFragment;
import ch.templer.fragments.MapLocationFragment;
import ch.templer.fragments.MultipleChoiceFragment;
import ch.templer.fragments.PictureSlideshowFragment;
import ch.templer.fragments.TextMessagesFragment;
import ch.templer.fragments.VideoFragment;
import ch.templer.model.MapLocationModel;
import ch.templer.model.MessageData;
import ch.templer.model.MultipleChoiceModel;
import ch.templer.model.PictureSlideshowModel;
import ch.templer.model.TextMessagesModel;
import ch.templer.model.VideoModel;

/**
 * Created by Templer on 24.04.2016.
 */
public class FragmentTransactionProcessingService {
    private static List<MessageData> messages;
    private static int fragmentCounter = 0;

    public static FragmentTransaction prepareNextFragmentTransaction(FragmentTransaction transaction) {

        if (messages == null){
            messages = TestData.getInstance().getMessages();
        }
        if (fragmentCounter >= messages.size()){
            FinishedFragment finishedFragment = FinishedFragment.newInstance("", "");
            transaction.replace(R.id.fragment_container, finishedFragment);
            fragmentCounter=0;
            return transaction;
        }
        MessageData message = messages.get(fragmentCounter);
        if (message instanceof PictureSlideshowModel) {
            PictureSlideshowFragment pictureSlideshowFragment = PictureSlideshowFragment.newInstance((PictureSlideshowModel) message);
            transaction.replace(R.id.fragment_container, pictureSlideshowFragment);
        } else if (message instanceof TextMessagesModel) {
            TextMessagesFragment textMessage = TextMessagesFragment.newInstance((TextMessagesModel) message);
            transaction.replace(R.id.fragment_container, textMessage);
        } else if (message instanceof VideoModel) {
            VideoFragment videoFragment = new VideoFragment();
            Bundle args = new Bundle();
            args.putInt(VideoFragment.audioID, R.raw.adelle_hello);
            videoFragment.setArguments(args);
            transaction.replace(R.id.fragment_container, videoFragment);
        } else if (message instanceof MultipleChoiceModel) {
            MultipleChoiceFragment multipleChoiceFragment = MultipleChoiceFragment.newInstance((MultipleChoiceModel) message);
            transaction.replace(R.id.fragment_container, multipleChoiceFragment);
        }else if(message instanceof MapLocationModel){
            MapFragment mapFragment = MapFragment.newInstance((MapLocationModel) message);
            transaction.replace(R.id.fragment_container, mapFragment);
        }
        fragmentCounter++;
        return transaction;
    }
}
