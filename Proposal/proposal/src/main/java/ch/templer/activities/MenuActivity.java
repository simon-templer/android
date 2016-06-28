package ch.templer.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ch.templer.activities.scenarioselectionactivity.ScenarioSelectionActivity;
import ch.templer.activities.settingsactivity.SettingsActivity;
import ch.templer.model.Scenario;
import ch.templer.services.SettingsService;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MenuActivity extends AppCompatActivity {
    public static String SCENARIO_EXTRA_ID = "SCENARIO_EXTRA_ID";
    private ArrayList<Scenario> scenarios;
    private CoordinatorLayout coordinatorLayout;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        getSupportActionBar().hide();
        Bundle bundle = getIntent().getExtras();
        if (bundle.get(SplashScreenActivity.SCENARIOS_EXTRA_ID) != null) {
            scenarios = (ArrayList<Scenario>) bundle.get(SplashScreenActivity.SCENARIOS_EXTRA_ID);
        }

        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(v.getContext(), R.anim.image_click));
                Intent intent = new Intent(v.getContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        ImageButton selectScenarioButton = (ImageButton) findViewById(R.id.selectScenarioButton);
        selectScenarioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(v.getContext(), R.anim.image_click));
                Intent intent = new Intent(v.getContext(), ScenarioSelectionActivity.class);
                intent.putExtra(ScenarioSelectionActivity.SCENARIO_EXTRA_ID, scenarios);
                startActivity(intent);
            }
        });
        ImageButton newScenarioButton = (ImageButton) findViewById(R.id.newScenarioButton);
        newScenarioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(v.getContext(), R.anim.image_click));
                showSnackbar(R.color.TRANSITION_COLOR_EMERALD, "This feature is comming soon....", Snackbar.LENGTH_LONG);
            }
        });

        if (SettingsService.getInstance().getBooleanSetting(SettingsService.PREV_SCENARIO_NOT_FINISHED, false)) {

            coordinatorLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showSnackbar(R.color.TRANSITION_COLOR_EMERALD, getString(R.string.snackbar_old_scenario_found_message), Snackbar.LENGTH_INDEFINITE);
                }
            }, 300);
        }
    }

    public void showSnackbar(int color, String message, int length) {
        snackbar = Snackbar.make(coordinatorLayout, message, length);
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(5);
        snackbarView.setBackgroundColor(color);
        snackbar.setActionTextColor(Color.WHITE);

        snackbar.setAction(getString(R.string.snackbar_continue_button), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long scenarioId = SettingsService.getInstance().getLongSetting(SettingsService.PREV_SCENARIO_ID, -1l);
                Scenario scenario = getScenarioById(scenarioId);
                Intent intent = new Intent(view.getContext(), FragmentContainerActivity.class);
                intent.putExtra(ScenarioSelectionActivity.SCENARIO_EXTRA_ID, scenario);
                startActivity(intent);
            }
        });
        snackbar.show();
    }

    private Scenario getScenarioById(long id) {
        for (Scenario scenario : scenarios) {
            if (scenario.getId() == id) {
                return scenario;
            }
        }
        return null;
    }
}
