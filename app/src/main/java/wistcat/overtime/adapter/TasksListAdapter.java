package wistcat.overtime.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Locale;

import wistcat.overtime.R;
import wistcat.overtime.data.TaskEngine;
import wistcat.overtime.data.db.TaskTableHelper;
import wistcat.overtime.interfaces.OnSearchTermChanged;
import wistcat.overtime.main.taskslist.TasksListContract;
import wistcat.overtime.model.Task;

/**
 * @author wistcat 2016/9/12
 */
public class TasksListAdapter extends CursorAdapter implements OnSearchTermChanged {

    private final TasksListContract.Presenter mPresenter;
    private TextAppearanceSpan mSpan;
    private String mTerm;

    public TasksListAdapter(Context context, TasksListContract.Presenter presenter) {
        super(context, null, 0);
        mPresenter = presenter;
        mSpan = new TextAppearanceSpan(context, R.style.SearchTextHighLightStyle);
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
        final String name = cursor.getString(TaskTableHelper.QUERY_TASK_PROJECTION.TASK_NAME);
        int position = cursor.getPosition();
        holder.mSeq.setText(String.valueOf(position + 1));
        holder.mSeq.setBackgroundColor(res);

        // 查找匹配的第一个字符位置
        final int index = indexOfSearchQuery(name);
        if (index == -1) {
            // 正常显示
            holder.mName.setText(name);
        } else {
            // 搜索的时候，高亮显示匹配字符
            SpannableString matched = new SpannableString(name);
            matched.setSpan(mSpan, index, index + mTerm.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.mName.setText(matched);
        }
        // other
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onItemSelected(task);
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

    @Override
    public void onTermChanged(String newTerm) {
        mTerm = newTerm;
    }

    private static class ViewHolder {
        View root;
        TextView mSeq;
        TextView mName;
    }
}
