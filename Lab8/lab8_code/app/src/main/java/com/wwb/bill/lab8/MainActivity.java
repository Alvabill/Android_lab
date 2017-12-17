package com.wwb.bill.lab8;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import static com.wwb.bill.lab8.myDB.TABLE_NAME;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private Button button;
    final myDB helper = new myDB(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        addListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        updatelistview();
    }

    private void addListener() {
        button.setOnClickListener(new clickBtn());
        listView.setOnItemClickListener(new clickItem());
        listView.setOnItemLongClickListener(new longClickItem());
    }

    private void initUI() {
        listView = (ListView) findViewById(R.id.listView);
        button = (Button) findViewById(R.id.button);
    }

    public void updatelistview() {
        SQLiteDatabase db = helper.getWritableDatabase();
        final Cursor cr = db.query(TABLE_NAME, null, null, null, null,
                null, null);
        String[] ColumnNames = cr.getColumnNames();
        // ColumnNames为数据库的表的列名，getColumnNames()为得到指定table的所有列名

        ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.item,
                cr, ColumnNames, new int[]{0, R.id.name, R.id.birthday, R.id.gifts});
        // layout为listView的布局文件，包括三个TextView，用来显示三个列名所对应的值
        // ColumnNames为数据库的表的列名
        // 最后一个参数是int[]类型的，为view类型的id，用来显示ColumnNames列名所对应的值。view的类型为TextView
        listView.setAdapter(adapter);
    }


    private class clickBtn implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, additem.class);
            startActivity(intent);
        }
    }


    private class clickItem implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            View view_in = layoutInflater.inflate(R.layout.dialoglayout, (ViewGroup) findViewById(R.id.dialog));
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            final AlertDialog dialog = alertDialog.setView(view_in).show();

            final TextView dialogName = (TextView) view_in.findViewById(R.id.dialogName);
            final EditText dialogBirthday = (EditText) view_in.findViewById(R.id.dialogBirthday);
            final EditText dialogGifts = (EditText) view_in.findViewById(R.id.dialogGifts);
            final Button quitBtn = (Button) view_in.findViewById(R.id.dialogBtnQuit);
            final Button successBtn = (Button) view_in.findViewById(R.id.dialogBtnSuccess);
            final TextView phoneText = (TextView) view_in.findViewById(R.id.phoneText);
            final String[] person = helper.getPoint(position);
            System.out.println(person);
            dialogName.setText(person[0]);
            dialogBirthday.setText(person[1]);
            dialogGifts.setText(person[2]);
            quitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            successBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    helper.update(person[0], dialogBirthday.getText().toString(), dialogGifts.getText().toString());
                    updatelistview();
                    dialog.dismiss();
                }
            });

            // getContentResolver方法读取联系人列表
            Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
            cursor.getCount();
            while(cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                System.out.println(name);
                if (name.equals(dialogName.getText().toString())){
                    System.out.println(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)));
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    int isHas = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER )));
                    System.out.println("isHas:"+isHas);
                    if (isHas == 1){
//                        Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + position, null, null);
                        //只查询手机电话
                        Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id +" and "+
                                        ContactsContract.CommonDataKinds.Phone.TYPE+"="+ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE, null, null);
                        String phoneNumber = "";
                        while(phone.moveToNext()) {
                            phoneNumber += phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                        phoneText.setText("电话： "+phoneNumber);
                        phoneText.setVisibility(View.VISIBLE);

                        //点击跳转到拨打电话界面
                        final String phoneCall = phoneNumber;
                        phoneText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                call(phoneCall);
                            }
                        });
                    }
                    break;
                }
            }
            cursor.close();

//            //判断某条联系人信息中是否有电话号码
//            int isHas = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER )));
//            Toast.makeText(getApplication(),"isHas:"+isHas,Toast.LENGTH_SHORT).show();
//            //取出该条离联系人的信息中的电话号码
//            Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + position, null, null);
//            String Number = "";
//            while(phone.moveToNext()) {
//                Number += phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))+ " ";
//            }
//            phoneText.setText(Number);
//            phoneText.setVisibility(View.VISIBLE);
        }
    }

    private class longClickItem implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setMessage("是否删除？")
                    .setNegativeButton("否", null)
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String name = helper.getPoint(position)[0];
                            helper.delete(name);
                            updatelistview();
                            //Toast.makeText(getApplication(),name,Toast.LENGTH_SHORT).show();
                        }
                    }).show();
            return false;
        }
    }

    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
