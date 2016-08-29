package wistcat.overtime.model;

/**
 * 实体基类
 *
 * @author wistcat 2016/8/28
 */
public abstract class Entity implements Item {

    protected final int mId;

    public Entity(int id) {
        mId = id;
    }

    public int getId() {
        return mId;
    }

    public abstract int getType();

}
