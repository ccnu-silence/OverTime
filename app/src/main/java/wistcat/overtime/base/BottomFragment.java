package wistcat.overtime.base;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import wistcat.overtime.R;
import wistcat.overtime.interfaces.ItemSelectListener;

/**
 * @author wistcat 2016/9/12
 */
public class BottomFragment extends BottomSheetDialogFragment {

    private static final String TITLE = "title";
    private static final String ITEMS = "items";
    private String mTitle;
    private String[] mItems;
    private ItemSelectListener<Integer> mSelectListener;

    public static BottomFragment getInstance(String title, String... items) {
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        bundle.putStringArray(ITEMS, items);
        BottomFragment fragment = new BottomFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setSelectListener(ItemSelectListener<Integer> listener) {
        mSelectListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mTitle = args.getString(TITLE);
            mItems = args.getStringArray(ITEMS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bottom_base, container, false);
        ((TextView)root.findViewById(R.id.title)).setText(mTitle);
        // listView
        ListView listView = (ListView) root.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), R.layout.list_item_bottom_dialog, R.id.list_item, mItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mSelectListener.onSelected(position);
            }
        });
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSelectListener = null;
    }

}
