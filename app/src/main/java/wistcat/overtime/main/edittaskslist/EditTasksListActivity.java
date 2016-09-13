package wistcat.overtime.main.edittaskslist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import wistcat.overtime.R;

public class EditTasksListActivity extends AppCompatActivity {
    public static final String KEY_EDIT_TASKS_LIST = "tasks_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tasks_list);
    }
}
