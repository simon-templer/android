package ch.templer.model;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import java.io.Serializable;

import ch.templer.activities.R;

/**
 * Created by Templer on 4/30/2016.
 */
public class ColorTheme implements Serializable{
    public enum ColorThemeType {LIGHTGREEN, TURQOUISE, DEEPPURPLE}

    private int ThirdaryTextColor;
    private int ThirdaryColor;
    private int MainTextColor;
    private int MainColor;
    private int FragmentBackgroundColor;
    private int SecondaryColor;
    private int SecondaryTextColor;

    private static int WHITE = Color.argb(255, 255, 255, 255);

    private static int PALETTE_TURQOUISE_FRAGMENT_BACKGROUND_COLOR = WHITE;
    private static int PALETTE_TURQOUISE_THIRDARY_COLOR = Color.argb(35, 39,125,140);
    private static int PALETTE_TURQOUISE_THIRDARY_TEXT_COLOR = Color.argb(255, 81, 138, 148);
    private static int PALETTE_TURQOUISE_MAIN_COLOR = Color.argb(255, 39, 125, 140);
    private static int PALETTE_TURQOUISE_MAIN_TEXT_COLOR = WHITE;
    private static int PALETTE_TURQOUISE_SECONDARY_COLOR = Color.argb(255, 0, 93, 111);
    private static int PALETTE_TURQOUISE_SECONDARY_TEXT_COLOR = WHITE;

    private String alerDialogStyle;

    private ColorTheme(){
    }

    public static ColorTheme constructColorTheme(ColorThemeType colorThemeType, Context context){
        ColorTheme colorTheme = new ColorTheme();
        switch (colorThemeType) {
            case LIGHTGREEN:
                colorTheme.ThirdaryTextColor = ContextCompat.getColor(context, R.color.PALETTE_LIGHTGREEN_THIRDARY_TEXT_COLOR);
                colorTheme.ThirdaryColor = ContextCompat.getColor(context, R.color.PALETTE_LIGHTGREEN_THIRDARY_COLOR);
                colorTheme.MainTextColor = ContextCompat.getColor(context, R.color.PALETTE_LIGHTGREEN_MAIN_TEXT_COLOR);
                colorTheme.MainColor = ContextCompat.getColor(context, R.color.PALETTE_LIGHTGREEN_MAIN_COLOR);
                colorTheme.FragmentBackgroundColor = ContextCompat.getColor(context,R.color.PALETTE_LIGHTGREEN_FRAGMENT_BACKGROUND_COLOR);
                colorTheme.SecondaryColor = ContextCompat.getColor(context, R.color.PALETTE_LIGHTGREEN_SECONDARY_COLOR);
                colorTheme.SecondaryTextColor = ContextCompat.getColor(context, R.color.PALETTE_LIGHTGREEN_SECONDARY_TEXT_COLOR);
                colorTheme.alerDialogStyle="alert_dialog_color_theme_lightgreen";
                break;
            case TURQOUISE:
                colorTheme.ThirdaryTextColor = ContextCompat.getColor(context, R.color.PALETTE_TURQOUISE_THIRDARY_TEXT_COLOR);
                colorTheme.ThirdaryColor = ContextCompat.getColor(context, R.color.PALETTE_TURQOUISE_THIRDARY_COLOR);
                colorTheme.MainTextColor = ContextCompat.getColor(context, R.color.PALETTE_TURQOUISE_MAIN_TEXT_COLOR);
                colorTheme.MainColor = ContextCompat.getColor(context, R.color.PALETTE_TURQOUISE_MAIN_COLOR);
                colorTheme.FragmentBackgroundColor = ContextCompat.getColor(context,R.color.PALETTE_TURQOUISE_FRAGMENT_BACKGROUND_COLOR);
                colorTheme.SecondaryColor = ContextCompat.getColor(context, R.color.PALETTE_TURQOUISE_SECONDARY_COLOR);
                colorTheme.SecondaryTextColor = ContextCompat.getColor(context, R.color.PALETTE_TURQOUISE_SECONDARY_TEXT_COLOR);
                colorTheme.alerDialogStyle="alert_dialog_color_theme_turquise";
                break;
            case DEEPPURPLE:
                colorTheme.ThirdaryTextColor = ContextCompat.getColor(context, R.color.PALETTE_DEEPPURPLE_THIRDARY_TEXT_COLOR);
                colorTheme.ThirdaryColor = ContextCompat.getColor(context, R.color.PALETTE_DEEPPURPLE_THIRDARY_COLOR);
                colorTheme.MainTextColor = ContextCompat.getColor(context, R.color.PALETTE_DEEPPURPLE_MAIN_TEXT_COLOR);
                colorTheme.MainColor = ContextCompat.getColor(context, R.color.PALETTE_DEEPPURPLE_MAIN_COLOR);
                colorTheme.FragmentBackgroundColor = ContextCompat.getColor(context,R.color.PALETTE_DEEPPURPLE_FRAGMENT_BACKGROUND_COLOR);
                colorTheme.SecondaryColor = ContextCompat.getColor(context, R.color.PALETTE_DEEPPURPLE_SECONDARY_COLOR);
                colorTheme.SecondaryTextColor = ContextCompat.getColor(context, R.color.PALETTE_DEEPPURPLE_SECONDARY_TEXT_COLOR);
                colorTheme.alerDialogStyle="alert_dialog_color_theme_deeppurple";
                break;
        }

        return colorTheme;
    }

    public int getThirdaryTextColor() {
        return ThirdaryTextColor;
    }

    public int getThirdaryColor() {
        return ThirdaryColor;
    }

    public int getMainTextColor() {
        return MainTextColor;
    }

    public int getMainColor() {
        return MainColor;
    }

    public int getFragmentBackgroundColor() {
        return FragmentBackgroundColor;
    }

    public int getSecondaryColor() {
        return SecondaryColor;
    }

    public String getAlerDialogStyle() {
        return alerDialogStyle;
    }

    public int getSecondaryTextColor() {
        return SecondaryTextColor;
    }
}
