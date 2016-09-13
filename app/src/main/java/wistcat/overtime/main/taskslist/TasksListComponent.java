package wistcat.overtime.main.taskslist;

import dagger.Component;
import wistcat.overtime.AppComponent;
import wistcat.overtime.PerActivity;

/**
 * @author wistcat 2016/9/12
 */
@PerActivity
@Component(dependencies = {AppComponent.class}, modules = {TasksListModule.class})
public interface TasksListComponent {
    void inject(TasksListActivity activity);
}
