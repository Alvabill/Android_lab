package com.wwb.bill.lab7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    public final static String PACKAGE_NAME = "com.wwb.bill.lab7";
    private String VERSION_KEY = "myKey";
    private boolean isfirstPage = true;
    //初始化SharedPreferences
    private final int MODE = MODE_PRIVATE;
    private final String PREFERENCE_NAME = "UserDatabase";


    private EditText newPW;
    private EditText confirmPW;
    private EditText Password;
    private Button okBtn;
    private Button clearBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //通过判断版本号设置启动界面
        {
            PackageInfo info = null;
            try {
                info = getPackageManager().getPackageInfo(PACKAGE_NAME, 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            int currentVersion = info.versionCode;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

            int lastVersion = prefs.getInt(VERSION_KEY, 0);
            if (currentVersion > lastVersion) {
                //如果当前版本大于上次版本，该版本属于第一次启动
                setContentView(R.layout.firstpasswordpage);
                isfirstPage = true;
                //将当前版本写入preference中，则下次启动的时候，据此判断，不再为首次启动
                prefs.edit().putInt(VERSION_KEY, currentVersion).commit();
            } else{
                setContentView(R.layout.password_page);
                isfirstPage = false;
            }
        }

        getViewID();
        addListener();

    }

    private void addListener() {
        okBtn.setOnClickListener(new okBtnClick());
        clearBtn.setOnClickListener(new clearBtnClick());
        if (isfirstPage) {
            confirmPW.setOnEditorActionListener(new PWfinnsh());
        }else {
            Password.setOnEditorActionListener(new PWfinnsh());
        }
    }


    private void getViewID(){
        if (isfirstPage){
            newPW = (EditText)findViewById(R.id.NewPW);
            confirmPW  =(EditText)findViewById(R.id.ConfirmPW);
            okBtn = (Button)findViewById(R.id.okBtn1);
            clearBtn = (Button)findViewById(R.id.clearBtn1);
        }else {
            Password = (EditText)findViewById(R.id.Password);
            okBtn = (Button)findViewById(R.id.okBtn);
            clearBtn = (Button)findViewById(R.id.clearBtn);
        }
    }
    //重写confirmPW回车事件
    private class PWfinnsh implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            if (actionId == EditorInfo.IME_ACTION_DONE || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                okBtn.callOnClick();
                return true;
            }
            return false;
        }
    }

    private class okBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(isfirstPage) {
                if (TextUtils.isEmpty(newPW.getText()) || TextUtils.isEmpty(confirmPW.getText())) { //判断是否为空：null 或者 ""
                    Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
                }else{
                    if (newPW.getText().toString().equals(confirmPW.getText().toString())){ //判断字符串是否相等
                        String password = confirmPW.getText().toString();
                        SharedPreferences sharedPreferences = getSharedPreferences ( PREFERENCE_NAME, MODE );//创建SharedPreferences对象
                        SharedPreferences.Editor editor = sharedPreferences.edit();//通过SharedPreferences.Editor类进行修改
                        editor.putString("user",password);
                        editor.commit();
                        changeActivity();
                    }else {
                        Toast.makeText(getApplicationContext(),"Password Mismatch",Toast.LENGTH_SHORT).show();
                    }
                }
            }else{
                if (TextUtils.isEmpty(Password.getText())) { //判断是否为空：null 或者 ""
                    Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences sharedPreferences = getSharedPreferences ( PREFERENCE_NAME, MODE );//创建SharedPreferences对象
                    String userPassword = sharedPreferences.getString("user","0000");
                    if(Password.getText().toString().equals(userPassword)){ //判断字符串是否相等
                        changeActivity();
                    }else {
                        Toast.makeText(getApplicationContext(),"Password Mismatch",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void changeActivity() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,fileEditorActivity.class);
        startActivity(intent);
    }

    private class clearBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (isfirstPage){
                newPW.setText("");
                confirmPW.setText("");
            }else{
                Password.setText("");
            }
        }
    }
}

