package com.su.lapponampai_w.testrestaurant;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    //Explicit
    private MyManage myManage;
    private EditText userEditText, passwordEditText;
    private String userString, passwordString; //รับค่าจาก EditText เพื่อไปประมวลผล


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind Widget
        bindWidget();


        //Request SQLite
        myManage = new MyManage(this);

        //Test Add Value ใส่ค่าเข้าไปแบบ ทดสอบ
        //testAddValue();

        //Delete All SQLite
        deleteSQLite();

        //Syn JSON to SQLite
        synJSONtoSQLite();


    } // Main Method

    public void clickLogin(View view) { //เพื่อให้มองเห็นใน xml

        userString = userEditText.getText().toString().trim(); //เปลี่ยน editext ให้เป็น string และ trim ตัดช่องว่างหน้าหลัง
        passwordString = passwordEditText.getText().toString().trim();

        //Check Space
        if (userString.equals("") || passwordString.equals("")) { //แบบว่าค่าเป็น null หรือป่าว
            //Have space
            myAlert("มีช่องว่าง"); //เพื่อให้บอกว่าใช้ String Type


        } else {
            //No Space
            checkUser(); //ทำ method แยก
        }


    } //clickLogin

    private void checkUser() {

        try {

            String[] myResultStrings = myManage.searchUser(userString);
            //ไปใช้ Method searchUser ที่มีค่า เป็น Public ถ้าค้นเจอจะทำการ return เข้าไปใน myResultStrings


            //Check Password
            if (passwordString.equals(myResultStrings[2])) { //เช็ค password จาก myResultStrings ตำแหน่งที่ 2
                //password True
                Intent intent = new Intent(MainActivity.this,OrderActivity.class); // ทำการย้ายการทำงาน จากหน้าหนึ่งไปอีกหน้าหนึ่ง หรือ แอฟ ตัวอื่น หรือ ส่ง SMS หรือ ที่ Web view
                intent.putExtra("Officer", myResultStrings[3]);
                startActivity(intent);
                finish();


            } else {
                //Password False
                myAlert("Password ผิด");


            } //if else statement


            myAlert("ยินดีต้อนรับ " + myResultStrings[3]);

        } catch (Exception e) {
            myAlert("ไม่มี " + userString + " ในฐานข้อมูลของเรา");
        }

    } // checkUser

    private void myAlert(String strMessage) {
        Toast.makeText(MainActivity.this, strMessage, Toast.LENGTH_SHORT).show();


    }

    private void bindWidget() {
        userEditText = (EditText) findViewById(R.id.editText);
        passwordEditText = (EditText) findViewById(R.id.editText2);


    }

    private void synJSONtoSQLite() {

        //Connected http://
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();   //เป็นรูปแบบเลย ทุกๆ หน้าที่ต้องเป็น http ต้องทำตลอด
        StrictMode.setThreadPolicy(threadPolicy);

        int intTable = 0;
        while (intTable <= 1) {

            //1 Create InputStream
            InputStream inputStream = null;
            String[] urlStrings = {"http://swiftcodingthai.com/19Mar/php_get_user_master.php", "http://swiftcodingthai.com/19Mar/php_get_food_master.php"};

            try { // Tray catch Statement ใช้เมื่อมีการเสี่ยงต่อการ error

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(urlStrings[intTable]);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();


            } catch (Exception e) { //คือ error ในสิ่งที่ยอมรับได้และ error ไปอยู่ใน ตัวแปร e
                Log.d("Rest", "InputStream ==>" + e.toString()); //ถ้า error จะโวยวายใน log cat เอง ว่า error อะไร

            }


            //2 Create JSON String
            String strJSON = null;
            try {

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                //เข้ารหัส ขากลับ BufferedReader คือประมวลผล Buffer rate ในการตัดเชือก แล้วเอาไปวางหน้าบ้าน
                StringBuilder stringBuilder = new StringBuilder(); //ต่อสิ่งที่ทำการตัด จาก bufferedReader
                String strLine = null; //คล้ายๆ ตัวส่งเชือก

                while ((strLine = bufferedReader.readLine()) != null) { // strline ทำงานไปเรื่อยๆ จนไม่มีค่า (null) ให้เลิก ทำ
                    stringBuilder.append(strLine);

                }

                inputStream.close(); //เลิก load
                strJSON = stringBuilder.toString();  // เอาเชือกทั้งหมดมาอยู่ในตัวแปร strJSON


            } catch (Exception e) {
                Log.d("Rest", "JSON String==>" + e.toString());

            }


            //3 Update to SQLite (เอา  String มาหั่นเป็น ท่อนๆ)
            try {

                JSONArray jsonArray = new JSONArray(strJSON); //เป็น Class ที่จะรู้ว่าจะต้องตัดตรงไหน รับแต่ string เท่าน้น
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i); //ตำแหน่งที่ i

                    switch (intTable) {
                        case 0:
                            String strUser = jsonObject.getString(MyManage.column_User); //นำค่าไปใส่ คล้ายกับ
                            String strPassword = jsonObject.getString(MyManage.column_Password);
                            String strName = jsonObject.getString(MyManage.column_Name);

                            myManage.addValue(1, strUser, strPassword, strName);

                            break;
                        case 1:
                            String strFood = jsonObject.getString(MyManage.column_Food);
                            String strPrice = jsonObject.getString(MyManage.column_Price);
                            String strSource = jsonObject.getString(MyManage.column_Source);

                            myManage.addValue(2, strFood, strPrice, strSource);
                            break;
                    }

                }


            } catch (Exception e) {
                Log.d("Rest", "Update SQLite==>" + e.toString());
            }


            intTable += 1;

        }// while loop


    } //synJSON

    private void deleteSQLite() {

        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyopenHelper.database_name,
                MODE_APPEND, null); //จะเปิด database name
        sqLiteDatabase.delete(MyManage.user_table, null, null); //ลบใน user_table ทั้งหมด
        sqLiteDatabase.delete(MyManage.food_table, null, null); //ลบใน food_table ทั้งหมด


    }

    private void testAddValue() {

        myManage.addValue(1, "user", "Pass", "name");
        myManage.addValue(2, "food", "price", "source");


    }
} // Main Class
