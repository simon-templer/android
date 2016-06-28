package ch.templer.activities.scenarioselectionactivity;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.templer.activities.R;
import ch.templer.fragments.service.ResourceQueryService;
import ch.templer.model.Scenario;

/**
 * Created by Templer on 6/22/2016.
 */
public class ScenarioArrayAdapter extends ArrayAdapter<Scenario> {
    private final Context context;
    private final List<Scenario> values;

    public ScenarioArrayAdapter(Context context, List<Scenario> values) {
        super(context, R.layout.list_view_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_view_item, parent, false);
        TextView title = (TextView) rowView.findViewById(R.id.titel);
        TextView secondTitle = (TextView) rowView.findViewById(R.id.second_title);
        TextView description = (TextView) rowView.findViewById(R.id.description);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        title.setText(values.get(position).getScenarioTitle());
        secondTitle.setText(values.get(position).getSecondaryTitle());
        description.setText(values.get(position).getDescription());
        imageView.setImageResource(values.get(position).getIconId());

        return rowView;
    }
}
