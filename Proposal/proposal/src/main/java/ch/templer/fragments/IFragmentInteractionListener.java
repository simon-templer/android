package ch.templer.fragments;

import ch.templer.fragments.finishfragment.FinishedFragment;
import ch.templer.fragments.pictureslideshowfragment.PictureSlideshowFragment;
import ch.templer.fragments.selfiefragment.SelfieFragment;
import ch.templer.fragments.textmessagesfragment.TextMessagesFragment;
import ch.templer.fragments.videofragment.VideoFragment;
import ch.templer.fragments.mapfragment.MapFragment;
import ch.templer.fragments.quizfragment.QuizFragment;

/**
 * Created by Templer on 24.04.2016.
 */
public interface IFragmentInteractionListener extends PictureSlideshowFragment.OnFragmentInteractionListener,
        VideoFragment.OnFragmentInteractionListener, TextMessagesFragment.OnFragmentInteractionListener,
        QuizFragment.OnFragmentInteractionListener,
        MapFragment.OnFragmentInteractionListener, FinishedFragment.OnFragmentInteractionListener, SelfieFragment.OnFragmentInteractionListener {

}
