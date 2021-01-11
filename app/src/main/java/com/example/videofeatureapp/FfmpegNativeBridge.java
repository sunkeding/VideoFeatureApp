package com.example.videofeatureapp;

/**
 * Description：native方法操作类
 * Author：lxl
 * Date： 2017/11/5 17:07
 */
public class FfmpegNativeBridge {
    static {
        System.loadLibrary("native-lib");
    }
    /**
     * 设置是否处于调试状态
     * @param debug
     */
    public static native void setDebug(boolean debug);

    /**
     * 执行ffmpeg命令
     * @param cmd
     * @return
     */
    public static native int RunCommand(String[] cmd);
}
