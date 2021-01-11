package com.example.videofeatureapp

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.videofeatureapp.FFmpegHandler.*
import java.io.File

class MainActivity : AppCompatActivity() {
    private val PATH = Environment.getExternalStorageDirectory().path
    var sample_text:TextView?=null
    var begin=0L
    var handler=object :Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MSG_BEGIN -> {
                    begin=System.currentTimeMillis()
//                    layoutProgress.setVisibility(View.VISIBLE)
//                    layoutVideoHandle.setVisibility(View.GONE)
                }
                MSG_FINISH -> {
//                    layoutProgress.setVisibility(View.GONE)
//                    layoutVideoHandle.setVisibility(View.VISIBLE)
//                    if (isJointing) {
//                        isJointing = false
//                        FileUtil.deleteFile(outputPath1)
//                        FileUtil.deleteFile(outputPath2)
//                        FileUtil.deleteFile(listPath)
//                    }
                }
                MSG_PROGRESS -> {
                    val progress = msg.arg1
                    val duration = msg.arg2
                    if (progress > 0) {
                        sample_text?.setVisibility(View.VISIBLE)
                        val percent = if (duration > 0) "%" else ""
                        val strProgress = progress.toString() + percent
                        sample_text?.setText(strProgress)
                        if (strProgress == "100%") {
                            val cost: Long = System.currentTimeMillis() - begin
                            Log.d("xxxxxx", "cost:$cost")
                        }
                    } else {
                        sample_text?.setVisibility(View.INVISIBLE)
                    }
                }
                else -> {
                }
            }
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE),100)
        }
        // Example of a call to a native method
        sample_text=findViewById<TextView>(R.id.sample_text)
        sample_text?.text = stringFromJNI()
        val transformVideo: String =
            PATH + File.separator + "transformVideo.mp4"
//        val srcFile=PATH + File.separator+"DCIM"+File.separator+"Camera"+ File.separator+"XHS_02730b8cff0e2c6abc49ef22ae783be7.mp4"
        val srcFile=PATH + File.separator+ File.separator+"keepvideo.mp4"
        val commandLine = FFmpegUtil.transformVideo(srcFile, transformVideo)
        FFmpegHandler(handler).executeFFmpegCmd(commandLine)
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}