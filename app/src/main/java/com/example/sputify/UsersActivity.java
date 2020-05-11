package com.example.sputify;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UsersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        Bundle extras = getIntent().getExtras();
        if (extras == null)
            System.out.println("Intent Data Emptya " + " Intent empty");
        else
            System.out.println(extras.getString("songs"));
        MyListData[] myListData = new MyListData[] {
                new MyListData("Say so", "anoob09" ,android.R.drawable.ic_dialog_email),
                new MyListData("Not ok", "anoob08" ,android.R.drawable.ic_dialog_email),
                new MyListData("One thing right", "anoob07" ,android.R.drawable.ic_dialog_email),
        };
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        MyListAdapter adapter = new MyListAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}

