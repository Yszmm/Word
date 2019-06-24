package com.example.yszm.learningword.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.yszm.learningword.R;
import com.example.yszm.learningword.fragment.DetailFgt;
import com.example.yszm.learningword.fragment.SearchFgt;
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
 * on 2019/5/27 15:13
 */
public class SearchActivity extends AppCompatActivity implements SearchFgt.OnSearchClickListener
,DetailFgt.onSpeechListener{

    private SearchView searchView;

    private SearchFgt searchFgt;
    private DetailFgt detailFgt;

    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //获取活动关联片段
        fragmentManager = getSupportFragmentManager();
        initfragment();

    }

    public void initfragment(){
        //在与此FragmentManager关联的Fragments上启动一系列编辑操作
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        searchFgt = new SearchFgt();
        transaction.replace(R.id.search_fragment,searchFgt);
        transaction.commit();
    }
    @Override
    public void intentWord(Word word) {
        //设置搜索框图标化
        searchView.setIconified(true);
        searchView.setVisibility(View.INVISIBLE);
        //用于执行一组Fragment操作的API。
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(searchFgt);
        detailFgt = DetailFgt.newInstance(word.getKey(), word.getPhono(), word.getTrans(), word.getExample());
        //设置特定的动画在碎片中进入和退出特效。
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        //添加碎片到容器
        transaction.replace(R.id.search_fragment, detailFgt);
        transaction.commit();//提交
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //设置搜索按钮
        getMenuInflater().inflate(R.menu.menu_search,menu);
        //获取搜索按钮id
        MenuItem item = menu.findItem(R.id.menu_item_search);
        //返回此菜单项的当前设置操作视图。
        searchView = (SearchView) item.getActionView();
        searchView.setIconified(false);
        searchView.findFocus();
        searchView.setQueryHint("请输入要查询的单词");
        //用户操作监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {//当点击按钮时触发该方法
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {//当搜索内容改变时触发该方法
                List<Word> word = null;
                if(!TextUtils.isEmpty(s))
                {   //获取单词数据
                    word = data(Wordservice.queryData(SearchActivity.this,s));
                }
                //实时刷新数据
                searchFgt.refresh(word);
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {//当搜索框关闭时销毁当前活动
                finish();
                return false;
            }
        });

        return true;
    }

    /**
     * 隐藏单词详情页面
     * @param transaction
     */
    private void hideDetailFgm(FragmentTransaction transaction){
        // 设置搜索视图可见
        searchView.setVisibility(View.VISIBLE);
        //设置搜索图片不可见
        searchView.setIconified(false);
        //移除单词详情碎片
        transaction.remove(detailFgt);
        //显示搜索碎片
        transaction.replace(R.id.search_fragment,searchFgt);
    }
    private List<Word> data (ArrayList<Word> word) //获取单词数据
    {
        List<Word> key = new ArrayList<>();
        Iterator<Word> iterator = word.iterator();
        while(iterator.hasNext())
        {
            Word nextdata = iterator.next();
            int id = nextdata.getId();
            String wordKey = nextdata.getKey();
            String wordTrans = nextdata.getTrans();
            String wordPhono = nextdata.getPhono();
            String wordExm = nextdata.getExample();
            int wordUnit = nextdata.getUnit();
            key.add(new Word(id,wordKey,wordPhono,wordTrans,wordExm,wordUnit));

        }
        return key;
    }

    @Override
    public void onBackPressed() {
        //可以对碎片进行操作
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (detailFgt != null && detailFgt.isVisible()) {
            hideDetailFgm(transaction);
        } else {//否则结束当前活动
            super.onBackPressed();
        }
        transaction.commit();//提交
    }

    @Override
    public void speech(String key) {
        //创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        SpeechSynthesizer mTts= SpeechSynthesizer.createSynthesizer(this, null);
        //设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, "x_catherine");
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
