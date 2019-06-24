package com.example.yszm.learningword.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yszm.learningword.R;
import com.example.yszm.learningword.common.Const;
import com.example.yszm.learningword.model.Word;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;

import org.w3c.dom.Text;
/**
 * @author 佐达.
 * on 2019/5/29 14:22
 */
public class DetailFgt extends Fragment implements FragmentBackHandler {

    private onSpeechListener onSpeechListener;

    public static DetailFgt newInstance(String key,String phono,String trans,String exm) {
        DetailFgt detailFgt = new DetailFgt();
        Bundle bundle = new Bundle();
        bundle.putString("key",key);
        bundle.putString("phono",phono);
        bundle.putString("trans",trans);
        bundle.putString("exm",exm);
        detailFgt.setArguments(bundle);
        return detailFgt;
    }
    public DetailFgt(){
//        Log.d("DetailActivity","zuoda"+getArguments().getString("key"));
    }

    @Override
    public void onAttach(Context context) { //碎片和活动建立关联时调用
        super.onAttach(context);
        if(context instanceof onSpeechListener) //判断其左边对象是否为其右边类的实例
        {
            onSpeechListener  = (onSpeechListener)context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment,container,false);
        TextView  tv_key = view.findViewById(R.id.tv_key);
        TextView tv_phono = view.findViewById(R.id.tv_phono);
        TextView tv_trans = view.findViewById(R.id.tv_trans);
        TextView tv_exm = view.findViewById(R.id.tv_exam);
        //变量'word'是从内部类中访问的，需要声明为final
        final Bundle word = getArguments();
                                    //speech 监听
        ImageView  imageView = view.findViewById(R.id.icon_speech);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSpeechListener.speech(word.getString("key"));
            }
        });

        if(word != null) {
            tv_exm.setText(word.getString("exm"));
            tv_key.setText(word.getString("key"));
            tv_phono.setText("[" + word.getString("phono") + "]");
            tv_trans.setText(word.getString("trans"));
        }
        return view;
    }

    @Override
    public void onDetach() { // 解除关联
        super.onDetach();
        onSpeechListener = null;
    }

    @Override
    public boolean onBackPressed() {
        return BackHandlerHelper.handleBackPress(this);

    }

    //语音回调接口
    public interface onSpeechListener{
        void speech(String key);
    }
}
