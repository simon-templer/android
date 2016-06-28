package ch.templer.fragments.finishfragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import ch.templer.activities.FragmentContainerActivity;
import ch.templer.activities.R;
import ch.templer.animation.ViewAppearAnimation;
import ch.templer.fragments.AbstractFragment;
import ch.templer.services.SettingsService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FinishedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FinishedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FinishedFragment extends AbstractFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RelativeLayout rootRelativeLayout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FinishedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FinishedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FinishedFragment newInstance(String param1, String param2) {
        FinishedFragment fragment = new FinishedFragment();
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
        View view = inflater.inflate(R.layout.fragment_finished, container, false);
        rootRelativeLayout = (RelativeLayout) view.findViewById(R.id.finished_fragment_content);
        rootRelativeLayout.setVisibility(View.INVISIBLE);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.back_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(v.getContext(), R.anim.image_click));
                SettingsService.getInstance().setLongSettting(SettingsService.PREV_SCENARIO_ID, -1l);
                SettingsService.getInstance().setIntegerSettting(SettingsService.PREV_SCENARIO_POSITION, 0);
                SettingsService.getInstance().setBolleanSettting(SettingsService.PREV_SCENARIO_NOT_FINISHED, false);
                ((FragmentContainerActivity) getActivity()).goBack();
            }
        });

//        rootLinearLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ViewAppearAnimation.runAnimation(rootLinearLayout,1000);
//            }
//        },500);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFragmentFirstStarted()) {
            ViewAppearAnimation.runAnimation(rootRelativeLayout, 1000);
        } else {
            rootRelativeLayout.setVisibility(View.VISIBLE);
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
     * to the activity and potentially other fragments contained in that
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
