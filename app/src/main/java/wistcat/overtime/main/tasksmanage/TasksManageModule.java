package wistcat.overtime.main.tasksmanage;

import android.support.v4.app.LoaderManager;

import dagger.Module;
import dagger.Provides;

/**
 * @author wistcat 2016/9/5
 */
@Module
public class TasksManageModule {

    private final LoaderManager mManager;
    private final TasksManageContract.View mView;

    public TasksManageModule(LoaderManager manager, TasksManageContract.View view) {
        mManager = manager;
        mView = view;
    }

    @Provides
    public LoaderManager provideLoaderMananger() {
        return mManager;
    }

    @Provides
    public TasksManageContract.View provideView() {
        return mView;
    }

}
