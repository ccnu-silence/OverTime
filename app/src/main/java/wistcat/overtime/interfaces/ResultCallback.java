package wistcat.overtime.interfaces;

/**
 * 一般用于操作数据库成功或者失败状态的回调
 *
 * @author wistcat 2016/9/18
 */
public interface ResultCallback {

    void onSuccess();

    void onError();
}
