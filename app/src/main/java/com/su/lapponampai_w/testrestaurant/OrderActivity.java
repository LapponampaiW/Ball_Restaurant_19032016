package com.su.lapponampai_w.testrestaurant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

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

    private void confirmOrder() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.icon_myaccount);
        builder.setTitle("โปรดตรวจทาน");
        builder.setMessage("Officer = " + officerString + "\n" +
                "Desk = " + deskString + "\n" +
                "Food = " + foodString + "\n" +
                "Amount = " + amountString);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateToServer();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();


    }   // confirmOrder

    private void updateToServer() {

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy); //ขออนุญาติ connect protocal ปกติต้อง connect ผ่าน browser แต่อันนี้ให้ Android ขออนุญาติโดยตรง

        try {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("isAdd", "true")); //ข้างหน้า key คืออะไร
            nameValuePairs.add(new BasicNameValuePair("Officer", officerString));
            nameValuePairs.add(new BasicNameValuePair("Desk", deskString));
            nameValuePairs.add(new BasicNameValuePair("Food", foodString));
            nameValuePairs.add(new BasicNameValuePair("Amount", amountString)); //key สี เขียว ต้องตรงกับ server ที่ทำไว้

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://swiftcodingthai.com/19Mar/php_add_order.php");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            httpClient.execute(httpPost);





            Toast.makeText(OrderActivity.this, "Order" + foodString + " เรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            Toast.makeText(OrderActivity.this, "ไม่สามารถเชื่อมต่อ Server ได้", Toast.LENGTH_SHORT).show();

        }

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
