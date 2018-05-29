package com.example.dong.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    private List<String> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView mLv = findViewById(R.id.test);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mLv.setLayoutManager(linearLayoutManager);
        initDatas();
        final CommonRecyclerAdapter commonRecyclerAdapter = new CommonRecyclerAdapter(this, mData);

        commonRecyclerAdapter.setOnItemEditListener(new CommonRecyclerAdapter.OnItemEditListener() {
            @Override
            public void onchange(int position) {

                Toast.makeText(MainActivity.this,mData.get(position),Toast.LENGTH_LONG).show();

            }
        });
        mLv.setAdapter(commonRecyclerAdapter);
    }

    private void initDatas() {
        mData = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mData.add("" + i);
        }
    }
}
