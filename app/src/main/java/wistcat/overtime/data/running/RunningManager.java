package wistcat.overtime.data.running;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

import wistcat.overtime.util.NotificationHelper;

/**
 * @author wistcat 2016/9/26
 */
@Singleton
public class RunningManager {

    private Context mContext;
    private int mRunningTaskCount;

    @Inject
    public RunningManager(Context context) {
        mContext = context;
    }

    public int startRunning() {
        NotificationHelper.notifyNormal(mContext, ++mRunningTaskCount);
        return mRunningTaskCount;
    }

    public int stopRunning() {
        NotificationHelper.notifyNormal(mContext, --mRunningTaskCount);
        return mRunningTaskCount;
    }

    public int getRunningTaskCount() {
        return mRunningTaskCount;
    }

}
