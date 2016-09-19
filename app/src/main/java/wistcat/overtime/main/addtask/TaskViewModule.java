package wistcat.overtime.main.addtask;

import dagger.Module;
import dagger.Provides;

/**
 * @author wistcat 2016/9/18
 */
@Module
public class TaskViewModule {

    private CreateTaskContract.View mView;

    public TaskViewModule(CreateTaskContract.View view) {
        mView = view;
    }

    @Provides
    public CreateTaskContract.View provideView() {
        return mView;
    }
}
