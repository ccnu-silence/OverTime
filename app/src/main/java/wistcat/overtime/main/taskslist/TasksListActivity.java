package wistcat.overtime.main.taskslist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import wistcat.overtime.App;
import wistcat.overtime.R;
import wistcat.overtime.model.TaskGroup;

public class TasksListActivity extends AppCompatActivity {

    public static final String KEY_TASK_GROUP = "task_group";

    @Inject
    public TasksListPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        Bundle data = getIntent().getExtras();
        // fragment
        TasksListFragment fragment =
                (TasksListFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = TasksListFragment.getInstance(data);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment, "TasksList")
                    .commit();
        }
        TaskGroup group;
        if (data != null) {
            group = (TaskGroup) data.getSerializable(TasksListActivity.KEY_TASK_GROUP);
        } else {
            throw new NullPointerException("没有TaskGroup!");
        }
        // dagger
        TasksListComponent component = DaggerTasksListComponent
                .builder()
                .appComponent(App.getInstance().getAppComponent())
                .tasksListModule(new TasksListModule(getSupportLoaderManager(), fragment, group))
                .build();
        component.inject(this);
    }
}
