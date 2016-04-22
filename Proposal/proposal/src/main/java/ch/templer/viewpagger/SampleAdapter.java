package ch.templer.viewpagger;

/**
 * Created by Templer on 03.04.2016.
 */
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class SampleAdapter extends FragmentStatePagerAdapter {
    public static int LOOPS_COUNT = 100;
    private Context context = null;
    private int[] imageId;

    public SampleAdapter(Context ctxt, FragmentManager mgr, int[] imageId) {
        super(mgr);
        this.context =ctxt;
        this.imageId = imageId;
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

    @Override
    public Fragment getItem(int position) {

        if (imageId != null && imageId.length > 0)
        {
            position = position % imageId.length; // use modulo for infinite cycling
            return ImageFragment.newInstance(position, imageId[position]);
        }
        else
        {
            return ImageFragment.newInstance(0,0);
        }

        //return(ImageFragment.newInstance(position, imageId[position]));
    }

    @Override
    public String getPageTitle(int position) {
        return(ImageFragment.getTitle(context, position));
    }

}