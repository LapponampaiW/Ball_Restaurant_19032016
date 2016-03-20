package com.su.lapponampai_w.testrestaurant;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
                MODE_PRIVATE, null);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + MyManage.food_table, null);
        cursor.moveToFirst();
        int intCount = cursor.getCount();

        String[] iconStrings = new String[intCount];
        final String[] foodStrings = new String[intCount];
        String[] priceStrings = new String[intCount];

        for (int i = 0; i < intCount; i++) {

            iconStrings[i] = cursor.getString(cursor.getColumnIndex(MyManage.column_Source));
            foodStrings[i] = cursor.getString(cursor.getColumnIndex(MyManage.column_Food));
            priceStrings[i] = cursor.getString(cursor.getColumnIndex(MyManage.column_Price));

            cursor.moveToNext();
        }   // for
        cursor.close();

        FoodAdapter foodAdapter = new FoodAdapter(OrderActivity.this, iconStrings,
                foodStrings, priceStrings);
        foodListView.setAdapter(foodAdapter);

        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                foodString = foodStrings[i];

                chooseAmount();

            }
        });

    }


    private void chooseAmount() {

        CharSequence[] charSequences = {"1 จาน", "2 จาน", "3 จาน", "4 จาน", "5 จาน"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this); //เจอ Builder แล้ว ctrl + space เลย
        builder.setTitle(foodString);
        builder.setSingleChoiceItems(charSequences, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                amountString = Integer.toString(i + 1); //เปลี่ยนตัวเลขให้กลายเป็นตัวอักษร โดยเอาค่า ของ i มาบวก หนึง

                dialogInterface.dismiss(); // ทำให้หายไป

                confirmOrder(); //พอกด command + enter จะมีให้เลือก 2 อย่าง สร้างใน Method หรือ สร้าง นอก Method


            }
        }); //OnclickListener คือการ เก็บ Event จากการ คลิก !!!! เก็บว่า order กี่จาน

        builder.show();

    }

    private void confirmOrder() { //ทำการ Confirm order

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.icon_myaccount);
        builder.setTitle("โปรดตรวจทาน");
        builder.setMessage("Officer =" + officerString + "\n" +
                "Desk = " + deskString + "\n" +
                "Food =" + foodString + "\n" +
                "Amount = " + amountString);



        //ทำปุ่ม cancel กับ ปุ่ม Ok
       builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { //setPositiveButtom เปลี่ยนเป็นอย่างอื่นได้ เต็มไปหมด
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateToServer();
                dialogInterface.dismiss();
            }
        });
        builder.show();

    } //confirmOrder

    private void updateToServer() {
    }

    private void createDeskSpinner() {

        final String[] deskStrings = {"โต๊ะ 1", "โต๊ะ 2", "โต๊ะ 3", "โต๊ะ 4", "โต๊ะ 5", "โต๊ะ 6", "โต๊ะ 7", "โต๊ะ 8", "โต๊ะ 9", "โต๊ะ 10"};

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, deskStrings);
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
