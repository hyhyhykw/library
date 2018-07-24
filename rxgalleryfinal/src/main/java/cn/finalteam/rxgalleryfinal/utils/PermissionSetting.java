/*
 * Copyright © Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.finalteam.rxgalleryfinal.utils;

import android.content.Context;
import android.text.TextUtils;

import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.SettingService;

import java.util.List;

import cn.finalteam.rxgalleryfinal.R;

/**
 * Created by YanZhenjie on 2018/1/1.
 */
public final class PermissionSetting {

    private final Context mContext;

    public PermissionSetting(Context context) {
        this.mContext = context;
    }

    public void showSetting(final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(mContext, permissions);
        String message = mContext.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames));

        final SettingService settingService = AndPermission.permissionSetting(mContext);
        AlertDialog.newBuilder(mContext)
                .setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.setting, (dialog, which) -> settingService.execute())
                .setNegativeButton(R.string.no, (dialog, which) -> settingService.cancel())
                .show();
    }
}
