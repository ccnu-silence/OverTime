package wistcat.overtime;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import wistcat.overtime.data.datasource.TaskDataSource;
import wistcat.overtime.data.datasource.local.Local;
import wistcat.overtime.data.datasource.local.LocalTaskDataSource;

/**
 * @author wistcat 2016/9/2
 */
@Module
public class AppModule {

    private Context mContext;

    public AppModule(Context context) {
        mContext = context;
    }

    @Singleton
    @Provides
    @Local
    public TaskDataSource provideLocalDataSource(Context context) {
        return new LocalTaskDataSource(context);
    }

    @Provides
    public Context provideContext() {
        return mContext;
    }

}
