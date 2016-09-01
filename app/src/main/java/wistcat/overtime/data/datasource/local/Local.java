package wistcat.overtime.data.datasource.local;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * 标记{@link LocalTaskDataSource}的Qualifier，用于区分TaskDataSource
 * <br/>ps: 当前TaskRepository只有一个数据源，所以暂时可以不需要
 *
 * @author wistcat 2016/9/1
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Local {
}
