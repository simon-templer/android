package ch.templer.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.templer.activities.R;
import ch.templer.viewpagger.SampleAdapter;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link PictureSlideshowFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PictureSlideshowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PictureSlideshowFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://developer.android.com";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ViewPager viewPager;

    private OnFragmentInteractionListener mListener;

    public PictureSlideshowFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PictureSlideshowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PictureSlideshowFragment newInstance(String param1, String param2) {
        PictureSlideshowFragment fragment = new PictureSlideshowFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_picture_slideshow, container, false);
        View result=inflater.inflate(R.layout.pager, container, false);
        ViewPager pager=(ViewPager)result.findViewById(R.id.pager);

        pager.setAdapter(buildAdapter());
        pager.setCurrentItem(pager.getChildCount() * SampleAdapter.LOOPS_COUNT / 2, false); // set current item in the adapter to middle
        return(result);
    }
    private PagerAdapter buildAdapter() {
        int[] imageId = {R.drawable.img_6786, R.drawable.img_6787, R.drawable.img_6788, R.drawable.img_6792, R.drawable.img_6797, R.drawable.img_6812, R.drawable.img_6869, R.drawable.img_6870 };
        return(new SampleAdapter(getActivity(), getChildFragmentManager(), imageId));
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    // TODO: Rename method, update argument and hook method into UI event
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other ch.templer.fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
