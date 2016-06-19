package ch.templer.activities;

import ch.templer.fragments.FinishedFragment;
import ch.templer.fragments.PictureSlideshowFragment;
import ch.templer.fragments.selfiefragment.SelfieFragment;
import ch.templer.fragments.TextMessagesFragment;
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
