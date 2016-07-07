package ch.templer.controls.viewpagger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.templer.activities.R;
import ch.templer.controls.photoview.PhotoView;
import ch.templer.model.ImageData;
import ch.templer.services.imageprocessing.BitmapWorkerTask;
import ch.templer.services.imageprocessing.ImageProcessingService;

/**
 * Created by Templer on 6/29/2016.
 */
public class ImageDetailFragment extends Fragment {
    private static final String IMAGE_DATA_EXTRA = "IMAGE_DATA_EXTRA";
    private static final String IMAGE_DATA_EXTRA_ID = "IMAGE_DATA_EXTRA_ID";
    private static final String IMAGE_DATA_EXTRA_TITLE = "IMAGE_DATA_EXTRA_TITLE";
    private ImageData imageData;
    private PhotoView mImageView;

    static ImageDetailFragment newInstance(ImageData imageData) {
        final ImageDetailFragment f = new ImageDetailFragment();
        final Bundle args = new Bundle();
        args.putSerializable(IMAGE_DATA_EXTRA, imageData);
        f.setArguments(args);
        return f;
    }

    // Empty constructor, required as per Fragment docs
    public ImageDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageData = getArguments() != null ? (ImageData)getArguments().getSerializable(IMAGE_DATA_EXTRA) : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.image_detail_fragment, container,false);
        mImageView = (PhotoView) v.findViewById(R.id.photoView);
        TextView title = (TextView) v.findViewById(R.id.imageTitle);
        title.setText(imageData.getDescription());

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageProcessingService.loadBitmap(imageData.getImageID(), mImageView, getContext());
        //mImageView.setImageResource(id); // Load image into ImageView out of memory exception if images are to big
    }
}
