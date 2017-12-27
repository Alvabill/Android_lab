package com.wwb.bill.lab9;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wwb.bill.lab9.adapter.CardAdapter;
import com.wwb.bill.lab9.factory.ServiceFactory;
import com.wwb.bill.lab9.model.Github;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerview;
    private LinearLayoutManager linearLayoutManager;
    private List<Github> list;
    private ServiceFactory service;
    private CardAdapter cardAdapter;


    private TextView searchText;
    private Button clearBtn;
    private Button fetchBtn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();//初始化布局
        setListener();//设置监听事件

    }

    private void initView() {
        recyclerview = (RecyclerView)findViewById(R.id.recycleLayout);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();
        cardAdapter = new CardAdapter(this,list);
        recyclerview.setAdapter(cardAdapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        searchText = (TextView)findViewById(R.id.searchText);
        clearBtn = (Button)findViewById(R.id.clearBtn);
        fetchBtn = (Button)findViewById(R.id.fetchBtn);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    private void setListener() {
        fetchBtn.setOnClickListener(new clickFetch());
        clearBtn.setOnClickListener(new clickClear());
        cardAdapter.setOnItemClickListener(new clickItem());
    }

    private class clickFetch implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            progressBar.setVisibility(View.VISIBLE);

            String user = searchText.getText().toString();
            service = new ServiceFactory();
            //Toast.makeText(getApplicationContext(),user,Toast.LENGTH_SHORT).show();
            service.getUser(user)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Github>() {
                        @Override
                        public void onCompleted() {
                            System.out.println("完成传输");
                            removeWait();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(MainActivity.this, e.hashCode()+ "请确认你搜索的用户存在", Toast.LENGTH_SHORT).show();
                            Log.e("Github-Demo",e.getMessage());
                            removeWait();
                        }

                        @Override
                        public void onNext(Github github) {
                            cardAdapter.addData(github);
                            cardAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    private void removeWait() {
        progressBar.setVisibility(View.GONE);
    }

    private class clickClear implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            cardAdapter.clear();
            cardAdapter.notifyDataSetChanged();
        }
    }

    private class clickItem implements CardAdapter.OnItemClickListener {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(MainActivity.this,ReposActivity.class);
            intent.putExtra("name", cardAdapter.getData(position).getLogin());
            startActivity(intent);
        }

        @Override
        public void onItemLongClick(View view, int position) {
            cardAdapter.removeData(position);
            cardAdapter.notifyDataSetChanged();
        }
    }
}
