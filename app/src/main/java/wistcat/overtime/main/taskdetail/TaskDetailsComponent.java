package wistcat.overtime.main.taskdetail;

import dagger.Component;
import wistcat.overtime.AppComponent;
import wistcat.overtime.PerFragment;

/**
 * @author wistcat 2016/9/22
 */
@PerFragment
@Component(dependencies = {AppComponent.class}, modules = {TaskDetailsModule.class})
public interface TaskDetailsComponent {
    void inject(TaskDetailsActivity activity);
}
