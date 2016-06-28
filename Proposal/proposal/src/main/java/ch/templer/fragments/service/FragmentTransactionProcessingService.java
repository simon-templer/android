package ch.templer.fragments.service;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

import ch.templer.activities.R;
import ch.templer.fragments.finishfragment.FinishedFragment;
import ch.templer.fragments.pictureslideshowfragment.PictureSlideshowFragment;
import ch.templer.fragments.textmessagesfragment.TextMessagesFragment;
import ch.templer.fragments.mapfragment.MapFragment;
import ch.templer.fragments.quizfragment.QuizFragment;
import ch.templer.fragments.selfiefragment.SelfieFragment;
import ch.templer.fragments.videofragment.VideoFragment;
import ch.templer.model.AbstractMessageModel;
import ch.templer.model.MapLocationModel;
import ch.templer.model.PictureSlideshowModel;
import ch.templer.model.QuizModel;
import ch.templer.model.SelfieModel;
import ch.templer.model.TextMessagesModel;
import ch.templer.model.VideoFragmentModel;
import ch.templer.services.SettingsService;
import ch.templer.utils.logging.Logger;

/**
 * Created by Templer on 24.04.2016.
 */
public class FragmentTransactionProcessingService {
    private static List<AbstractMessageModel> messages;
    private static int fragmentCounter = 0;
    private static Logger log = Logger.getLogger();
    private static FragmentTransaction currentFragment;

    public static void setMessages(List<AbstractMessageModel> messages) {
        FragmentTransactionProcessingService.messages = messages;
        fragmentCounter=SettingsService.getInstance().getIntSetting(SettingsService.PREV_SCENARIO_POSITION,0);;
    }


    private static FragmentTransaction getFragmentTransactioFromPosition(FragmentTransaction transaction, Context context, int position) {
        if (position >= messages.size() || messages == null) {
            log.d("Messages done. Show FinishedFragment", Logger.LOGGER_DEPTH.LOGGER_METHOD);
            FinishedFragment finishedFragment = FinishedFragment.newInstance("", "");
            transaction.replace(R.id.fragment_container, finishedFragment);
            SettingsService.getInstance().setIntegerSettting(SettingsService.PREV_SCENARIO_POSITION, fragmentCounter);
            fragmentCounter = 0;
            return transaction;
        }

        AbstractMessageModel message = messages.get(position);
        if (message instanceof PictureSlideshowModel) {
            log.d("PictureSlideshowModel found. start PictureSlideshowFragment", Logger.LOGGER_DEPTH.LOGGER_METHOD);
            PictureSlideshowFragment pictureSlideshowFragment = PictureSlideshowFragment.newInstance((PictureSlideshowModel) message);
            transaction.replace(R.id.fragment_container, pictureSlideshowFragment);
        } else if (message instanceof TextMessagesModel) {
            log.d("TextMessagesModel found. start TextMessagesFragment", Logger.LOGGER_DEPTH.LOGGER_METHOD);
            TextMessagesFragment textMessage = TextMessagesFragment.newInstance((TextMessagesModel) message);
            transaction.replace(R.id.fragment_container, textMessage);
        } else if (message instanceof QuizModel) {
            log.d("QuizModel found. start QuizFragment", Logger.LOGGER_DEPTH.LOGGER_METHOD);
            QuizFragment quizFragment = QuizFragment.newInstance((QuizModel) message);
            transaction.replace(R.id.fragment_container, quizFragment);
        } else if (message instanceof MapLocationModel) {
            log.d("MapLocationModel found. start MapFragment", Logger.LOGGER_DEPTH.LOGGER_METHOD);
            MapFragment mapFragment = MapFragment.newInstance((MapLocationModel) message);
            transaction.replace(R.id.fragment_container, mapFragment);
        } else if (message instanceof SelfieModel) {
            log.d("SelfieModel found. start SelfieFragment", Logger.LOGGER_DEPTH.LOGGER_METHOD);
            SelfieFragment mapFragment = SelfieFragment.newInstance((SelfieModel) message);
            transaction.replace(R.id.fragment_container, mapFragment);
        } else if (message instanceof VideoFragmentModel) {
            log.d("VideoFragmentModel found. start VideoFragment", Logger.LOGGER_DEPTH.LOGGER_METHOD);
            VideoFragment videoFragment = VideoFragment.newInstance((VideoFragmentModel) message);
            transaction.replace(R.id.fragment_container, videoFragment);
        }
        SettingsService.getInstance().setIntegerSettting(SettingsService.PREV_SCENARIO_POSITION,fragmentCounter);
        fragmentCounter++;
        return transaction;
    }

    public static FragmentTransaction prepareNextFragmentTransaction(FragmentTransaction transaction, Context context) {
        return getFragmentTransactioFromPosition(transaction, context, fragmentCounter);
    }

    public static void transactionStarted() {
        //fragmentCounter++;
    }
}
