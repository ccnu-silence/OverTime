package wistcat.overtime.main.runningpage;

import android.support.v4.app.LoaderManager;

import dagger.Module;
import dagger.Provides;

/**
 * @author wistcat 2016/9/25
 */
@Module
public class RunningTasksModule {

    private final LoaderManager mManager;
    private final RunningTasksContract.View mView;

    public RunningTasksModule(LoaderManager manager, RunningTasksContract.View view) {
        mManager = manager;
        mView = view;
    }

    @Provides
    public LoaderManager provideLoaderManager() {
        return mManager;
    }

    @Provides
    public RunningTasksContract.View provideView() {
        return mView;
    }


}
