package com.example.yszm.learningword.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.yszm.learningword.R;
import com.example.yszm.learningword.common.Const;
import com.example.yszm.learningword.common.DbopenHelper;
import com.example.yszm.learningword.common.FileUtils;
import com.example.yszm.learningword.common.InsertData;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 佐达.
 * on 2019/5/20 12:11
 */
public class MainActivity extends Activity {

    private DbopenHelper dbopenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化讯飞语音
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5ce3caa6");
        SharedPreferences sharedPreferences = getSharedPreferences("info", MODE_PRIVATE);
        boolean exist = sharedPreferences.getBoolean("TenthDelete", false);
        if (!exist) {
            SharedPreferences.Editor editor = getSharedPreferences("info",MODE_PRIVATE).edit();
            editor.putBoolean("TenthDelete",true);
            editor.apply();
            FileUtils.deleteDirectory(this);//删除数据
            FileUtils.firstWriteData(MainActivity.this); //写入数据
            //第一次执行程序插入数据
//            new FileTask().execute();//同一个异步实例对象只能执行1次
            startMain();
        } else {
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    startMain();
                }
            };
            Timer timer = new Timer();
            timer.schedule(timerTask, 1000);
        }
    }

    private void startMain() {
        Intent intent = new Intent(MainActivity.this, BottomTabbar.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    //异步任务
    private class FileTask extends AsyncTask<Void, Void, Void> {//3个泛型参数指定类型；若不使用，可用Void类型代替
        //此处指定为：输入参数 = Void类型、执行进度 = Void类型、执行结果 = Void类型
        // 方法1：onPreExecute（）
        // 作用：执行 线程任务前的操作
        @Override
        protected void onPreExecute() {
            //方法1
            // 执行前显示提示
        }
        @Override
        // 2:doInBackground（）
        // 作用：接收输入参数、执行任务中的耗时操作、返回 线程任务执行的结果
        // 注：必须复写，从而自定义线程任务
        protected Void doInBackground(Void... params) {
            return null;
        }
        @Override
        protected void onProgressUpdate(Void... progresses) {
            //  方法3
            //  作用：在主线程 显示线程任务执行的进度
        }
        // 4:onPostExecute（）
        // 作用：接收线程任务执行结果、将执行结果显示到UI组件
        // 注：必须复写，从而自定义UI操作
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
                    startMain();
        }
    }
}



