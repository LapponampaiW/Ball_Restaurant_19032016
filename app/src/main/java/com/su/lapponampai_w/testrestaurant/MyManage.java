package com.su.lapponampai_w.testrestaurant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    public String[] searchUser(String strUser) { // การทำ Search
        try {

            String[] resultStrings = null;
            Cursor cursor = readSqLiteDatabase.query(user_table,
                    new String[]{column_id, column_User, column_Password, column_Name},
                    column_User + "=?",
                    new String[]{String.valueOf(strUser)},
                    null,null,null,null);  //cursor ตัวที่จะไปประมวลผลใน ram เพราะ Android จะได้แค่ write & read

            // table, censor (คล้าย select * form =? คือ เท่ากับ อะไรก็ได้) ,where
            // ทำการค้นหาที่ละบรรทัดไปเรื่อยๆ ถ้าเจอแล้วจะหยุด
            if (cursor != null ) { //ไม่เท่ากับความว่างป่าว คือต้องมี Data
                if (cursor.moveToFirst()) {
                    resultStrings = new String[4]; //จองหน่วยความจำ 4 ค่า id user password Name
                    for (int i = 0; i < 4; i++) {
                        resultStrings[i] = cursor.getString(i); //i จะวิ่งไปที่ 0 1 2 3 จะเอา id เป็น i ตำแน่งที่ 1 ... Name เป็น i ตำแหน่งที่ 3

                    }
                }

            } //if
            cursor.close();
            return resultStrings;

        } catch (Exception e) {
            return null;
        }


        //return new String[0];
    } //ให้มันทำการค้นหา
    /*เมื่อทำงานสำเร็จจะทำการ return ค่าเป็น array ถ้าค้นไม่เจอจะ error จิงๆ ไม่ดี แต่เราสามารถ
     ใส่ try catch เพื่อนำ error มาใช้ให้เป็นประโยชน์  การใส่ () เป็นการแสดงค่าเป็น method*/



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
