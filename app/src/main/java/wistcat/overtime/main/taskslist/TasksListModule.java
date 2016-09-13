package wistcat.overtime.main.taskslist;

import android.support.v4.app.LoaderManager;

import dagger.Module;
import dagger.Provides;

/**
 * @author wistcat 2016/9/12
 */
@Module
public class TasksListModule {

    private final LoaderManager mManager;
    private final TasksListContract.View mView;

    public TasksListModule(LoaderManager manager, TasksListContract.View view) {
        mManager = manager;
        mView = view;
    }

    @Provides
    public LoaderManager provideLoaderManager() {
        return mManager;
    }

    @Provides
    public TasksListContract.View provideView() {
        return mView;
    }


}
