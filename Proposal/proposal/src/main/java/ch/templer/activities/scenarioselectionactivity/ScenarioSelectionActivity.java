package ch.templer.activities.scenarioselectionactivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ch.templer.activities.FragmentContainerActivity;
import ch.templer.activities.MenuActivity;
import ch.templer.activities.R;
import ch.templer.activities.SplashScreenActivity;
import ch.templer.fragments.service.SnackbarService;
import ch.templer.model.Scenario;
import ch.templer.services.SettingsService;

public class ScenarioSelectionActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private List<Scenario> scenarios;
    public static String SCENARIO_EXTRA_ID = "SCENARIO_EXTRA_ID";
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_selection);
        Bundle bundle = getIntent().getExtras();
        if (bundle.get(MenuActivity.SCENARIO_EXTRA_ID) != null) {
            scenarios = (ArrayList<Scenario>) bundle.get(MenuActivity.SCENARIO_EXTRA_ID);
        }

        setListAdapter(new ScenarioArrayAdapter(this, scenarios));
        ListView listView = getListView();
        listView.setSelector(R.drawable.list_item_selector);
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(this);

        registerForContextMenu(listView);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // if (v.getId()== R.id.scenarios) {
        menu.setHeaderTitle("Content menu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_scenario_list_view, menu);
        //   }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.create_shortcut:
                createShortcut(info.position);
                return true;
            case R.id.open_scenario:
                openScenario(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void createShortcut(int position) {
        Scenario scenario = scenarios.get(position);

        Intent shortcutIntent = new Intent(getApplicationContext(),
                SplashScreenActivity.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        shortcutIntent.putExtra(SplashScreenActivity.START_SCENARIO_DIRECTLY_EXTRA_PARAMETER_ID, scenario.getId());
        shortcutIntent.putExtra(SplashScreenActivity.START_SCENARIO_DIRECTLY_BOOLEAN_FLAG, true);

        Intent addIntent = new Intent();
        addIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, scenario.getScenarioTitle());
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                        scenarios.get(position).getIconId()));

        addIntent
                .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
        SnackbarService.showSnackbar(coordinatorLayout, getString(R.string.snackbar_shortcut_created), R.color.TRANSITION_COLOR_EMERALD, Snackbar.LENGTH_LONG);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        openScenario(position);
    }

    private void openScenario(int position) {
        SettingsService.getInstance().setLongSettting(SettingsService.PREV_SCENARIO_ID, scenarios.get(position).getId());
        SettingsService.getInstance().setIntegerSettting(SettingsService.PREV_SCENARIO_POSITION, 0);
        SettingsService.getInstance().setBolleanSettting(SettingsService.PREV_SCENARIO_NOT_FINISHED, true);
        Intent intent = new Intent(this, FragmentContainerActivity.class);
        intent.putExtra(SCENARIO_EXTRA_ID, scenarios.get(position));
        startActivity(intent);
    }
}
