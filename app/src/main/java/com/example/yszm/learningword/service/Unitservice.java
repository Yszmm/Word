package com.example.yszm.learningword.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yszm.learningword.common.Const;
import com.example.yszm.learningword.common.DbopenHelper;
import com.example.yszm.learningword.model.Unit;

import java.util.ArrayList;
import java.util.List;
/**
 * @author 佐达.
 * on 2019/5/21 13:51
 */
public class Unitservice {
    public Unitservice(){

    }
    public static List<Unit> getUnit(Context context){
        List<Unit> units = new ArrayList<>();
        DbopenHelper dbopenHelper = new DbopenHelper(context,Const.DB_NAME,null,1);
        SQLiteDatabase db = dbopenHelper.getDatabase();
        String sql = "select Id,unit_key from table_unit";
        Cursor cursor = db.rawQuery(sql,null);
        while(cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndex("Id"));
            int unit_key = cursor.getInt(cursor.getColumnIndex("unit_key"));
            units.add(new Unit(id,unit_key));
        }
        cursor.close();
        return units;
    }
}
