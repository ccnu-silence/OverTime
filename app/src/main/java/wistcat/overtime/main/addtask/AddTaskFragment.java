package wistcat.overtime.main.addtask;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import wistcat.overtime.R;
import wistcat.overtime.interfaces.CreateTaskListener;

/**
 * 任务选择界面
 *
 * @author wistcat 2016/8/25
 */
public class AddTaskFragment extends Fragment implements View.OnClickListener {

    public static final String MARK = "Task";

    private CreateTaskListener mCreateListener;
    private Button bn_manual, bn_timing, bn_short, bn_limited;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateTaskListener) {
            mCreateListener = (CreateTaskListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_add_task, container, false);

        initToolbar(rootView);
        initButton(rootView);
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCreateListener = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && isAdded()) {
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar(View layout) {
        Toolbar toolbar = (Toolbar) layout.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.create_task);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initButton(View layout) {
        bn_manual = (Button) layout.findViewById(R.id.bn_manual);
        bn_timing = (Button) layout.findViewById(R.id.bn_timing);
        bn_short = (Button) layout.findViewById(R.id.bn_short);
        bn_limited = (Button) layout.findViewById(R.id.bn_limited);

        bn_manual.setOnClickListener(this);
        bn_timing.setOnClickListener(this);
        bn_short.setOnClickListener(this);
        bn_limited.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (bn_manual == view) {
            calcCenter(view, CreateManualFragment.MARK);
        }

        if (bn_timing == view) {
            calcCenter(view, CreateTimingFragment.MARK);
        }

        if (bn_short == view) {
            calcCenter(view, CreateShortFragment.MARK);
        }

        if (bn_limited == view) {
            calcCenter(view, CreateLimitedFragment.MARK);
        }
    }

    /* 根据按钮位置计算动画中心点 */
    private void calcCenter(View view, String tag) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);

        int cx = rect.centerX();
        int cy = rect.centerY() - getStatusBarHeight();
        postSwitch(cx, cy, tag);
    }

    private void postSwitch(final int cx, final int cy, final String tag) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (mCreateListener != null) {
                    mCreateListener.switchPage(cx, cy, tag);
                }
            }
        });
    }

    private int getStatusBarHeight() {
        int ret = 0;
        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            ret = getResources().getDimensionPixelOffset(resId);
        }
        return ret;
    }

}
