package ch.templer.services;

import android.content.Context;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ch.templer.activities.R;
import ch.templer.model.Scenario;

/**
 * Created by Templer on 6/22/2016.
 */
public class DataDeserializationService {

    public static ArrayList<Scenario> deserializeData(Context context, int dataResource) {
        ArrayList<Scenario> scenarios = null;

        ObjectMapper mapper = new ObjectMapper();

        String jsonData = readRawTextFile(context, dataResource);
        try {
            scenarios= mapper.readValue(jsonData, new TypeReference<List<Scenario>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scenarios;
    }

    public static String readRawTextFile(Context ctx, int resId) {
        InputStream inputStream = ctx.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while ((line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        return text.toString();
    }
}
