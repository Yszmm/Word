package com.example.yszm.learningword.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.yszm.learningword.R;
import com.example.yszm.learningword.common.FileUtils;
import com.example.yszm.learningword.common.InsertData;
import com.example.yszm.learningword.fragment.RandomDetailFgt;
import com.example.yszm.learningword.fragment.SettingFgt;
import com.example.yszm.learningword.fragment.UnitFgt;
import com.example.yszm.learningword.model.Word;
import com.example.yszm.learningword.service.Wordservice;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.resource.Resource;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateEntity;
import com.xuexiang.xupdate.utils.UpdateUtils;
import com.xuexiang.xutil.resource.ResUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author 佐达.
 * on 2019/5/28 16:36
 */
public class BottomTabbar extends AppCompatActivity  implements RandomDetailFgt.onClickUpdate{



    private ViewPager mViewPager;
    private RadioGroup mTabRadioGroup;

    private List<Fragment> mFragments;
    private FragmentPagerAdapter mAdapter;

    //服务器版本名称
    private String serviceVersionName="";
    //当前版本名称
    private String VersionName ;
    String mUpdateUrl = "https://www.wuzuoda.top/Android/update.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_tabbar);

        VersionName = UpdateUtils.getVersionName(this);

        //第一次运行需要写入数据
//        FileUtils.writeData(BottomTabbar.this);
        //第一次运行需要插入数据
//        InsertData.writedata(BottomTabbar.this);
        initView();
        checkVersion();

    }


    private void initView() {

        mViewPager = findViewById(R.id.fragment_vp);
        mTabRadioGroup = findViewById(R.id.tabs_rg);

        List<Word> list = Wordservice.randomQueryData(this);
        Word word = list.get(0);

        // 初始化碎片
        mFragments = new ArrayList<>(4);
        mFragments.add(new UnitFgt());
        mFragments.add(RandomDetailFgt.newInstance(word.getKey(),word.getPhono(),word.getTrans(),word.getStatus()));
        mFragments.add(new SettingFgt());
        // 初始化 分页
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mAdapter);
        // 设置监听
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mTabRadioGroup.setOnCheckedChangeListener(mOnCheckedChangeListener);
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

    @Override
    protected void onDestroy() { //销毁时移除监听
        super.onDestroy();
        mViewPager.removeOnPageChangeListener(mPageChangeListener);
    }

    /**
     * 分页改变监听
     *
     */
    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) mTabRadioGroup.getChildAt(position);
            radioButton.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * 按钮改变监听
     */
    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    };

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter { //分页适配器

        private List<Fragment> mList;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.mList = list;
        }


        @Override
        public Fragment getItem(int position) { //获取当前数据项
            return this.mList == null ? null : this.mList.get(position);
        }

        @Override
        public int getCount() {
            return this.mList == null ? 0 : this.mList.size();
        }
    }

    @Override
    public void onBackPressed() { //覆盖Activity  onBackPressed 方法
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed();
        }    }

    private void checkVersion(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取json数据
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("https://www.wuzuoda.top/Android/update.json").build();
                    Response response = client.newCall(request).execute();
                    String Data = response.body().string();
                    parseJSONWithJSONObject(Data);
                    //判断是否更新
                    Log.d("BottomTabbar","zuoda===="+UpdateUtils.compareVersionName(VersionName,serviceVersionName));
                           //如果当前版本小于服务器版本 更新软件
                            if(UpdateUtils.compareVersionName(VersionName,serviceVersionName) == -1 ) {
                                XUpdate.newBuild(BottomTabbar.this)
                                        .updateUrl(mUpdateUrl)
                                        .themeColor(ResUtils.getColor(R.color.theme_color))//设置主题颜色
                                        .topResId(R.mipmap.top_4)//设置背景图片
                                        .supportBackgroundUpdate(true)
                                        .update();
                            }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithJSONObject(String jsonData)
        {
            try {
                JSONArray jsonArray = new JSONArray("["+jsonData+"]");
                for(int i=0; i < jsonArray.length();i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    serviceVersionName = jsonObject.getString("VersionName");
                }
            }catch (Exception e)
            {
                e.printStackTrace();

            }
        }

}




