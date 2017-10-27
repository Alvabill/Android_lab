package com.example.a15945.lab3;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 15945 on 2017/10/23.
 */

public class goods extends Activity{
    private ListView listView;
    boolean flag = true;
    private int position;
    private TextView textView;
    private List<String> data_name;
    private List<String> data_price;
    private List<String> data_msg;
    private ImageView imageView;
    private int[] data_img_id;
    private static int j = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods);

        initData();
        Intent intent = getIntent();

        int i = 10000;
        position = intent.getIntExtra("position", i);

        if(position == 10000) {
            String name = intent.getStringExtra("name");
            for (int k = 0; k < data_name.size(); k++) {
                if (name.equals(data_name.get(k))) {
                    position = k;
                    break;
                }
            }
        }

        textView = (TextView)findViewById(R.id.goods_name);
        textView.setText(data_name.get(position));
        textView = (TextView)findViewById(R.id.goods_price);
        textView.setText(data_price.get(position));
        textView = (TextView)findViewById(R.id.goods_msg);
        textView.setText(data_msg.get(position));

        data_img_id = new int[]{R.mipmap.enchatedforest,R.mipmap.arla,R.mipmap.devondale,R.mipmap.kindle,R.mipmap.waitrose,R.mipmap.mcvitie,R.mipmap.ferrero,R.mipmap.maltesers,R.mipmap.lindt,R.mipmap.borggreve};
        imageView = (ImageView)findViewById(R.id.goods_picture);
        imageView.setImageResource(data_img_id[position]);


        listView = (ListView)findViewById(R.id.listView_goods);
        String[] Msg = new String[]{"   一键下单","   分享商品","   不感兴趣","   查看更多商品促销信息"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.msg,Msg);
        listView.setAdapter(arrayAdapter);
        listView.addFooterView(new ViewStub(this));
    }

    protected void initData() {
        String[] myData = getResources().getStringArray(R.array.item_name_array);
        String[] myData1 = getResources().getStringArray(R.array.goods_price_array);
        String[] myData2 = getResources().getStringArray(R.array.goods_msg_item);

        data_name = Arrays.asList(myData);
        data_price = Arrays.asList(myData1);
        data_msg = Arrays.asList(myData2);
    }

    public void back(View view){
        goods.this.finish();
    }

    public void star(View view){
        ImageView imageView = (ImageView)findViewById(R.id.imageStar);
        if(flag == true) {
            //Toast.makeText(getApplication(),"s",Toast.LENGTH_SHORT).show();
            imageView.setImageResource(R.mipmap.full_star);
            flag = false;
        }
        else {
            imageView.setImageResource(R.mipmap.empty_star);
            flag = true;
        }
    }

    public void shopping(View view){
        Toast.makeText(getApplication(),"商品已加到购物车",Toast.LENGTH_SHORT).show();
        textView = (TextView)findViewById(R.id.goods_name);
        String str = (String)textView.getText();
        int pos = 0;

        for(int i = 0;i<data_name.size();i++){
            if(str.equals(data_name.get(i))){
                pos = i;
                SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                editor.putInt("number"+j,pos);
                editor.commit();
           //     Toast.makeText(getApplicationContext(), pos+"设置成功", Toast.LENGTH_SHORT).show();
                j++;
                break;
            }
        }
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putInt("j",j);
        editor.commit();
     //   Toast.makeText(getApplicationContext(), j+"j设置成功", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this,shoppingcartList.class);
//        intent.putExtra("pos",pos);
//        //startActivity(intent);
    }
}
