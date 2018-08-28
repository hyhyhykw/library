package com.hy.library.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v4.app.AppOpsManagerCompat;
import android.widget.Toast;

import com.hy.library.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;

/**
 * Created time : 2018/4/19 11:43.
 *
 * @author HY
 */
public class PermissionUtils {
    private final Context mActivity;
    private PermissionListener mPermissionListener;

    private Rationale mRationale;
    private PermissionSetting mSetting;

    public PermissionUtils setPermissionListener(PermissionListener permissionListener) {
        mPermissionListener = permissionListener;
        return this;
    }

    public PermissionUtils(Context activity) {
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


    private static boolean hasPermission(Context context, String permission) {
        String opStr = AppOpsManagerCompat.permissionToOp(permission);
        return opStr == null || context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    private static boolean hasRecordPermission() {
        int bufferSizeInBytes = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, bufferSizeInBytes);

        try {
            audioRecord.startRecording();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (audioRecord.getRecordingState() == 3) {
            audioRecord.stop();
            return true;
        } else {
            return false;
        }
    }


    public interface PermissionListener {
        void onResult();
    }
}
