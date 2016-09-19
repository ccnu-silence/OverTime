package wistcat.overtime.interfaces;

/**
 * @author wistcat 2016/9/19
 */
public interface DialogButtonListener<T> {

    void onNegative();

    void onNeutral();

    void onPositive();

    void onData(T data);
}
