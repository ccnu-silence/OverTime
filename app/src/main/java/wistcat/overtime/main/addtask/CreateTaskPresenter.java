package wistcat.overtime.main.addtask;

import java.util.List;

import javax.inject.Inject;

import wistcat.overtime.App;
import wistcat.overtime.data.TaskEngine;
import wistcat.overtime.data.datasource.TaskRepository;
import wistcat.overtime.interfaces.GetDataListCallback;
import wistcat.overtime.interfaces.ResultCallback;
import wistcat.overtime.model.Task;
import wistcat.overtime.model.TaskGroup;
import wistcat.overtime.model.TaskState;
import wistcat.overtime.util.Utils;

/**
 * @author wistcat 2016/9/18
 */
public class CreateTaskPresenter implements CreateTaskContract.Presenter {

    private final TaskRepository mRespository;
    private final CreateTaskContract.View mView;

    @Inject
    public CreateTaskPresenter(TaskRepository repository, CreateTaskContract.View view) {
        mRespository = repository;
        mView = view;
    }

    @Inject
    public void setup() {
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadSpinner() {
        mRespository.getTaskGroups(new GetDataListCallback<TaskGroup>() {
            @Override
            public void onDataLoaded(List<TaskGroup> dataList) {
                mView.showSpinner(dataList);
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void saveTask(TaskGroup group, String name, String description, int type) {
        String date = Utils.getDate(Utils.FORMAT_DATE_TEMPLATE_CHN);
        Task task = new Task(group.getId(), group.getName(), 0,
                TaskEngine.createId(), type, TaskState.Activate, name, description, 0);
        task.setExtra1(date);
        mRespository.saveTask(task, new ResultCallback() {
            @Override
            public void onSuccess() {
                App.showToast("创建新任务成功");
                quit();
            }

            @Override
            public void onError() {
                App.showToast("创建新任务失败");
            }
        });
    }


    @Override
    public void quit() {
        mView.quit();
    }
}
