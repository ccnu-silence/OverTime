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
import wistcat.overtime.data.db.TaskTableHelper;
import wistcat.overtime.interfaces.EditItemSelectListener;
import wistcat.overtime.model.Task;

/**
 * {@link wistcat.overtime.main.main.TasksFragment} 显示Activate任务
 *
 * @author wistcat 2016/9/2
 */
public class MainTasksAdapter extends CursorRecyclerViewAdapter<MainTasksAdapter.ViewHolder> {
    // FIXME: 要改成LRU排序的活动任务...现在先演示用...

    private EditItemSelectListener<Task> mItemListener;
    private Context mContext;

    public MainTasksAdapter(Context context, EditItemSelectListener<Task> listener) {
        super(context, null);
        mContext = context;
        mItemListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.list_item_simple_task, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor, int position) {
        final Task task = TaskEngine.taskFrom(cursor);
        final int res = TaskEngine.taskToColor(task.getType());
        final String name = cursor.getString(TaskTableHelper.QUERY_TASK_PROJECTION.TASK_NAME);
        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemListener.onSelected(task);
            }
        });
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemListener.onEditSelected(task);
            }
        });
        if (task.isRunning()) {
            viewHolder.running.setVisibility(View.VISIBLE);
        } else {
            viewHolder.running.setVisibility(View.GONE);
        }
        viewHolder.seq.setText(String.valueOf(position + 1));
        viewHolder.header.setBackgroundColor(res);
        viewHolder.name.setText(name);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View root;
        View header;
        TextView seq;
        TextView name;
        View edit;
        View running;

        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            header = root.findViewById(R.id.header);
            seq = (TextView) root.findViewById(R.id.seq);
            name = (TextView) root.findViewById(R.id.task_name);
            edit = root.findViewById(R.id.edit);
            running = root.findViewById(R.id.running);
        }
    }

}
