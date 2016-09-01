package wistcat.overtime;


import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import wistcat.overtime.model.Account;
import wistcat.overtime.util.Const;

/**
 * @author wistcat
 */
public class App extends Application {

    private static App INSTANCE;
    private static Toast mToast;
    private static SharedPreferences mDefaultSP;

    private Account mCurrentAccount;
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mCurrentAccount = new Account(Const.ACCOUNT_GUEST);
        INSTANCE = this;
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(INSTANCE))
                .build();
    }

    /** 获取Context实例 */
    public static App getInstance() {
        return INSTANCE;
    }

    /** 统一显示Toast */
    public static void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(INSTANCE, text, Toast.LENGTH_SHORT);
            View root = mToast.getView();
            root.setBackgroundResource(R.drawable.toast_background);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    /** 获取SP */
    public static SharedPreferences getDefaultSharedPreferences() {
        if (mDefaultSP == null) {
            mDefaultSP = PreferenceManager.getDefaultSharedPreferences(INSTANCE);
        }
        return mDefaultSP;
    }

    /** 设置当前账户 */
    public void setCurrentAccount(Account account) {
        mCurrentAccount = account;
    }

    /** 获取当前账户 */
    public Account getCurrentAccount() {
        return mCurrentAccount;
    }

    /** 获取当前账户名 */
    public String getAccountName() {
        return mCurrentAccount.getName();
    }

    /** 获取AppComponent，同于提供单例对象 */
    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}

