package ch.templer.fragments.service;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

import ch.templer.activities.R;
import ch.templer.model.AbstractMessageModel;
import ch.templer.model.data.TestData;
import ch.templer.fragments.FinishedFragment;
import ch.templer.fragments.MapFragment;
import ch.templer.fragments.quizfragment.MultipleChoiceFragment;
import ch.templer.fragments.PictureSlideshowFragment;
import ch.templer.fragments.TextMessagesFragment;
import ch.templer.fragments.VideoFragment;
import ch.templer.model.MapLocationModel;
import ch.templer.model.MultipleChoiceModel;
import ch.templer.model.PictureSlideshowModel;
import ch.templer.model.TextMessagesModel;
import ch.templer.model.VideoModel;
import ch.templer.utils.logging.Logger;

/**
 * Created by Templer on 24.04.2016.
 */
public class FragmentTransactionProcessingService {
    private static List<AbstractMessageModel> messages;
    private static int fragmentCounter = 0;
    private static Logger log = Logger.getLogger();

    public static FragmentTransaction prepareNextFragmentTransaction(FragmentTransaction transaction) {

        if (messages == null){
            log.d("initalize Messages", Logger.LOGGER_DEPTH.LOGGER_METHOD);
            messages = TestData.getInstance().getMessages();
        }
        if (fragmentCounter >= messages.size()){
            log.d("Messages done. Show FinishedFragment", Logger.LOGGER_DEPTH.LOGGER_METHOD);
            FinishedFragment finishedFragment = FinishedFragment.newInstance("", "");
            transaction.replace(R.id.fragment_container, finishedFragment);
            fragmentCounter=0;
            return transaction;
        }
        AbstractMessageModel message = messages.get(fragmentCounter);
        if (message instanceof PictureSlideshowModel) {
            log.d("PictureSlideshowModel found. start PictureSlideshowFragment", Logger.LOGGER_DEPTH.LOGGER_METHOD);
            PictureSlideshowFragment pictureSlideshowFragment = PictureSlideshowFragment.newInstance((PictureSlideshowModel) message);
            transaction.replace(R.id.fragment_container, pictureSlideshowFragment);
        } else if (message instanceof TextMessagesModel) {
            log.d("TextMessagesModel found. start TextMessagesFragment", Logger.LOGGER_DEPTH.LOGGER_METHOD);
            TextMessagesFragment textMessage = TextMessagesFragment.newInstance((TextMessagesModel) message);
            transaction.replace(R.id.fragment_container, textMessage);
        } else if (message instanceof VideoModel) {
            log.d("VideoModel found. start VideoFragment", Logger.LOGGER_DEPTH.LOGGER_METHOD);
            VideoFragment videoFragment = new VideoFragment();
            Bundle args = new Bundle();
            args.putInt(VideoFragment.audioID, R.raw.adelle_hello);
            videoFragment.setArguments(args);
            transaction.replace(R.id.fragment_container, videoFragment);
        } else if (message instanceof MultipleChoiceModel) {
            log.d("MultipleChoiceModel found. start MultipleChoiceFragment", Logger.LOGGER_DEPTH.LOGGER_METHOD);
            MultipleChoiceFragment multipleChoiceFragment = MultipleChoiceFragment.newInstance((MultipleChoiceModel) message);
            transaction.replace(R.id.fragment_container, multipleChoiceFragment);
        }else if(message instanceof MapLocationModel){
            log.d("MapLocationModel found. start MapFragment", Logger.LOGGER_DEPTH.LOGGER_METHOD);
            MapFragment mapFragment = MapFragment.newInstance((MapLocationModel) message);
            transaction.replace(R.id.fragment_container, mapFragment);
        }
        fragmentCounter++;
        return transaction;
    }
}
