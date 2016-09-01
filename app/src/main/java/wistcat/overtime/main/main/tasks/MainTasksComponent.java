package wistcat.overtime.main.main.tasks;

import dagger.Component;
import wistcat.overtime.AppComponent;
import wistcat.overtime.PerFragment;
import wistcat.overtime.main.main.TasksFragment;

/**
 * @author wistcat 2016/9/2
 */
@PerFragment
@Component(dependencies = {AppComponent.class}, modules = {MainTasksModule.class})
public interface MainTasksComponent {
    void inject(TasksFragment tasksFragment);
}
