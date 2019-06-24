package com.example.yszm.learningword.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yszm.learningword.R;
import com.example.yszm.learningword.model.Word;

import java.util.List;

import androidx.annotation.NonNull;
/**
 * @author 佐达.
 * on 2019/5/26 15:43
 */
public abstract class SearchAdapter<Word> extends BaseAdapter {

    private List<Word> mData ;
    private int resouceId;

    public SearchAdapter(List<Word> mData, int resouceId) {

        this.mData = mData;
        this.resouceId = resouceId;
    }

    @Override
    public Word getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null)
        {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(resouceId,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.word_key = view.findViewById(R.id.tv_item_word);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)view.getTag();
        }

        bindView(viewHolder,getItem(i));

        return view;
    }
    public abstract void bindView(ViewHolder holder, Word obj); //设置抽象方法 绑定视图

    public void setmData(List<Word> data)
    {
        mData = data;
        notifyDataSetChanged();
    }
   public static class ViewHolder{
        public TextView word_key;
    }

}
