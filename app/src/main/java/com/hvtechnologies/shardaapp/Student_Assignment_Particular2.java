package com.hvtechnologies.shardaapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Student_Assignment_Particular2 extends AppCompatActivity {

    TextView txt;
    ListView lstV;
    DatabaseReference InfoRef ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__assignment__particular2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt = (TextView)findViewById(R.id.SubInfo);
        lstV = (ListView)findViewById(R.id.listMrks);

        Intent intent = getIntent();
        String SubId = intent.getExtras().getString("SubId");
        String SubName = intent.getExtras().getString("SubName");
        String SubCode = intent.getExtras().getString("SubCode");

        SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
        final String Id1 = sharedPrefs.getString("SchId", "");
        final String Id2 = sharedPrefs.getString("DptId", "");
        final String Id3 = sharedPrefs.getString("YrId", "");

        txt.setText(SubName + "\n" + SubCode);

        InfoRef = FirebaseDatabase.getInstance().getReference("Assignments/" + Id1 + "/" + Id2 + "/" + Id3 + "/" + SubId + "/" ) ;
        InfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    //mStudTT.clear();

                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        String day = dataSnapshot1.getKey();

                        String Date = dataSnapshot1.child("Upload Date").getValue().toString();
                        String Topic = dataSnapshot1.child("Topic").getValue().toString();
                        String Desc = dataSnapshot1.child("Desc").getValue().toString();
                        String DownloadUrl = dataSnapshot1.child("Download Url").getValue().toString();
                      // mStudTT.add(new AssignmentClass(  SubjectName, SubCode , Date , Topic, Desc , DownloadUrl));
                        //adapter.notifyDataSetChanged();

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });









    }
}
