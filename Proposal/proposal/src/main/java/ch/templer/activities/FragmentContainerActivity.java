package ch.templer.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import ch.templer.fragments.PictureSlideshowFragment;
import ch.templer.fragments.VideoFragment;

public class FragmentContainerActivity extends FragmentActivity implements PictureSlideshowFragment.OnFragmentInteractionListener, VideoFragment.OnFragmentInteractionListener, AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            PictureSlideshowFragment firstFragment = new PictureSlideshowFragment();
            firstFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.combobox_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
//        if (findViewById(R.id.fragment_container) != null) {
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
//            switch (position) {
//                case 0:
//                    PictureSlideshowFragment newFragment = new PictureSlideshowFragment();
//                    transaction.replace(R.id.fragment_container, newFragment);
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
//
//                    break;
//                case 3:
//
//                    break;
//                default:
//                    break;
//            }
//        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
