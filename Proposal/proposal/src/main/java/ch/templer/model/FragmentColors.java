package ch.templer.model;

import java.io.Serializable;

import ch.templer.model.ColorTheme;

/**
 * Created by Templer on 4/30/2016.
 */
public class FragmentColors implements Serializable{
    private int nextFragmentBackroundColor;
    private ColorTheme colorTheme;

    public int getNextFragmentBackroundColor() {
        return nextFragmentBackroundColor;
    }

    public void setNextFragmentBackroundColor(int nextFragmentBackroundColor) {
        this.nextFragmentBackroundColor = nextFragmentBackroundColor;
    }

    public ColorTheme getColorTheme() {
        return colorTheme;
    }

    public void setColorTheme(ColorTheme colorTheme) {
        this.colorTheme = colorTheme;
    }
}
