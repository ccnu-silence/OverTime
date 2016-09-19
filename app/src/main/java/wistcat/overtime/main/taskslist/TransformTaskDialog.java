package wistcat.overtime.main.taskslist;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import wistcat.overtime.R;
import wistcat.overtime.adapter.SpinnerAdapter;
import wistcat.overtime.interfaces.DialogButtonListener;
import wistcat.overtime.model.TaskGroup;

/**
 * @author wistcat 2016/9/19
 */
public class TransformTaskDialog extends AppCompatDialogFragment {

    public static final String MOVE = "move";
    private String mAlert;
    private DialogButtonListener<TaskGroup> mListener;
    private List<TaskGroup> mGroups;
    private TaskGroup mSelectedGroup;

    public static TransformTaskDialog getInstance(String alert) {
        Bundle bundle = new Bundle();
        bundle.putString(MOVE, alert);
        TransformTaskDialog fragment = new TransformTaskDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setListener(List<TaskGroup> groups, DialogButtonListener<TaskGroup> listener) {
        mGroups = groups;
        mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle data = getArguments();
        mAlert = data.getString(MOVE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_transform_task_dialog, container, false);
        ((TextView)root.findViewById(R.id.description)).setText(mAlert);
        root.findViewById(R.id.positive).setOnClickListener(mBnListener);
        root.findViewById(R.id.negative).setOnClickListener(mBnListener);
        // spinner
        Spinner spinner = (Spinner) root.findViewById(R.id.spinner);
        final SpinnerAdapter adapter = new SpinnerAdapter(getContext(), mGroups);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mSelectedGroup = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                    mListener.onNegative();
                    break;
                case R.id.positive:
                    mListener.onData(mSelectedGroup);
                    break;
                default:
                    break;
            }
        }
    };

}
