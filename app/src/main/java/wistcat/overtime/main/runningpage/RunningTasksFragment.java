package wistcat.overtime.main.runningpage;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import wistcat.overtime.R;
import wistcat.overtime.adapter.RunningTasksAdapter;
import wistcat.overtime.interfaces.EditItemSelectListener;
import wistcat.overtime.model.Record;
import wistcat.overtime.widget.DividerItemDecoration;

/**
 * @author wistcat 2016/9/25
 */
public class RunningTasksFragment extends Fragment implements RunningTasksContract.View {

    private RunningTasksContract.Presenter mPresenter;
    private RunningTasksAdapter mAdapter;

    public static RunningTasksFragment getInstance() {
        return new RunningTasksFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_running_tasks, container, false);

        // Toolbar
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        // actionBar
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            setHasOptionsMenu(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("任务运行列表");
        }

        // adapter
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.list);
        mAdapter = new RunningTasksAdapter(getContext(), new EditItemSelectListener<Record>() {
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
        inflater.inflate(R.menu.menu_running_tasks, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_stop_all:
                // TODO;
                return true;
            case android.R.id.home:
                // TODO
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setPresenter(RunningTasksContract.Presenter presenter) {
        mPresenter = presenter;
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
    public void redirectRunningRecord(@NonNull Record record) {
        // TODO
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
}
