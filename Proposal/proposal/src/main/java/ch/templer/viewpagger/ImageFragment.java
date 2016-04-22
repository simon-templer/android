package ch.templer.viewpagger;

/**
 * Created by Templer on 03.04.2016.
 */
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ch.templer.activities.R;
import ch.templer.utils.BitmapResizeWorkerTask;

public class ImageFragment extends Fragment {
    private static final String KEY_POSITION="position";
    private static final String IMAGE_ID="imageId";
    private ImageView imageView;

    static ImageFragment newInstance(int position, int imageId) {
        ImageFragment frag=new ImageFragment();
        Bundle args=new Bundle();

        args.putInt(KEY_POSITION, position);
        args.putInt(IMAGE_ID, imageId);
        frag.setArguments(args);

        return(frag);
    }

    static String getTitle(Context ctxt, int position) {
        return(String.format(ctxt.getString(R.string.hint), position + 1));
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.editor, container, false);
        imageView=(ImageView)result.findViewById(R.id.imageView);
        int position=getArguments().getInt(KEY_POSITION, -1);
        int imageID = getArguments().getInt(IMAGE_ID);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        BitmapResizeWorkerTask task = new BitmapResizeWorkerTask(imageView, getResources(),size.x / 2, size.y / 2);
        task.execute(imageID);

        return(result);
    }
}
