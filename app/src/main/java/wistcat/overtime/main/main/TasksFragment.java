package wistcat.overtime.main.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import wistcat.overtime.App;
import wistcat.overtime.R;
import wistcat.overtime.adapter.MainTasksAdapter;
import wistcat.overtime.base.BottomFragment;
import wistcat.overtime.base.CursorRecyclerViewAdapter;
import wistcat.overtime.base.MoveTaskDialog;
import wistcat.overtime.interfaces.DialogButtonListener;
import wistcat.overtime.interfaces.EditItemSelectListener;
import wistcat.overtime.interfaces.ItemSelectListener;
import wistcat.overtime.main.addtask.AddTaskActivity;
import wistcat.overtime.main.main.tasks.DaggerMainTasksComponent;
import wistcat.overtime.main.main.tasks.MainTasksComponent;
import wistcat.overtime.main.main.tasks.MainTasksContract;
import wistcat.overtime.main.main.tasks.MainTasksModule;
import wistcat.overtime.main.main.tasks.MainTasksPresenter;
import wistcat.overtime.main.runningpage.RunningTasksActivity;
import wistcat.overtime.main.taskdetail.TaskDetailsActivity;
import wistcat.overtime.main.taskslist.TasksListActivity;
import wistcat.overtime.main.tasksmanage.TasksManageActivity;
import wistcat.overtime.main.tasksmore.TasksMoreActivity;
import wistcat.overtime.main.taskstatistics.TasksStatisticsActivity;
import wistcat.overtime.model.Task;
import wistcat.overtime.model.TaskGroup;
import wistcat.overtime.widget.DividerItemDecoration;
import wistcat.overtime.widget.ScrollChildSwipeRefreshLayout;

/**
 * 首页Activate任务展示
 *
 * @author wistcat
 */
public class TasksFragment extends Fragment implements MainTasksContract.View, EditItemSelectListener<Task> {

    private String[] ITEMS = new String[]{null, "启动任务", "删除任务"};
    private String[] ITEMS_RUNNING = new String[]{null, "运行中"};
    private final String MENU = "menu";
    private final String DELETE = "delete";
    private TextView mNoText;
    private View mBottom;
    private CursorRecyclerViewAdapter mAdapter;

