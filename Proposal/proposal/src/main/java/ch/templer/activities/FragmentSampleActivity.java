package ch.templer.activities;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.List;

import ch.FragmentFinishedListener;
import ch.templer.animation.RevealLayout;
import ch.templer.data.TestData;
import ch.templer.fragments.MapLocationFragment;
import ch.templer.fragments.MultipleChoiceFragment;
import ch.templer.fragments.PictureSlideshowFragment;
import ch.templer.fragments.TextMessagesFragment;
import ch.templer.fragments.VideoFragment;
import ch.templer.model.MapLocationModel;
import ch.templer.model.Message;
import ch.templer.model.MultipleChoiceModel;
import ch.templer.model.PictureSlideshowModel;
import ch.templer.model.TextMessagesModel;
import ch.templer.model.VideoModel;

/**
 * Created by yugy on 14/11/21.
 */
public class FragmentSampleActivity extends AppCompatActivity implements PictureSlideshowFragment.OnFragmentInteractionListener, VideoFragment.OnFragmentInteractionListener, TextMessagesFragment.OnFragmentInteractionListener, MultipleChoiceFragment.OnFragmentInteractionListener, MapLocationFragment.OnFragmentInteractionListener, View.OnClickListener, OnMapReadyCallback, FragmentFinishedListener {

    private boolean mIsInBackAnimation = false;
    private List<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        messages = TestData.getInstance().getMessages();
        Message message = messages.get(0);
        messages.remove(0);
        processMessage(message);
    }

    private void processMessage(Message message) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

        if (message instanceof PictureSlideshowModel) {
            PictureSlideshowFragment pictureSlideshowFragment = PictureSlideshowFragment.newInstance((PictureSlideshowModel) message);
            transaction.replace(R.id.container, pictureSlideshowFragment);
        } else if (message instanceof TextMessagesModel) {
            TextMessagesFragment textMessage = TextMessagesFragment.newInstance((TextMessagesModel) message);
            //textMessage.setFragmentFinishedListener(this);
            transaction.replace(R.id.container, textMessage);
        } else if (message instanceof VideoModel) {
            VideoFragment newFragment1 = new VideoFragment();
            Bundle args = new Bundle();
            args.putInt(VideoFragment.audioID, R.raw.adelle_hello);
            newFragment1.setArguments(args);
            transaction.replace(R.id.container, newFragment1);
        } else if (message instanceof MultipleChoiceModel) {
            MultipleChoiceFragment multipleChoiceFragment = MultipleChoiceFragment.newInstance((MultipleChoiceModel) message);
            transaction.replace(R.id.container, multipleChoiceFragment);
        }else if(message instanceof MapLocationModel){
            MapLocationFragment fragment = MapLocationFragment.newInstance((MapLocationModel)message);
            transaction.replace(R.id.container, fragment);
        }
        transaction.commit();
    }

    @Override
    public void onFragmentFinished() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    public static class SimpleFragment extends Fragment {

        private static final int[] COLOR_LIST = new int[]{
            0xff33b5e5,
            0xff99cc00,
            0xffff8800,
            0xffaa66cc,
            0xffff4444,
        };

        private RevealLayout mRevealLayout;
        private TextView mTextView;
        private int mIndex;

        public static SimpleFragment newInstance(int index) {
            SimpleFragment fragment = new SimpleFragment();
            Bundle args = new Bundle();
            args.putInt("index", index);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_simple, container, false);
            mRevealLayout = (RevealLayout) rootView.findViewById(R.id.reveal_layout);
            mTextView = (TextView) rootView.findViewById(R.id.text);
            mIndex = getArguments().getInt("index");
            mTextView.setBackgroundColor(COLOR_LIST[mIndex % 5]);
            mTextView.setText("Fragment " + mIndex);
            mRevealLayout.setContentShown(false);
            mRevealLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mRevealLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        //noinspection deprecation
                        mRevealLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    mRevealLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mRevealLayout.show();
                        }
                    }, 50);
                }
            });
            mRevealLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().beginTransaction()
                            .addToBackStack(null)
                            .add(R.id.container, SimpleFragment.newInstance(mIndex + 1))
                            .commit();
                }
            });
            return rootView;
        }

        public void onBackPressed(Animation.AnimationListener listener) {
            mRevealLayout.hide(listener);
        }
    }

    @Override
    public void onBackPressed() {
        if (mIsInBackAnimation) return;
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        //fragments.size() is not correct.
        final int fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments != null && fragmentCount > 0) {
            Fragment lastFragment = fragments.get(fragmentCount);
            if (lastFragment != null && lastFragment instanceof SimpleFragment) {
                ((SimpleFragment) lastFragment).onBackPressed(new Animation.AnimationListener() {
                    @Override public void onAnimationRepeat(Animation animation) {}

                    @Override
                    public void onAnimationStart(Animation animation) {
                        mIsInBackAnimation = true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        getSupportFragmentManager().popBackStackImmediate();
                        mIsInBackAnimation = false;
                    }
                });
                return;
            }
        }
        super.onBackPressed();
    }
}
