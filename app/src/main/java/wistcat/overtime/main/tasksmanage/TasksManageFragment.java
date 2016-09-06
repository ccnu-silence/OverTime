package wistcat.overtime.main.tasksmanage;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import wistcat.overtime.R;
import wistcat.overtime.interfaces.ItemSelectListener;
import wistcat.overtime.model.TaskGroup;

/**
 * 任务管理页面
 *
 * @author wistcat 2016/9/5
 */
public class TasksManageFragment extends ListFragment implements TasksManageContract.View, ItemSelectListener<TaskGroup> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tasks_manage, container, false);
        // toolbar
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.inflateMenu(R.menu.menu_taskgroup_more);
            setHasOptionsMenu(true);
//            toolbar.setOnMenuItemClickListener(mMenuItemListener);
            // actionBar
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(R.string.title_tasks_manage);
            }
        }
        return root;
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
                // TODO
                return true;
            case R.id.action_recycled:
                // TODO
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void setPresenter(TasksManageContract.Presenter presenter) {

    }

    @Override
    public void onSelected(TaskGroup item) {

    }

    @Override
    public void showTaskGroups(Cursor data) {

    }

    @Override
    public void showCreateDialog() {

    }

    @Override
    public void showGroupManage() {

    }

    @Override
    public void showCompletedTasks() {

    }

    @Override
    public void showRecycledTasks() {

    }

    @Override
    public void showTaskList(int groupId) {

    }

    @Override
    public void clearCursor() {

    }
}
