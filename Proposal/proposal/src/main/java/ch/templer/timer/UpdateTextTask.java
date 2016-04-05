package ch.templer.timer;

import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.TimerTask;

/**
 * Created by Templer on 4/5/2016.
 */
public class UpdateTextTask extends TimerTask {

    private TextView textView;

    public UpdateTextTask(TextView textView){
        this.textView = textView;
    }
    @Override
    public void run() {

    }
}
