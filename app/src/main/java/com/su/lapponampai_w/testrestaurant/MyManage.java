package com.su.lapponampai_w.testrestaurant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by apple on 3/19/16.
 */
public class MyManage {

    //Explicit
    private MyopenHelper myopenHelper;
    private SQLiteDatabase writeSqLiteDatabase, readSqLiteDatabase; //process ที่ใช้ในการเขียน และ อ่าน


    public MyManage(Context context) {//ถ้า object ต้องมีการติดต่อข้อมูลต้องทำ context เสมอ

        //Create & Connected SQLite (ถ้ายังไม่มี จะ Create ถ้ามีแล้วจะ Connect)
        myopenHelper = new MyopenHelper(context); // Connect กับยามเรียบร้อยแล้ว
        writeSqLiteDatabase = myopenHelper.getWritableDatabase(); //process เขียน
        readSqLiteDatabase = myopenHelper.getReadableDatabase(); //process อ่าน


    } //Constructor
} //Main Class
