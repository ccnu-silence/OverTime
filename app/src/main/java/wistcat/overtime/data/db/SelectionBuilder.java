package wistcat.overtime.data.db;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author wistcat 2016/9/7
 */
public class SelectionBuilder {

    private StringBuilder mSelection = new StringBuilder();
    private ArrayList<String> mSelectionArgs = new ArrayList<>();

    /**
     * 连接多个查询语句，使用 "AND" 连接where语句。<br/>
     * 需要自己注意输入的where语句和参数的正确性。
     *
     * @param selection SQL where语句，一般是 "columns contains ?" 或者 "columns = ?"等，请使用占位符
     * @param selectionArgs 用于替换{@code selection}参数中的 "?" 占位符，并注意与"?"的1对1数量关系
     *
     */
    public SelectionBuilder whereAnd(String selection, String... selectionArgs) {
        if (TextUtils.isEmpty(selection)) {
            if (selectionArgs != null && selectionArgs.length > 0) {
                throw new IllegalArgumentException("selection为空，无效的查询");
            }
        }
        if (mSelection.length() > 0) {
            mSelection.append(" AND ");
        }
        mSelection.append("(").append(selection).append(")");
        if (selectionArgs != null) {
            Collections.addAll(mSelectionArgs, selectionArgs);
        }
        return this;
    }

    /**
     * 连接多个查询语句，使用 "OR" 连接where语句。<br/>
     * 需要自己注意输入的where语句和参数的正确性。
     *
     * @param selection SQL where语句，一般是 "columns contains ?" 或者 "columns = ?"等，请使用占位符
     * @param selectionArgs 用于替换{@code selection}参数中的 "?" 占位符，并注意与"?"的1对1数量关系
     *
     */
    public SelectionBuilder whereOr(String selection, String... selectionArgs) {
        if (TextUtils.isEmpty(selection)) {
            if (selectionArgs != null && selectionArgs.length > 0) {
                throw new IllegalArgumentException("selection为空，无效的查询");
            }
        }
        if (mSelection.length() > 0) {
            mSelection.append(" OR ");
        }
        mSelection.append("(").append(selection).append(")");
        if (selectionArgs != null) {
            Collections.addAll(mSelectionArgs, selectionArgs);
        }
        return this;
    }

    public String getSelection() {
        return mSelection.toString();
    }

    public String[] getSelectionArgs() {
        return mSelectionArgs.toArray(new String[mSelectionArgs.size()]);
    }


}
