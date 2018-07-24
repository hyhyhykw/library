package com.hy.library.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.hy.library.BaseApp;

/**
 * Created time : 2018/5/10 15:19.
 *
 * @author HY
 */
public class InitializeService extends IntentService {
    private static final String INIT_ACTION = "com.snhccm.touch.action.INIT";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public InitializeService() {
        super("Initialize");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (null == intent) return;
        String action = intent.getAction();
        if (!INIT_ACTION.equals(action)) return;

        BaseApp.getBaseApp().init();
        //友盟
    }


    public static void start(Context context) {
        context.startService(new Intent(context, InitializeService.class)
                .setAction(INIT_ACTION));
    }
}
