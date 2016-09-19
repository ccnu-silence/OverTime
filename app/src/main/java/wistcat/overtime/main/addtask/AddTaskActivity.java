package wistcat.overtime.main.addtask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import java.util.List;

import javax.inject.Inject;

import wistcat.overtime.App;
import wistcat.overtime.R;
import wistcat.overtime.interfaces.CreateTaskListener;
import wistcat.overtime.main.addtask.limited.CreateLimitedFragment;
import wistcat.overtime.main.addtask.manual.CreateManualFragment;
import wistcat.overtime.main.addtask.shortly.CreateShortFragment;
import wistcat.overtime.main.addtask.timing.CreateTimingFragment;
import wistcat.overtime.model.TaskGroup;


/**
 * 创建新任务
 *
 * @author wistcat
 */
public class AddTaskActivity extends AppCompatActivity implements CreateTaskListener {

    public static final String GROUP_KEY = "Group";
    public static final String ARG_CX = "cx";
    public static final String ARG_CY = "cy";
    private TaskGroup mGroup;

    // 注意不能是接口
    @Inject
    public CreateTaskPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new AddTaskFragment(), AddTaskFragment.MARK)
                .commit();

        Bundle data = getIntent().getExtras();
        if (data != null) {
            mGroup = (TaskGroup) data.getSerializable(GROUP_KEY);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO ...
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void switchPage(int cx, int cy, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        BaseCreateFragment fragment;
        switch (tag) {
            case CreateManualFragment.MARK:
                fragment = CreateManualFragment.getInstance(cx, cy, mGroup);
                break;
            case CreateTimingFragment.MARK:
                fragment = CreateTimingFragment.getInstance(cx, cy, mGroup);
                break;
            case CreateShortFragment.MARK:
                fragment = CreateShortFragment.getInstance(cx, cy, mGroup);
                break;
            case CreateLimitedFragment.MARK:
                fragment = CreateLimitedFragment.getInstance(cx, cy, mGroup);
                break;
            default:
                throw new IllegalArgumentException();
        }
        // dagger
        TaskViewModule viewModule = new TaskViewModule(fragment);
        CreateTaskComponent component = DaggerCreateTaskComponent
                .builder()
                .appComponent(App.getInstance().getAppComponent())
                .taskViewModule(viewModule)
                .build();
        component.inject(this);
        ft.add(R.id.container, fragment, tag);
        ft.commit();
    }

    @Override
    public void goBack() {
        // 返回到 AddTaskFragment
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.container, new AddTaskFragment(), AddTaskFragment.MARK)
                .commit();
    }

    @Override
    public void remove() {
        // 添加所选的Fragment后，删除 AddTaskFragment
        List<Fragment> frags = getSupportFragmentManager().getFragments();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (Fragment f : frags) {
            if (f == null) {
                continue;
            }
            // getTag 返回的是在 add/replace 时添加的 Tag
            if (AddTaskFragment.MARK.equals(f.getTag())){
                ft.remove(f);
            }
        }
        ft.commit();
    }
}
