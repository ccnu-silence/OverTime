package wistcat.overtime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import wistcat.overtime.R;
import wistcat.overtime.model.TaskGroup;

/**
 * @author wistcat 2016/9/19
 */
public class SpinnerAdapter extends BaseAdapter {

    private final List<TaskGroup> mList;
    private Context mContext;

    public SpinnerAdapter(Context context, List<TaskGroup> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public TaskGroup getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_spinner, parent, false);
            holder.t = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TaskGroup item = getItem(position);
        holder.t.setText(item.getName());
        return convertView;
    }

    private class ViewHolder {
        TextView t;
    }
}
