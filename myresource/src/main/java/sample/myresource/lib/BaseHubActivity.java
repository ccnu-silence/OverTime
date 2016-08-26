package sample.myresource.lib;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import sample.myresource.R;

public abstract class BaseHubActivity extends AppCompatActivity {

    private static final String TAG = "TEST_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_hub);

        // list
        ListView mList = (ListView)findViewById(R.id.list);
        List<Class<?>> clazzs = new ArrayList<>();
        List<String> descriptions = new ArrayList<>();
        addListItems(clazzs, descriptions);
        initialList(mList, clazzs, descriptions);
    }

    public abstract void addListItems(List<Class<?>> clazzs, List<String> descriptions);

    private void initialList(final ListView list, final List<Class<?>> clazzs, final List<String> descriptions){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_0, descriptions);
        list.setAdapter(adapter);
        // item
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(clazzs.size() < i){
                    Log.e(TAG, "--- Class not found! ---");
                }else{
                    Intent intent = new Intent(BaseHubActivity.this, clazzs.get(i));
                    startActivity(intent);
                }
            }
        });

    }
}
