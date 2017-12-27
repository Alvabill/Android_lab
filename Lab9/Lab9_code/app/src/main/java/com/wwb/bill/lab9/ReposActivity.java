package com.wwb.bill.lab9;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.wwb.bill.lab9.factory.ServiceFactory;
import com.wwb.bill.lab9.model.Repos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 15945 on 2017/12/27.
 */

public class ReposActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private ArrayList<Map<String,String>> mInfos= new ArrayList<Map<String,String>>();
    private ListView listView;
    private ServiceFactory service;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);
        listView = (ListView)findViewById(R.id.listView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
        final SimpleAdapter simpleAdapter = new SimpleAdapter(ReposActivity.this, mInfos, R.layout.repos_item,
                new String[]{"name","description","language"},
                new int[]{R.id.reposName,R.id.reposDes,R.id.reposLang});
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        service = new ServiceFactory();
        service.getRepos(name)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Repos>>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("完成传输");
                        removeWait();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(ReposActivity.this, e.hashCode()+ "请确认你搜索的用户存在", Toast.LENGTH_SHORT).show();
                        Log.e("Github-Demo",e.getMessage());
                        removeWait();
                    }

                    @Override
                    public void onNext(List<Repos> repos) {
                        for (Repos temp:repos) addRepos(temp);
                        simpleAdapter.notifyDataSetChanged();
                    }
                });
        listView.setAdapter(simpleAdapter);
    }
    private void removeWait() {
        progressBar.setVisibility(View.GONE);
    }
    private void addRepos(Repos temp) {
        Map<String,String> item=new HashMap<>();
        item.put("name", temp.getName());
        item.put("description", temp.getDescription());
        item.put("language", temp.getLanguage());
        mInfos.add(item);
    }
}
