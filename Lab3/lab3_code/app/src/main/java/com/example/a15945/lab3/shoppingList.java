package com.example.a15945.lab3;

import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;


public class shoppingList extends AppCompatActivity {

    private RecyclerView mRecyclerView = null;
    private commonAdapter mAdapter = null;
    private String[] myDatas = null;
    private String[] myDatas1 = null;
    private String[] myDatas2 = null;
    private int[] data_img_id;
    private FloatingActionButton FAB;
    private static int rmBug = 0;
    private static int[] rmBugArr;

    public static final String STATICACTION = "com.example.a15945.lab3.staticReceiver";
    public static final String DYNAMICACTION = "com.example.a15945.lab3.dynamicReceiver";
    public static List<int[]> msg = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        initData();
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new commonAdapter();

        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(mAdapter);
        animationAdapter.setDuration(1000);
        mRecyclerView.setAdapter(animationAdapter);
        mRecyclerView.setItemAnimator(new OvershootInLeftAnimator());

        rmBugArr = new int[10];
        mAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                for(int i = 0;i<rmBug;i++){
                    if(rmBugArr[i] <= position){
                        position++;
                    }
                }

                Intent intent = new Intent();
                intent.putExtra("position",position);
                intent.setClass(shoppingList.this,goods.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新将要跳到的界面
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity
                startActivity(intent);
                //Toast.makeText(getApplication(),"s",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(int position) {
                mAdapter.removeItem(position);
                String toast = "删除第"+position+"个商品";
                rmBugArr[rmBug] = position;
                rmBug++;
                Toast.makeText(getApplication(),toast,Toast.LENGTH_SHORT).show();
            }
        });

        FAB = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        FABListener fabListener = new FABListener();
        FAB.setOnClickListener(fabListener);

        //发送静态广播
        Intent intent = new Intent();
        intent.setAction(STATICACTION);

            Random random = new Random();
            int randomNumber = random.nextInt(10);
            myDatas2 = getResources().getStringArray(R.array.goods_price_array);
            data_img_id = new int[]{R.mipmap.enchatedforest,R.mipmap.arla,R.mipmap.devondale,R.mipmap.kindle,R.mipmap.waitrose,R.mipmap.mcvitie,R.mipmap.ferrero,R.mipmap.maltesers,R.mipmap.lindt,R.mipmap.borggreve};

        Bundle bundle = new Bundle();
        bundle.putString("name", myDatas1[randomNumber]);
        bundle.putString("price", myDatas2[randomNumber]);
        bundle.putInt("imgID",data_img_id[randomNumber]);
        intent.putExtras(bundle);
        sendBroadcast(intent);

        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMain(Event0 event){
        int[] number = new int[]{0};
        number[0] = event.getMessage();
        msg.add(number);
      //  Toast.makeText(getApplication(),""+number[0],Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册
        EventBus.getDefault().unregister(this);
    }

    class FABListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(shoppingList.this,shoppingcartList.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新将要跳到的界面
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//它可以关掉所要到的界面中间的activity
                startActivity(intent);

        }
    }

    protected void initData() {
        myDatas = getResources().getStringArray(R.array.item_letter_array);
        myDatas1 = getResources().getStringArray(R.array.item_name_array);
    }

    public String[] rmData(String[] strA ,int pos){
        int len = strA.length;

        for(int i = pos;i<len-1;i++){
            strA[i] = strA[i+1];
        }
        strA[len-1] = null;
        return strA;
    }

    public class commonAdapter extends RecyclerView.Adapter<commonAdapter.MyViewHolder> {
        private OnItemClickListener itemClickListener;

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    shoppingList.this).inflate(R.layout.item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position)
        {
            holder.tv.setText(myDatas[position]);
            holder.tv1.setText(myDatas1[position]);
            if(myDatas[position] == null){
                holder.imgV.setVisibility(View.INVISIBLE);
            }
            if(itemClickListener!=null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onClick(holder.getAdapterPosition());
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                       itemClickListener.onLongClick(holder.getAdapterPosition());
                        return false;
                    }
                });
            }
        }

        @Override
        public int getItemCount()
        {
            return myDatas.length;
        }

        class MyViewHolder extends ViewHolder
        {
            TextView tv;
            TextView tv1;
            ImageView imgV;

            public MyViewHolder(View view)
            {
                super(view);
                tv = (TextView) view.findViewById(R.id.id_letter);
                tv1 = (TextView) view.findViewById(R.id.id_text);
                imgV = (ImageView) view.findViewById(R.id.imageView);
            }
        }
//        public interface OnItemClickListener{
//            void onClick(int position);
//            void onLongClick(int position);
//        }
        //设置点击事件的方法
        public void setItemClickListener(OnItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        public void removeItem(int position){
            rmData(myDatas,position);
            rmData(myDatas1,position);
            notifyItemRemoved(position);//刷新被删除的地方
            notifyItemRangeChanged(position, getItemCount()); //刷新被删除数据，以及其后面的数据


        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }

}


