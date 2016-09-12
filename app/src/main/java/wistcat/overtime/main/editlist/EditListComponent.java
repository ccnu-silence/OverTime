package wistcat.overtime.main.editlist;

import dagger.Component;
import wistcat.overtime.AppComponent;
import wistcat.overtime.PerActivity;

/**
 * @author wistcat 2016/9/9
 */
@PerActivity
@Component(dependencies = {AppComponent.class}, modules = {EditListModule.class})
public interface EditListComponent {
    void inject(EditListActivity activity);
}
