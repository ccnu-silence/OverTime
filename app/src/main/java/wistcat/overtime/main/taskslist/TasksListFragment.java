package wistcat.overtime.main.taskslist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import wistcat.overtime.App;
import wistcat.overtime.R;
import wistcat.overtime.adapter.TasksListAdapter;
import wistcat.overtime.base.BottomFragment;
import wistcat.overtime.base.MoveTaskDialog;
import wistcat.overtime.interfaces.DialogButtonListener;
import wistcat.overtime.interfaces.EditItemSelectListener;
import wistcat.overtime.interfaces.ItemSelectListener;
import wistcat.overtime.interfaces.OnSearchTermChanged;
import wistcat.overtime.main.addtask.AddTaskActivity;
import wistcat.overtime.main.edittaskslist.EditTasksListActivity;
import wistcat.overtime.main.taskdetail.TaskDetailsActivity;
import wistcat.overtime.model.Task;
import wistcat.overtime.model.TaskGroup;
import wistcat.overtime.util.Const;
import wistcat.overtime.widget.DividerItemDecoration;

/**
 * 任务列表，从TaskGroup列表打开
 *
 * @author wistcat 2016/9/12
 */
public class TasksListFragment extends Fragment implements TasksListContract.View,
        SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener, EditItemSelectListener<Task> {

    private final String GROUP = "group";
    private final String ITEM = "item";
    private final String DELETE = "delete";
    private final String MOVE = "move";
    private final String SAVE = "save";
    private final String[] TASK_MENU = new String[] {"移动任务", "收藏任务", "删除任务"};
    private final String[] COMMON_MENU = new String[] {"编辑分组", "管理任务", "新建任务"};
    private final String[] SPECIAL_MENU = new String[] {"编辑分组", "管理任务"};

    private String[] mMoreMenu;
    private TaskGroup mTaskGroup;
    private OnSearchTermChanged mTermChangedListener;
    private TasksListContract.Presenter mPresenter;
    private View mNoText;
    private String mSearchTerm;
    private TasksListAdapter mAdapter;

    public static TasksListFragment getInstance(Bundle data) {
        TasksListFragment fragment = new TasksListFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            TaskGroup taskGroup = (TaskGroup) bundle.getSerializable(TasksListActivity.KEY_TASK_GROUP);
            if (taskGroup == null) {
                throw new NullPointerException("找不到TaskGroup");
            }
            mTaskGroup = taskGroup;
            if (mTaskGroup.getId() == Const.COMPLETED_GROUP_ID || mTaskGroup.getId() == Const.RECYCLED_GROUP_ID) {
                mMoreMenu = SPECIAL_MENU;
            } else {
                mMoreMenu = COMMON_MENU;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tasks_list, container, false);

        // toolbar
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        if (toolbar != null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            setHasOptionsMenu(true);
            activity.setSupportActionBar(toolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(mTaskGroup.getName());
            }
        }
        // more
        root.findViewById(R.id.more_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.openMoreMenu();
            }
        });

        // list
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.list);
        mAdapter = new TasksListAdapter(getContext().getApplicationContext(), this);
        mTermChangedListener = mAdapter;
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

        // state
        if (savedInstanceState != null) {
            mSearchTerm = savedInstanceState.getString(SearchManager.QUERY);
            mTermChangedListener.onTermChanged(mSearchTerm);
        }

        // no task text
        mNoText = root.findViewById(R.id.no_task);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_search_tasks_list, menu);
        // search
        MenuItem item = menu.findItem(R.id.action_search);
        if (item != null) {
            SearchManager manager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            final SearchView view = (SearchView) MenuItemCompat.getActionView(item);
            SearchableInfo info = manager.getSearchableInfo(getActivity().getComponentName());
            view.setSearchableInfo(info);
            view.setOnQueryTextListener(this);
            // searchview action 展开监听
            MenuItemCompat.setOnActionExpandListener(item, this);
            // 接收初始化参数时... FIXME 复用的时候会用到吧，具体场景暂时没有考虑...
            if (mSearchTerm != null) {
                final String queryTerm = mSearchTerm;
                view.setQuery(queryTerm, false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && isAdded()) {
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!TextUtils.isEmpty(mSearchTerm)) {
            outState.putString(SearchManager.QUERY, mSearchTerm);
        }
    }

    // searchView search
    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String filter = TextUtils.isEmpty(newText) ? null : newText;
        if (mSearchTerm == null && filter == null) {
            return true;
        }
        if (mSearchTerm != null && mSearchTerm.equals(filter)) {
            return true;
        }
        mSearchTerm = filter;
        mTermChangedListener.onTermChanged(mSearchTerm);
        mPresenter.loadTasks(mSearchTerm);
        return true;
    }

    // menu Expand
    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        // 收拢的时候，要清空，并且复原ListView
        mSearchTerm = null;
        mTermChangedListener.onTermChanged(null);
        mPresenter.loadTasks(null);
        return true;
    }

    // mvp-view
    @Override
    public void setPresenter(TasksListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showList(Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void clearList() {
        showList(null);
    }

    @Override
    public void redirectTaskDetails(@NonNull Task task) {
        Intent intent = new Intent(getActivity(), TaskDetailsActivity.class);
        Bundle data = new Bundle();
        data.putSerializable(TaskDetailsActivity.KEY_TASK, task);
        intent.putExtras(data);
        startActivity(intent);
    }

    @Override
    public void showMoreMenu() {
        if (isAdded()) {
            BottomFragment fragment = BottomFragment.getInstance("选项", mMoreMenu);
            fragment.setSelectListener(new ItemSelectListener<Integer>() {
                @Override
                public void onSelected(Integer item) {
                    switch (item) {
                        case 0:
                            mPresenter.openTaskGroupDetails();
                            break;
                        case 1:
                            mPresenter.openEditTasksList();
                            break;
                        case 2:
                            mPresenter.createNewTask();
                        default:
                            break;
                    }
                }
            });
            fragment.show(getFragmentManager(), GROUP);
        }
    }

    @Override
    public void dismissMoreMenu() {
        if (isAdded()) {
            dismissDialog(GROUP);
        }
    }

    @Override
    public void redirectEditTasksList() {
        Intent intent = new Intent(getActivity(), EditTasksListActivity.class);
        Bundle data = new Bundle();
        data.putSerializable(EditTasksListActivity.KEY_EDIT_TASKS_LIST, mTaskGroup);
        intent.putExtras(data);
        startActivity(intent);
    }

    @Override
    public void redirectTaskGroupDetails() {
        // TODO
        App.showToast(Const.X);
    }

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
    public void redirectCreateTask() {
        Bundle data = new Bundle();
        data.putSerializable(AddTaskActivity.GROUP_KEY, mTaskGroup);
        Intent intent = new Intent(getActivity(), AddTaskActivity.class);
        intent.putExtras(data);
        startActivity(intent);
    }

    @Override
    public void showDeleteDialog() {
        MoveTaskDialog dialog = MoveTaskDialog.getInstance("将任务放入回收站，是否确认？");
        dialog.setListener(new DialogButtonListener() {
            @Override
            public void onNegative() {
                mPresenter.closeDeleteDialog();
            }

            @Override
            public void onNeutral() {
                //
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

    @Override
    public void showItemMenu(@NonNull Task task) {
        /* FIXME: Running态的任务有待处理.. */
        BottomFragment fragment = BottomFragment.getInstance(task.getName(), TASK_MENU);
        fragment.setSelectListener(new ItemSelectListener<Integer>() {
            @Override
            public void onSelected(Integer item) {
                switch (item) {
                    case 0: // 移动任务
                        mPresenter.openMoveDialog();
                        break;
                    case 1: // 收藏任务
                        mPresenter.openSaveDialog();
                        break;
                    case 2: // 删除任务
                        mPresenter.openDeleteDialog();
                        break;
                    default:
                        break;
                }
            }
        });
        fragment.show(getFragmentManager(), ITEM);
    }

    @Override
    public void dismissItemMenu() {
        dismissDialog(ITEM);
    }

    @Override
    public void showMoveDialog(@NonNull List<TaskGroup> groups) {
        TransformTaskDialog dialog = TransformTaskDialog.getInstance("移动分组");
        dialog.setListener(groups, new DialogButtonListener<TaskGroup>() {
            @Override
            public void onNegative() {
                mPresenter.closeMoveDialog();
            }

            @Override
            public void onNeutral() {
                // null
            }

            @Override
            public void onPositive() {
                // null
            }

            @Override
            public void onData(TaskGroup data) {
                mPresenter.doMove(data);
            }
        });
        dialog.show(getFragmentManager(), MOVE);
    }

    @Override
    public void dismissMoveDialog() {
        dismissDialog(MOVE);
    }

    @Override
    public void showSaveDialog() {
        MoveTaskDialog dialog = MoveTaskDialog.getInstance("移动任务到收藏，是否确认？");
        dialog.setListener(new DialogButtonListener() {
            @Override
            public void onNegative() {
                mPresenter.closeSaveDialog();
            }

            @Override
            public void onNeutral() {
                // null
            }

            @Override
            public void onPositive() {
                mPresenter.doSave();
            }

            @Override
            public void onData(Object data) {
                // null
            }
        });
        dialog.show(getFragmentManager(), SAVE);
    }

    @Override
    public void dismissSaveDialog() {
        dismissDialog(SAVE);
    }

    @Override
    public void showToast(String msg) {
        App.showToast(msg);
    }

    // Adapter
    @Override
    public void onEditSelected(Task item) {
        mPresenter.onItemEditSelected(item);
    }

    // Adapter
    @Override
    public void onSelected(Task item) {
        mPresenter.onItemSelected(item);
    }

    private void dismissDialog(String tag) {
        AppCompatDialogFragment fragment = (AppCompatDialogFragment) getFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            fragment.dismiss();
        }
    }
}
