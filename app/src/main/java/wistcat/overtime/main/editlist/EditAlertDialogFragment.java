package wistcat.overtime.main.editlist;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import wistcat.overtime.R;

/**
 * 警告Dialog
 *
 * @author wistcat 2016/9/10
 */
public class EditAlertDialogFragment extends AppCompatDialogFragment {

    public static final String ALERT = "alert";
    private String mAlert;
    private EditListContract.Presenter mPresenter;

    private View.OnClickListener mBnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.positive:
                    mPresenter.doCancel();
                    break;
                case R.id.neutral:
                    mPresenter.doQuit();
                    break;
                case R.id.negative:
                    mPresenter.cancelDialog();
                    break;
                default:
                    break;
            }
        }
    };

    public static EditAlertDialogFragment getInstance(String alert) {
        Bundle bundle = new Bundle();
        bundle.putString(ALERT, alert);
        EditAlertDialogFragment fragment = new EditAlertDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setPresenter(EditListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle data = getArguments();
        mAlert = data.getString(ALERT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_alert_dialog, container, false);
        ((TextView)root.findViewById(R.id.description)).setText(mAlert);
        root.findViewById(R.id.positive).setOnClickListener(mBnListener);
        root.findViewById(R.id.neutral).setOnClickListener(mBnListener);
        root.findViewById(R.id.negative).setOnClickListener(mBnListener);
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
}
