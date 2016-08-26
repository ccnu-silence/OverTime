package wistcat.overtime.main.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wistcat.overtime.R;
import wistcat.overtime.main.addtask.AddTaskActivity;
import wistcat.overtime.main.tasksmanage.TasksManageActivity;
import wistcat.overtime.main.taskstatistics.TasksStatisticsActivity;
import wistcat.overtime.widget.ScrollChildSwipeRefreshLayout;

public class TasksFragment extends Fragment {

    private CardView mManagerCard, mAnalysisCard, mAddCard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tasks, parent, false);

        mManagerCard = (CardView) root.findViewById(R.id.tasks_manager);
        mManagerCard.setOnClickListener(mCardListener);
        mAnalysisCard = (CardView) root.findViewById(R.id.tasks_analy);
        mAnalysisCard.setOnClickListener(mCardListener);
        mAddCard = (CardView) root.findViewById(R.id.tasks_add);
        mAddCard.setOnClickListener(mCardListener);

        ScrollChildSwipeRefreshLayout scrollView = (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.scrollview);
        scrollView.setEnabled(false);

        return root;
    }

    private View.OnClickListener mCardListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mManagerCard == view) {
                intentTo(TasksManageActivity.class);
            }
            if (mAnalysisCard == view) {
                intentTo(TasksStatisticsActivity.class);
            }
            if (mAddCard == view) {
                intentTo(AddTaskActivity.class);
            }
        }
    };

    private void intentTo(Class<?> clazz) {
        startActivity(new Intent(getActivity(), clazz));
    }


}
