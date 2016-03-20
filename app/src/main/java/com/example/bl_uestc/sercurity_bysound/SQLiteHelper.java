package com.example.bl_uestc.sercurity_bysound;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by LENOVO on 2016/3/15.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String TB_NAME = "keys";
    public final static String FIELD_id = "_id";
    public final static String Vname="Vname";
    public final static String Dh_B="Dh_B";
    public final static String DH_k="DH_k";
    public final static String Name="Name";
    public final static String PASSWORD="PASSWORD";
    public final static String EXT="EXT";

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table IF NOT EXISTS "+TB_NAME+"  (_id INTEGER primary key autoincrement, "+
                "Vname VARCHAR(100),Dh_a VAR_CHAR(100) ,Dh_B VARCHAR(100),DH_k VARCHAR（100），"+
                "Name  VARCHAR(25),PASSWORD VARCHAR(25),EXT VARCHAR(1000) )";
        Log.v("sound_sql",sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
    }
}
