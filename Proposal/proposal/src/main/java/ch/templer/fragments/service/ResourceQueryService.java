package ch.templer.fragments.service;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.content.ContextCompat;

/**
 * Created by Templer on 6/21/2016.
 */
public class ResourceQueryService {
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static int getColorByName(String id) throws Resources.NotFoundException {
        if (!idCheck(id)) {
            throw new Resources.NotFoundException();
        }
        return ContextCompat.getColor(mContext, mContext.getResources().getIdentifier(id, "color", mContext.getPackageName()));
    }

    public static int getDrawableByName(String id) throws Resources.NotFoundException {
        if (!idCheck(id)) {
            throw new Resources.NotFoundException();
        }
        return mContext.getResources().getIdentifier(id, "drawable", mContext.getPackageName());
    }

    public static int getRawByName(String id) throws Resources.NotFoundException {
        if (!idCheck(id)) {
            throw new Resources.NotFoundException();
        }
        return mContext.getResources().getIdentifier(id, "raw", mContext.getPackageName());
    }

    private static boolean idCheck(String id) {
        return (id != null && !id.isEmpty()) ? true : false;
    }
}
