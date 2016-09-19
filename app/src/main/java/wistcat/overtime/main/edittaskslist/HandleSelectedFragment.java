package wistcat.overtime.main.edittaskslist;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;

import wistcat.overtime.R;
import wistcat.overtime.adapter.SpinnerAdapter;
import wistcat.overtime.model.TaskGroup;
import wistcat.overtime.util.Const;

/**
 * @author wistcat 2016/9/14
 */
public class HandleSelectedFragment extends AppCompatDialogFragment {

    public static final String TAG = "HandleSelected";
    public static final String HANDLE_TYPE = "handle_type";
    public static final int DIALOG_TYPE_MOVE = 1;
    public static final int DIALOG_TYPE_DELETE = 2;
    public static final int DIALOG_TYPE_RESTORE = 3;
    public static final int DIALOG_TYPE_RECYCLE = 4;

    private int mType;
    private String mContent;
    private TaskGroup mSelectedGroup;
    private EditTasksListContract.Presenter mPresenter;

    public static HandleSelectedFragment getInstance(int type) {
        Bundle data = new Bundle();
        data.putInt(HANDLE_TYPE, type);
        HandleSelectedFragment fragment = new HandleSelectedFragment();
        fragment.setArguments(data);
        return fragment;
    }

    public void setPresenter(EditTasksListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle data = getArguments();
        if (data == null) {
            throw new NullPointerException("no group type found");
        }
        mType = data.getInt(HANDLE_TYPE);
        switch (mType) {
            case DIALOG_TYPE_MOVE:
                mContent = "";
                mSelectedGroup = new TaskGroup(Const.DEFAULT_GROUP_ID, 0, Const.DEFAULT_GROUP, null);
                break;
            case DIALOG_TYPE_DELETE:
                mContent = "删除后将无法恢复，确定？";
                mSelectedGroup = new TaskGroup(Const.DELETE_GROUP_ID, 0, null, null);
                break;
            case DIALOG_TYPE_RESTORE:
                mContent = "保存到收藏，确定？";
                mSelectedGroup = new TaskGroup(Const.COMPLETED_GROUP_ID, 0, Const.DEFAULT_COMPLETED_GROUP, null);
                break;
            case DIALOG_TYPE_RECYCLE:
                mContent = "将任务放入回收站，确定？";
                mSelectedGroup = new TaskGroup(Const.RECYCLED_GROUP_ID, 0, Const.DEFAULT_RECYCLED_GROUP, null);
                break;
            default:
                mContent = "";
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_handle_selected, container, false);

        TextView content = (TextView) root.findViewById(R.id.content);
        AppCompatSpinner spinner = (AppCompatSpinner) root.findViewById(R.id.spinner);
        View choose = root.findViewById(R.id.select_group);

        if (mType == DIALOG_TYPE_MOVE) {
            content.setVisibility(View.GONE);
            choose.setVisibility(View.VISIBLE);
            final SpinnerAdapter adapter = new SpinnerAdapter(getContext(), mPresenter.getGroups());
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    mSelectedGroup = adapter.getItem(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // TODO
                }
            });
        } else {
            content.setVisibility(View.VISIBLE);
            choose.setVisibility(View.GONE);
            content.setText(mContent);
        }

        // items
        root.findViewById(R.id.negative).setOnClickListener(mBnListener);
        root.findViewById(R.id.neutral).setOnClickListener(mBnListener);
        root.findViewById(R.id.positive).setOnClickListener(mBnListener);

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
                    mPresenter.cancelDialog();
                    break;
                case R.id.neutral:
                    mPresenter.doQuit();
                    break;
                case R.id.positive:
                    mPresenter.doRemove(mSelectedGroup);
                    break;
                default:
                    break;
            }
        }
    };
}
