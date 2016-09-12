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
     * 需要自己注意输入的where语句和参数的正确性。<br/>
     * 注意不要和其他方法混用！
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
     * 需要自己注意输入的where语句和参数的正确性。<br/>
     * 注意不要和其他方法混用！
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


    /**
     * 用于IN操作拼接：... where 列名 in (?, ?, ...) <br/>
     * 配合 selectionArgs参数使用 <br/>
     * 注意不要和其他方法混用！
     *
     * @param column 第一次需要输入列名，并且只有第一次有效，因此注意使用
     * @param selectionArgs 用于替换in(?, ?..)中的占位符，第一次必须为null
     *
     */
    public SelectionBuilder in(String column, String selectionArgs) {
        if (mSelection.length() == 0) {
            if (TextUtils.isEmpty(column) || selectionArgs != null) {
                throw new IllegalArgumentException("列名为空，无效的查询");
            }
            mSelection.append(column).append(" IN ()");
        } else {
            mSelection.deleteCharAt(mSelection.length() - 1);
            if (mSelectionArgs.size() != 0) {
                mSelection.append(",");
            }
            mSelection.append("?)");
        }
        if (selectionArgs != null) {
            mSelectionArgs.add(selectionArgs);
        }
        return this;
    }

    /**
     * 用于NOT IN操作拼接：... where 列名 not in (?, ?, ...) <br/>
     * 配合 selectionArgs参数使用<br/>
     * 注意不要和其他方法混用！
     *
     * @param column 第一次需要输入列名，并且只有第一次有效，因此注意使用
     * @param selectionArgs 用于替换not in(?, ?..)中的占位符，第一次必须为null
     *
     */
    public SelectionBuilder notIn(String column, String selectionArgs) {
        if (mSelection.length() == 0) {
            if (TextUtils.isEmpty(column) || selectionArgs != null) {
                throw new IllegalArgumentException("列名为空，无效的查询");
            }
            mSelection.append(column).append(" NOT IN ()");
        } else {
            mSelection.deleteCharAt(mSelection.length() - 1);
            if (mSelectionArgs.size() != 0) {
                mSelection.append(",");
            }
            mSelection.append("?)");
        }
        if (selectionArgs != null) {
            mSelectionArgs.add(selectionArgs);
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
