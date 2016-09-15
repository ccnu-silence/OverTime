package wistcat.overtime.interfaces;

/**
 * @author wistcat 2016/9/15
 */
public interface GetDataCallback<T> {

    void onDataLoaded(T data);

    void onError();
}
