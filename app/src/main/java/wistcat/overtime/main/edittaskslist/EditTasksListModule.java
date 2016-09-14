package wistcat.overtime.main.edittaskslist;

import android.support.v4.app.LoaderManager;

import dagger.Module;
import dagger.Provides;

/**
 * @author wistcat 2016/9/12
 */
@Module
public class EditTasksListModule {

    private final EditTasksListContract.View mView;
    private final LoaderManager mManager;

    public EditTasksListModule(LoaderManager manager, EditTasksListContract.View view) {
        mManager = manager;
        mView = view;
    }

    @Provides
    public EditTasksListContract.View provideView() {
        return mView;
    }

    @Provides
    public LoaderManager provideLoaderManager() {
        return mManager;
    }

}
