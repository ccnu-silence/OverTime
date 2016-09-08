package wistcat.overtime.base;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;

import wistcat.overtime.R;

/**
 * 用来显示底部Dialog动画效果的Fragment
 *
 * @author wistcat 2016/9/8
 */
public abstract class BaseBottomDialogFragment extends AppCompatDialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除标题栏
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        // 设置Dialog在底部显示
        window.setGravity(Gravity.BOTTOM);
        // 设置进出动画 (NOTE：必须在这里设置)
        window.setWindowAnimations(R.style.DialogAccessAnim);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        Window window = getDialog().getWindow();
        // 设置宽高
        int height = Math.min(window.getAttributes().height, dm.heightPixels/2);
        window.setLayout(dm.widthPixels, height);
        window.setBackgroundDrawable(new ColorDrawable(0xffffffff));
    }

}
