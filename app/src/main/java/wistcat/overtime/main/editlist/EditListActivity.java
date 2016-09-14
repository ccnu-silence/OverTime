package wistcat.overtime.main.editlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import wistcat.overtime.App;
import wistcat.overtime.R;

public class EditListActivity extends AppCompatActivity {

    @Inject
    public EditListPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        EditListFragment fragment =
                (EditListFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = EditListFragment.getInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment, "EditList")
                    .commit();
        }

        EditListComponent component = DaggerEditListComponent
                .builder()
                .appComponent(App.getInstance().getAppComponent())
                .editListModule(new EditListModule(getSupportLoaderManager(), fragment))
                .build();
        component.inject(this);
    }

}
