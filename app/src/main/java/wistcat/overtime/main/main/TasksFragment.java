package wistcat.overtime.main.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import javax.inject.Inject;

import wistcat.overtime.App;
import wistcat.overtime.R;
import wistcat.overtime.adapter.MainTasksAdapter;
import wistcat.overtime.interfaces.ItemSelectListener;
import wistcat.overtime.main.addtask.AddTaskActivity;
import wistcat.overtime.main.main.tasks.DaggerMainTasksComponent;
import wistcat.overtime.main.main.tasks.MainTasksComponent;
import wistcat.overtime.main.main.tasks.MainTasksContract;
import wistcat.overtime.main.main.tasks.MainTasksModule;
import wistcat.overtime.main.main.tasks.MainTasksPresenter;
import wistcat.overtime.main.taskdetail.TaskDetailsActivity;
import wistcat.overtime.main.tasksmanage.TasksManageActivity;
import wistcat.overtime.main.tasksmore.TasksMoreActivity;
import wistcat.overtime.main.taskstatistics.TasksStatisticsActivity;
import wistcat.overtime.model.Task;
import wistcat.overtime.widget.ScrollChildSwipeRefreshLayout;

/**
 * 首页Activate任务展示
 *
 * @author wistcat
 */
public class TasksFragment extends Fragment implements MainTasksContract.View, ItemSelectListener<Task> {

    private CardView mManagerCard, mAnalysisCard, mAddCard;
    private ListView mActivateList;
    private TextView mNoTaskText;
    private TextView mMoreText;
    private MainTasksAdapter mAdapter;
    private View currentView;
    private Handler mHandler = new Handler();

    @Inject
    public MainTasksPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        // CardView
        mManagerCard = (CardView) root.findViewById(R.id.tasks_manager);
        mManagerCard.setOnClickListener(mClickListener);
        mAnalysisCard = (CardView) root.findViewById(R.id.tasks_analy);
        mAnalysisCard.setOnClickListener(mClickListener);
        mAddCard = (CardView) root.findViewById(R.id.tasks_add);
        mAddCard.setOnClickListener(mClickListener);

        // TextView
        mNoTaskText = (TextView) root.findViewById(R.id.text_no_task);
        mMoreText = (TextView) root.findViewById(R.id.text_more);
        mMoreText.setOnClickListener(mClickListener);

        // List
        mActivateList = (ListView) root.findViewById(R.id.tasks_list);
        mAdapter = new MainTasksAdapter(getContext());
        mActivateList.setAdapter(mAdapter);
        mAdapter.setTaskItemListener(this);
        currentView = mActivateList;

        // swipeRefresh
        ScrollChildSwipeRefreshLayout refreshLayout = (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.scrollview);
        refreshLayout.setScrollUpChild(mActivateList);
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
            if (mManagerCard == view) {
                mPresenter.manageTasks();
            }
            if (mAnalysisCard == view) {
                mPresenter.statisticsTasks();
            }
            if (mAddCard == view) {
                mPresenter.addNewTask();
            }
            if (mMoreText == view) {
                mPresenter.moreTasks();
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
    public void showTasks(final Cursor tasks) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.swapCursor(tasks);
                crossAnim(true);
            }
        });
    }

    @Override
    public void showNoTasks() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                crossAnim(false);
            }
        });

    }

    @Override
    public void showLoadingTasksError() {
        App.showToast("load tasks error");
    }

    @Override
    public void showTaskDetails(Task task) {
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

    private void intentTo(Class<?> clazz) {
        startActivity(new Intent(getActivity(), clazz));
    }

    private void crossAnim(boolean isLoaded) {
        final View show = isLoaded ? mActivateList : mNoTaskText;
        final View hide = isLoaded ? mNoTaskText : mActivateList;
        if (show == currentView) {
            return;
        }
        final int duration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        show.setAlpha(0.f);
        show.setVisibility(View.VISIBLE);
        show.animate().alpha(1.f).setDuration(duration).setListener(null);

        hide.animate().alpha(0.f).setDuration(duration).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                hide.setVisibility(View.GONE);
            }
        });
        currentView = show;
    }

    @Override
    public void onSelected(Task task) {
        mPresenter.openTaskDetails(task);
    }

    private int getPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

}
