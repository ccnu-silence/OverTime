package wistcat.overtime.main.editlist;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.CursorAdapter;

import java.text.MessageFormat;
import java.util.List;

import wistcat.overtime.App;
import wistcat.overtime.R;
import wistcat.overtime.adapter.EditListAdapter;
import wistcat.overtime.interfaces.GetDataListCallback;

/**
 * 显示列表编辑
 *
 * @author wistcat 2016/9/9
 */
public class EditListFragment extends ListFragment implements EditListContract.View {

    private final String TITLE = "已选择{0}项";
    private EditListContract.Presenter mPresenter;
    private ActionBar mActionBar;
    private boolean isAllSelected = true;

    public static EditListFragment getInstance() {
        return new EditListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_list, container, false);

        // toolbar
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        if (toolbar != null) {
            setHasOptionsMenu(true);
            // actionBar
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbar);
            mActionBar = activity.getSupportActionBar();
            if (mActionBar != null) {
                mActionBar.setDisplayHomeAsUpEnabled(true);
                mActionBar.setTitle(MessageFormat.format(TITLE, 0));
            }
        }
        // listView
        final EditListAdapter adapter = new EditListAdapter(getContext(), mPresenter);
        setListAdapter(adapter);

        // button
        View button = root.findViewById(R.id.confirm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.askForCancel();
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
        inflater.inflate(R.menu.menu_list_all_select, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.action_select_all:
                mPresenter.selectAll();
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
    public void showList(Cursor cursor) {
        CursorAdapter adapter = (CursorAdapter) getListAdapter();
        adapter.swapCursor(cursor);
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
        EditListAdapter adapter = (EditListAdapter) getListAdapter();
        if (adapter != null) {
            adapter.setSelectedList(list);
        }
    }

    @Override
    public void getAdapterState(@NonNull GetDataListCallback<Integer> callback) {
        EditListAdapter adapter = (EditListAdapter) getListAdapter();
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
    public void showMenuAllSelected() {
        isAllSelected = true;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void showMenuReverseAllSelected() {
        isAllSelected = false;
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void showAlertDialog() {
        if (isAdded()) {
            EditAlertDialogFragment f = EditAlertDialogFragment.getInstance(
                    "删除任务组会将其中的任务放入回收站，确认删除？");
            f.setPresenter(mPresenter);
            f.show(getFragmentManager(), "Delete");
        }
    }

    @Override
    public void showErrorToast(String str) {
        App.showToast(str);
    }

    @Override
    public void dismissAlertDialog() {
        if (isAdded()) {
            EditAlertDialogFragment f = (EditAlertDialogFragment)
                    getFragmentManager().findFragmentByTag("Delete");
            f.dismiss();
        }
    }

    @Override
    public void quit() {
        if (isAdded()) {
            getActivity().finish();
        }
    }

    @Override
    public void setPresenter(EditListContract.Presenter presenter) {
        mPresenter = presenter;
    }

}
