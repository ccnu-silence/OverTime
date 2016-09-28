package wistcat.overtime.main.runningpage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.ParseException;

import wistcat.overtime.R;
import wistcat.overtime.adapter.EpisodesListAdapter;
import wistcat.overtime.interfaces.DialogButtonListener;
import wistcat.overtime.model.Record;
import wistcat.overtime.util.Utils;

/**
 * @author wistcat 2016/9/25
 */
public class RunningTasksFragment extends Fragment implements RunningTasksContract.View {

    private final String TAG_ADD = "add_apisode";
    private RunningTasksContract.Presenter mPresenter;

    private Drawable mDrawable1, mDrawable2;
    private TextView mTaskName, mStartTime, mContinueTime;
    private View mContent, mNoTask;
    private SwitchCompat mSwitch;
    private EpisodesListAdapter mAdapter;
    private boolean isFirst = true;
    private boolean isRunning;
    private volatile long mInitTime;
    private Thread mCounter;
    private Handler mHandler = new Handler();

    public static RunningTasksFragment getInstance() {
        return new RunningTasksFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_running_tasks, container, false);
        final int size = getResources().getDimensionPixelSize(R.dimen.episode_node_size);

        // nestedScrollView
        mContent = root.findViewById(R.id.record_content);

        // menu
        View menu = root.findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.openDrawer();
            }
        });

        // TextView
        mTaskName = (TextView) root.findViewById(R.id.task_name);
        mContinueTime = (TextView) root.findViewById(R.id.time);
        mStartTime = (TextView) root.findViewById(R.id.start);
        mNoTask = root.findViewById(R.id.no_task);

        // switch
        mSwitch = (SwitchCompat) root.findViewById(R.id.stop);
        mSwitch.setChecked(true);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    mPresenter.doPause();
                }
            }
        });

        // recyclerView
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.list2);
        // adapter
        mAdapter = new EpisodesListAdapter(getContext(), size);
        LinearLayoutManager adapterManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(adapterManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mAdapter);

        // add episode
        View add = root.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.openAddDialog();
            }
        });

        // drawable
        mDrawable1 = ContextCompat.getDrawable(getContext(), R.drawable.episode_node_0);
        mDrawable2 = ContextCompat.getDrawable(getContext(), R.drawable.episode_node_3);
        mDrawable1.setBounds(0, 0, size, size);
        mDrawable2.setBounds(0, 0, size, size);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirst) {
            isFirst = false;
            mPresenter.start();
        }

        if (mInitTime != 0) {
            isRunning = true;
            checkAndStartThread();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isRunning = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void setPresenter(RunningTasksContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showList(Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void clearList() {
        showList(null);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        // FIXME
    }

    @Override
    public void loadRecordDetails(@NonNull final Record record) { // ★
        if (mContent.getVisibility() == View.GONE) {
            mContent.setVisibility(View.VISIBLE);
        }
        setNoTask(false);
        mSwitch.setChecked(true);
        mTaskName.setText(record.getTaskName());
        mStartTime.setText(String.format("%s  开始", record.getStartTime()));
        //
        mContinueTime.post(new Runnable() {
            @Override
            public void run() {
                try {
                    mInitTime = Utils.parseTime(record.getStartTime());
                    checkAndStartThread();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void loadRecordEmpty() {
        if (mContent.getVisibility() == View.VISIBLE) {
            mContent.setVisibility(View.GONE);
        }
        setNoTask(true);
        isRunning = false;
        mInitTime = 0;
        mContinueTime.post(new Runnable() {
            @Override
            public void run() {
                mContinueTime.setText(R.string.time_zero);
            }
        });
    }

    @Override
    public void showAddEpisodeDialog(String addTime) {
        AddEpisodeDialog fragment = AddEpisodeDialog.getInstance(addTime);
        fragment.setDialogListener(mDialogListener);
        fragment.show(getFragmentManager(), TAG_ADD);
    }

    @Override
    public void dismissAddEpisodeDialog() {
        AddEpisodeDialog fragment =
                (AddEpisodeDialog) getFragmentManager().findFragmentByTag(TAG_ADD);
        fragment.dismiss();
    }

    public void setNoTask(boolean isEmpty) {
        final int duration = 200;
        if (isEmpty && mNoTask.getVisibility() == View.GONE) {
            mNoTask.setAlpha(0.f);
            mNoTask.setVisibility(View.VISIBLE);
            mNoTask.animate().alpha(1.f).setDuration(duration).setListener(null);
        } else if (!isEmpty && mNoTask.getVisibility() == View.VISIBLE) {
            mNoTask.animate().alpha(0.f).setDuration(duration).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mNoTask.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void setStartTimeImage(boolean isEmpty) {
        Drawable d = isEmpty ? mDrawable1 : mDrawable2;
        mStartTime.setCompoundDrawables(d, null, null, null);
    }

    private void checkAndStartThread() {
        if (mCounter == null) {
            isRunning = true;
            mCounter = new Thread(new TimeRunner());
            mCounter.start();
        }
    }

    private DialogButtonListener<String> mDialogListener = new DialogButtonListener<String>() {
        @Override
        public void onNegative() {
            mPresenter.doNotAdd();
        }

        @Override
        public void onNeutral() {
            //
        }

        @Override
        public void onPositive() {
            //
        }

        @Override
        public void onData(String data) {
            mPresenter.doAddEpisode(data.trim());
        }
    };

    private class TimeRunner implements Runnable {
        @Override
        public void run() {
            while (isRunning && mInitTime > 0) {
                mContinueTime.post(new Runnable() {
                    @Override
                    public void run() {
                        long t = System.currentTimeMillis() - mInitTime;
                        mContinueTime.setText(Utils.getAccumulateTime(t));
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mCounter = null;
        }
    }
}
