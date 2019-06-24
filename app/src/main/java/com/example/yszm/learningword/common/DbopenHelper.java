package com.example.yszm.learningword.common;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
/**
 * @author 佐达.
 * on 2019/5/21 14:58
 */
public class DbopenHelper extends SQLiteOpenHelper {

    Context context;

    public DbopenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//            String sql = "create table table_b("+
//                    "Id integer primary key autoincrement,"+
//                    "word_Key text,"+
//                    "word_Phono ntext,"+
//                    "word_Trans ntext,"+
//                    "word_Example ntext,"+
//                    "word_Unit integer)";
//        String sql = "create table table_unit("+
//                "Id integer primary key autoincrement,"+
//                "unit_key integer,"+
//                "unit_time double)";
//           sqLiteDatabase.execSQL(sql);
//        Toast.makeText(context,"创建成功",Toast.LENGTH_SHORT).show();
    }

    //获取数据库
    public SQLiteDatabase getDatabase(){

                                    //getDir(需要检索的目录名称 , 操作模式)       file.separator 分隔符/
        String Path = context.getDir(Const.DB_DIR,Context.MODE_PRIVATE)   +   File.separator       +   Const.DB_NAME;
            ///data/user/0/com.example.yszm.learningword/app_databases/words.db
        return SQLiteDatabase.openDatabase(Path,null,SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}