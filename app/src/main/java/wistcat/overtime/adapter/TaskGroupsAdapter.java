package wistcat.overtime.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.MessageFormat;

import wistcat.overtime.R;
import wistcat.overtime.data.TaskEngine;
import wistcat.overtime.interfaces.ItemSelectListener;
import wistcat.overtime.model.TaskGroup;

/**
 * 用于{@link wistcat.overtime.main.tasksmanage.TasksManageFragment}
 *
 * @author wistcat 2016/9/5
 */
public class TaskGroupsAdapter extends CursorAdapter {

    private ItemSelectListener<TaskGroup> mItemListener;

    public TaskGroupsAdapter(Context context) {
        super(context, null, 0);
    }

    public void setTaskGroupItemListener(ItemSelectListener<TaskGroup> listener) {
        mItemListener = listener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View root = LayoutInflater.from(context).inflate(R.layout.list_item_simple_taskgroup, viewGroup, false);
        ViewHolder holder  = new ViewHolder();
        holder.root = root;
        holder.mSeq = (TextView) root.findViewById(R.id.seq);
        holder.mName = (TextView) root.findViewById(R.id.name);
        holder.mDescription = (TextView) root.findViewById(R.id.description);
        root.setTag(holder);
        return root;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        final TaskGroup group = TaskEngine.taskGroupFrom(cursor);
        String name = group.getName();
        int activate = group.getActive();
        String description = MessageFormat.format("活动任务：{0}", activate);

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemListener.onSelected(group);
            }
        });
        holder.mName.setText(name);
        holder.mDescription.setText(description);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        ViewHolder holder = (ViewHolder) v.getTag();
        holder.mSeq.setText(String.valueOf(position + 1));
        return v;
    }

    private static class ViewHolder {
        View root;
        TextView mSeq;
        TextView mName;
        TextView mDescription;
    }
}
