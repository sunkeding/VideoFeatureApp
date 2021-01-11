package com.example.videofeatureapp;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class TestFFmpegActivity extends AppCompatActivity {
    private String mFileBeforeName = "keepvideo.mp4";
    private String mFileAfterName = "keepvideoout.mp4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},100);
        }
        String pa= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            pa = getExternalCacheDir().toString();
        }
        Log.d("xxxxx","pa:"+pa);
        FfmpegNativeBridge.setDebug(true);//开启日志系统

        Button bt = (Button) findViewById(R.id.bt_text);
        final ProgressBar pb = (ProgressBar) findViewById(R.id.pb);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String basePath = Environment.getExternalStorageDirectory().getPath();
                        if (new File(basePath + "/" + mFileBeforeName).exists() && !new File(basePath + "/" + mFileAfterName).exists()) {
                            String cmd_transcoding = String.format(
                                    "ffmpeg -i %s -c:v libx264 -c:a libfdk_aac %s",
                                    basePath + "/" + mFileBeforeName,
                                    basePath + "/" + mFileAfterName);
//                            String cmd_transcoding ="ffmpeg -i "+basePath+File.separator+mFileBeforeName;
//                            String cmd_transcoding ="ffmpeg";
                            Log.e("命令", cmd_transcoding);
                            int i = FFmpegCMDRun(cmd_transcoding);
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    pb.setVisibility(View.GONE);
                                    Toast.makeText(TestFFmpegActivity.this, "压缩成功了", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (new File(basePath + "/" + mFileBeforeName).exists() && new File(basePath + "/" + mFileAfterName).exists()) {
                            pb.post(new Runnable() {
                                @Override
                                public void run() {
                                    pb.setVisibility(View.GONE);
                                    Toast.makeText(TestFFmpegActivity.this, "已经压缩成功了", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pb.setVisibility(View.GONE);
                                    Toast.makeText(TestFFmpegActivity.this, "没找过该视频文件", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });

    }

    /**
     * 以空格分割字符串
     */
    public int FFmpegCMDRun(String cmd) {
        String regulation = "[ \\t]+";
        String[] split = cmd.split(regulation);
        //执行命令
        return FfmpegNativeBridge.RunCommand(split);
    }
}
