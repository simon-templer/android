package ch.templer.controls.viewpagger;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import ch.templer.model.ImageData;

/**
 * Created by Templer on 6/29/2016.
 */
public class ImagePagerAdapter extends FragmentStatePagerAdapter {
    private final int mSize;
    List<ImageData> images;

    public ImagePagerAdapter(FragmentManager fm, List<ImageData> images) {
        super(fm);
        this.images =images;
        mSize = images.size();
    }

    @Override
    public int getCount() {
        return mSize;
    }

    @Override
    public Fragment getItem(int position) {
        return ImageDetailFragment.newInstance(images.get(position));
    }


}