package wistcat.overtime.main.taskdetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import javax.inject.Inject;

import wistcat.overtime.App;
import wistcat.overtime.R;
import wistcat.overtime.model.Task;

public class TaskDetailsActivity extends AppCompatActivity {
    public static final String KEY_TASK = "task";

    @Inject
    public TaskDetailsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        // data
        Bundle data = getIntent().getExtras();
        Task task = (Task) data.getSerializable(KEY_TASK);
        if (task == null) {
            throw new NullPointerException("缺少Task");
        }
        // fragment
        TaskDetailsFragment fragment =
                (TaskDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = TaskDetailsFragment.getInstance(task);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
        // dagger
        TaskDetailsModule module = new TaskDetailsModule(task, getSupportLoaderManager(), fragment);
        TaskDetailsComponent component = DaggerTaskDetailsComponent
                .builder()
                .appComponent(App.getInstance().getAppComponent())
                .taskDetailsModule(module)
                .build();
        component.inject(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
