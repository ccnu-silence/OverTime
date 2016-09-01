package wistcat.overtime.model;

/**
 * 任务组
 *
 * @author wistcat 2016/8/31
 */
public class TaskGroup extends Entity {

    private final String mName;
    private final String mAccount;
    private int mActive;
    private int mRunning;
    private int mCompleted;
    private int mRecycled;

    public TaskGroup(int id, String name, String account) {
        super(id);
        mName = name;
        mAccount = account;
    }

    @Override
    public int getType() {
        return 0;
    }

    public String getName() {
        return mName;
    }

    public String getAccount() {
        return mAccount;
    }

    public void init(int active, int running, int completed, int recycled) {
        mActive = active;
        mRunning = completed;
        mCompleted = completed;
        mRecycled = recycled;
    }

    public int getRecycled() {
        return mRecycled;
    }

    public void setRecycled(int mRecycled) {
        this.mRecycled = mRecycled;
    }

    public int getCompleted() {
        return mCompleted;
    }

    public void setCompleted(int mCompleted) {
        this.mCompleted = mCompleted;
    }

    public int getRunning() {
        return mRunning;
    }

    public void setRunning(int mRunning) {
        this.mRunning = mRunning;
    }

    public int getActive() {
        return mActive;
    }

    public void setActive(int mActive) {
        this.mActive = mActive;
    }
}
