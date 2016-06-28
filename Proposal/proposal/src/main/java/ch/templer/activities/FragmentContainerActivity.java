package ch.templer.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import ch.templer.activities.scenarioselectionactivity.ScenarioSelectionActivity;
import ch.templer.fragments.IFragmentInteractionListener;
import ch.templer.fragments.service.FragmentTransactionProcessingService;
import ch.templer.model.Scenario;
import ch.templer.utils.logging.Logger;

public class FragmentContainerActivity extends FragmentActivity implements IFragmentInteractionListener {

    private Scenario scenario;
    private static int fragmentCounter = 0;
    private static Logger log = Logger.getLogger();

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
        }

        scenario = (Scenario)getIntent().getSerializableExtra(ScenarioSelectionActivity.SCENARIO_EXTRA_ID);

        FragmentTransactionProcessingService.setMessages(scenario.getMessages());
        FragmentTransactionProcessingService.prepareNextFragmentTransaction(getSupportFragmentManager().beginTransaction(), this).commit();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @Override
    public void onBackPressed() {
        //ignored
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void goBack() {
        super.onBackPressed();
    }
}
