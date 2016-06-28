package ch.templer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import ch.templer.activities.scenarioselectionactivity.ScenarioSelectionActivity;
import ch.templer.fragments.service.ResourceQueryService;
import ch.templer.model.Scenario;
import ch.templer.services.DataDeserializationService;
import ch.templer.services.SettingsService;
import ch.templer.utils.logging.Logger;

public class SplashScreenActivity extends AppCompatActivity {
    public static String SCENARIOS_EXTRA_ID = "SCENARIOS_EXTRA_ID";
    public static String START_SCENARIO_DIRECTLY_EXTRA_PARAMETER_ID = "START_SCENARIO_DIRECTLY_ID_PARAMETER";
    public static String START_SCENARIO_DIRECTLY_BOOLEAN_FLAG = "START_SCENARIO_DIRECTLY_BOOLEAN_FLAG";
    private static Logger log = Logger.getLogger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialization of static services.
        // Is done only once.
        SettingsService.init(this);
        ResourceQueryService.init(this);

        Bundle bundel = getIntent().getExtras();
        long id = 0;
        boolean isIdSet = false;
        if (bundel != null) {
            id = bundel.getLong(START_SCENARIO_DIRECTLY_EXTRA_PARAMETER_ID);
            isIdSet = bundel.getBoolean(START_SCENARIO_DIRECTLY_BOOLEAN_FLAG);
            log.d("---------------> savedInstanceState found. position: " + id + " idisset: " + isIdSet);
        }
        DeserializeJsonDataAsync yourTask = new DeserializeJsonDataAsync(new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(DeserializeJsonDataAsyncParameters deserializeJsonDataAsyncParameters, ArrayList<Scenario> scenarios) {
                if (deserializeJsonDataAsyncParameters.isIdSet()) {
                    Scenario scenario = getScenarioById(scenarios, deserializeJsonDataAsyncParameters.getId());
                    if (scenario != null) {
                        if (!(SettingsService.getInstance().getLongSetting(SettingsService.PREV_SCENARIO_ID,-1l) == scenario.getId())){
                            SettingsService.getInstance().setLongSettting(SettingsService.PREV_SCENARIO_ID, scenario.getId());
                            SettingsService.getInstance().setIntegerSettting(SettingsService.PREV_SCENARIO_POSITION, 0);
                            SettingsService.getInstance().setBolleanSettting(SettingsService.PREV_SCENARIO_NOT_FINISHED, true);
                        }
                        startFragmentContainerActivity(deserializeJsonDataAsyncParameters.getContext(), scenario);
                    } else {
                        startMenuActivity(deserializeJsonDataAsyncParameters.getContext(), scenarios);
                    }
                } else {
                    startMenuActivity(deserializeJsonDataAsyncParameters.getContext(), scenarios);
                }
            }
        }, new DeserializeJsonDataAsyncParameters(this, id, isIdSet));
        yourTask.execute();
    }

    private void startFragmentContainerActivity(Context context, Scenario scenario) {
        Intent intent = new Intent(context, FragmentContainerActivity.class);
        intent.putExtra(ScenarioSelectionActivity.SCENARIO_EXTRA_ID, scenario);
        startActivity(intent);
        finish();
    }

    private void startMenuActivity(Context context, ArrayList<Scenario> scenarios) {
        Intent intent = new Intent(context, MenuActivity.class);
        intent.putExtra(SCENARIOS_EXTRA_ID, scenarios);
        startActivity(intent);
        finish();
    }

    private Scenario getScenarioById(ArrayList<Scenario> scenarios, long id) {
        for (Scenario scenario : scenarios) {
            if (scenario.getId() == id) {
                return scenario;
            }
        }
        return null;
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(DeserializeJsonDataAsyncParameters deserializeJsonDataAsyncParameters, ArrayList<Scenario> scenarios);
    }

    public class DeserializeJsonDataAsync extends AsyncTask<Object, Object, ArrayList<Scenario>> { //change Object to required type
        private OnTaskCompleted listener;
        private DeserializeJsonDataAsyncParameters deserializeJsonDataAsyncParameters;

        public DeserializeJsonDataAsync(OnTaskCompleted listener, DeserializeJsonDataAsyncParameters deserializeJsonDataAsyncParameters) {
            this.deserializeJsonDataAsyncParameters = deserializeJsonDataAsyncParameters;
            this.listener = listener;
        }

        @Override
        protected ArrayList<Scenario> doInBackground(Object... params) {
            return DataDeserializationService.deserializeData(deserializeJsonDataAsyncParameters.getContext(), R.raw.testdata1);
        }

        protected void onPostExecute(ArrayList<Scenario> scenarios) {
            // your stuff
            listener.onTaskCompleted(deserializeJsonDataAsyncParameters, scenarios);
        }
    }

    private class DeserializeJsonDataAsyncParameters {
        private Context context;
        private long id;
        private boolean isIdSet;

        public DeserializeJsonDataAsyncParameters(Context context) {
            this.context = context;
            this.id = -1;
            this.isIdSet = false;
        }

        public DeserializeJsonDataAsyncParameters(Context context, long id, boolean isIdSet) {
            this.context = context;
            this.id = id;
            this.isIdSet = isIdSet;
        }

        public Context getContext() {
            return context;
        }


        public long getId() {
            return id;
        }

        public boolean isIdSet() {
            return isIdSet;
        }
    }
}
