package wistcat.overtime.main.tasksmanage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.KeyEvent;

import javax.inject.Inject;

import wistcat.overtime.App;
import wistcat.overtime.R;
import wistcat.overtime.base.BottomFragment;
import wistcat.overtime.interfaces.ItemSelectListener;

public class TasksManageActivity extends AppCompatActivity implements ItemSelectListener<Integer> {

    private String[] mMoreItems = new String[]{"添加任务组", "管理任务组"};

    @Inject
    public TasksManagePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        // fragment
        TasksManageFragment fragment =
                (TasksManageFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = new TasksManageFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }

        // dagger
        TasksManageComponent component = DaggerTasksManageComponent
                .builder()
                .appComponent(App.getInstance().getAppComponent())
                .tasksManageModule(new TasksManageModule(getSupportLoaderManager(), fragment))
                .build();
        component.inject(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_MENU || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSelected(Integer item) {
        switch (item) {
            case 0:
                mPresenter.openAddDialog();
                break;
            case 1:
                mPresenter.openEditList();
                break;
            default:
                break;
        }
    }

    public void popupMoreMenu() {
        BottomFragment fragment = BottomFragment.getInstance("更多选项", mMoreItems);
        fragment.setSelectListener(this);
        fragment.show(getSupportFragmentManager(), "MoreMenu");
    }

    public void popupAddDialog() {
        AddTaskGroupFragment fragment = AddTaskGroupFragment.getInstance();
        fragment.setPresenter(mPresenter);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(getSupportFragmentManager().findFragmentByTag("MoreMenu"));
        fragment.show(transaction, "AddTaskGroup");
    }

    public void dismissFragment(@NonNull String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            AppCompatDialogFragment f = (AppCompatDialogFragment) fragment;
            f.dismiss();
        }
    }

}
