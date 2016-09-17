package wistcat.overtime.main.tasksmanage;

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
 * @author wistcat 2016/9/17
 */
public class DeleteGroupFragment extends AppCompatDialogFragment {

    public static final String DELETE = "delete";
    private String mAlert;
    private TasksManageContract.Presenter mPresenter;

    public static DeleteGroupFragment getInstance(String alert) {
        Bundle bundle = new Bundle();
        bundle.putString(DELETE, alert);
        DeleteGroupFragment fragment = new DeleteGroupFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setPresenter(TasksManageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle data = getArguments();
        mAlert = data.getString(DELETE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_delete_group, container, false);
        ((TextView)root.findViewById(R.id.description)).setText(mAlert);
        root.findViewById(R.id.positive).setOnClickListener(mBnListener);
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

    private View.OnClickListener mBnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.negative:
                    mPresenter.closeDeleteDialog();
                    break;
                case R.id.positive:
                    mPresenter.deleteTaskGroup();
                    break;
                default:
                    break;
            }
        }
    };

}
