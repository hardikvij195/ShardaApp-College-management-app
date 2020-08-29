package com.hvtechnologies.shardaapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Student_Assignment_Particular1 extends AppCompatActivity {


    TextView txt;
    ListView lstV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__assignment__particular1);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt = (TextView)findViewById(R.id.SubInfo);
        lstV = (ListView)findViewById(R.id.listMrks);

        Intent intent = getIntent();
        String SubId = intent.getExtras().getString("SubId");
        String SubName = intent.getExtras().getString("SubName");
        String SubCode = intent.getExtras().getString("SubCode");

        txt.setText(SubName + "\n" + SubCode);






    }
}
