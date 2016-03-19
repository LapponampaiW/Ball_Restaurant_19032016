package com.su.lapponampai_w.testrestaurant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //Explicit
    private MyManage myManage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Request SQLite
        myManage = new MyManage(this);

        //Test Add Value ใส่ค่าเข้าไปแบบ ทดสอบ
        testAddValue();



    } // Main Method

    private void testAddValue() {

        myManage.addValue(1, "user", "Pass", "name");
        myManage.addValue(2, "food", "price", "source");


    }
} // Main Class
