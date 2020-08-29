package com.hvtechnologies.shardaapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Student_Particular_Sub_Marksheet_Activity extends AppCompatActivity {

    TextView txt;
    ListView lstV;
    DatabaseReference InfoRef ;

    List<StudentTimeTableClass> mStudTT = new ArrayList<>() ;
    Student_All_TT_ListView_Adap adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__particular__sub__marksheet_);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt = (TextView)findViewById(R.id.SubInfo);
        lstV = (ListView)findViewById(R.id.listMrks);
        adapter = new Student_All_TT_ListView_Adap( getApplicationContext() , mStudTT);

        lstV.setAdapter(adapter);



        Intent intent = getIntent();
        final String SubId = intent.getExtras().getString("SubId");
        final String SubName = intent.getExtras().getString("SubName");
        final String SubCode = intent.getExtras().getString("SubCode");

        txt.setText(SubName + "\n" + SubCode);


        SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
        String Id1 = sharedPrefs.getString("SchId", "");
        String Id2 = sharedPrefs.getString("DptId", "");
        String Id3 = sharedPrefs.getString("YrId", "");
        String Id4 = sharedPrefs.getString("ClsId", "");
        String Id5 = sharedPrefs.getString("GrpId", "");
        String StdId = sharedPrefs.getString("StdId", "");


        InfoRef = FirebaseDatabase.getInstance().getReference("Marksheets/StudentWise/" + Id1 + "/" + Id2 + "/"+ Id3 + "/"+ Id4 + "/"+ Id5 + "/Att/" + StdId + "/" ) ;

        InfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        String SubCode2 = dataSnapshot1.child("SubCode").getValue().toString();

                        if(SubCode2.equals(SubCode)){


                            String MarksheetName = dataSnapshot1.child("Name").getValue().toString();
                            String ByName = dataSnapshot1.child("ByName").getValue().toString();
                            String ByCode = dataSnapshot1.child("ByCode").getValue().toString();
                            String Date = dataSnapshot1.child("Date").getValue().toString();
                            String Marks = dataSnapshot1.child("Marks").getValue().toString();
                            String Total = dataSnapshot1.child("Total").getValue().toString();


                            mStudTT.add(new StudentTimeTableClass( MarksheetName , Marks + "/" + Total  , ByName + " - " + ByCode , Date ,"" ));
                            adapter.notifyDataSetChanged();


                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });











    }

}
