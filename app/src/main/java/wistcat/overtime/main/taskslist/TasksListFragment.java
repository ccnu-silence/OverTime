package wistcat.overtime.main.taskslist;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import wistcat.overtime.R;
import wistcat.overtime.adapter.TasksListAdapter;
import wistcat.overtime.base.BottomFragment;
import wistcat.overtime.interfaces.ItemSelectListener;
import wistcat.overtime.interfaces.OnSearchTermChanged;
import wistcat.overtime.main.edittaskslist.EditTasksListActivity;
import wistcat.overtime.main.taskdetail.TaskDetailsActivity;
import wistcat.overtime.model.Task;
import wistcat.overtime.model.TaskGroup;

/**
 * 任务列表页，从TaskGroup列表打开
 *
 * @author wistcat 2016/9/12
 */
public class TasksListFragment extends ListFragment implements TasksListContract.View,
        SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener, ItemSelectListener<Integer> {

    private final String[] mMoreMenu = new String[]{"编辑分组", "管理任务"};
    private TaskGroup mTaskGroup;
    private OnSearchTermChanged mTermChangedListener;
    private TasksListContract.Presenter mPresenter;
    private FloatingActionButton mFab;
    private String mSearchTerm;

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
        TasksListAdapter adapter = new TasksListAdapter(getContext(), mPresenter);
        mTermChangedListener = adapter;
        setListAdapter(adapter);

        // state
        if (savedInstanceState != null) {
            mSearchTerm = savedInstanceState.getString(SearchManager.QUERY);
            mTermChangedListener.onTermChanged(mSearchTerm);
        }

        // floating
        mFab = (FloatingActionButton) root.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.scrollTop();
            }
        });
        return root;
    }

    private AbsListView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            // nothing
        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(getListView().getLastVisiblePosition() > 20 && mFab != null && mFab.getVisibility() == View.GONE){
                mFab.show();
            }else if(getListView().getLastVisiblePosition() <= 20 && mFab != null && mFab.getVisibility() == View.VISIBLE){
                mFab.hide();
            }
        }
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setOnScrollListener(mScrollListener);
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
            // 接收初始化参数时... TODO 复用的时候会用到吧，具体场景还不清楚...
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
        if(TextUtils.isEmpty(mSearchTerm)){
            getListView().clearChoices();
        }
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
    public int getGroupId() {
        return mTaskGroup.getId();
    }

    @Override
    public void showList(Cursor cursor) {
        CursorAdapter adapter = (CursorAdapter) getListAdapter();
        if (adapter != null) {
            adapter.swapCursor(cursor);
        }
    }

    @Override
    public void clearList() {
        showList(null);
    }

    @Override
    public void showTaskDetails(@NonNull Task task) {
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
            fragment.setSelectListener(this);
            fragment.show(getFragmentManager(), BottomFragment.TAG);
        }
    }

    @Override
    public void hideMoreMenu() {
        if (isAdded()) {
            BottomFragment fragment = (BottomFragment) getFragmentManager().findFragmentByTag(BottomFragment.TAG);
            if (fragment != null) {
                fragment.dismiss();
            }
        }
    }

    @Override
    public void showEditTasksList() {
        Intent intent = new Intent(getActivity(), EditTasksListActivity.class);
        Bundle data = new Bundle();
        data.putSerializable(EditTasksListActivity.KEY_EDIT_TASKS_LIST, mTaskGroup);
        startActivity(intent);
    }

    @Override
    public void showTaskGroupDetails() {
        // TODO
    }

    @Override
    public void showScrollUp() {
        ListView list = getListView();
        if (list != null) {
            list.smoothScrollBy(0, 0);
            list.setSelectionAfterHeaderView();
        }
    }

    @Override
    public void onSelected(Integer item) {
        switch (item) {
            case 0:
                mPresenter.openTaskGroupDetails();
                break;
            case 1:
                mPresenter.openEditTasksList();
                break;
            default:
                break;
        }
    }
}
