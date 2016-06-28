package ch.templer.fragments.pictureslideshowfragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import ch.templer.activities.R;
import ch.templer.activities.settingsactivity.SettingsActivity;
import ch.templer.animation.ViewAppearAnimation;
import ch.templer.controls.reveallayout.RevealLayout;
import ch.templer.controls.viewpagger.PhotoViewPagerAdapter;
import ch.templer.fragments.AbstractFragment;
import ch.templer.model.PictureSlideshowModel;
import ch.templer.services.SettingsService;
import ch.templer.viewpagger.SampleAdapter;

public class PictureSlideshowFragment extends AbstractFragment {
    private static final String PICTURE_SLIDESHOW_MODEL_ID = "PICTURE_SLIDESHOW_MODEL_ID";

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

        ViewPager pager = (ViewPager) view.findViewById(R.id.view_pager);
        PhotoViewPagerAdapter photoViewPagerAdapter = new PhotoViewPagerAdapter(pictureSlideshowModel.getImageIDs());
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == pictureSlideshowModel.getImageIDs().length -1){
                    endReached();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pager.setAdapter(photoViewPagerAdapter);

        ViewAppearAnimation.runAnimation(pager, 1000);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingButton);
        floatingActionButton.setVisibility(View.INVISIBLE);
        mRevealView = view.findViewById(R.id.reveal_view);
        mRevealView.setBackgroundColor(pictureSlideshowModel.getFragmentColors().getNextFragmentBackroundColor());
        mRevealLayout = (RevealLayout) view.findViewById(R.id.reveal_layout);

        frameLayout = (FrameLayout) view.findViewById(R.id.PictureFragment_FrameLayout);
        frameLayout.setBackgroundColor(pictureSlideshowModel.getFragmentColors().getFragmentBackgroundColor());

        if (SettingsService.getInstance().getBooleanSetting(SettingsActivity.GENERAL_DEBUGGING_SWITCH, false)){
            endReached();
        }

        return (view);
    }

    private void endReached() {
        ViewAppearAnimation.runAnimation(floatingActionButton, 1000);
        fragmentFinished(floatingActionButton, mRevealView, mRevealLayout);
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
