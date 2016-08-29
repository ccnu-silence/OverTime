package wistcat.overtime.model;

/**
 * @author wistcat 2016/8/28
 */
public class Episode extends Entity {

    public Episode(int id) {
        super(id);
    }

    @Override
    public int getType() {
        return 0;
    }
}
