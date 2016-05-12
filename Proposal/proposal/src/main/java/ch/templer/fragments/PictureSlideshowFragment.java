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
import ch.templer.animation.ViewAppearAnimation;
import ch.templer.controls.reveallayout.RevealLayout;
import ch.templer.controls.viewpagger.PhotoViewPagerAdapter;
import ch.templer.fragments.service.FragmentTransactionProcessingService;
import ch.templer.model.PictureSlideshowModel;
import ch.templer.viewpagger.SampleAdapter;

public class PictureSlideshowFragment extends AbstractFragment {
    private static final String PICTURE_SLIDESHOW_MODEL_ID = "PICTURE_SLIDESHOW_MODEL_ID";
    private static final String BACKGROUND_COLOR_ID = "BACKGROUND_COLOR_ID";
    private static final String NEXT_FRAGMENT_BACKGROUND_COLOR_ID = "NEXT_FRAGMENT_BACKGROUND_COLOR_ID";

//    private int[] imageIds;
//
//    private int backgroundColor;
//    private int nextFragmentBackgroundColor;

    private PictureSlideshowModel pictureSlideshowModel;

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
        args.putSerializable(PICTURE_SLIDESHOW_MODEL_ID, pictureSlideshowModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pictureSlideshowModel = (PictureSlideshowModel) getArguments().getSerializable(PICTURE_SLIDESHOW_MODEL_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.pager, container, false);

//        ViewPager pager=(ViewPager)view.findViewById(R.id.pager);
//        //PagerAdapter adapter = new InfinitePagerAdapter(buildAdapter());
//        PagerAdapter adapter = new InfinitePagerAdapter(imageIds);
//        pager.setAdapter(adapter);

        ViewPager pager = (ViewPager) view.findViewById(R.id.view_pager);

        pager.setAdapter(new PhotoViewPagerAdapter(pictureSlideshowModel.getImageIDs()));

        ViewAppearAnimation.runAnimation(pager, 3000);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingButton);
        mRevealView = view.findViewById(R.id.reveal_view);
        mRevealView.setBackgroundColor(pictureSlideshowModel.getNextFragmentBackroundColor());
        mRevealLayout = (RevealLayout) view.findViewById(R.id.reveal_layout);

        frameLayout = (FrameLayout) view.findViewById(R.id.PictureFragment_FrameLayout);
        frameLayout.setBackgroundColor(pictureSlideshowModel.getBackgroundColor());

        FragmentTransaction transaction = FragmentTransactionProcessingService.prepareNextFragmentTransaction(getFragmentManager().beginTransaction());

        FloatingActionButtonTransitionAnimation floatingActionButtonAnimationOnClickListener = new FloatingActionButtonTransitionAnimation(floatingActionButton, mRevealView, mRevealLayout, transaction);
        floatingActionButton.setOnClickListener(floatingActionButtonAnimationOnClickListener);

        return (view);
    }

    private PagerAdapter buildAdapter() {
        return (new SampleAdapter(getActivity(), getChildFragmentManager(), pictureSlideshowModel.getImageIDs()));
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
