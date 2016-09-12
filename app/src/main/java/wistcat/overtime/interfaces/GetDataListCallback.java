package wistcat.overtime.interfaces;

import java.util.List;

/**
 * @author wistcat 2016/9/4
 */
public interface GetDataListCallback<T> {

    void onDataLoaded(List<T> dataList);

    void onError();
}
