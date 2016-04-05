package ch.templer.activities;

import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.List;

import ch.templer.data.TestData;
import ch.templer.fragments.PictureSlideshowFragment;
import ch.templer.fragments.TextMessagesFragment;
import ch.templer.fragments.VideoFragment;
import ch.templer.model.Message;
import ch.templer.model.PictureSlideshowModel;
import ch.templer.model.TextMessagesModel;
import ch.templer.model.VideoModel;

public class FragmentContainerActivity extends FragmentActivity implements PictureSlideshowFragment.OnFragmentInteractionListener, VideoFragment.OnFragmentInteractionListener, TextMessagesFragment.OnFragmentInteractionListener, View.OnClickListener {

    private List<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_fragment_container);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            PictureSlideshowFragment firstFragment = new PictureSlideshowFragment();
            firstFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }

        Button nextFragmentButton = (Button) findViewById(R.id.next_fragment_button);
        nextFragmentButton.setOnClickListener(this);

        messages = TestData.getInstance().getMessages();
        Message message = messages.get(0);
        messages.remove(0);
        processMessage(message);
    }

    private void processMessage(Message message) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

        if (message instanceof PictureSlideshowModel) {
            PictureSlideshowFragment pictureSlideshowFragment = PictureSlideshowFragment.newInstance((PictureSlideshowModel) message);
            transaction.replace(R.id.fragment_container, pictureSlideshowFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (message instanceof TextMessagesModel) {
            TextMessagesFragment textMessage = TextMessagesFragment.newInstance((TextMessagesModel) message);
            transaction.replace(R.id.fragment_container, textMessage);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (message instanceof VideoModel) {
            VideoFragment newFragment1 = new VideoFragment();
            Bundle args = new Bundle();
            args.putInt(VideoFragment.audioID, R.raw.adelle_hello);
            newFragment1.setArguments(args);
            transaction.replace(R.id.fragment_container, newFragment1);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        Message message = this.messages.get(0);
        this.messages.remove(0);

        processMessage(message);
    }

    //  @Override
//   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        // Check that the activity is using the layout version with
//        // the fragment_container FrameLayout
//        if (findViewById(R.id.fragment_container) != null) {
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
//
//            switch (position) {
//                case 0:
//                    PictureSlideshowFragment pictureSlideshowFragment = new PictureSlideshowFragment();
//                    transaction.replace(R.id.fragment_container, pictureSlideshowFragment);
//                    transaction.addToBackStack(null);
//                    transaction.commit();
//                    break;
//                case 1:
//                    VideoFragment newFragment1 = new VideoFragment();
//                    Bundle args = new Bundle();
//                    args.putInt(VideoFragment.audioID, R.raw.adelle_hello);
//                    newFragment1.setArguments(args);
//                    transaction.replace(R.id.fragment_container, newFragment1);
//                    transaction.addToBackStack(null);
//                    transaction.commit();
//                    break;
//                case 2:
//                    TextMessagesFragment textMessage = new TextMessagesFragment();
//                    transaction.replace(R.id.fragment_container, textMessage);
//                    transaction.addToBackStack(null);
//                    transaction.commit();
//                    break;
//                case 3:
//
//                    break;
//                default:
//                    break;
//            }
//        }
//    }

//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
}
