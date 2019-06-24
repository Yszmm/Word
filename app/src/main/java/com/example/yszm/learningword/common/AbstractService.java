package com.example.yszm.learningword.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class AbstractService {

        public static SQLiteDatabase db;

    public AbstractService(Context context ){
        DbopenHelper databases = new DbopenHelper(context,Const.DB_NAME,null,1);
            db = databases.getWritableDatabase();
        }

}
