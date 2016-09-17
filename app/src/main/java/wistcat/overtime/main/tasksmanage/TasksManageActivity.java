package wistcat.overtime.main.tasksmanage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import javax.inject.Inject;

import wistcat.overtime.App;
import wistcat.overtime.R;

public class TasksManageActivity extends AppCompatActivity {

    @Inject
    public TasksManagePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        // fragment
        TasksManageFragment fragment =
                (TasksManageFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = new TasksManageFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }

        // dagger
        TasksManageComponent component = DaggerTasksManageComponent
                .builder()
                .appComponent(App.getInstance().getAppComponent())
                .tasksManageModule(new TasksManageModule(getSupportLoaderManager(), fragment))
                .build();
        component.inject(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_MENU || super.onKeyDown(keyCode, event);
    }

}