    @Inject
    public MainTasksPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // 完成Presenter的注入
        MainTasksComponent component = DaggerMainTasksComponent
                .builder()
                .appComponent(App.getInstance().getAppComponent())
                .mainTasksModule(new MainTasksModule(this, getLoaderManager()))
                .build();
        component.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tasks, parent, false);

        // Guide选项
        root.findViewById(R.id.tasks_manage).setOnClickListener(mClickListener);
        root.findViewById(R.id.tasks_analyse).setOnClickListener(mClickListener);
        root.findViewById(R.id.tasks_create).setOnClickListener(mClickListener);

        // 更多选项
        mNoText = (TextView) root.findViewById(R.id.text_no_task);
        root.findViewById(R.id.text_more).setOnClickListener(mClickListener);

        // List
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.tasks_list);
        mAdapter = new MainTasksAdapter(getContext().getApplicationContext(), this);
        LinearLayoutManager adapterManager = new LinearLayoutManager(getContext());
        // 设置ItemDecoration，用于绘制分割线
        int left = getResources().getDimensionPixelOffset(R.dimen.default_task_item_seq_width);
        DividerItemDecoration itemDecoration =
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL, left, 0);
        // 设置RecyclerView
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(adapterManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mAdapter);

        // swipeRefresh
        ScrollChildSwipeRefreshLayout refreshLayout = (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.scrollview);
        refreshLayout.setScrollUpChild(recyclerView);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        // 触发refresh需要拉动的距离
        refreshLayout.setDistanceToTriggerSync(getPx(36));
        // 显示进度条的位置 scale=true
        refreshLayout.setProgressViewOffset(true, getPx(72), getPx(100));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadTasks();
            }
        });

        //
        mBottom = root.findViewById(R.id.bottom_guide);
        mBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.openRunningPage();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tasks_manage:
                    mPresenter.manageTasks();
                    break;
                case R.id.tasks_analyse:
                    mPresenter.statisticsTasks();
                    break;
                case R.id.tasks_create:
                    mPresenter.addNewTask();
                    break;
                case R.id.text_more:
                    mPresenter.moreTasks();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void setLoadingIndicator(final boolean active) {
        View root = getView();
        if (root != null) { //...
            final ScrollChildSwipeRefreshLayout refreshLayout = (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.scrollview);
            refreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setRefreshing(active);
                }
            });
        }
    }

    @Override
    public void showList(final Cursor tasks) {
        mAdapter.swapCursor(tasks);
    }

    @Override
    public void showLoadingTasksError() {
        App.showToast("load tasks error");
    }

    @Override
    public void showTaskDetails(Task task) {
        // 跳转到任务详情页
        Intent intent = new Intent(getContext(), TaskDetailsActivity.class);
        Bundle data = new Bundle();
        data.putSerializable(TaskDetailsActivity.KEY_TASK, task);
        intent.putExtras(data);
        startActivity(intent);
    }

    @Override
    public void showTasksManage() {
        intentTo(TasksManageActivity.class);
    }

    @Override
    public void showTasksStatistics() {
        intentTo(TasksStatisticsActivity.class);
    }

    @Override
    public void showAddTask() {
        intentTo(AddTaskActivity.class);
    }

    @Override
    public void showMoreTasks() {
        intentTo(TasksMoreActivity.class);
    }

    @Override
    public void setPresenter(MainTasksContract.Presenter presenter) {
        // 不需要...
    }

    @Override
    public void clearCursor() {
        mAdapter.swapCursor(null);
    }

    @Override
    public void showTaskMenu(String name, String group) {
        ITEMS[0] = String.format("任务组: %s", group);
        BottomFragment fragment = BottomFragment.getInstance(name, ITEMS);
        fragment.setSelectListener(new ItemSelectListener<Integer>() {
            @Override
            public void onSelected(Integer item) {
                switch (item) {
                    case 0: // 打开任务组
                        mPresenter.openGroup();
                        break;
                    case 1: // 开始任务
                        mPresenter.doStart();
                        break;
                    case 2: // 删除任务
                        mPresenter.openDeleteDialog();
                        break;
                    default:
                        break;
                }
            }
        });
        fragment.show(getFragmentManager(), MENU);
    }

    @Override
    public void showRunningTaskMenu(String name, String group) {
        ITEMS_RUNNING[0] = String.format("任务组: %s", group);
        BottomFragment fragment = BottomFragment.getInstance(name, ITEMS_RUNNING);
        fragment.setSelectListener(new ItemSelectListener<Integer>() {
            @Override
            public void onSelected(Integer item) {
                switch (item) {
                    case 0: // 打开任务组
                        mPresenter.openGroup();
                        break;
                    case 1: // 打开运行页面
                        mPresenter.openRunningPage();
                        break;
                    default:
                        break;
                }
            }
        });
        fragment.show(getFragmentManager(), MENU);
    }

    @Override
    public void showToast(String msg) {
        App.showToast(msg);
    }

    @Override
    public void dismissTaskMenu() {
        dismissDialog(MENU);
    }

    @Override
    public void showDeleteDialog(@NonNull Task task) {
        MoveTaskDialog dialog = MoveTaskDialog.getInstance("删除的任务会放入回收站，是否确定删除？");
        dialog.setListener(new DialogButtonListener() {
            @Override
            public void onNegative() {
                mPresenter.closeDeleteDialog();
            }

            @Override
            public void onNeutral() {
                // null
            }

            @Override
            public void onPositive() {
                mPresenter.doDelete();
            }

            @Override
            public void onData(Object data) {
                // null
            }
        });
        dialog.show(getFragmentManager(), DELETE);
    }

    @Override
    public void dismissDeleteDialog() {
        dismissDialog(DELETE);
    }

    private void intentTo(Class<?> clazz) {
        startActivity(new Intent(getActivity(), clazz));
    }

    /** 列表为空/不为空时，切换提示 */
    @Override
    public void showNoText(boolean isNull) {
        final int duration = 200;
        if (isNull && mNoText.getVisibility() == View.GONE) {
            mNoText.setAlpha(0.f);
            mNoText.setVisibility(View.VISIBLE);
            mNoText.animate().alpha(1.f).setDuration(duration).setListener(null);

        } else if (!isNull && mNoText.getVisibility() == View.VISIBLE) {
            mNoText.animate().alpha(0.f).setDuration(duration).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mNoText.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void redirectGroup(@NonNull TaskGroup group) {
        Bundle data = new Bundle();
        data.putSerializable(TasksListActivity.KEY_TASK_GROUP, group);
        Intent intent = new Intent(getActivity(), TasksListActivity.class);
        intent.putExtras(data);
        startActivity(intent);
    }

    @Override
    public void redirectRunningPage() {
        Intent intent = new Intent(getActivity(), RunningTasksActivity.class);
        startActivity(intent);
    }

    @Override
    public void redirectRunningPage(@NonNull Task task) {
        Intent intent = new Intent(getActivity(), RunningTasksActivity.class);
        Bundle data = new Bundle();
        data.putSerializable(RunningTasksActivity.RUNNING_TASK, task);
        intent.putExtras(data);
        startActivity(intent);
    }

    @Override
    public void showRunningBottom() {
        mBottom.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRunningBottom() {
        mBottom.setVisibility(View.GONE);
    }

    @Override
    public void onSelected(Task task) {
        mPresenter.openTaskDetails(task);
    }

    private int getPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    public void onEditSelected(Task item) {
        mPresenter.openTaskMenu(item);
    }

    private void dismissDialog(String tag) {
        AppCompatDialogFragment fragment = (AppCompatDialogFragment) getFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            fragment.dismiss();
        }
    }
}
