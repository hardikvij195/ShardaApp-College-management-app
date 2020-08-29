package com.hvtechnologies.shardaapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Student_Marks_Activity extends AppCompatActivity {

    ListView Subs;

    DatabaseReference InfoRef ;

    List<StudentTimeTableClass> mStudTT = new ArrayList<>() ;

    Student_Subs_Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__marks_);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Subs = (ListView)findViewById(R.id.lstMrks);
        adapter = new Student_Subs_Adapter(this , mStudTT );
        Subs.setAdapter(adapter);

        SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
        String Id = sharedPrefs.getString("USERID", "");

        InfoRef = FirebaseDatabase.getInstance().getReference("Users/StudentLogin/" + Id + "/Subjects/" ) ;
        InfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        String key = dataSnapshot1.getKey();
                        String SubjectName = dataSnapshot1.child("SubName").getValue().toString();
                        String SubCode = dataSnapshot1.child("SubCode").getValue().toString();
                        mStudTT.add(new StudentTimeTableClass( SubCode , SubjectName , key , "" , ""));
                        adapter.notifyDataSetChanged();

                    }

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Subs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String subId = mStudTT.get(position).getTeacherName();
                String subName = mStudTT.get(position).getSubjectNAme();
                String subCode = mStudTT.get(position).getSubjectCode();


                Intent mainIntent = new Intent(Student_Marks_Activity.this, Student_Particular_Sub_Marksheet_Activity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                mainIntent.putExtra("SubId" , subId);
                mainIntent.putExtra("SubName" , subName);
                mainIntent.putExtra("SubCode" , subCode);
                startActivity(mainIntent);
                overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);




            }
        });





    }
}
