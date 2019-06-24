package com.example.yszm.learningword.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.yszm.learningword.common.AbstractService;
import com.example.yszm.learningword.common.Const;
import com.example.yszm.learningword.common.DbopenHelper;
import com.example.yszm.learningword.model.Word;

import java.util.ArrayList;
import java.util.List;
/**
 * @author 佐达.
 * on 2019/5/21 15:13
 */
public class Wordservice  {
    public Wordservice(){

    }
    public static List<Word> getdata (Context context , String name,int Unit)
    {
        List<Word> words = new ArrayList<Word>();

        String sql = "select Id,word_key,word_phono,word_trans,word_example from "+name+" where word_unit = ? order by word_key ";
        DbopenHelper dbopenHelper = new DbopenHelper(context,Const.DB_NAME,null,1);
        SQLiteDatabase db = dbopenHelper.getDatabase();

        Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(Unit)});
        while(cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndex("Id"));
                String key = cursor.getString(cursor.getColumnIndex("word_Key"));
                String phono = cursor.getString(cursor.getColumnIndex("word_Phono"));
                String trans = cursor.getString(cursor.getColumnIndex("word_Trans"));
                String exam = cursor.getString(cursor.getColumnIndex("word_Example"));
                words.add(new Word(id, key, phono, trans, exam, Unit));
            }

        cursor.close();
        return words;
    }
    public static ArrayList<Word> queryData(Context context,String s)
    {
        ArrayList<Word> words = new ArrayList<>();
        DbopenHelper dbopenHelper = new DbopenHelper(context,Const.DB_NAME,null,1);
        SQLiteDatabase db = dbopenHelper.getDatabase();
        String sql = "select Id,word_key,word_phono,word_trans,word_example,word_unit from table_b where word_key like ? ;";
        Cursor cursor = db.rawQuery(sql,new String[]{ "%"+s+"%"});
        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("Id"));
                String key = cursor.getString(cursor.getColumnIndex("word_Key"));
                String phono = cursor.getString(cursor.getColumnIndex("word_Phono"));
                String trans = cursor.getString(cursor.getColumnIndex("word_Trans"));
                String exam = cursor.getString(cursor.getColumnIndex("word_Example"));
                int unit = cursor.getInt(cursor.getColumnIndex("word_Unit"));
                words.add(new Word(id, key, phono, trans, exam, unit));
            } while (cursor.moveToNext()) ;
        }
        cursor.close();
        return words;
    }
    public static List<Word> randomQueryData(Context context)
    {   //获取数据库有多少单词
        int count = 0;
        //随机数
        int random = 0;
        List<Word> list = new ArrayList<>();
        DbopenHelper dbopenHelper = new DbopenHelper(context,Const.DB_NAME,null,1);
        SQLiteDatabase db = dbopenHelper.getDatabase();
        Cursor cursor = db.rawQuery("select id from table_b",null);
        while (cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndex("Id"));
            count++;
        }

        random = (int) (Math.random() * count + 1);

        cursor = db.rawQuery("select word_key,word_phono,word_trans,status from table_b where Id = ?",new String[]{random+""});
        while(cursor.moveToNext())
        {
         String key = cursor.getString(cursor.getColumnIndex("word_Key"));
         String phono = cursor.getString(cursor.getColumnIndex("word_Phono"));
         String trans = cursor.getString(cursor.getColumnIndex("word_Trans"));
         int status = cursor.getInt(cursor.getColumnIndex("status"));
         list.add(new Word(key,phono,trans,status));
        }
        cursor.close();
        return list;
    }
    public static List<Word> statusData (Context context)
    {
        List<Word> words = new ArrayList<Word>();

        String sql = "select * from table_b where status = ?";
        DbopenHelper dbopenHelper = new DbopenHelper(context,Const.DB_NAME,null,1);
        SQLiteDatabase db = dbopenHelper.getDatabase();
        Cursor cursor = db.rawQuery(sql,new String[]{1+""});
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("Id"));
            String key = cursor.getString(cursor.getColumnIndex("word_Key"));
            String phono = cursor.getString(cursor.getColumnIndex("word_Phono"));
            String trans = cursor.getString(cursor.getColumnIndex("word_Trans"));
            String exam = cursor.getString(cursor.getColumnIndex("word_Example"));
            int unit = cursor.getInt(cursor.getColumnIndex("word_Unit"));
            words.add(new Word(id, key, phono, trans, exam,unit));
        }

        cursor.close();
        return words;
    }

}
