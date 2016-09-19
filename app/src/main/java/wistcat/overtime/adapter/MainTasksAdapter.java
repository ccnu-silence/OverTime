package wistcat.overtime.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import wistcat.overtime.R;
import wistcat.overtime.data.TaskEngine;
import wistcat.overtime.data.db.TaskTableHelper;
import wistcat.overtime.interfaces.ItemSelectListener;
import wistcat.overtime.model.Task;

/**
 * {@link wistcat.overtime.main.main.TasksFragment} 显示Activate任务
 *
 * @author wistcat 2016/9/2
 */
public class MainTasksAdapter extends CursorAdapter {
    // FIXME: 要改成LRU排序的活动任务...现在先演示用...

    private ItemSelectListener<Task> mItemListener;

    public MainTasksAdapter(Context context) {
        super(context, null, 0);
    }

    public void setTaskItemListener(ItemSelectListener<Task> listener) {
        mItemListener = listener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View root = LayoutInflater.from(context).inflate(R.layout.list_item_simple_task, viewGroup, false);
        ViewHolder holder = new ViewHolder();
        holder.root = root;
        holder.mName = (TextView) root.findViewById(R.id.task_name);
        holder.mSeq = (TextView) root.findViewById(R.id.seq);
        root.setTag(holder);
        return root;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        final Task task = TaskEngine.taskFrom(cursor);
        final int res = TaskEngine.taskToColor(task.getType());
        int position = cursor.getPosition();
        holder.mSeq.setText(String.valueOf(position + 1));
        holder.mSeq.setBackgroundColor(res);
        final String name = cursor.getString(TaskTableHelper.QUERY_TASK_PROJECTION.TASK_NAME);
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemListener.onSelected(task);
            }
        });
        holder.mName.setText(name);
    }


    private static class ViewHolder {
        View root;
        TextView mSeq;
        TextView mName;
    }
}
