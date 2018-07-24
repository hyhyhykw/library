package com.hy.library.entity;

import com.hy.library.receiver.NetChangeReceiver;

/**
 * Created time : 2018/4/10 17:51.
 *
 * @author HY
 */
public class NetChangeEvent {

    private final NetChangeReceiver.NetType mNetType;

    public NetChangeEvent(NetChangeReceiver.NetType netType) {
        mNetType = netType;
    }

    public NetChangeReceiver.NetType getNetType() {
        return mNetType;
    }
}
