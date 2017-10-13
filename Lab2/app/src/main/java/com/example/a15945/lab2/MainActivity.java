package com.example.a15945.lab2;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.support.design.widget.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private ImageView mImage = null;
    private RadioGroup mGroup = null;
    private Button mBtnLogin = null;
    private Button mBtnRegister = null;
    private EditText mNumberEdit = null;
    private EditText mPasswordEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImage = (ImageView) findViewById(R.id.imageView);
        mGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mBtnLogin = (Button) findViewById(R.id.button);
        mBtnRegister = (Button) findViewById(R.id.button2);

        mImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("上传头像")
                        .setItems(
                                new String[]{"拍摄", "从相册选择"},
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0:
                                                Toast.makeText(getApplication(), "您选择了[拍摄]", Toast.LENGTH_SHORT).show();
                                                break;
                                            case 1:
                                                Toast.makeText(getApplication(), "您选择了[从相册选择]", Toast.LENGTH_SHORT).show();
                                                break;
                                        }
                                    }
                                }
                        )
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getApplication(), "您选择了[取消]", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        )
                        .show();
            }
        });

        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int checkID) {
                switch (checkID) {
                    case R.id.radioButton:
                        Snackbar.make(radioGroup, "您选择了学生", Snackbar.LENGTH_SHORT)
                                .setAction("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(getApplication(), "Snackbar的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show();
                        break;
                    case R.id.radioButton2:
                        Snackbar.make(radioGroup, "您选择了教职工", Snackbar.LENGTH_SHORT)
                                .setAction("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(getApplication(), "Snackbar的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show();
                        break;
                }
            }
        });

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            final TextInputLayout layout1 = (TextInputLayout) findViewById(R.id.textInputLayout2);
            final TextInputLayout layout2 = (TextInputLayout) findViewById(R.id.textInputLayout3);

            public void onClick(View view) {
                mNumberEdit = layout1.getEditText();
                mPasswordEdit = layout2.getEditText();
                String number = mNumberEdit.getText().toString();
                String password = mPasswordEdit.getText().toString();
                if (TextUtils.isEmpty(number)) {
                    layout1.setErrorEnabled(true);
                    layout1.setError("学号不能为空");
                } else {
                    layout1.setErrorEnabled(false);
                    if (TextUtils.isEmpty(password)) {
                        layout2.setErrorEnabled(true);
                        layout2.setError("密码不能为空");
                    } else {
                        layout2.setErrorEnabled(false);
                        if(number.equals("123456") && password.equals("6666")){  //equals & == 的区别
                            Snackbar.make(view,"登录成功",Snackbar.LENGTH_SHORT)
                                    .setAction("确定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(getApplication(), "Snackbar的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .show();
                        }
                        else {
                            Snackbar.make(view,"学号或密码错误",Snackbar.LENGTH_SHORT)
                                    .setAction("确定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(getApplication(), "Snackbar的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .show();
                        }
                    }
                }
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view){
                int selected = mGroup.getCheckedRadioButtonId();
                switch (selected){
                    case R.id.radioButton:
                        Snackbar.make(view,"学生注册功能尚未启用",Snackbar.LENGTH_SHORT)
                                .setAction("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(getApplication(), "Snackbar的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show();
                        break;
                    case R.id.radioButton2:
                        Toast.makeText(getApplication(), "教职工注册功能尚未启用", Toast.LENGTH_SHORT).show();
                        break;
                    case -1:
                        Toast.makeText(getApplication(), "bug", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


    }
}

//
//    public void clickImg (View target) {
//        new AlertDialog.Builder(MainActivity.this)
//        .setTitle("上传头像")
//        .setItems(
//                new String[]{"拍摄", "从相册选择"},
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which){
//                            case 0:
//                                Toast.makeText(getApplication() ,"您选择了[拍摄]",Toast.LENGTH_SHORT).show();
//                            case 1:
//                                Toast.makeText(getApplication() ,"您选择了[从相册选择]",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//        )
//        .setNegativeButton("取消",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplication() ,"您选择了[取消]",Toast.LENGTH_SHORT).show();
//                    }
//                }
//        )
//        .show();
//        //Toast.makeText(getApplication() ,"ssssss",Toast.LENGTH_LONG).show();
//    }
