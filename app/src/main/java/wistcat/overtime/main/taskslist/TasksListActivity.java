package wistcat.overtime.main.taskslist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import wistcat.overtime.App;
import wistcat.overtime.R;

public class TasksListActivity extends AppCompatActivity {
    public static final String KEY_TASK_GROUP = "task_group";

    @Inject
    public TasksListPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        // fragment
        TasksListFragment fragment =
                (TasksListFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            Bundle data = getIntent().getExtras();
            fragment = TasksListFragment.getInstance(data);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment, "TasksList")
                    .commit();
        }

        // dagger
        TasksListComponent component = DaggerTasksListComponent
                .builder()
                .appComponent(App.getInstance().getAppComponent())
                .tasksListModule(new TasksListModule(getSupportLoaderManager(), fragment))
                .build();
        component.inject(this);
    }
}
