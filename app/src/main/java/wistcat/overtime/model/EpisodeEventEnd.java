package wistcat.overtime.model;

/**
 * 任务进行时，插入事件的结束节点
 *
 * @author wistcat 2016/8/31
 */
public class EpisodeEventEnd extends Episode {

    public static final int TYPE = 0x011;

    public EpisodeEventEnd(int id, int recordId, String name, String remark, String startTime, int seq) {
        super(id, recordId, name, remark, startTime, seq);
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
