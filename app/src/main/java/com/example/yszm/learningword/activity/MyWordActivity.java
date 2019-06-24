package com.example.yszm.learningword.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yszm.learningword.R;
import com.example.yszm.learningword.adapter.WordAdapter;
import com.example.yszm.learningword.fragment.DetailFgt;
import com.example.yszm.learningword.model.Word;
import com.example.yszm.learningword.service.Wordservice;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * @author 佐达.
 * on 2019/5/31 18:32
 */
public class MyWordActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
,DetailFgt.onSpeechListener {

    private ListView listView;
    private WordAdapter wordAdapter;
    private List<Word> list ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_word);
        listView = findViewById(R.id.my_word_list);
        list = initData(Wordservice.statusData(this));
        wordAdapter = new WordAdapter(this,0,list);
        listView.setAdapter(wordAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String wordKey="";String wordPhono="";String wordTrans="";String wordExample="";

        for(int n=0;n<list.size();n++)
        {
            Word word = list.get(i);
            wordKey = word.getKey();
            wordPhono = word.getPhono();
            wordTrans = word.getTrans();
            wordExample = word.getExample();
        }
        Intent intent = new Intent();
        intent.setClass(this,DetailActivity.class);
        intent.putExtra("key",wordKey);
        intent.putExtra("phono",wordPhono);
        intent.putExtra("trans",wordTrans);
        intent.putExtra("exm",wordExample);
        startActivity(intent);
    }

    @Override
    public void speech(String key) {
        //创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        SpeechSynthesizer mTts= SpeechSynthesizer.createSynthesizer(this, null);
        //设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        //设置语速
        mTts.setParameter(SpeechConstant.SPEED, "50");
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

    private List<Word> initData(List<Word> list){
        List<Word> wordList = new ArrayList<>();

        Iterator<Word> iterator = list.iterator();
        while(iterator.hasNext()) {
            Word nextWord = iterator.next();
            int id = nextWord.getId();
            String key = nextWord.getKey();
            String phono = nextWord.getPhono();
            String trans = nextWord.getTrans();
            String exm = nextWord.getExample();
            int unit = nextWord.getUnit();
            wordList.add(new Word(id,key,phono,trans,exm,unit));
        }
        return wordList;
    }
}
