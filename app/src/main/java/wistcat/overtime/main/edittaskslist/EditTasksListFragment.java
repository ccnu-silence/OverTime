package wistcat.overtime.main.edittaskslist;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import java.text.MessageFormat;
import java.util.List;

import wistcat.overtime.App;
import wistcat.overtime.R;
import wistcat.overtime.adapter.EditTasksListAdapter;
import wistcat.overtime.base.BottomFragment;
import wistcat.overtime.interfaces.GetDataListCallback;
import wistcat.overtime.interfaces.ItemSelectListener;

/**
 * @author wistcat 2016/9/12
 */
public class EditTasksListFragment extends ListFragment
        implements EditTasksListContract.View, ItemSelectListener<Integer>{

    /* TODO: 不展示正在执行中的任务 */

    public static final String TAG = "EditTasksList";
    private EditTasksListContract.Presenter mPresenter;
    private final String TITLE = "已选择{0}项";
    private ActionBar mActionBar;
    private boolean isAllSelected = true;

    public static EditTasksListFragment getInstance() {
        return  new EditTasksListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceSate) {
        super.onCreate(savedInstanceSate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View root = inflater.inflate(R.layout.fragment_edit_tasks_list, container, false);

        // toolbar
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        if (toolbar != null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            setHasOptionsMenu(true);
            activity.setSupportActionBar(toolbar);
            // actionBar
            mActionBar = activity.getSupportActionBar();
            if (mActionBar != null) {
                mActionBar.setDisplayHomeAsUpEnabled(true);
                mActionBar.setTitle(MessageFormat.format(TITLE, 0));
            }
        }

        // listView
        EditTasksListAdapter adapter = new EditTasksListAdapter(getActivity(), this);
        setListAdapter(adapter);

        // bottomSheet
        View bottomSheet = root.findViewById(R.id.bottom_sheet);
        bottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.loadBottomSheet();
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_list_all_select, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.action_select_all:
                mPresenter.doSelectAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (isAllSelected) {
            menu.findItem(R.id.action_select_all).setTitle(R.string.select_all);
        } else {
            menu.findItem(R.id.action_select_all).setTitle(R.string.select_all_reverse);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setPresenter(EditTasksListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onSelected(Integer count) {
        mPresenter.doItemChanged(count);
    }

    @Override
    public void showList(Cursor data) {
        CursorAdapter adapter = (CursorAdapter) getListAdapter();
        if (adapter != null) {
            adapter.swapCursor(data);
        }
    }

    @Override
    public void clearList() {
        showList(null);
    }

    @Override
    public void showMenuAllSelect() {
        isAllSelected = true;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void showMenuReverseAllSelect() {
        isAllSelected = false;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void showSelectedCount(int count) {
        if (mActionBar != null) {
            String title = MessageFormat.format(TITLE, count);
            mActionBar.setTitle(title);
        }
    }

    @Override
    public void showAdapterChanged(List<Integer> list) {
        EditTasksListAdapter adapter = (EditTasksListAdapter) getListAdapter();
        if (adapter != null) {
            adapter.setSelectedList(list);
        }
    }

    @Override
    public void getAdapterState(@NonNull GetDataListCallback<Integer> callback) {
        EditTasksListAdapter adapter = (EditTasksListAdapter) getListAdapter();
        if (adapter != null) {
            List<Integer> data = adapter.getSelectedList();
            if (data == null) {
                callback.onError();
            } else {
                callback.onDataLoaded(data);
            }
        }
    }

    @Override
    public void showHandleDialog(int groupType) {
        HandleSelectedFragment fragment = HandleSelectedFragment.getInstance(groupType);
        fragment.setPresenter(mPresenter);
        fragment.show(getFragmentManager(), HandleSelectedFragment.TAG);
    }

    @Override
    public void showBottomSheet(@NonNull String title, @NonNull String[] items) {
        BottomFragment fragment = BottomFragment.getInstance(title, items);
        fragment.setSelectListener(new ItemSelectListener<Integer>() {
            @Override
            public void onSelected(Integer item) {
                mPresenter.askForHandle(item);
            }
        });
        fragment.show(getFragmentManager(), BottomFragment.TAG);
    }

    @Override
    public void showErrorToast(String msg) {
        App.showToast(msg);
    }

    @Override
    public void dismissDialog(String tag) {
        AppCompatDialogFragment fragment = (AppCompatDialogFragment) getFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            fragment.dismiss();
        }
    }

    @Override
    public void quit() {
        if (isAdded()) {
            getActivity().finish();
        }
    }
}
