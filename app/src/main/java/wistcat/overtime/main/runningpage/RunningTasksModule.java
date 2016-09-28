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
    private final RunningTasksContract.RunningView mMainView;

    public RunningTasksModule(LoaderManager manager, RunningTasksContract.View view,
                              RunningTasksContract.RunningView mainView) {
        mManager = manager;
        mView = view;
        mMainView = mainView;
    }

    @Provides
    public LoaderManager provideLoaderManager() {
        return mManager;
    }

    @Provides
    public RunningTasksContract.View provideView() {
        return mView;
    }

    @Provides
    public RunningTasksContract.RunningView provideMainView() {
        return mMainView;
    }

}
