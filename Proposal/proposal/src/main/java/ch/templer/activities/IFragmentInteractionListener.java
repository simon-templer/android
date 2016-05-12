package ch.templer.activities;

import ch.templer.fragments.FinishedFragment;
import ch.templer.fragments.MapFragment;
import ch.templer.fragments.MapLocationFragment;
import ch.templer.fragments.quizfragment.MultipleChoiceFragment;
import ch.templer.fragments.PictureSlideshowFragment;
import ch.templer.fragments.TextMessagesFragment;
import ch.templer.fragments.VideoFragment;

/**
 * Created by Templer on 24.04.2016.
 */
public interface IFragmentInteractionListener extends PictureSlideshowFragment.OnFragmentInteractionListener,
        VideoFragment.OnFragmentInteractionListener, TextMessagesFragment.OnFragmentInteractionListener,
        MultipleChoiceFragment.OnFragmentInteractionListener, MapLocationFragment.OnFragmentInteractionListener,
        MapFragment.OnFragmentInteractionListener, FinishedFragment.OnFragmentInteractionListener{

}
