package com.example.yszm.learningword.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yszm.learningword.R;
import com.example.yszm.learningword.activity.BottomTabbar;
import com.example.yszm.learningword.adapter.SearchAdapter;
import com.example.yszm.learningword.model.Word;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;

import java.util.List;
/**
 * @author 佐达.
 * on 2019/5/27 14:49
 */
public class SearchFgt extends ListFragment implements FragmentBackHandler {


    private SearchAdapter searchAdapter;
    private OnSearchClickListener onSearchClickListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchClickListener) {
            onSearchClickListener = (OnSearchClickListener) context;
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchAdapter = new SearchAdapter<Word>(null,R.layout.item_word) {
            @Override
            public void bindView(ViewHolder holder, Word obj) {
                holder.word_key.setText(obj.getKey());
            }
        };
        setListAdapter(searchAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSearchClickListener = null;
    }

    public void refresh(List<Word> wordList) {
        searchAdapter.setmData(wordList);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d("BottomTabbar","zuoda"+onSearchClickListener);
        if(onSearchClickListener != null)
        {
            onSearchClickListener.intentWord((Word) l.getItemAtPosition(position));
        }
    }

    @Override
    public boolean onBackPressed() {

            return BackHandlerHelper.handleBackPress(this);
    }

    public interface OnSearchClickListener{
        /**
         * 跳转到单词活动
         * @param word
         */
        void intentWord(Word word);
    }

}
