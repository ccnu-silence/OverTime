package wistcat.overtime.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wistcat.overtime.R;
import wistcat.overtime.data.db.TaskTableHelper;
import wistcat.overtime.interfaces.ItemSelectListener;

/**
 * @author wistcat 2016/9/15
 */
public class EditTasksListAdapter extends CursorAdapter {

    private SparseBooleanArray mChecked = new SparseBooleanArray();
    private ItemSelectListener<Integer> mSelectListener;

    public EditTasksListAdapter(Context context, ItemSelectListener<Integer> listener) {
        super(context, null, 0);
        mSelectListener = listener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View root = LayoutInflater.from(context).inflate(R.layout.list_item_edit, viewGroup, false);
        ViewHolder holder = new ViewHolder();
        holder.root = root;
        holder.checkBox = (CheckBox) root.findViewById(R.id.checkbox);
        holder.name = (TextView) root.findViewById(R.id.name);
        holder.linear = root.findViewById(R.id.linear);
        root.setTag(holder);
        return root;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder holder = (ViewHolder) view.getTag();
        final int itemId = cursor.getInt(0);
        final String itemName = cursor.getString(TaskTableHelper.QUERY_TASK_PROJECTION.TASK_NAME);
        holder.name.setText(itemName);

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.checkBox.toggle();
            }
        });
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mChecked.put(itemId, true);
                } else {
                    mChecked.delete(itemId);
                }
                mSelectListener.onSelected(mChecked.size());
            }
        });
        holder.checkBox.setChecked(mChecked.get(itemId, false));

    }

    public List<Integer> getSelectedList() {
        List<Integer> list = new ArrayList<>(mChecked.size());
        for (int i = 0; i < mChecked.size(); i++) {
            list.add(mChecked.keyAt(i));
        }
        return list;
    }

    public void setSelectedList(List<Integer> list) {
        mChecked.clear();
        if (list != null) {
            for (int i : list) {
                mChecked.put(i, true);
            }
        }
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        View root;
        CheckBox checkBox;
        TextView name;
        View linear;
    }
}
