package com.example.yszm.learningword.activity;

import android.content.Intent;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.yszm.learningword.R;
import com.example.yszm.learningword.adapter.WordAdapter;
import com.example.yszm.learningword.common.Const;
import com.example.yszm.learningword.model.Word;
import com.example.yszm.learningword.service.Wordservice;

import java.util.ArrayList;
import java.util.List;
/**
 * @author 佐达.
 * on 2019/5/21 14:54
 */
public class WordListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private List<Word> list = new ArrayList<>();

    private ListView listView;
    private WordAdapter wordAdapter;

    private ActionBar actionBar; //活动中的主工具栏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            //设置是否应将主页显示为“向上”可供性
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listView = findViewById(R.id.word_list);

        list = Wordservice.getdata(this,Const.WORDS_B,getIntent().getIntExtra("unit_position",1));
        wordAdapter = new WordAdapter(this,0,list);

        listView.setAdapter(wordAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //获取当前文字
//        String text =  (String) ((TextView) view.findViewById(R.id.tv_item_word)).getText();
        int id =0;String wordKey="";String wordPhono="";String wordTrans="";String wordExample="";

        for(int n=0;n<list.size();n++)
        {
            Word word = list.get(i);
            id = word.getId();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //返回按钮
            case android.R.id.home:
                this.finish();
                return true;

                default:
        }
        return super.onOptionsItemSelected(item);
    }
}
