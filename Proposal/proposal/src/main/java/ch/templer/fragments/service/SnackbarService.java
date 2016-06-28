package ch.templer.fragments.service;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import ch.templer.activities.R;

/**
 * Created by Templer on 6/14/2016.
 */
public class SnackbarService {

    private static Snackbar snackbar;

    public static void showSnackbar(Context constetx, CoordinatorLayout coordinatorLayout, int color, String message, int length) {
        showSnackbar(constetx, coordinatorLayout, color, message, length, false);
    }

    public static void showSnackbar(Context context, CoordinatorLayout coordinatorLayout, int color, String message, int length, boolean asAllert) {
        Spanned formatedMessage = Html.fromHtml(message);
        snackbar = Snackbar.make(coordinatorLayout, formatedMessage, length);
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(5);
        if (asAllert) {
            snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.error_red));
            snackbar.setActionTextColor(Color.WHITE);
        } else {
            snackbarView.setBackgroundColor(color);
            snackbar.setActionTextColor(Color.WHITE);
        }

        snackbar.setAction(context.getString(R.string.snackbar_dismiss_button), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public static void dismiss() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    public static void showSnackbar(CoordinatorLayout coordinatorLayout, String message,int color, int lenght) {
        snackbar = Snackbar.make(coordinatorLayout, message, lenght);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(color);
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(4);
        snackbar.show();
    }


    public static Snackbar getSnackbar() {
        return snackbar;
    }
}
