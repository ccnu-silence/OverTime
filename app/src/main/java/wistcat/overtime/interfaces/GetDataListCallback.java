package wistcat.overtime.interfaces;

import java.util.List;

import wistcat.overtime.model.Entity;

/**
 * @author wistcat 2016/9/4
 */
public interface GetDataListCallback<T extends Entity> {

    void onDataLoaded(List<T> dataList);

    void onError();
}
