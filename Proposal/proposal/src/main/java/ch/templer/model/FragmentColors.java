package ch.templer.model;

import android.content.res.Resources;

import java.io.Serializable;

import ch.templer.activities.R;
import ch.templer.fragments.service.ResourceQueryService;

/**
 * Created by Templer on 4/30/2016.
 */


public class FragmentColors implements Serializable {
    private int nextFragmentBackroundColor=R.color.default_fallback_color;
    private int fragmentBackgroundColor=R.color.default_fallback_color;
    private ColorTheme.ColorThemeType colorThemeType;

    public int getFragmentBackgroundColor() {
        return fragmentBackgroundColor;
    }

    public void setFragmentBackgroundColor(String fragmentBackgroundColor) {
        try {
            this.fragmentBackgroundColor = ResourceQueryService.getColorByName(fragmentBackgroundColor);
        } catch (Resources.NotFoundException e) {
            this.fragmentBackgroundColor = R.color.default_fallback_color;
        }
    }

    public int getNextFragmentBackroundColor() {
        return nextFragmentBackroundColor;
    }

    public void setNextFragmentBackroundColor(String nextFragmentBackroundColor) {
        try {
            this.nextFragmentBackroundColor = ResourceQueryService.getColorByName(nextFragmentBackroundColor);
        } catch (Resources.NotFoundException e) {
            this.nextFragmentBackroundColor = R.color.default_fallback_color;
        }
    }

    public ColorTheme.ColorThemeType getColorThemeType() {
        return colorThemeType;
    }

    public void setColorThemeType(ColorTheme.ColorThemeType colorThemeType) {
        this.colorThemeType = colorThemeType;
    }
}
