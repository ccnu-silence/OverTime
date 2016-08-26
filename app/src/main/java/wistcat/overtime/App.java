package wistcat.overtime;


import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

/**
 * @author wistcat
 */
public class App extends Application {

    private static App INSTANCE;
    private static Toast mToast;
    private static SharedPreferences mDefaultSP;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public static App getINSTANCE() {
        return INSTANCE;
    }

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

    public static SharedPreferences getDefaultSharedPreferences() {
        if (mDefaultSP == null) {
            mDefaultSP = PreferenceManager.getDefaultSharedPreferences(INSTANCE);
        }
        return mDefaultSP;
    }


}

