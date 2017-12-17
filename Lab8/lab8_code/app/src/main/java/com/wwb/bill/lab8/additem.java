package com.wwb.bill.lab8;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by 15945 on 2017/12/8.
 */

public class additem extends AppCompatActivity{
    private EditText addName;
    private EditText addBirthday;
    private EditText addGifts;
    private Button addBtn;

    final myDB helper = new myDB(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        initUI();
        addListener();
    }

    private void addListener() {
        addBtn.setOnClickListener(new clickAddBtn());
    }

    private void initUI() {
        addName = (EditText)findViewById(R.id.addName);
        addBirthday = (EditText)findViewById(R.id.addBirthday);
        addGifts = (EditText)findViewById(R.id.addGifts);
        addBtn = (Button)findViewById(R.id.addButton);
    }

    private class clickAddBtn implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(TextUtils.isEmpty(addName.getText())){
                Toast.makeText(getApplicationContext(), "名字为空，请完善", Toast.LENGTH_SHORT).show();
            }else if(helper.hasName(addName.getText().toString())){
                Toast.makeText(getApplicationContext(), "名字重复啦，请检查", Toast.LENGTH_SHORT).show();
            }else {
                helper.insert(addName.getText().toString(), addBirthday.getText().toString(), addGifts.getText().toString());
                Intent intent = new Intent();
                intent.setClass(additem.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}
