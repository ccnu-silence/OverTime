package wistcat.overtime.main.addtask;

import dagger.Component;
import wistcat.overtime.AppComponent;
import wistcat.overtime.PerFragment;

/**
 * @author wistcat 2016/9/18
 */
@PerFragment
@Component(dependencies = {AppComponent.class}, modules = {TaskViewModule.class})
public interface CreateTaskComponent {
    void inject(AddTaskActivity activity);
}
