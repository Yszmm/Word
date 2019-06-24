package com.example.yszm.learningword.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yszm.learningword.R;
import com.example.yszm.learningword.adapter.UnitAdapter;
import com.example.yszm.learningword.adapter.WordAdapter;
import com.example.yszm.learningword.fragment.DetailFgt;
import com.example.yszm.learningword.fragment.SearchFgt;
import com.example.yszm.learningword.model.Unit;
import com.example.yszm.learningword.model.Word;
import com.example.yszm.learningword.service.Unitservice;
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
 * on 2019/5/23 11:33
 */
public class UnitActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
        ,SearchFgt.OnSearchClickListener,DetailFgt.onSpeechListener{

    private ListView listView;
    private UnitAdapter unitAdapter;
    private LinearLayout linearLayout;

    /**
     * 活动主工具栏
     */
    private ActionBar actionBar;
    /**
     * 碎片管理
     */
    private FragmentManager fragmentManager;
    /**
     * 搜索视图
     */
    private SearchView searchView;

    /**
     *  搜索碎片
     */
    private SearchFgt searchFgt;
    /**
     * 单词详情碎片
     */
    private DetailFgt detailFgt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);

        actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            //设置是否应将主页显示为"向上"可供性
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //获取活动关联片段
        fragmentManager  = getSupportFragmentManager();
        linearLayout = findViewById(R.id.unit_layout);

        listView = findViewById(R.id.unit_list);
        unitAdapter = new UnitAdapter(this,0,Unitservice.getUnit(this));
        listView.setAdapter(unitAdapter);
        //设置标题
        setTitle(getIntent().getStringExtra("word"));
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intent = new Intent(UnitActivity.this,WordListActivity.class);
        intent.putExtra("unit_position",i+1);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //设置搜索按钮
            getMenuInflater().inflate(R.menu.menu_search,menu);
        //获取搜索按钮id
            MenuItem item = menu.findItem(R.id.menu_item_search);
        //返回此菜单项的当前设置操作视图。
            searchView = (SearchView) item.getActionView();
            if(searchView != null)
            {
                //设置类型为普通文本
                searchView.setInputType(InputType.TYPE_CLASS_TEXT);
                searchView.setOnSearchClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //设置是否应将主页显示为"向上"可供性
                        actionBar.setDisplayHomeAsUpEnabled(true);
                        //设置listview隐藏状态
                        listView.setVisibility(View.GONE);
                        //在与此FragmentManager关联的Fragments上启动一系列编辑操作
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                            searchFgt = new SearchFgt();
                            transaction.replace(R.id.unit_fragment,searchFgt);
                        transaction.commit();
                    }
                });
            }
            searchView.setQueryHint("请输入要查询的单词");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {//当点击按钮时触发该方法

                return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {//当搜索内容改变时触发该方法
                    List<Word> word = null;
                    if(!TextUtils.isEmpty(s))
                    {
                        word = data(Wordservice.queryData(UnitActivity.this,s));
                    }
                    searchFgt.refresh(word);
                    return true;
                }
            });
            return true;
        }

    @Override
    public void intentWord(Word word) {  //跳转Detail界面
        //设置搜索框为不可见状态
        searchView.setVisibility(View.INVISIBLE);
        //设置搜索框内容为空且不提交
        searchView.setQuery(null,false);
        //设置搜索框图标化
        searchView.setIconified(true);
        setTitle("查询结果");
        //用于执行一组Fragment操作的API。
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        detailFgt = DetailFgt.newInstance(word.getKey(),word.getPhono(),word.getTrans(),word.getExample());
        //设置特定的动画在碎片中进入和退出特效。
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        //添加碎片到容器
        transaction.replace(R.id.unit_fragment,detailFgt);
        transaction.commit();//提交
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //返回按钮
            case android.R.id.home:
                //可以对碎片进行操作
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                //如果碎片对当前用户可见
                if(searchFgt != null && searchFgt.isVisible())
                {
                    //移除单词详情碎片
                    hideSearchFgm(transaction);
                }
                else if(detailFgt != null && detailFgt.isVisible()){
                    hideDetailFgm(transaction);
                }
                else {//否则结束当前活动
                    finish();
                }
                transaction.commit();//提交
                return true;

                default:
        }
        return super.onOptionsItemSelected(item);
    }
    private void hideDetailFgm(FragmentTransaction transaction){
        setTitle("词汇");
        //设置搜索图片不可见
        searchView.setIconified(false);
        // 设置搜索视图可见
        searchView.setVisibility(View.VISIBLE);
        //移除单词详情碎片
        transaction.hide(detailFgt);
        //显示搜索碎片
        transaction.show(searchFgt);
    }
    private void hideSearchFgm(FragmentTransaction transaction)
    {
        searchView.setQuery(null,false);
        searchView.clearFocus();//清楚焦点
        searchView.setIconified(true);
        transaction.hide(searchFgt);
        //设置listview为显示状态
        listView.setVisibility(View.VISIBLE);
    }
    private List<Word> data (ArrayList<Word> word)
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

//        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm"); //设置合成音频保存位置

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

    @Override
    public void onBackPressed() {  //重写系统返回键
        //可以对碎片进行操作
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //如果碎片对当前用户可见
        if(searchFgt != null && searchFgt.isVisible())
        {
            //移除单词详情碎片
            hideSearchFgm(transaction);
        }
        else if(detailFgt != null && detailFgt.isVisible()){
            hideDetailFgm(transaction);
        }
        else {//否则结束当前活动
            super.onBackPressed();
        }
        transaction.commit();//提交
    }
}

