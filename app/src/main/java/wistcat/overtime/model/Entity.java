package wistcat.overtime.model;

/**
 * 实体基类
 *
 * @author wistcat 2016/8/28
 */
public abstract class Entity implements Item {
    // TODO: 以后添加排序功能，数据结构可能需要更改
    /** _id为自增的rowid，目的是ListView的排序 */
    protected final int _id;
    protected final int _uuid;

    public Entity(int id, int uuid) {
        _id = id;
        _uuid = uuid;
    }

    public int getId() {
        return _id;
    }

    public int getUUID() {
        return _uuid;
    }

    public abstract int getType();

}
