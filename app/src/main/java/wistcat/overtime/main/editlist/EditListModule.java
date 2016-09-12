package wistcat.overtime.main.editlist;

import android.support.v4.app.LoaderManager;

import dagger.Module;
import dagger.Provides;

/**
 * @author wistcat 2016/9/9
 */
@Module
public class EditListModule {

    private final LoaderManager mManager;
    private final EditListContract.View mView;

    public EditListModule(LoaderManager manager, EditListContract.View view) {
        mManager = manager;
        mView = view;
    }

    @Provides
    public LoaderManager provideLoaderManager() {
        return mManager;
    }

    @Provides
    public EditListContract.View provideView() {
        return mView;
    }

}
