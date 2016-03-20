package com.su.lapponampai_w.testrestaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by apple on 3/20/16.
 */
public class FoodAdapter extends BaseAdapter{

    // Adapter เป็นการโยนของ เป็นการ ต่อท่อ
    //Explicit
    private Context context;
    private String[] urlIconStrings, nameFoodStrings, priceStrings;  // เป็น Strings ของ Url ที่อยู่ใน Server อยู่ บน Server ไม่ได้อยู่ในนี้

    public FoodAdapter(Context context, String[] urlIconStrings, String[] nameFoodStrings, String[] priceStrings) {
        this.context = context;
        this.urlIconStrings = urlIconStrings;
        this.nameFoodStrings = nameFoodStrings;
        this.priceStrings = priceStrings;
    }

    @Override
    public int getCount() {
        return urlIconStrings.length; //กำหนดว่าจะสร้าง listview กี่ชุด
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //ขออนุญาติการใช้ Layout ต้องมีการเปิด service เสมอ เป็นทฤษฎี
        View view1 = layoutInflater.inflate(R.layout.food_listview, viewGroup, false);
        // ใส่ค่าลงไปทีละค่า ใน object view ส่งค่าไปทีละค่าให้ครับ เท่ากับจำนวน getCount ให้ view1 ทำการหิ้ว string เข้าไปตรงไหน

        //For Image ต้อง add Libraby Picaso ไปที่ project >> app >> libs >> แล้ว paste ลงไปในนี้
        //ถึงแม้จะ ใส่ลงมาแล้ว แต่ต้องทำให้ active ด้วย

        ImageView iconImageView = (ImageView) view1.findViewById(R.id.imageView2);
        Picasso.with(context).load(urlIconStrings[i]).resize(120,120).into(iconImageView);
                //resize คือ ไม่สนใจขนาดจริง จะกำหนดเท่ากับที่อยู่ใน layout

        //For Text
        TextView foodNameTextView = (TextView) view1.findViewById(R.id.textView2);
        foodNameTextView.setText(nameFoodStrings[i]);
        TextView priceTextView = (TextView) view1.findViewById(R.id.textView3);
        priceTextView.setText(priceStrings[i]);

        return view1;
    }

} //Main Class
