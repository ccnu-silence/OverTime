package wistcat.overtime.main.edittaskslist;

import dagger.Component;
import wistcat.overtime.AppComponent;
import wistcat.overtime.PerActivity;

/**
 * @author wistcat 2016/9/12
 */
@PerActivity
@Component(dependencies = {AppComponent.class}, modules = {EditTasksListModule.class})
public interface EditTasksListComponent {
    void inject(EditTasksListActivity activity);
}
