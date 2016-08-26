package wistcat.overtime.main.welcome;

import android.content.Intent;
import android.os.Bundle;

import wistcat.overtime.R;
import wistcat.overtime.base.AbsBaseActivity;
import wistcat.overtime.main.main.MainActivity;

public class WelcomeActivity extends AbsBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void redirect() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.anim_activity_in, R.anim.anim_activity_out);
    }

}
