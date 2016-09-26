package wistcat.overtime;

import javax.inject.Singleton;

import dagger.Component;
import wistcat.overtime.data.datasource.TaskRepository;
import wistcat.overtime.data.running.RunningManager;

/**
 * @author wistcat 2016/9/2
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    TaskRepository getRepository();
    RunningManager getRunningManager();
}
