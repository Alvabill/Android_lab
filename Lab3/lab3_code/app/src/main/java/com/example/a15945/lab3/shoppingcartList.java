package com.example.a15945.lab3;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


@SuppressLint("ParserError")
public class shoppingcartList extends Activity {
    private ListView listView;
    private FloatingActionButton floatingActionButton;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppingcart_list);

        listView = (ListView) this.findViewById(R.id.shoppingcart);

        SharedPreferences read = getSharedPreferences("data", MODE_PRIVATE);
        int value = read.getInt("j",0);
        int p = 0;
        list = getData();
        for (int j = 0;j<value;j++){
            p = read.getInt("number"+j,100);
            if(p != 100)
            list.add(push(p));
            else{
                //p == 100 说明已经删除数据，下面用替换的方式删除数据源
                SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                int k = 0;
                for(k = j;k<value-1;k++){
                    int k1 = read.getInt("number"+(k+1),100);
                    if(k1 != 100) {
                        editor.putInt("number" + k, k1);
                        editor.commit();
                    }
                        else break;
                }
                editor.remove("number"+ k);
                editor.commit();

            }
        }
//        Toast.makeText(getApplicationContext(), "value："+value, Toast.LENGTH_LONG).show();


        simpleAdapter = new SimpleAdapter(this,list,R.layout.itemcart,
                new String[]{"img","letter","name","price"},
                new int[]{R.id.imageView1,R.id.id_letter1,R.id.id_text1,R.id.id_price1});
        simpleAdapter.notifyDataSetChanged();
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,Object> map = list.get(position);
                String name = (String)map.get("name");
                if(!name.equals("购物车")) {
                    Intent intent = new Intent();
                    intent.putExtra("name", name);
                    intent.setClass(shoppingcartList.this, goods.class);
                    startActivity(intent);
                }else
                Toast.makeText(getApplication() ,"我要剁手", Toast.LENGTH_LONG).show();

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Map<String,Object> map = list.get(position);
                String name = (String)map.get("name");
                if(!name.equals("购物车")) {
                    new AlertDialog.Builder(shoppingcartList.this)
                            .setTitle("移除商品")
                            .setMessage("从购物车移除" + name + "?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                  //  Toast.makeText(getApplication(), "您选择了[确定]", Toast.LENGTH_SHORT).show();
                                    list.remove(position);
                                    simpleAdapter.notifyDataSetChanged();
                                    SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                                    editor.remove("number"+(position-1));
                                    editor.commit();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(getApplication(), "您选择了取消", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            ).show();
                }
                return false;
            }
        });
        floatingActionButton = (FloatingActionButton)findViewById(R.id.floatingActionButton1);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(shoppingcartList.this,shoppingList.class);
                startActivity(intent);
            }
        });
    }

    public Map<String,Object> push(int position){
        Map<String, Object> map = new LinkedHashMap<>();
        switch (position){
            case 0:
                map.put("img", R.drawable.img);
                map.put("letter", "E");
                map.put("name", "Enchated Forest");
                map.put("price","￥5.00");
                break;
            case 1:
                map.put("img", R.drawable.img);
                map.put("letter", "A");
                map.put("name", "Arla Milk");
                map.put("price","￥59.00");
                break;
            case 2:
                map.put("img", R.drawable.img);
                map.put("letter", "D");
                map.put("name", "Devondale Milk");
                map.put("price","￥79.00");
                break;
            case 3:
                map.put("img", R.drawable.img);
                map.put("letter", "K");
                map.put("name", "Kindle Oasis");
                map.put("price","￥2399.00");
                break;
            case 4:
                map.put("img", R.drawable.img);
                map.put("letter", "W");
                map.put("name", "Waitrose 早餐麦片");
                map.put("price","￥179.00");
                break;
            case 5:
                map.put("img", R.drawable.img);
                map.put("letter", "M");
                map.put("name", "Mcvitie\'s 饼干");
                map.put("price","￥14.90");
                break;
            case 6:
                map.put("img", R.drawable.img);
                map.put("letter", "F");
                map.put("name", "Ferrero Rocher");
                map.put("price","￥132.59");
                break;
            case 7:
                map.put("img", R.drawable.img);
                map.put("letter", "M");
                map.put("name", "Maltesers");
                map.put("price","￥141.43");
                break;
            case 8:
                map.put("img", R.drawable.img);
                map.put("letter", "L");
                map.put("name", "Lindt");
                map.put("price","￥139.43");
                break;
            case 9:
                map.put("img", R.drawable.img);
                map.put("letter", "B");
                map.put("name", "Borggreve");
                map.put("price","￥28.90");
                break;
            default:break;
        }
        return map;
    }

    public List<Map<String,Object>> getData(){
        List<Map<String, Object>> list1 = new ArrayList<>();

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("img", R.drawable.img);
        map.put("letter", "*");
        map.put("name", "购物车");
        map.put("price","价格");
        list1.add(map);

        return list1;
    }
    //自动刷新
    @Override
    protected void onResume() {
        super.onResume();
        onCreate(null);
    }
}
