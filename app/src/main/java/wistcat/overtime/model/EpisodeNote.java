package wistcat.overtime.model;

/**
 * 任务进行时，插入一个记事
 *
 * @author wistcat 2016/8/31
 */
public class EpisodeNote extends Episode {

    public static final int TYPE = 0x020;

    public EpisodeNote(int id, int recordId, String name, String remark, String startTime, int seq) {
        super(id, recordId, name, remark, startTime, seq);
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
