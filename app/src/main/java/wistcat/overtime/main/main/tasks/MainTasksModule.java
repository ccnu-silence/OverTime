package wistcat.overtime.main.main.tasks;

import android.support.v4.app.LoaderManager;

import dagger.Module;
import dagger.Provides;

/**
 * @author wistcat 2016/9/2
 */
@Module
public class MainTasksModule {
    private MainTasksContract.View mView;
    private LoaderManager mManager;

    public MainTasksModule(MainTasksContract.View view, LoaderManager loaderManager) {
        mView = view;
        mManager = loaderManager;
    }

    @Provides
    public MainTasksContract.View provideView() {
        return mView;
    }

    @Provides
    public LoaderManager provideLoaderManager() {
        return mManager;
    }
}
