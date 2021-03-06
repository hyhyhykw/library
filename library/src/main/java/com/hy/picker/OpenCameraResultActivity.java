package com.hy.picker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.hy.library.R;
import com.hy.library.base.CommonBaseActivity;
import com.hy.library.utils.Logger;
import com.hy.picker.utils.CommonUtils;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created time : 2018/8/23 10:56.
 *
 * @author HY
 */
public class OpenCameraResultActivity extends CommonBaseActivity {
    public static final int REQUEST_OPEN_CAMERA = 0x357;
    public static final int REQUEST_OPEN_EDIT = 0x753;
    private File mEditFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestCamera();
    }

    @Override
    public void onSucceed(int requestCode) {
        super.onSucceed(requestCode);
        if (requestCode == REQUEST_CAMERA)
            openCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PhotoPicker.destroy();
    }

    private void toEdit(Uri uri) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!path.exists()) {
            boolean mkdirs = path.mkdirs();
            Logger.d("文件夹：" + path + "创建" + (mkdirs ? "成功" : "失败"));
        }

        String name = "IMG-EDIT-" + CommonUtils.format(new Date(), "yyyy-MM-dd-HHmmss") + ".jpg";
        mEditFile = new File(path, name);

        startActivityForResult(new Intent(this, IMGEditActivity.class)
                .putExtra(IMGEditActivity.EXTRA_IMAGE_URI, uri)
                .putExtra(IMGEditActivity.EXTRA_IMAGE_SAVE_PATH, mEditFile.getAbsolutePath()), REQUEST_OPEN_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_OPEN_CAMERA) {
                if (mTakePictureUri != null) {
                    String path = mTakePictureUri.getEncodedPath();// getPathFromUri(this, mTakePhotoUri);

                    if (mTakePictureUri.toString().startsWith("content")) {
                        path = path.replaceAll("/external_storage_root", "");

                        path = Environment.getExternalStorageDirectory() + path;
                    }

                    File file = new File(path);
                    if (file.exists()) {
                        PictureSelectorActivity.PicItem item = new PictureSelectorActivity.PicItem();
                        item.uri = path;
                        item.selected = true;

                        MediaScannerConnection.scanFile(this, new String[]{path}, null, (path1, uri) -> Logger.d("path===" + path1));
                        if (PhotoPicker.isEdit) {
                            toEdit(Uri.fromFile(file));
                        } else {
                            PhotoPicker.sTakePhotoListener.onTake(item);
                            finish();
                        }

                    } else {
                        Toast.makeText(this, R.string.picker_photo_failure, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(this, R.string.picker_photo_failure, Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else if (requestCode == REQUEST_OPEN_EDIT) {
                if (mEditFile != null) {
                    PictureSelectorActivity.PicItem item = new PictureSelectorActivity.PicItem();
                    String uriPath = mEditFile.getAbsolutePath();
                    item.uri = uriPath;
                    item.selected = true;
                    MediaScannerConnection.scanFile(this, new String[]{uriPath}, null, (path, uri) -> Logger.d("crop path===" + path));

                    PhotoPicker.sTakePhotoListener.onTake(item);
                } else {
                    Toast.makeText(this, R.string.picker_photo_failure, Toast.LENGTH_SHORT).show();
                }
                finish();

            } else {
                finish();
            }
        } else {
            finish();
        }

    }

    private Uri mTakePictureUri;

    protected void openCamera() {
        if (!CommonUtils.existSDCard()) {
            Toast.makeText(this, R.string.picker_empty_sdcard, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!path.exists()) {
            boolean mkdirs = path.mkdirs();
            Logger.d("文件夹：" + path + "创建" + (mkdirs ? "成功" : "失败"));
        }

        String name = "IMG-" + CommonUtils.format(new Date(), "yyyy-MM-dd-HHmmss") + ".jpg";
        File file = new File(path, name);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resInfoList.size() <= 0) {
            Toast.makeText(this, getResources().getString(R.string.picker_voip_cpu_error), Toast.LENGTH_SHORT).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mTakePictureUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".file_provider", file);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                mTakePictureUri = Uri.fromFile(file);
            }

            intent.putExtra(MediaStore.EXTRA_OUTPUT, mTakePictureUri);
            startActivityForResult(intent, REQUEST_OPEN_CAMERA);
        }
    }

}
