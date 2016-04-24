package ch.templer.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import ch.templer.activities.R;
import ch.templer.animation.FloatingActionButtonTransitionAnimation;
import ch.templer.animation.reveallayout.RevealLayout;
import ch.templer.animation.ViewAppearAnimation;
import ch.templer.fragments.service.FragmentTransactionProcessingService;
import ch.templer.model.PictureSlideshowModel;
import ch.templer.viewpagger.InfinitePagerAdapter;
import ch.templer.viewpagger.SampleAdapter;

public class PictureSlideshowFragment extends AbstractFragment {
    private static final String IMAGE_IDS_KEY = "imageIds";
    private static final String BACKGROUND_COLOR_ID = "BACKGROUND_COLOR_ID";
    private static final String NEXT_FRAGMENT_BACKGROUND_COLOR_ID = "NEXT_FRAGMENT_BACKGROUND_COLOR_ID";
    private final String PLUS_ONE_URL = "http://developer.android.com";

    private int[] imageIds;

    private int backgroundColor;
    private int nextFragmentBackgroundColor;

    FrameLayout frameLayout;

    private OnFragmentInteractionListener mListener;

    private FloatingActionButton floatingActionButton;
    private View mRevealView;
    private RevealLayout mRevealLayout;

    public PictureSlideshowFragment() {
        // Required empty public constructor
    }

    public static PictureSlideshowFragment newInstance(PictureSlideshowModel pictureSlideshowModel) {
        PictureSlideshowFragment fragment = new PictureSlideshowFragment();
        Bundle args = new Bundle();
        args.putIntArray(IMAGE_IDS_KEY, pictureSlideshowModel.getImageIDs());
        args.putInt(BACKGROUND_COLOR_ID, pictureSlideshowModel.getBackgroundColor());
        args.putInt(NEXT_FRAGMENT_BACKGROUND_COLOR_ID, pictureSlideshowModel.getNextFragmentBackroundColor());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageIds = getArguments().getIntArray(IMAGE_IDS_KEY);
            backgroundColor  = getArguments().getInt(BACKGROUND_COLOR_ID);
            nextFragmentBackgroundColor =  getArguments().getInt(NEXT_FRAGMENT_BACKGROUND_COLOR_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.pager, container, false);
        ViewPager pager=(ViewPager)view.findViewById(R.id.pager);
        PagerAdapter adapter = new InfinitePagerAdapter(buildAdapter());
        pager.setAdapter(adapter);
        ViewAppearAnimation.runAnimation(pager, 3000);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingButton);
        mRevealView = view.findViewById(R.id.reveal_view);
        mRevealView.setBackgroundColor(nextFragmentBackgroundColor);
        mRevealLayout = (RevealLayout) view.findViewById(R.id.reveal_layout);

        frameLayout = (FrameLayout) view.findViewById(R.id.PictureFragment_FrameLayout);
        frameLayout.setBackgroundColor(backgroundColor);

        FragmentTransaction transaction = FragmentTransactionProcessingService.prepareNextFragmentTransaction(getFragmentManager().beginTransaction());

        FloatingActionButtonTransitionAnimation floatingActionButtonAnimationOnClickListener = new FloatingActionButtonTransitionAnimation(floatingActionButton,mRevealView, mRevealLayout, transaction);
        floatingActionButton.setOnClickListener(floatingActionButtonAnimationOnClickListener);

        return(view);
    }
    private PagerAdapter buildAdapter() {
        return(new SampleAdapter(getActivity(), getChildFragmentManager(), imageIds));
    }

    @Override
    public void onResume() {
        super.onResume();

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
