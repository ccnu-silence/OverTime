package wistcat.overtime.base;


import android.annotation.TargetApi;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import sample.myresource.util.PermissionUtil;
import sample.myresource.util.PlatformVersion;

/**
 * 实现了动态权限请求
 *
 * @author wistcat
 */
public abstract class AbsBaseActivity extends AppCompatActivity{

    protected boolean mayRequestPermission(String description, int permissionId) {
        String perm = PermissionUtil.alterPermById(this, permissionId);
        return mayRequestPermission(description, permissionId, perm);
    }

    private boolean mayRequestPermission(String description, int permId, String perm) {
        if (!PlatformVersion.isM()) {
            return true;
        }
        if (PermissionUtil.checkPermission(this, perm)) {
            return true;
        }
        showRequest(description, permId, perm);
        return false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void showRequest(String description, final int permId, final String perm) {
        if (shouldShowRequestPermissionRationale(perm)) {
            // FIXME: 可能需要换成 Dialog
            Snackbar.make(findViewById(android.R.id.content), description, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{perm}, permId);
                        }
                    });
        } else {
            requestPermissions(new String[]{perm}, permId);
        }
        // result --> onRequestPermissionsResult()
    }

}
