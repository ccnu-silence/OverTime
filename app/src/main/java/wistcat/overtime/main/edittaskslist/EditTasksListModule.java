package wistcat.overtime.main.edittaskslist;

import android.support.v4.app.LoaderManager;

import dagger.Module;
import dagger.Provides;
import wistcat.overtime.model.TaskGroup;

/**
 * @author wistcat 2016/9/12
 */
@Module
public class EditTasksListModule {

    private final EditTasksListContract.View mView;
    private final LoaderManager mManager;
    private final TaskGroup mTaskGroup;

    public EditTasksListModule(LoaderManager manager, EditTasksListContract.View view, TaskGroup taskGroup) {
        mManager = manager;
        mView = view;
        mTaskGroup = taskGroup;
    }

    @Provides
    public EditTasksListContract.View provideView() {
        return mView;
    }

    @Provides
    public LoaderManager provideLoaderManager() {
        return mManager;
    }

    @Provides
    public TaskGroup provideTaskGroup() {
        return mTaskGroup;
    }

}
