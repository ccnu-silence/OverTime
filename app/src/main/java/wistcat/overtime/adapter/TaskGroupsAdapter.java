package wistcat.overtime.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.MessageFormat;

import wistcat.overtime.R;
import wistcat.overtime.base.CursorRecyclerViewAdapter;
import wistcat.overtime.data.TaskEngine;
import wistcat.overtime.interfaces.EditItemSelectListener;
import wistcat.overtime.model.TaskGroup;
import wistcat.overtime.util.Const;

/**
 * 用于{@link wistcat.overtime.main.tasksmanage.TasksManageFragment}
 *
 * @author wistcat 2016/9/5
 */
public class TaskGroupsAdapter extends CursorRecyclerViewAdapter<TaskGroupsAdapter.ViewHolder> {

    private EditItemSelectListener<TaskGroup> mItemListener;
    private Context mContext;

    public TaskGroupsAdapter(Context context, EditItemSelectListener<TaskGroup> listener) {
        super(context, null);
        mContext = context;
        mItemListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.list_item_simple_taskgroup, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor, int position) {
        // 将数据与界面进行绑定的操作
        final TaskGroup group = TaskEngine.taskGroupFrom(cursor);
        String name = group.getName();
        int count = group.getTaskCount();
        String description = MessageFormat.format("任务数：{0}", count);

        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemListener.onSelected(group);
            }
        });
        viewHolder.mSeq.setText(String.valueOf(position + 1));
        viewHolder.mName.setText(name);
        viewHolder.mDescription.setText(description);
        if (group.getId() == Const.DEFAULT_GROUP_ID) {
            viewHolder.mEdit.setOnClickListener(null);
            viewHolder.mEdit.setVisibility(View.GONE);
        } else {
            viewHolder.mEdit.setVisibility(View.VISIBLE);
            viewHolder.mEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onEditSelected(group);
                }
            });
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View root;
        TextView mSeq;
        TextView mName;
        TextView mDescription;
        View mEdit;

        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            mSeq = (TextView) root.findViewById(R.id.seq);
            mName = (TextView) root.findViewById(R.id.name);
            mDescription = (TextView) root.findViewById(R.id.description);
            mEdit = root.findViewById(R.id.edit);
        }
    }
}
