package com.example.yszm.learningword.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.yszm.learningword.R;
import com.example.yszm.learningword.common.Const;
import com.example.yszm.learningword.common.WavWriter;
import com.example.yszm.learningword.fragment.DetailFgt;
import com.example.yszm.learningword.model.Word;
import com.example.yszm.learningword.service.Wordservice;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
/**
 * @author 佐达.
 * on 2019/5/26 14:32
 */
public class DetailActivity extends AppCompatActivity implements DetailFgt.onSpeechListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initfragment();

    }

   public void initfragment(){
       Intent intent = getIntent();
       FragmentManager fragmentManager = getSupportFragmentManager();
       FragmentTransaction transaction = fragmentManager.beginTransaction();
       transaction.replace(R.id.detail,DetailFgt.newInstance(intent.getStringExtra("key"),
               intent.getStringExtra("phono"),
               intent.getStringExtra("trans"),
               intent.getStringExtra("exm")));
       transaction.commit();
   }

    @Override
    public void speech(String key) {
        //创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        SpeechSynthesizer mTts= SpeechSynthesizer.createSynthesizer(this, null);
        //设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, "x_xiaoxue");
        //设置语速
        mTts.setParameter(SpeechConstant.SPEED, "30");
        //设置音量，范围0~100
        mTts.setParameter(SpeechConstant.VOLUME, "80");
        //设置云端
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        //设置合成音频保存位置
        //mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/speech/"+key+".pcm");
        //合成监听器
        SynthesizerListener mSynListener = new SynthesizerListener() {
            @Override
            public void onSpeakBegin() { //开始播放

            }

            @Override
            public void onBufferProgress(int i, int i1, int i2, String s) {//缓冲进度回调

            }

            @Override
            public void onSpeakPaused() { //暂停播放

            }

            @Override
            public void onSpeakResumed() {//恢复播放回调接口

            }

            @Override
            public void onSpeakProgress(int i, int i1, int i2) {//播放进度回调

            }

            @Override
            public void onCompleted(SpeechError speechError) { //会话结束回调接口，没有错误时，error为null
                if(speechError != null)
                {
                    Toast.makeText(getApplicationContext(),"请检查网络",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {//会话事件回调接口

            }
        };

        //开始合成
        mTts.startSpeaking(key, mSynListener);

    }
}
