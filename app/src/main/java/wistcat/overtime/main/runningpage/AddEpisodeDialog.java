package wistcat.overtime.main.runningpage;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import wistcat.overtime.R;
import wistcat.overtime.interfaces.DialogButtonListener;

/**
 * 添加Episode
 *
 * @author wistcat 2016/9/28
 */
public class AddEpisodeDialog extends AppCompatDialogFragment {

    public static final String ADD = "Add";
    private String mTime;
    private TextInputEditText mInput;
    private DialogButtonListener<String> mListener;

    public static AddEpisodeDialog getInstance(String time) {
        Bundle bundle = new Bundle();
        bundle.putString(ADD, time);
        AddEpisodeDialog fragment = new AddEpisodeDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setDialogListener(DialogButtonListener<String> listener) {
        mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle data = getArguments();
        mTime = data.getString(ADD);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_episode, container, false);
        ((TextView)root.findViewById(R.id.time)).setText(mTime);
        mInput = (TextInputEditText) root.findViewById(R.id.input);
        root.findViewById(R.id.positive).setOnClickListener(mBnListener);
        root.findViewById(R.id.negative).setOnClickListener(mBnListener);
        // keyboard
        popupKeyboard(mInput);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        Window window = getDialog().getWindow();
        window.setLayout((int) (dm.widthPixels * 0.8), window.getAttributes().height);
    }

    /** 显示软键盘 */
    private void popupKeyboard(final EditText edit) {
        edit.requestFocus();
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        edit.post(new Runnable() {
            @Override
            public void run() {
                imm.showSoftInput(edit, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    private View.OnClickListener mBnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.negative:
                    mListener.onNegative();
                    break;
                case R.id.positive:
                    mListener.onData(mInput.getText().toString());
                    break;
                default:
                    break;
            }
        }
    };
}
