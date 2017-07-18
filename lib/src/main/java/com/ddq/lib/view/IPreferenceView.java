package com.ddq.lib.view;

import android.content.SharedPreferences;

/**
 * Created by dongdaqing on 2017/7/18.
 */

public interface IPreferenceView {
    SharedPreferences getPreference();

    SharedPreferences getPreference(String name);
}