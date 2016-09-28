package wistcat.overtime.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import wistcat.overtime.R;
import wistcat.overtime.base.CursorRecyclerViewAdapter;
import wistcat.overtime.data.TaskEngine;
import wistcat.overtime.interfaces.EditItemSelectListener;
import wistcat.overtime.model.Record;

/**
 * @author wistcat 2016/9/25
 */
public class RunningTasksAdapter extends CursorRecyclerViewAdapter<RunningTasksAdapter.ViewHolder> {

    private Context mContext;
    private EditItemSelectListener<Record> mItemListener;

    public RunningTasksAdapter(Context context, EditItemSelectListener<Record> listener) {
        super(context, null);
        mContext = context;
        mItemListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.list_item_running_tasks, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor, int position) {
        final Record record = TaskEngine.recordFrom(cursor);
        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemListener.onSelected(record);
            }
        });
        viewHolder.seq.setText(String.valueOf(position + 1));
        viewHolder.task.setText(record.getTaskName());
        viewHolder.start.setText(String.format("开始：%s", record.getStartTime()));
        viewHolder.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemListener.onEditSelected(record);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View root;
        TextView seq;
        TextView task;
        TextView start;
        View pause;
        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            seq = (TextView) root.findViewById(R.id.seq);
            task = (TextView) root.findViewById(R.id.task);
            start = (TextView) root.findViewById(R.id.start);
            pause = root.findViewById(R.id.pause);
        }
    }
}
