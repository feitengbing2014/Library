package com.ddq.lib.view;

import android.content.Intent;

/**
 * Created by dongdaqing on 2017/7/18.
 */

public interface IReceiverView {
    void register(String... params);

    void onReceive(Intent intent);
}
