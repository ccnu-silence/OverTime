package wistcat.overtime.main.addtask;

import dagger.Module;
import dagger.Provides;
import wistcat.overtime.PerFragment;
import wistcat.overtime.model.TaskGroup;

/**
 * @author wistcat 2016/9/18
 */
@Module
public class CreateTaskModule {

    private final TaskGroup mGroup;

    public CreateTaskModule(TaskGroup group) {
        mGroup = group;
    }

    @PerFragment
    @Provides
    public TaskGroup provideGroup() {
        return mGroup;
    }

}
