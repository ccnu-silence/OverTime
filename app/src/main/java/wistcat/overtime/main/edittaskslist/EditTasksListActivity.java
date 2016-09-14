package wistcat.overtime.main.edittaskslist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import wistcat.overtime.App;
import wistcat.overtime.R;

public class EditTasksListActivity extends AppCompatActivity {
    public static final String KEY_EDIT_TASKS_LIST = "tasks_list";

    @Inject
    public EditTasksListPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        // fragment
        EditTasksListFragment fragment =
                (EditTasksListFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            Bundle data = getIntent().getExtras();
            fragment = EditTasksListFragment.getInstance(data);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment, EditTasksListFragment.TAG)
                    .commit();
        }

        //dagger
        EditTasksListComponent component = DaggerEditTasksListComponent
                .builder()
                .appComponent(App.getInstance().getAppComponent())
                .editTasksListModule(new EditTasksListModule(getSupportLoaderManager(), fragment))
                .build();
        component.inject(this);
    }
}
