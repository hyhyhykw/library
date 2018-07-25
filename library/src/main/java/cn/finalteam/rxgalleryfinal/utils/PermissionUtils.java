package cn.finalteam.rxgalleryfinal.utils;

import android.app.Activity;
import android.widget.Toast;

import com.hy.library.R;
import com.hy.library.utils.Logger;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;

/**
 * Created time : 2018/4/19 11:43.
 *
 * @author HY
 */
public class PermissionUtils {
    private final Activity mActivity;
    private PermissionListener mPermissionListener;

    private Rationale mRationale;
    private PermissionSetting mSetting;

    public PermissionUtils setPermissionListener(PermissionListener permissionListener) {
        mPermissionListener = permissionListener;
        return this;
    }

    public PermissionUtils(Activity activity) {
        mActivity = activity;
        mRationale = new DefaultRationale();
        mSetting = new PermissionSetting(activity);
    }


    public void requestPermission(String... permissions) {
        AndPermission.with(mActivity)
                .permission(permissions)
                .rationale(mRationale)
                .onGranted(permission -> {
                    Logger.d("permission request success");
                    if (null != mPermissionListener) mPermissionListener.onResult();
                })
                .onDenied(permission -> {
                    Toast.makeText(mActivity, R.string.failure, Toast.LENGTH_SHORT).show();
                    if (AndPermission.hasAlwaysDeniedPermission(mActivity, permissions)) {
                        mSetting.showSetting(permission);
                    }
                })
                .start();
    }

    public interface PermissionListener {
        void onResult();
    }
}
