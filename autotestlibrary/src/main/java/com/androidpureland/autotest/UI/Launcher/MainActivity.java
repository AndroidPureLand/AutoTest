package com.androidpureland.autotest.UI.Launcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidpureland.autotest.HttpData.Bean.BaseUrlEntity;
import com.androidpureland.autotest.HttpData.HttpData;
import com.androidpureland.autotest.R;
import com.androidpureland.autotest.UI.Launcher.Apater.APIButtonAdapter;
import com.androidpureland.autotest.UI.MainTest.ApiListActivity;


public class MainActivity extends AppCompatActivity {


    private ImageView imageView;
    private TextView textview;
    private RecyclerView recyclerView;

    private APIButtonAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        textview = (TextView) findViewById(R.id.textview);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new APIButtonAdapter(R.layout.item_button_list_layout,null);
        recyclerView.setAdapter(adapter);
        adapter.setCallBack(new APIButtonAdapter.OnAPIButtonAdapterOperationCallBack() {
            @Override
            public void onUrlClick(BaseUrlEntity baseUrlEntity, int position) {
                HttpData.getInstance().initURL(position);
                startActivity(new Intent(MainActivity.this, ApiListActivity.class));
            }
        });
        adapter.setNewData(HttpData.getInstance().getBaseUrlEntityList());
    }


}
