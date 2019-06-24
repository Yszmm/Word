package com.example.yszm.learningword.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yszm.learningword.R;
import com.example.yszm.learningword.activity.BottomTabbar;
import com.example.yszm.learningword.activity.SearchActivity;
import com.example.yszm.learningword.activity.UnitActivity;
import com.example.yszm.learningword.activity.WordListActivity;
import com.example.yszm.learningword.adapter.UnitAdapter;
import com.example.yszm.learningword.model.Word;
import com.example.yszm.learningword.service.Unitservice;
import com.example.yszm.learningword.service.Wordservice;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * @author 佐达.
 * on 2019/5/22 13:16
 */
public class UnitFgt extends Fragment implements AdapterView.OnItemClickListener
        ,FragmentBackHandler{

    private ListView listView;
    private UnitAdapter unitAdapter;
    private Context activity;

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
    public void onAttach(Context context) {
        super.onAttach(context);
        activity  = getActivity();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.activity_unit,container,false);


        listView = view.findViewById(R.id.unit_list);
        unitAdapter = new UnitAdapter(activity,0,Unitservice.getUnit(activity));
        listView.setAdapter(unitAdapter);
        listView.setOnItemClickListener(this);
        //获取活动关联片段
        fragmentManager  = getChildFragmentManager();

        //在fragment中使用oncreateOptionsMenu时需要在onCrateView中添加此方法，否则不会调用
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) { //跳转单词列表
        Intent intent = new Intent(activity,WordListActivity.class);
        intent.putExtra("unit_position",i+1);
        startActivity(intent);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        //设置搜索按钮
        inflater.inflate(R.menu.menu_search,menu);
        //获取搜索按钮id
        final MenuItem item = menu.findItem(R.id.menu_item_search);
        //返回此菜单项的当前设置操作视图。
        searchView = (SearchView) item.getActionView();
        if(searchView != null)
        {   //设置类型为普通文本
            searchView.setInputType(InputType.TYPE_CLASS_TEXT);
            //当按下搜索按钮时
            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //设置搜索按钮为图标
                    searchView.setIconified(true);
                    //跳转
                   Intent intent = new Intent(getActivity(),SearchActivity.class);
                   startActivity(intent);
              }
            });
        }

        super.onCreateOptionsMenu(menu, inflater);
    }


    /**
     * 隐藏单词详情页面
     * @param transaction
     */
    private void hideDetailFgm(FragmentTransaction transaction){
        //设置搜索图片不可见
        searchView.setIconified(false);
        // 设置搜索视图可见
        searchView.setVisibility(View.VISIBLE);
        //移除单词详情碎片
        transaction.hide(detailFgt);
        //显示搜索碎片
        transaction.show(searchFgt);
    }

    /**
     * 隐藏搜索页面
     * @param transaction
     */
    private void hideSearchFgm(FragmentTransaction transaction)
    {
        searchView.setQuery(null,false);
        searchView.clearFocus();//清楚焦点
        searchView.setIconified(true);
        transaction.hide(searchFgt);
        //设置listview为显示状态
        listView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onBackPressed() { //返回键处理 需要继承第三方接口FragmentBackHandler
        //可以对碎片进行操作
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //如果碎片对当前用户可见
        if (searchFgt != null && searchFgt.isVisible())
        {//移除搜索详情碎片
            hideSearchFgm(transaction);
        } else if (detailFgt != null && detailFgt.isVisible()) {
            hideDetailFgm(transaction);

        } else {//否则结束当前活动
            return BackHandlerHelper.handleBackPress(this);
        }
        transaction.commit();//提交
        return true;
    }
}
