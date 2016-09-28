package wistcat.overtime.main.runningpage;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;

import wistcat.overtime.App;
import wistcat.overtime.R;
import wistcat.overtime.adapter.RunningTasksAdapter;
import wistcat.overtime.interfaces.EditItemSelectListener;
import wistcat.overtime.model.Record;
import wistcat.overtime.model.Task;
import wistcat.overtime.widget.DividerItemDecoration;

public class RunningTasksActivity extends AppCompatActivity implements RunningTasksContract.RunningView {

    public static final int REQUEST_NOTIFICATION = 1;
    public static final String RUNNING_TASK = "running_task";
    private DrawerLayout mDrawer;
    private RunningTasksAdapter mAdapter;
    private Task mTargetTask;
    private boolean isFirst = true;

    @Inject
    public RunningTasksPresenter mPresenter;

    @Override
    public void onStart() {
        super.onStart();
        Bundle data = getIntent().getExtras();
        if (data != null) {
            mTargetTask = (Task) data.getSerializable(RUNNING_TASK);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_tasks);

        // Fragment
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
                .runningTasksModule(new RunningTasksModule(getSupportLoaderManager(), fragment, this))
                .build();
        component.inject(this);

        // Drawer
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);

        // nav_button
        View menu = findViewById(R.id.nav_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDrawer();
            }
        });

        // stop all
        View stopAll = findViewById(R.id.nav_stop);
        stopAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.stopAll();
            }
        });

        // recyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        mAdapter = new RunningTasksAdapter(this, new EditItemSelectListener<Record>() {
            @Override
            public void onEditSelected(Record item) {
                mPresenter.onRecordPaused(item);
            }

            @Override
            public void onSelected(Record item) {
                mPresenter.onItemSelected(item);
            }
        });
        initRecyclerView(recyclerView);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mPresenter.initState(mTargetTask);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirst) {
            if (mTargetTask == null) {
                showDrawer();
            } else {
                hideDrawer();
            }
            isFirst = false;
            mPresenter.loadRecordByTask();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            showDrawer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                        mDrawer.closeDrawer(GravityCompat.START);
                    } else {
                        // FIXME
                        finish();
                    }
                    return true;
                case KeyEvent.KEYCODE_MENU:
                    if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                        hideDrawer();
                    } else {
                        showDrawer();
                    }
                    return true;
                default:
                    break;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void initRecyclerView(RecyclerView view) {
        LinearLayoutManager adapterManager = new LinearLayoutManager(this);
        // 设置ItemDecoration，用于绘制分割线
        int left = getResources().getDimensionPixelOffset(R.dimen.default_task_item_seq_width);
        DividerItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL, left, 0);
        view.addItemDecoration(itemDecoration);
        view.setLayoutManager(adapterManager);
        view.setItemAnimator(new DefaultItemAnimator());
        view.setHasFixedSize(true);
        view.setNestedScrollingEnabled(false);
        view.setAdapter(mAdapter);
    }

    @Override
    public void showList(Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void clearList() {
        showList(null);
    }

    @Override
    public void showDrawer() {
        mDrawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void hideDrawer() {
        mDrawer.closeDrawer(GravityCompat.START);
    }
}
