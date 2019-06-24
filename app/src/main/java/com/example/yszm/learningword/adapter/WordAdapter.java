package com.example.yszm.learningword.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.yszm.learningword.R;
import com.example.yszm.learningword.model.Word;

import java.util.Iterator;
import java.util.List;
/**
 * @author 佐达.
 * on 2019/5/20 17:25
 */
public class WordAdapter extends ArrayAdapter<Word> {
    private Context context;


    public WordAdapter(Context context, int resource, List<Word> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @androidx.annotation.NonNull
    @Override
    public View getView(int position, @androidx.annotation.Nullable View convertView, @androidx.annotation.NonNull ViewGroup parent) {
        Word word = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_word,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tvWord =(TextView) convertView.findViewById(R.id.tv_item_word);
            convertView.setTag(viewHolder); //设置与此视图关联的标记
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvWord.setText(word.getKey());

        return convertView;
    }
    static class ViewHolder {
        TextView tvWord;
    }

}
