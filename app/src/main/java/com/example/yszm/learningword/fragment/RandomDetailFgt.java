package com.example.yszm.learningword.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yszm.learningword.R;
import com.example.yszm.learningword.common.Const;
import com.example.yszm.learningword.common.DbopenHelper;
import com.example.yszm.learningword.model.Word;
import com.example.yszm.learningword.service.Wordservice;

import java.util.List;

/**
 * @author 佐达.
 * on 2019/5/29 16:49
 */
public class RandomDetailFgt extends Fragment {

    private onClickUpdate onClickNext;
    private String newKey="";
    private View view;
    private  Button no;
    DbopenHelper dbopenHelper ;
    SQLiteDatabase db ;
    public static RandomDetailFgt newInstance (String key,String phono,String trans,int status){
        RandomDetailFgt randomDetailFgt = new RandomDetailFgt();
        Bundle bundle = new Bundle();
        bundle.putString("key",key);
        bundle.putString("phono",phono);
        bundle.putString("trans",trans);
        bundle.putInt("status",status);
        randomDetailFgt.setArguments(bundle);
        return randomDetailFgt;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onClickUpdate)
        {
            onClickNext = (onClickUpdate)context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onClickNext = null;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.randomdetail,container,false);
       final TextView key = view.findViewById(R.id.random_key);
       final TextView phono = view.findViewById(R.id.random_phono);
       final  TextView trans = view.findViewById(R.id.random_trans);
        no = view.findViewById(R.id.random_no);
        Button next = view.findViewById(R.id.random_next);
        ImageView imageView = view.findViewById(R.id.random_speech);

        dbopenHelper = new DbopenHelper(getActivity(),Const.DB_NAME,null,1);
        db = dbopenHelper.getDatabase();
        final Bundle word = getArguments();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newKey.isEmpty())
                {
                    onClickNext.speech(word.getString("key"));
                }
                else
                {
                    onClickNext.speech(newKey);
                }
            }
        });
        if(word != null)
        {
            key.setText(word.getString("key"));
            phono.setText("["+word.getString("phono")+"]");
            trans.setText(word.getString("trans"));
            if(word.getInt("status")==1)
            {
                no.setBackgroundResource(R.mipmap.timgyellow);
            }
            else{
                no.setBackgroundResource(R.mipmap.timg);
            }
        }

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(newKey.isEmpty())
                {
                    updateStatus(word.getString("key"));
                }
                else
                {
                    updateStatus(newKey);
                }

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Word> list = Wordservice.randomQueryData(getActivity());
                Word words = list.get(0);
                newKey = words.getKey();
                key.setText(newKey);
                phono.setText("["+words.getPhono()+"]");
                trans.setText(words.getTrans());
                if(words.getStatus() == 1)
                {
                    no.setBackgroundResource(R.mipmap.timgyellow);
                }
                else{
                    no.setBackgroundResource(R.mipmap.timg);
                }
            }
        });


        return view;
    }
    private void updateStatus(String key){
        Cursor cursor = db.rawQuery("select status from table_b where word_Key = ?",new String[]{key});
        cursor.moveToFirst();
        int status = cursor.getInt(cursor.getColumnIndex("status"));
        if(status == 0)
        {
            ContentValues values = new ContentValues();
            values.put("status", "1");
            db.update(Const.WORDS_B,values,"word_Key=?",new String[]{key});
            no.setBackgroundResource(R.mipmap.timgyellow);
            Toast.makeText(getActivity(),"添加"+key+"到单词本",Toast.LENGTH_LONG).show();
        }
        else {
            ContentValues values = new ContentValues();
            values.put("status", "0");
            db.update(Const.WORDS_B,values,"word_Key=?",new String[]{key});
            no.setBackgroundResource(R.mipmap.timg);
            Toast.makeText(getActivity(),"从单词本移除"+key,Toast.LENGTH_LONG).show();
        }
        cursor.close();
    }
    public interface onClickUpdate{
       void speech(String key);
    }

}
