package wistcat.overtime.main.addtask.manual;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import wistcat.overtime.R;
import wistcat.overtime.adapter.SpinnerAdapter;
import wistcat.overtime.main.addtask.BaseCreateFragment;
import wistcat.overtime.model.Task;
import wistcat.overtime.model.TaskGroup;
import wistcat.overtime.util.Const;

/**
 * @author wistcat 2016/8/25
 */
public class CreateManualFragment extends BaseCreateFragment {

    public static final int TYPE = Task.TYPE_MANUAL;
    public static final String MARK = "InfiniteManual";
    private EditText mEdit;
    private Spinner mSpinner;

    public static CreateManualFragment getInstance() {
        return new CreateManualFragment();
    }

    public static CreateManualFragment getInstance(int cx, int cy, TaskGroup group) {
        CreateManualFragment fragment = getInstance();
        fragment.setArguments(createArgs(cx, cy, group));
        return fragment;
    }

    @Override
    protected int getTheme() {
        return R.style.ManualTaskStyle;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_create_manual;
    }

    @Override
    protected String getMark() {
        return MARK;
    }

    @Override
    protected int getTitle() {
        return R.string.create_manual;
    }

    @Override
    protected void initView(View layout) {
        //
        mEdit = (EditText) layout.findViewById(R.id.task_name);
        //
        View choose = layout.findViewById(R.id.linear_choose_group);
        View show = layout.findViewById(R.id.show_choose_group);
        if (mGroup != null) {
            show.setVisibility(View.VISIBLE);
            choose.setVisibility(View.GONE);
            TextView g = (TextView) layout.findViewById(R.id.specify_group);
            g.setText(mGroup.getName());
        } else {
            show.setVisibility(View.GONE);
            choose.setVisibility(View.VISIBLE);
            mSpinner = (Spinner) layout.findViewById(R.id.spinner);
        }

        // Button
        View quit = layout.findViewById(R.id.negative);
        View save = layout.findViewById(R.id.positive);
        quit.setOnClickListener(mBnListener);
        save.setOnClickListener(mBnListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGroup == null) {
            mPresenter.loadSpinner();
        }
    }

    private View.OnClickListener mBnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.negative:
                    mPresenter.quit();
                    break;
                case R.id.positive:
                    String name = mEdit.getText().toString();
                    if (TextUtils.isEmpty(name)) {
                        name = Const.DEFAULT_TASK_NAME;
                    }
                    mPresenter.saveTask(mGroup, name, null, TYPE);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void showSpinner(List<TaskGroup> groups) {
        final SpinnerAdapter adapter = new SpinnerAdapter(getContext(), groups);
        if (mSpinner == null) {
            throw new NullPointerException("初始化错误！");
        }
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mGroup = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void quit() {
        getActivity().finish();
    }

}
