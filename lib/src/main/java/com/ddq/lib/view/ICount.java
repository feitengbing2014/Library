package com.ddq.lib.view;

/**
 * Created by dongdaqing on 2017/4/24.
 * 用于更新进度，例如文件上传下载
 */

public interface ICount {
    void update(long current, long total);
}
