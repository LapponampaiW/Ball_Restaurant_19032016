package com.su.lapponampai_w.testrestaurant;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by apple on 3/19/16.
 */
public class MyManage {

    //Explicit
    private MyopenHelper myopenHelper;
    private SQLiteDatabase writeSqLiteDatabase, readSqLiteDatabase; //process ที่ใช้ในการเขียน และ อ่าน


    //จะทำการ add ข้อมูลเข้าไปในตาราง userTABLE
    public static final String user_table = "userTABLE";
    public static final String column_id = "_id";
    public static final String column_User = "User";
    public static final String column_Password = "Password";
    public static final String column_Name = "Name";


    //จะทำการ add ข้อมูลเข้าไปในตาราง foodTABLE ไม่ต้องมี id 2 รอบใช้ร่วมกันได้
    public static final String food_table = "foodTABLE";
    public static final String column_Food = "Food";
    public static final String column_Price = "Price";
    public static final String column_Source = "Source";



    public MyManage(Context context) {//ถ้า object ต้องมีการติดต่อข้อมูลต้องทำ context เสมอ

        //Create & Connected SQLite (ถ้ายังไม่มี จะ Create ถ้ามีแล้วจะ Connect)
        myopenHelper = new MyopenHelper(context); // Connect กับยามเรียบร้อยแล้ว
        writeSqLiteDatabase = myopenHelper.getWritableDatabase(); //process เขียน
        readSqLiteDatabase = myopenHelper.getReadableDatabase(); //process อ่าน
    } //Constructor

    public long addValue(int intTABLE,
                         String strColumn2,
                         String strColumn3,
                         String strColumn4) { // ถ้า inTABLE มีค่าเท่ากับ 1 จะทำงานที่ userTABLE ถ้าเป็ฯ 2 จะทำงานที่ foodTABLE

        ContentValues contentValues = new ContentValues();  //เปลี่ยน string ให้กลายเป็น integer

        long mylong = 0;


        switch (intTABLE) {
            case 1:
                //For userTABLE
                contentValues.put(column_User, strColumn2);
                contentValues.put(column_Password, strColumn3);
                contentValues.put(column_Name, strColumn4);

                mylong = writeSqLiteDatabase.insert(user_table, null, contentValues);

                break;
            case 2:
                //For foodTABLE
                contentValues.put(column_Food, strColumn2);
                contentValues.put(column_Price, strColumn3);
                contentValues.put(column_Source, strColumn4);

                mylong = writeSqLiteDatabase.insert(food_table, null, contentValues);


                break;
            default:
                break;

        }



        return mylong;
    }


} //Main Class
