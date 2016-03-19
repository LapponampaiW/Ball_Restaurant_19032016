package com.su.lapponampai_w.testrestaurant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by apple on 3/19/16.
 */
public class MyopenHelper extends SQLiteOpenHelper { //ต้องใช้ความสามารถของ SQLiteOpenHelper


    //Explicit
    public static final String database_name = "Restaurant.db"; //ให้ใช้จากภายนอกได้ และมีค่าคงที่ ส่วน final ตัวแปรไม่สามารถเปลี่ยนค่าได้
    private static final int database_version = 1; //กำหนด version เริ่มต้นมันเท่ากับ หนึ่ง
    private static final String create_user_table ="create table userTABLE (" +
            "_id integer primary key, " +
            "User text," +
            "Password text," +
            "Name text);"; //สร้างรายละเอียดของ table  google กำหนดให้ใช้ _id เท่านั้น
                                            // primary key = auto increase having null
                                              // เป็นภาษา SQL ใช้คำสั่ง create table ............... ();


    private static final String create_food_table = "create table foodTABLE (" +
            "_id integer primary key, " +
            "Food text, " +
            "Price text, " +
            "Source text);";


    public MyopenHelper(Context context) {
        super(context,database_name,null,database_version); /*ทำให้ ต่อท่อไปหาอีกสิ่งได้

        constructor ทำ context ก่อน ต่อท่อ
        เดินหา database_name (หาข้อมูล) ครั้งแรกถ้าไม่มีจะ oncreate อัตโนมัติ
        null คือ security
        database version แล้วแต่ version
        */

    }  //Constructor

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(create_user_table);
        sqLiteDatabase.execSQL(create_food_table);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}  //Main Class
