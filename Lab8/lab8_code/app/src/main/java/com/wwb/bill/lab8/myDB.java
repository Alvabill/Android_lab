package com.wwb.bill.lab8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 15945 on 2017/12/8.
 */

public class myDB extends SQLiteOpenHelper {
    private static final String DB_NAME = "Contacts.db";
    public static final String TABLE_NAME = "Contacts";
    private static final int DB_VERSION = 1;

    private myDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //数据库的构造函数，传递三个参数的
    public myDB(Context context, String name, int version){
        this(context, name, null, version);
    }

    //数据库的构造函数，传递一个参数的， 数据库名字和版本号都写死了
    public myDB(Context context){
        this(context, DB_NAME, null,  DB_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREACT_TABLE = "create table " + TABLE_NAME + "(_id integer primary key , name text, birthday text, gifts text);";
        sqLiteDatabase.execSQL(CREACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //查询方法
    public String[] getPoint(int position) {
        String[] str = new String[3];
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from Contacts limit ?,?",
                new String[] { String.valueOf(position),
                        String.valueOf(1) });
        while (cursor.moveToNext()) {
            str[0] = cursor.getString(1);
            str[1] = cursor.getString(2);
            str[2] = cursor.getString(3);
        }
        return str;
    }

    public boolean hasName(String name){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Contacts",null);
        while (cursor.moveToNext()){
            String strName = cursor.getString(1);
            if(strName.equals(name)){
                return true;
            }
        }
        return false;
    }
    //插入方法
    public void insert(String name, String birthday, String gifts){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("birthday", birthday);
        values.put("gifts", gifts);
        db.insert(TABLE_NAME, null ,values);
        db.close();
    }
    //更新方法
    public void update(String name, String birthday, String gifts){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        String whereClause = "name = ?";  //主键列名 = ？
        String[] whereArgs = { name };  //主键的值
        values.put("name", name);
        values.put("birthday", birthday);
        values.put("gifts", gifts);
        db.update(TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }
    //删除方法
    public void delete(String name){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "name = ?";  //主键列名 = ？
        String[] whereArgs = { name };  //主键的值
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

}
