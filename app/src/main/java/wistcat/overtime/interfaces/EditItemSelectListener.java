package wistcat.overtime.interfaces;

/**
 * @author wistcat 2016/9/17
 */
public interface EditItemSelectListener<T> extends ItemSelectListener<T>{
    void onEditSelected(T item);
}
