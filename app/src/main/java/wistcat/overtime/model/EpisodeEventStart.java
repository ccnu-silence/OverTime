package wistcat.overtime.model;

/**
 * 任务进行时，插入事件的开始节点
 * <br/>应该和{@link EpisodeEventEnd} 有不同的id和type，相同的seq
 *
 * @author wistcat 2016/8/31
 */
public class EpisodeEventStart extends Episode {
    public static final int TYPE = 0x010;

    public EpisodeEventStart(int id, int recordId, String name, String remark, String startTime, int seq) {
        super(id, recordId, name, remark, startTime, seq);
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
