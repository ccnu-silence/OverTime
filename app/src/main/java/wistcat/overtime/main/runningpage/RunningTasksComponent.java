package wistcat.overtime.main.runningpage;

import dagger.Component;
import wistcat.overtime.AppComponent;
import wistcat.overtime.PerActivity;

/**
 * @author wistcat 2016/9/25
 */
@PerActivity
@Component(dependencies = {AppComponent.class}, modules = {RunningTasksModule.class})
public interface RunningTasksComponent {
    void inject(RunningTasksActivity activity);
}
