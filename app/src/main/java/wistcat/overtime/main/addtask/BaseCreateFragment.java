package wistcat.overtime.main.addtask;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import sample.myresource.util.PlatformVersion;
import wistcat.overtime.R;
import wistcat.overtime.interfaces.CreateTaskListener;
import wistcat.overtime.widget.RippleFrameLayout;

/**
 * 任务创建Fragment基类
 *
 * @author wistcat 2016/8/25
 */
public abstract class BaseCreateFragment extends Fragment {

    private static final int ANIM_DURATION = 800;
    private CreateTaskListener mCreateListener;

    abstract int getTheme();
    abstract int getLayout();
    abstract String getMark();
    abstract int getTitle();
    abstract void initView(View layout);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateTaskListener) {
            mCreateListener = (CreateTaskListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        final View root = inflater.cloneInContext(
                new ContextThemeWrapper(getContext(), getTheme()))
                .inflate(getLayout(), container, false);

        // args
        final Bundle args = getArguments();
        if (args != null) {
            // 为 Fragment 在 add 时添加动画效果
            root.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int l, int t, int r, int b,
                                           int oldl, int oldt, int oldr, int oldb) {

                    root.removeOnLayoutChangeListener(this);
                    int cx = args.getInt(AddTaskActivity.ARG_CX);
                    int cy = args.getInt(AddTaskActivity.ARG_CY);
                    float radius = (float) Math.hypot(r, b);

                    createAnimator((RippleFrameLayout) root, cx, cy, 32, radius).start();
                }
            });
        }

        // toolbar
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        if (toolbar != null) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(getTitle());
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        initView(root);
        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && mCreateListener != null) {
            mCreateListener.goBack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCreateListener = null;
    }


    private Animator createAnimator(final RippleFrameLayout layout,
                                    int cx, int cy, float startRadius, float endRadius) {

        Animator animator;

        if (PlatformVersion.isLollipop()) {
            animator = ViewAnimationUtils.createCircularReveal(layout, cx, cy, startRadius, endRadius);
        } else {
            layout.setCenter(cx, cy);
            layout.setClipOutlines(true);
            layout.setRadius(startRadius);

            animator = ObjectAnimator.ofFloat(layout, "radius", startRadius, endRadius);
        }

        animator.setDuration(ANIM_DURATION);
        animator.setInterpolator(new LinearInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                layout.setClipOutlines(false);
                if (mCreateListener != null) {
                    mCreateListener.remove();
                }
            }
        });
        return animator;
    }

    public static Bundle createArgs(int cx, int cy) {
        Bundle args = new Bundle();
        args.putInt(AddTaskActivity.ARG_CX, cx);
        args.putInt(AddTaskActivity.ARG_CY, cy);
        return args;
    }

}
