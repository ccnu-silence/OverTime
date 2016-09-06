package wistcat.overtime.util;

import java.util.List;

import wistcat.overtime.interfaces.GetDataListCallback;
import wistcat.overtime.model.Entity;

/**
 * @author wistcat 2016/9/6
 */
public class GetDataListCallbackAdapter<T extends Entity> implements GetDataListCallback<T> {
    @Override
    public void onDataLoaded(List<T> dataList) {

    }

    @Override
    public void onError() {

    }
}
