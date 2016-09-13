package wistcat.overtime.util;


/**
 * 常量
 *
 * @author wistcat
 */
public class Const {
    private Const(){}

    // AES加密算法的字符串密钥
    public static final String AES_ENCRYPT_KEY = "ovtjf1MwRiBqkPn7qtIxlQRwIw";

    /** 默认账号名 */
    public static final String ACCOUNT_GUEST = "Guest";

    /** 默认TaskGroup名 */
    public static final String DEFAULT_GROUP = "默认";

    public static final String DEFAULT_COMPLETED_GROUP = "收藏室";

    public static final String DEFAULT_RECYCLED_GROUP = "回收站";

    public static final int DEFAULT_GROUP_ID = 1;

    public static final int COMPLETED_GROUP_ID = 10;

    public static final int RECYCLED_GROUP_ID = 20;

    public static final String ACCOUNT_IS_FIRST = "%s_is_first";

    /** 默认swipeRefresh显示时间 */
    public static final int DEFAULT_REFRESH_DURATION = 500;

}
