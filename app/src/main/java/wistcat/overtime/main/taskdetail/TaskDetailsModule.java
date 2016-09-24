package wistcat.overtime.main.taskdetail;

import android.support.v4.app.LoaderManager;

import dagger.Module;
import dagger.Provides;
import wistcat.overtime.model.Task;

/**
 * @author wistcat 2016/9/22
 */
@Module
public class TaskDetailsModule {

    private final Task mTask;
    private final LoaderManager mManager;
    private final TaskDetailsContract.View mView;

    public TaskDetailsModule(Task task, LoaderManager manager, TaskDetailsContract.View view) {
        mTask = task;
        mManager = manager;
        mView = view;
    }

    @Provides
    public Task provideTask() {
        return mTask;
    }

    @Provides
    public LoaderManager provideLoaderManager() {
        return mManager;
    }

    @Provides
    public TaskDetailsContract.View provideView() {
        return mView;
    }

}
