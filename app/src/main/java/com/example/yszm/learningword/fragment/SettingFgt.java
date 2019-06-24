package com.example.yszm.learningword.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.yszm.learningword.R;
import com.example.yszm.learningword.activity.AboutActivity;
import com.example.yszm.learningword.activity.MyWordActivity;

/**
 * @author 佐达.
 * on 2019/5/28 15:41
 */
public class SettingFgt extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.setting_fragment,container,false);
        Button myword = view.findViewById(R.id.my_word);
        Button about = view.findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AboutActivity.class);
                startActivity(intent);
            }
        });
        myword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MyWordActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
