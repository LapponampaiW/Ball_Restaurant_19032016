package com.su.lapponampai_w.testrestaurant;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class OrderActivity extends AppCompatActivity {

    //Explicit
    private TextView showOfficerTextView;
    private Spinner deskSpinner;
    private ListView foodListView;

    private String officerString, deskString, foodString, amountString; //สิ่งที่ต้องการโยนเข้าไปใน orderTABLE



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //bindWidget
        bindWidget();

        //Show view
        showView();

        //Create Desk Spinner
        createDeskSpinner();

        //Create Food ListView
        createFoodListView();




    } //Main Method

    private void createFoodListView() {

        //Read All SQLite
        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyopenHelper.database_name,
                MODE_PRIVATE, null); // เปิด หรือ สร้าง อะไรหละ ก็เรียก database ขึ้นมาก ซิ
        // ดึงข้อมูลทั้งก้อนมาอยู่ใน ram จะได้ประมวลผได้
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FORM " +
                MyManage.food_table, null); //ใส่ ภาษา SQL ตรง Search เขียนอย่างนี้ก็ได้
        cursor.moveToFirst();

        int intCount = cursor.getCount();


        String[] iconStrings = new String[intCount]; //จองหน่วยความจำ = จำนวนตัว
        String[] foodStrings = new String[intCount];
        String[] priceStrings = new String[intCount];

        for (int i = 0; i < intCount; i++) {
            iconStrings[i] = cursor.getString(cursor.getColumnIndex(MyManage.column_Source)); //การดึงค่า ที่ i = 0 มี source อะไรมาเก็บไว้ที่ iconStrings
            foodStrings[i] = cursor.getString(cursor.getColumnIndex(MyManage.column_Food));
            priceStrings[i] = cursor.getString(cursor.getColumnIndex(MyManage.column_Price));

            cursor.moveToNext();


        } //for
        cursor.close();

            FoodAdapter foodAdapter = new FoodAdapter(OrderActivity.this, iconStrings, foodStrings, priceStrings);
        foodListView.setAdapter(foodAdapter);

    }

    private void createDeskSpinner() {

        final String[] deskStrings = {"โต๊ะ 1","โต๊ะ 2","โต๊ะ 3","โต๊ะ 4","โต๊ะ 5","โต๊ะ 6","โต๊ะ 7","โต๊ะ 8","โต๊ะ 9","โต๊ะ 10"};

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,deskStrings);
                /* นำ arryAdapter  ของ google มาใช้ ต้องจำไปใช้เอง เป็น template มาใช้
                * simple_list_item_1 เป็น แบบ
                * deskStrings คือ ข้อมูลที่นำมาใช้*/
        deskSpinner.setAdapter(stringArrayAdapter);

        deskSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                deskString = deskStrings[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                deskString = deskStrings[0];
            }
        });

    } // desk Spinner


    private void showView() {
        //Receive Value From Intent
        officerString = getIntent().getStringExtra("Officer");
        showOfficerTextView.setText(officerString);

    }

    private void bindWidget() {
        showOfficerTextView = (TextView) findViewById(R.id.textView);
        deskSpinner = (Spinner) findViewById(R.id.spinner);
        foodListView = (ListView) findViewById(R.id.listView);

    }

} //Main class
