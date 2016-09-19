package wistcat.overtime.main.taskslist;

import android.support.v4.app.LoaderManager;

import dagger.Module;
import dagger.Provides;
import wistcat.overtime.model.TaskGroup;

/**
 * @author wistcat 2016/9/12
 */
@Module
public class TasksListModule {

    private final LoaderManager mManager;
    private final TasksListContract.View mView;
    private final TaskGroup mGroup;

    public TasksListModule(LoaderManager manager, TasksListContract.View view, TaskGroup group) {
        mManager = manager;
        mView = view;
        mGroup = group;
    }

    @Provides
    public LoaderManager provideLoaderManager() {
        return mManager;
    }

    @Provides
    public TasksListContract.View provideView() {
        return mView;
    }

    @Provides
    public TaskGroup provideTaskGroup() {
        return mGroup;
    }

}
