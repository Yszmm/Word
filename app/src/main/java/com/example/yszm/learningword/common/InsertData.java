package com.example.yszm.learningword.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
/**
 * @author 佐达.
 * on 2019/5/21 18:23
 */
public class InsertData {
    private InsertData(){

    }
    public static void  writedata (Context context)
    {
        try {
                                //getResourceAsStream 返回应用程序包实例
            InputStream is = context.getClass().getClassLoader().getResourceAsStream("assets/" + "data.json");
                                //从字符输入流中读取文本
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
                        //创建字符序列
            StringBuilder stringBuilder = new StringBuilder();
                        //readLine 读一行文字,如果已到达流的末尾，则为null
            while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
            }
            is.close();
            bufferedReader.close();

            try {
                //
                JSONArray jsonArray  = new JSONArray(stringBuilder.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject lan = jsonArray.getJSONObject(i);

                    DbopenHelper dbopenHelper = new DbopenHelper(context,Const.DB_NAME,null,1);
                    dbopenHelper.getWritableDatabase();
                    SQLiteDatabase database = dbopenHelper.getDatabase();

                    database.execSQL("insert into table_b (word_key,word_phono,word_trans,word_example,word_unit)"+
                            "values(?,?,?,?,?);",new Object[]{
                            lan.getString("word_key"),
                            lan.getString("word_phono"),
                            lan.getString("word_trans"),
                            lan.getString("word_example"),
                            lan.getString("word_unit")
                    });
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
