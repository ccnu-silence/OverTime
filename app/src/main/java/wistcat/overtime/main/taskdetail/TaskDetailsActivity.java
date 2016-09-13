package wistcat.overtime.main.taskdetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import wistcat.overtime.R;

public class TaskDetailsActivity extends AppCompatActivity {

    public static final String KEY_TASK = "task";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
    }
}
