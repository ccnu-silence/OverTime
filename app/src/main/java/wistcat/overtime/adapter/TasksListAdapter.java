package wistcat.overtime.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import wistcat.overtime.R;
import wistcat.overtime.base.CursorRecyclerViewAdapter;
import wistcat.overtime.data.TaskEngine;
import wistcat.overtime.data.db.TaskTableHelper;
import wistcat.overtime.interfaces.EditItemSelectListener;
import wistcat.overtime.interfaces.OnSearchTermChanged;
import wistcat.overtime.model.Task;

/**
 * @author wistcat 2016/9/12
 */
public class TasksListAdapter extends CursorRecyclerViewAdapter<TasksListAdapter.ViewHolder>
        implements OnSearchTermChanged {

    private EditItemSelectListener<Task> mListenter;
    private Context mContext;
    private TextAppearanceSpan mSpan;
    private String mTerm;

    public TasksListAdapter(Context context, EditItemSelectListener<Task> listenter) {
        super(context, null);
        mContext = context;
        mListenter = listenter;
        mSpan = new TextAppearanceSpan(context, R.style.SearchTextHighLightStyle);
    }

    @Override
    public void onTermChanged(String newTerm) {
        mTerm = newTerm;
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
        // 查找匹配的第一个字符位置
        final int index = indexOfSearchQuery(name);
        if (index == -1) {
            // 正常显示
            viewHolder.name.setText(name);
        } else {
            // 搜索的时候，高亮显示匹配字符
            SpannableString matched = new SpannableString(name);
            matched.setSpan(mSpan, index, index + mTerm.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.name.setText(matched);
        }
        viewHolder.seq.setText(String.valueOf(position + 1));
        viewHolder.header.setBackgroundColor(res);
        // other
        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListenter.onSelected(task);
            }
        });
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListenter.onEditSelected(task);
            }
        });
    }

    private int indexOfSearchQuery(String displayName) {
        if (!TextUtils.isEmpty(mTerm)) {
            return displayName.toLowerCase(Locale.getDefault())
                    .indexOf(mTerm.toLowerCase(Locale.getDefault()));
        }
        return -1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View root;
        View header;
        TextView seq;
        TextView name;
        View edit;

        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            header = root.findViewById(R.id.header);
            seq = (TextView) root.findViewById(R.id.seq);
            name = (TextView) root.findViewById(R.id.task_name);
            edit = root.findViewById(R.id.edit);
        }
    }

}
