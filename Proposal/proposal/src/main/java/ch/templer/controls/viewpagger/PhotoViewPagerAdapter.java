package ch.templer.controls.viewpagger;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import ch.templer.controls.photoview.PhotoView;

/**
 * Created by Templer on 4/29/2016.
 */
public class PhotoViewPagerAdapter extends PagerAdapter {
    private int[] imageIds;

    @Override
    public int getCount() {
        return imageIds.length;
    }

    public PhotoViewPagerAdapter(int[] imageIds) {
        this.imageIds = imageIds;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        photoView.setImageResource(imageIds[position]);

        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
