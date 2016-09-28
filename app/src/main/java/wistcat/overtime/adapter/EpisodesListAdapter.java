package wistcat.overtime.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import wistcat.overtime.R;
import wistcat.overtime.base.CursorRecyclerViewAdapter;
import wistcat.overtime.data.TaskEngine;
import wistcat.overtime.model.Episode;

/**
 * @author wistcat 2016/9/27
 */
public class EpisodesListAdapter extends CursorRecyclerViewAdapter <EpisodesListAdapter.ViewHolder> {

    private Context mContext;
    private Drawable mDrawable1, mDrawable2;
    public EpisodesListAdapter(Context context, int size) {
        super(context, null);
        mContext = context;
        mDrawable1 = ContextCompat.getDrawable(mContext, R.drawable.episode_node_1);
        mDrawable1.setBounds(0, 0, size, size);
        mDrawable2 = ContextCompat.getDrawable(mContext, R.drawable.episode_node_2);
        mDrawable2.setBounds(0, 0, size, size);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.list_item_simple_episode, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor, int position) {
        final Episode episode = TaskEngine.episodeFrom(cursor);
        viewHolder.time.setText(episode.getStartTime());
        if (!TextUtils.isEmpty(episode.getRemark())) {
            viewHolder.note.setText(episode.getRemark());
        }
        // check
        if (getItemCount() == position + 1) {
            viewHolder.time.setCompoundDrawables(mDrawable1, null, null, null);
            if (viewHolder.line.getVisibility() == View.VISIBLE) {
                viewHolder.line.setVisibility(View.INVISIBLE);
            }
        } else {
            viewHolder.time.setCompoundDrawables(mDrawable2, null, null, null);
            if (viewHolder.line.getVisibility() == View.INVISIBLE) {
                viewHolder.line.setVisibility(View.VISIBLE);
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView note;
        View line;
        public ViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.episode_time);
            note = (TextView) itemView.findViewById(R.id.episode_note);
            line = itemView.findViewById(R.id.line);
        }
    }

}
