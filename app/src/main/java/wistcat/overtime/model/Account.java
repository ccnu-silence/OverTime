package wistcat.overtime.model;

import java.io.Serializable;

/**
 * 本地账户
 *
 * @author wistcat 2016/8/28
 */
public class Account implements Serializable {

    private final String mName;

    public Account(String name) {
        this.mName = name;
    }

    public String getName() {
        return mName;
    }

}
