package com.sabirov.warehouse.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sabirov.warehouse.R;
import com.sabirov.warehouse.adapters.MenuRVA;
import com.sabirov.warehouse.models.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private User user;
    private TextView uid_tv;
    private RecyclerView menu_rv;
    private MenuRVA adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user=getIntent().getExtras().getParcelable("user");

        findIDs();
        setInfo();
        setActions();
    }

    private void findIDs() {
        uid_tv=findViewById(R.id.uid_tv);
        menu_rv=findViewById(R.id.menu_rv);
    }

    private void setInfo(){
        String text=user.getUserId()+"\t"+"—"+"\t"+user.getFirstName();
        uid_tv.setText(text);

        ArrayList<String> items=new ArrayList<>();
        items.add("Orders");

        adapter=new MenuRVA(MainActivity.this, items);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false);
        menu_rv.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(menu_rv.getContext(),
                        linearLayoutManager.getOrientation());
        menu_rv.addItemDecoration(dividerItemDecoration);
        menu_rv.setAdapter(adapter);
    }

    private void setActions() {
        adapter.setOnItemClickListener(position -> {
            switch (position){
                case 0:
                    Intent intent=new Intent(MainActivity.this, OrdersActivity.class);
                    startActivity(intent);
                    break;
            }
        });
    }
}
