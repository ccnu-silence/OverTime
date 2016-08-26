package sample.myresource.util;


import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class PermissionUtil {

    public static final String PERMISSION_PREFIX = "Manifest.permission.";
    // permissions request code
    public static final int PERM_REQ_READ_CALENDAR          = 10;
    public static final int PERM_REQ_WRITE_CALENDAR         = 11;
    public static final int PERM_REQ_CAMERA                 = 12;
    public static final int PERM_REQ_READ_CONTACTS          = 13;
    public static final int PERM_REQ_WRITE_CONTACTS         = 14;
    public static final int PERM_REQ_GET_ACCOUNTS           = 15;
    public static final int PERM_REQ_ACCESS_FINE_LOCATION   = 16;
    public static final int PERM_REQ_ACCESS_COARSE_LOCATION = 17;
    public static final int PERM_REQ_RECORD_AUDIO           = 18;
    public static final int PERM_REQ_READ_PHONE_STATE       = 19;
    public static final int PERM_REQ_CALL_PHONE             = 20;
    public static final int PERM_REQ_READ_CALL_LOG          = 21;
    public static final int PERM_REQ_WRITE_CALL_LOG         = 22;
    public static final int PERM_REQ_ADD_VOICEMAIL          = 23;
    public static final int PERM_REQ_USE_SIP                = 24;
    public static final int PERM_REQ_PROCESS_OUTGOING_CALLS = 25;
    public static final int PERM_REQ_BODY_SENSORS           = 26;
    public static final int PERM_REQ_SEND_SMS               = 27;
    public static final int PERM_REQ_RECEIVE_SMS            = 28;
    public static final int PERM_REQ_READ_SMS               = 29;
    public static final int PERM_REQ_RECEIVE_WAP_PUSH       = 30;
    public static final int PERM_REQ_RECEIVE_MMS            = 31;
    public static final int PERM_REQ_READ_EXTERNAL_STORAGE  = 32;
    public static final int PERM_REQ_WRITE_EXTERNAL_STORAGE = 33;


    public static boolean checkPermission(Context context, int id){
        return checkPermission(context, alterPermById(context, id));
    }


    public static boolean checkPermission(Context context, String arg){
        return ContextCompat.checkSelfPermission(context, arg) == PackageManager.PERMISSION_GRANTED;
    }

    public static String alterPermById(Context context, int id){
        return PERMISSION_PREFIX + context.getString(id);
    }

}
