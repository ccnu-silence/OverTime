package wistcat.overtime.main.welcome;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;

import wistcat.overtime.R;
import wistcat.overtime.widget.spans.MutableForegroundColorSpan;
import wistcat.overtime.widget.spans.MutableSpanGroup;

/**
 * @author wistcat
 */
public class FireworkFragment extends Fragment {

    private static final int DURATION = 1400;
    private static final int DELAYED = 200;
    private TextView mPreface;
    private AtomicBoolean mCompleted = new AtomicBoolean(false);

    private WelcomeActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (WelcomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_firework, parent, false);
        mPreface = (TextView) root.findViewById(R.id.text);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkAndLoad();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    private void checkAndLoad() {
        // 动画
        animPreface();
        //
        initAccount();
    }

    /* 欢迎页面的文字动画效果 */
    private void animPreface() {
        final SpannableString ss = new SpannableString(getString(R.string.app_name));
        int color = ContextCompat.getColor(getActivity(), R.color.White);

        final int textLength = ss.length();
        final MutableSpanGroup spanGroup = new MutableSpanGroup();
        for (int i = 0; i < textLength; i++) {
            MutableForegroundColorSpan span = new MutableForegroundColorSpan(0, color);
            spanGroup.addSpan(span);
            ss.setSpan(span, i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        spanGroup.shuffle();

        // 设置动画
        ObjectAnimator anim = ObjectAnimator.ofFloat(spanGroup, "progress", 0.f, 1.f);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mPreface.setText(ss);
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doCompleted();
                    }
                }, DELAYED);

            }
        });
        anim.setDuration(DURATION);
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
    }

    private void initAccount() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //TODO
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                doCompleted();
            }
        }).start();
    }

    private void doCompleted() {
        if (!mCompleted.compareAndSet(false, true)) {
            redirect();
        }
    }

    private void redirect() {
        mActivity.redirect();
    }

}
