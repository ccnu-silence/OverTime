package wistcat.overtime.main.tasksmanage;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import wistcat.overtime.App;
import wistcat.overtime.R;
import wistcat.overtime.adapter.TaskGroupsAdapter;
import wistcat.overtime.base.BottomFragment;
import wistcat.overtime.interfaces.EditItemSelectListener;
import wistcat.overtime.interfaces.ItemSelectListener;
import wistcat.overtime.main.editlist.EditListActivity;
import wistcat.overtime.main.taskslist.TasksListActivity;
import wistcat.overtime.model.TaskGroup;
import wistcat.overtime.util.ColorHelper;
import wistcat.overtime.widget.DividerItemDecoration;

/**
 * 任务管理页面
 *
 * @author wistcat 2016/9/5
 */
public class TasksManageFragment extends Fragment implements TasksManageContract.View, EditItemSelectListener<TaskGroup> {
    private static final String MENU_TAG = "Bottom_sheet_menu";
    private static final String CREATE_TAG = "Create_new_group";
    private static final String DELETE_TAG = "Delete_task_group";
    private String[] EDIT_GROUP = new String[]{"添加任务组", "管理任务组"};
    private String[] EDIT_ITEM = new String[]{"编辑任务组", "删除任务组"};

    private TasksManageContract.Presenter mPresenter;
    private TaskGroupsAdapter mAdapter;
    private ColorDrawable mToolbarBackground;
    private ColorHelper mColorHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tasks_manage, container, false);
        // toolbar
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        if (toolbar != null) {
            setHasOptionsMenu(true);
            // actionBar
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(R.string.title_tasks_manage);
            }
            // 获取Toolbar背景Drawable，用于手动调整颜色
            mToolbarBackground = (ColorDrawable) toolbar.getBackground();
            mColorHelper = new ColorHelper(mToolbarBackground.getColor(), 0x44000000, 0, 240);
        }

        // more
        TextView tmore = (TextView) root.findViewById(R.id.more_title);
        tmore.setText("任务组");
        final View more = root.findViewById(R.id.more_menu);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.openEditMenu();
            }
        });

        // footer
        final View footer = root.findViewById(R.id.footer);
        final View content = root.findViewById(R.id.content);

        // scrollview
        final NestedScrollView scrollView = (NestedScrollView) root.findViewById(R.id.scrollview);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int y = v.getScrollY();
                if (mToolbarBackground != null && mColorHelper != null) {
                    // 根据ScrollView的Y轴偏移，设置Toolbar背景色（colorPrimary -> 半透明）

                    int color = mColorHelper.getColor(y);
                    mToolbarBackground.setColor(color);
                    mToolbarBackground.invalidateSelf();
                }
            }
        });

        // RecyclerView & adapter
        mAdapter = new TaskGroupsAdapter(getContext().getApplicationContext(), this);
        LinearLayoutManager adapterManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.list);
        // 设置ItemDecoration，用于绘制分割线
        int left = getResources().getDimensionPixelOffset(R.dimen.default_list_item_seq_width);
        DividerItemDecoration itemDecoration =
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL, left, 0);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(adapterManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false); // FIXME: 取消同时滚动，似乎仍有滚动不流畅的情况...
        recyclerView.setAdapter(mAdapter);

        // 在列表选项增减的时候，显示/隐藏底部文字
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int l, int t, int r, int b, int oldl, int oldt, int oldr, int oldb) {
                int height = b - t;
                DisplayMetrics dm = getResources().getDisplayMetrics();
                int dp = (int) (dm.heightPixels * 0.67);
                if (height > dp && footer.getVisibility() == View.GONE) {
                    footer.setVisibility(View.VISIBLE);
                    // 初始化时需要的处理
                    content.post(new Runnable() {
                        @Override
                        public void run() {
                            // 通知重新测量和绘制
                            content.requestLayout();
                        }
                    });
                } else if (height <= dp && footer.getVisibility() == View.VISIBLE) {
                    footer.setVisibility(View.GONE);
                }
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_taskgroup_more, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.action_completed:
                mPresenter.openCompletedList();
                return true;
            case R.id.action_recycled:
                mPresenter.openRecycledList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Override
    public void setPresenter(TasksManageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onSelected(TaskGroup item) {
        mPresenter.openTasksList(item);
    }

    @Override
    public void onEditSelected(TaskGroup item) {
        mPresenter.openItemEditMenu(item);
    }

    @Override
    public void showList(Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void showCreateDialog() {
        // 创建对话框
        AddTaskGroupFragment dialog = AddTaskGroupFragment.getInstance();
        dialog.setPresenter(mPresenter);
        dialog.show(getFragmentManager(), CREATE_TAG);
    }

    @Override
    public void redirectGroupsManage() {
        Intent intent = new Intent(getActivity(), EditListActivity.class);
        // TODO maybe add flags
        startActivity(intent);
    }

    @Override
    public void redirectCompletedList(@NonNull TaskGroup group) {
        redirectTasksList(group);
    }

    @Override
    public void redirectRecycledList(@NonNull TaskGroup group) {
        redirectTasksList(group);
    }

    @Override
    public void redirectTasksList(@NonNull TaskGroup group) {
        Intent intent = new Intent(getActivity(), TasksListActivity.class);
        Bundle data = new Bundle();
        data.putSerializable(TasksListActivity.KEY_TASK_GROUP, group);
        intent.putExtras(data);
        startActivity(intent);
    }

    @Override
    public void showEditMenu() {
        BottomFragment menu = BottomFragment.getInstance("更多选项", EDIT_GROUP);
        menu.setSelectListener(new ItemSelectListener<Integer>() {
            @Override
            public void onSelected(Integer item) {
                mPresenter.onMenuSelected(item);
            }
        });
        menu.show(getFragmentManager(), MENU_TAG);
    }

    @Override
    public void showItemEditMenu() {
        BottomFragment menu = BottomFragment.getInstance("我的分组", EDIT_ITEM);
        menu.setSelectListener(new ItemSelectListener<Integer>() {
            @Override
            public void onSelected(Integer item) {
                mPresenter.onMenuSelected(item);
            }
        });
        menu.show(getFragmentManager(), MENU_TAG);
    }

    @Override
    public void clearCursor() {
        showList(null);
    }

    @Override
    public void dismissCreateDialog() {
        dismissDialog(CREATE_TAG);
    }

    @Override
    public void showGroupDetails() {
        App.showToast("未完成");
        // TODO...
    }

    @Override
    public void showDeleteDialog() {
        DeleteGroupFragment dialog = DeleteGroupFragment.getInstance(
                "删除任务组会将其中的任务放入回收站，是否确定删除?");
        dialog.setPresenter(mPresenter);
        dialog.show(getFragmentManager(), DELETE_TAG);
    }

    @Override
    public void dismissDeleteDialog() {
        dismissDialog(DELETE_TAG);
    }

    @Override
    public void dismissMenu() {
        dismissDialog(MENU_TAG);
    }

    @Override
    public void showToast(String msg) {
        App.showToast(msg);
    }

    private void dismissDialog(String tag) {
        AppCompatDialogFragment menu = (AppCompatDialogFragment)
                getFragmentManager().findFragmentByTag(tag);
        if (menu != null) {
            menu.dismiss();
        }
    }

}
