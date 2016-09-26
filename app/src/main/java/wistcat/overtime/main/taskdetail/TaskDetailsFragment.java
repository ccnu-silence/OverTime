package wistcat.overtime.main.taskdetail;

import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wistcat.overtime.R;
import wistcat.overtime.adapter.RecordsListAdapter;
import wistcat.overtime.interfaces.EditItemSelectListener;
import wistcat.overtime.model.Record;
import wistcat.overtime.model.Task;
import wistcat.overtime.util.ColorHelper;
import wistcat.overtime.util.Utils;
import wistcat.overtime.widget.DividerItemDecoration;

/**
 * @author wistcat 2016/9/22
 */
public class TaskDetailsFragment extends Fragment implements TaskDetailsContract.View, EditItemSelectListener<Record> {

    private Task mTask;
    private TaskDetailsContract.Presenter mPresenter;
    private RecordsListAdapter mAdapter;

    public static TaskDetailsFragment getInstance(@NonNull Task task) {
        Bundle data = new Bundle();
        data.putSerializable(TaskDetailsActivity.KEY_TASK, task);
        TaskDetailsFragment fragment = new TaskDetailsFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        mTask = (Task) data.getSerializable(TaskDetailsActivity.KEY_TASK);
    }

    @Override
    public View  onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LayoutInflater wrapperedInflater = inflater.cloneInContext(
                new ContextThemeWrapper(getContext(), getThemeRes(mTask.getType())));
        View root = wrapperedInflater.inflate(R.layout.fragment_task_details, container, false);

        // toolbar
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        // 获取Toolbar背景Drawable，用于手动调整颜色
        final ColorDrawable mToolbarBackground = (ColorDrawable) toolbar.getBackground();
        final ColorHelper mColorHelper = new ColorHelper(mToolbarBackground.getColor(), 0x44000000, 0, 240);
        // actionBar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar ab = activity.getSupportActionBar();
        if (ab != null) {
            setHasOptionsMenu(true);
            ab.setTitle("任务详情");
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // scrollView
        NestedScrollView scrollView = (NestedScrollView) root.findViewById(R.id.scrollview);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int y = v.getScrollY();
                // 根据ScrollView的Y轴偏移，设置Toolbar背景色（colorPrimary -> 半透明）
                int color = mColorHelper.getColor(y);
                mToolbarBackground.setColor(color);
                mToolbarBackground.invalidateSelf();
            }
        });

        // name
        TextView name = (TextView) root.findViewById(R.id.task_name);
        name.setText(mTask.getName());

        // header
        LinearLayout header = (LinearLayout) root.findViewById(R.id.task_details);
        initHeader(wrapperedInflater, header);

        // record edit
        TextView more_title = (TextView) root.findViewById(R.id.more_title);
        more_title.setText("我的记录");
        root.findViewById(R.id.more_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
            }
        });

        // recyclerView
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.list);
        mAdapter = new RecordsListAdapter(getContext(), this);
        initRecyclerView(recyclerView);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(TaskDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onEditSelected(Record item) {
        // TODO
    }

    @Override
    public void onSelected(Record item) {
        // TODO
    }

    // ----------

    private int getThemeRes(int taskType) {
        switch (taskType) {
            case Task.TYPE_MANUAL:
                return R.style.ManualTaskStyle;
            case Task.TYPE_LIMITED:
                return R.style.LimitedTaskStyle;
            case Task.TYPE_SHORT:
                return R.style.ShortTaskStyle;
            case Task.TYPE_TIMING:
                return R.style.TimingTaskStyle;
            default:
                throw new IllegalArgumentException("无法识别的Task类型");
        }
    }

    private void initRecyclerView(RecyclerView view) {
        LinearLayoutManager adapterManager = new LinearLayoutManager(getContext());
        // 设置ItemDecoration，用于绘制分割线
        int left = getResources().getDimensionPixelOffset(R.dimen.default_task_item_seq_width);
        DividerItemDecoration itemDecoration =
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL, left, 0);
        view.addItemDecoration(itemDecoration);
        view.setLayoutManager(adapterManager);
        view.setItemAnimator(new DefaultItemAnimator());
        view.setHasFixedSize(true);
        view.setNestedScrollingEnabled(false);
        view.setAdapter(mAdapter);
    }

    private void initHeader(LayoutInflater inflater, LinearLayout layout) {
        List<String[]> data = new ArrayList<>();
        String group = mTask.getGroupName();
        int type = mTask.getType();
        String date = mTask.getCreateTime();
        String sum = Utils.getSumTime(mTask.getSumTime());
        data.add(new String[]{"任务组: ", group});
        data.add(new String[]{"任务类型: ", getTaskType(type)});
        data.add(new String[]{"开始时间: ", date});
        data.add(new String[]{"累计任务时间: ", sum});

        addHeaderItem(inflater, layout, 0, data);
    }

    private void addHeaderItem(final LayoutInflater inflater, final LinearLayout layout,
                               final int id, final List<String[]> data) {
        if (id >= data.size()) {
            // TODO
            return;
        }
        layout.postDelayed(new Runnable() {
            @Override
            public void run() {
                final String[] strs = data.get(id);
                final ViewGroup newView =
                        (ViewGroup) inflater.inflate(R.layout.list_item_task_header, layout, false);
                TextView t1 = (TextView) newView.findViewById(R.id.text1);
                t1.setText(strs[0]);
                TextView t2 = (TextView) newView.findViewById(R.id.text2);
                t2.setText(strs[1]);
                layout.addView(newView);
                addHeaderItem(inflater, layout, id + 1, data);
            }
        }, 200);
    }

    private String getTaskType(int type) {
        switch (type) {
            case Task.TYPE_MANUAL:
                return "长期任务";
            case Task.TYPE_LIMITED:
                return "定期任务";
            case Task.TYPE_SHORT:
                return "短期任务";
            case Task.TYPE_TIMING:
                return "定时任务";
            default:
                throw new IllegalArgumentException("无法识别的Task类型");
        }
    }

    @Override
    public void showList(Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void clearList() {
        showList(null);
    }
}
