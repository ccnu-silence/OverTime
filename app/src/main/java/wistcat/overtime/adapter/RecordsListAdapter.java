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
import wistcat.overtime.model.Record;

/**
 * @author wistcat 2016/9/22
 */
public class RecordsListAdapter extends CursorRecyclerViewAdapter<RecordsListAdapter.ViewHolder> {

    private Context mContext;
    private EditItemSelectListener<Record> mListener;

    public RecordsListAdapter(Context context, EditItemSelectListener<Record> listener) {
        super(context, null);
        mContext = context;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.list_item_simple_record, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor, int position) {
        final Record record = TaskEngine.recordFrom(cursor);
        final String start = cursor.getString(TaskTableHelper.QUERY_RECORD_PROJECTION.COLUMN_NAME_START_TIME);
        final String end = cursor.getString(TaskTableHelper.QUERY_RECORD_PROJECTION.COLUMN_NAME_END_TIME);
        viewHolder.seq.setText(String.valueOf(position + 1));
        viewHolder.startTime.setText(start);
        viewHolder.endTime.setText(end);
        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onSelected(record);
            }
        });
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onEditSelected(record);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View root;
        TextView seq;
        TextView startTime;
        TextView endTime;
        View edit;

        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            seq = (TextView) root.findViewById(R.id.seq);
            startTime = (TextView) root.findViewById(R.id.start_time);
            endTime = (TextView) root.findViewById(R.id.end_time);
            edit = root.findViewById(R.id.edit);
        }
    }
}
