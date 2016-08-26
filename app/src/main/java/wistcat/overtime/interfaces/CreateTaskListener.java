package wistcat.overtime.interfaces;

/**
 * 创建新任务时的回调接口
 *
 * @author wistcat 2016/8/25
 */
public interface CreateTaskListener {

    void switchPage(int cx, int cy, String tag);

    void goBack();

    void remove();
}
