package ch.templer.viewpagger;

/**
 * Created by Templer on 03.04.2016.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

//public class SampleAdapter extends FragmentStatePagerAdapter {
public class SampleAdapter extends FragmentStatePagerAdapter {
    public static int LOOPS_COUNT = 100;
    private Context context = null;
    private int[] imageId;
    private ImageFragment[] imageFragments;

    public SampleAdapter(Context ctxt, FragmentManager mgr, int[] imageId) {
        super(mgr);
        this.context = ctxt;
        this.imageId = imageId;
        imageFragments = new ImageFragment[imageId.length];
    }

    @Override
    public int getCount() {

        if (imageId != null && imageId.length > 0)
        {
            return imageId.length * LOOPS_COUNT; // simulate infinite by big number of products
        }
        else
        {
            return 1;
        }

        //return imageId.length;
    }

//        @Override
//      public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((View) object);
//    }

    @Override
    public Fragment getItem(int position) {

        if (imageId != null && imageId.length > 0) {
            position = position % imageId.length; // use modulo for infinite cycling
            if (imageFragments[position] != null) {
                return imageFragments[position];
            } else {
                ImageFragment imageFragment = ImageFragment.newInstance(position, imageId[position]);
                imageFragments[position] = imageFragment;
                return imageFragment;
            }
        }
        else
        {
//            if (imageFragments[0]!= null){
//                return imageFragments[0];
//            }else{
//                ImageFragment imageFragment = ImageFragment.newInstance(0,0);
//                imageFragments[0]=imageFragment;
//                return imageFragment;
//            }

        }
        return null;
    }

    //
    @Override
    public String getPageTitle(int position) {
        return (ImageFragment.getTitle(context, position));
    }


    //    @Override
//    public View instantiateItem(ViewGroup container, int position) {
//        PhotoView photoView = new PhotoView(container.getContext());
//        photoView.setImageResource(imageId[position]);
//
//        // Now just add PhotoView to ViewPager and return it
//        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
//        return photoView;
//    }

//    @Override
//      public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((View) object);
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view == object;
//    }

}