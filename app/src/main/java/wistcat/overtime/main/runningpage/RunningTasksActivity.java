package wistcat.overtime.main.runningpage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import wistcat.overtime.App;
import wistcat.overtime.R;

public class RunningTasksActivity extends AppCompatActivity {

    public static final int REQUEST_NOTIFICATION = 1;
    public static final String RUNNING_TASK = "running_task";

    @Inject
    public RunningTasksPresenter mPresenter;

    @Override
    public void onStart() {
        super.onStart();
        Bundle data = getIntent().getExtras();
        if (data != null) {
            // TODO
        } else {
            // TODO
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        RunningTasksFragment fragment =
                (RunningTasksFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = RunningTasksFragment.getInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
        RunningTasksComponent component = DaggerRunningTasksComponent
                .builder()
                .appComponent(App.getInstance().getAppComponent())
                .runningTasksModule(new RunningTasksModule(getSupportLoaderManager(), fragment))
                .build();
        component.inject(this);
    }

}
