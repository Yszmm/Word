package com.example.yszm.learningword.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yszm.learningword.R;
import com.example.yszm.learningword.model.Unit;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
/**
 * @author 佐达.
 * on 2019/5/21 15:13
 */
public class UnitAdapter extends ArrayAdapter<Unit> {

    private Context context;

    public UnitAdapter(@NonNull Context context, int resource, @NonNull List<Unit> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Unit unit = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_unit,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_unit = convertView.findViewById(R.id.tv_unit);
            viewHolder.linearLayout = convertView.findViewById(R.id.item_unit);
            convertView.setTag(viewHolder);
        }
        else
        {

           viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_unit.setText(unit.getUnit_key()+"");
        if(position % 3 == 1)
        {
            viewHolder.linearLayout.setBackgroundResource(R.drawable.unit_yellow_bg);
        }else if(position % 3 == 2 )
        {
            viewHolder.linearLayout.setBackgroundResource(R.drawable.unit_red_bg);
        }
        else
        {
            viewHolder.linearLayout.setBackgroundResource(R.drawable.unit_blue_bg);
        }

        return convertView;
    }

    static class ViewHolder{
        TextView  tv_unit;
        LinearLayout linearLayout;

    }
}
