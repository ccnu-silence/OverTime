package wistcat.overtime.main.tasksmanage;

import dagger.Component;
import wistcat.overtime.AppComponent;
import wistcat.overtime.PerActivity;

/**
 * @author wistcat 2016/9/5
 */
@PerActivity
@Component(modules = {TasksManageModule.class}, dependencies = {AppComponent.class})
public interface TasksManageComponent {
    void inject(TasksManageActivity activity);
}
