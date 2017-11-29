package com.wwb.bill.lab7;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by 15945 on 2017/11/29.
 */

public class fileEditorActivity extends AppCompatActivity {
    private EditText fileName;
    private EditText fileEdit;
    private Button saveBtn;
    private Button loadBtn;
    private Button clearBtn;
    private Button deleteBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_editor);

        InitViewID();
        addListener();
    }

    private void addListener() {
        saveBtn.setOnClickListener(new saveBtnClick());
        loadBtn.setOnClickListener(new loadBtnClick());
        clearBtn.setOnClickListener(new clearBtnClick());
        deleteBtn.setOnClickListener(new deleteBtnClick());

        fileEdit.setOnEditorActionListener(new editFinish());
        fileEdit.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);//设置文本显示为多行输入
        fileEdit.setSingleLine(false);//默认单行显示设为false
        fileEdit.setHorizontallyScrolling(false);//水平不滚动
    }

    private void InitViewID() {
        fileName = (EditText)findViewById(R.id.fileName);
        fileEdit = (EditText)findViewById(R.id.fileEdit);
        saveBtn = (Button)findViewById(R.id.saveBtn);
        loadBtn = (Button)findViewById(R.id.loadBtn);
        clearBtn = (Button)findViewById(R.id.clearfileEditBtn);
        deleteBtn = (Button)findViewById(R.id.deleteBtn);
    }

    //重写fileEdit回车事件
    private class editFinish implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            if (actionId == EditorInfo.IME_ACTION_DONE || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                saveBtn.callOnClick();
                return true;
            }
            return false;
        }
    }

    private class saveBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String FILE_NAME = fileName.getText().toString();
            String fileContent = fileEdit.getText().toString();

            if (writeFile(FILE_NAME,fileContent)) {
                Toast.makeText(getApplicationContext(),"Save successfully",Toast.LENGTH_SHORT).show();
            }
//            try(FileOutputStream fileOutputStream = openFileOutput(FILE_NAME,MODE_PRIVATE)){
//                fileOutputStream.write(fileContent.getBytes());
//                Toast.makeText(getApplicationContext(),"Save successfully",Toast.LENGTH_SHORT).show();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    private class loadBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String FILE_NAME = fileName.getText().toString();
            String strContent = readFile(FILE_NAME);
            fileEdit.setText(strContent);

//            int read;
//            StringBuffer strContent = new StringBuffer("");
//            try(FileInputStream fileInputStream = openFileInput(FILE_NAME)) {
//                while ((read = fileInputStream.read()) != -1){ //转换为字符串
//                    strContent.append((char)read);
//                }
//
//                fileEdit.setText(strContent);
//                Toast.makeText(getApplicationContext(),"Load successfully" ,Toast.LENGTH_SHORT).show();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                Toast.makeText(getApplicationContext(),"Fail to load file" ,Toast.LENGTH_SHORT).show();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }
    }

    private class clearBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            fileName.setText("");
            fileEdit.setText("");
        }
    }

    private class deleteBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String FILE_NAME = fileName.getText().toString();
            getApplicationContext().deleteFile(FILE_NAME);
            Toast.makeText(getApplicationContext(),"Delete successfully" ,Toast.LENGTH_SHORT).show();

        }
    }


    //UTF-8读写文件，解决中文乱码
    public String readFile(String filePathAndName) {
        String fileContent = null;
        try {
            FileInputStream fileInputStream = openFileInput(filePathAndName);
            fileContent = "";
            InputStreamReader read = new InputStreamReader(fileInputStream , "UTF-8");
            BufferedReader reader = new BufferedReader(read);
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent += line;
            }
            read.close();
            Toast.makeText(getApplicationContext(),"Load successfully" ,Toast.LENGTH_SHORT).show();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Fail to load file" ,Toast.LENGTH_SHORT).show();
            return null;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fileContent;
    }

    public boolean writeFile(String filePathAndName, String fileContent) {
        try {
            FileOutputStream fileOutputStream = openFileOutput(filePathAndName,MODE_PRIVATE);
            OutputStreamWriter write = new OutputStreamWriter(fileOutputStream, "UTF-8");
            BufferedWriter writer = new BufferedWriter(write);
            writer.write(fileContent);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}