package ch.templer.model;

import android.graphics.Color;

import java.io.Serializable;

/**
 * Created by Templer on 4/30/2016.
 */
public class ColorTheme implements Serializable{
    public enum ColorThemeType {BLUE, TURQOUISE, YELLOW}

    private int TextViewTextColor;
    private int TextViewBackgroundColor;
    private int ButtonTextColor;
    private int ButtonBackgroundColor;
    private int ViewBackgroundColor;
    private int ToolBarBackgroundColor;

    private static int WHITE = Color.argb(255, 255, 255, 255);

    private static int PALETTE_TURQOUISE_VIEW_BACKGROUND_COLOR = WHITE;
    private static int PALETTE_TURQOUISE_TEXT_VIEW_BACKGROUND_COLOR = Color.argb(35, 39,125,140);
    private static int PALETTE_TURQOUISE_TEXT_VIEW_TEXT_COLOR = Color.argb(255, 81, 138, 148);
    private static int PALETTE_TURQOUISE_BUTTON_BACKGROUND_COLOR = Color.argb(255, 39, 125, 140);
    private static int PALETTE_TURQOUISE_BUTTON_TEXT_COLOR = WHITE;
    private static int PALETTE_TURQOUISE_TOOL_BAR = Color.argb(255, 0, 93, 111);

    private static int PALETTE_TEST_MEDUIUM_DARK = Color.argb(255, 0, 157, 145);
    private static int PALETTE_TEST_LIGHT = Color.argb(255, 51, 206, 195);
    private static int PALETTE_TEST_MEDUIUM_LIGHT = Color.argb(255, 93, 206, 198);
    private static int PALETTE_TEST_DARK = Color.argb(255, 0, 102, 94);
    
    private ColorTheme(){

    }

    public static ColorTheme constructColorTheme(ColorThemeType colorThemeType){
        ColorTheme colorTheme = new ColorTheme();
        switch (colorThemeType) {
            case BLUE:
                colorTheme.TextViewTextColor=PALETTE_TURQOUISE_TEXT_VIEW_TEXT_COLOR;
                colorTheme.TextViewBackgroundColor=PALETTE_TURQOUISE_TEXT_VIEW_BACKGROUND_COLOR;
                colorTheme.ButtonTextColor=PALETTE_TURQOUISE_BUTTON_TEXT_COLOR;
                colorTheme.ButtonBackgroundColor=PALETTE_TURQOUISE_BUTTON_BACKGROUND_COLOR;
                colorTheme.ViewBackgroundColor=PALETTE_TURQOUISE_VIEW_BACKGROUND_COLOR;
                colorTheme.ToolBarBackgroundColor = PALETTE_TURQOUISE_TOOL_BAR;
                break;
            case TURQOUISE:
                colorTheme.TextViewTextColor=PALETTE_TURQOUISE_TEXT_VIEW_TEXT_COLOR;
                colorTheme.TextViewBackgroundColor=PALETTE_TURQOUISE_TEXT_VIEW_BACKGROUND_COLOR;
                colorTheme.ButtonTextColor=PALETTE_TURQOUISE_BUTTON_TEXT_COLOR;
                colorTheme.ButtonBackgroundColor=PALETTE_TURQOUISE_BUTTON_BACKGROUND_COLOR;
                colorTheme.ViewBackgroundColor=PALETTE_TURQOUISE_VIEW_BACKGROUND_COLOR;
                colorTheme.ToolBarBackgroundColor = PALETTE_TURQOUISE_TOOL_BAR;
                break;
            case YELLOW:
                colorTheme.TextViewTextColor=PALETTE_TURQOUISE_TEXT_VIEW_TEXT_COLOR;
                colorTheme.TextViewBackgroundColor=PALETTE_TURQOUISE_TEXT_VIEW_BACKGROUND_COLOR;
                colorTheme.ButtonTextColor=PALETTE_TURQOUISE_BUTTON_TEXT_COLOR;
                colorTheme.ButtonBackgroundColor=PALETTE_TURQOUISE_BUTTON_BACKGROUND_COLOR;
                colorTheme.ViewBackgroundColor=PALETTE_TURQOUISE_VIEW_BACKGROUND_COLOR;
                colorTheme.ToolBarBackgroundColor = PALETTE_TURQOUISE_TOOL_BAR;
                break;
        }

        return colorTheme;
    }

    public int getTextViewTextColor() {
        return TextViewTextColor;
    }

    public int getTextViewBackgroundColor() {
        return TextViewBackgroundColor;
    }

    public int getButtonTextColor() {
        return ButtonTextColor;
    }

    public int getButtonBackgroundColor() {
        return ButtonBackgroundColor;
    }

    public int getViewBackgroundColor() {
        return ViewBackgroundColor;
    }

    public int getToolBarBackgroundColor() {
        return ToolBarBackgroundColor;
    }
}
