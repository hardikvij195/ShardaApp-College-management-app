package com.hvtechnologies.shardaapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Student_Assignment_Activity1 extends AppCompatActivity {


    ListView Subs;

    DatabaseReference InfoRef ;

    List<StudentTimeTableClass> mStudTT = new ArrayList<>() ;

    Student_Subs_Adapter adapter;

    //DeanCheckAttListAdapter adapter1;
    //List<DeanCheckAttClass> mCheckTT = new ArrayList<>() ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__assignment_1);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Subs = (ListView)findViewById(R.id.lstAss);

        //adapter1 = new DeanCheckAttListAdapter(this , mCheckTT );
        adapter = new Student_Subs_Adapter(this , mStudTT );
        Subs.setAdapter(adapter);

        SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
        String SchId = sharedPrefs.getString("SchId", "");
        String DeptId = sharedPrefs.getString("DptId", "");

        mStudTT.clear();

        /*

        mCheckTT.add(new DeanCheckAttClass( "CS" , "1st Year" , "CSE111" , "DAA" , "Teacher1" , "TEA1" ,"CSE-A" , "G1 , G2" , "12:00 - 1:00" , "210 - Block 1" , "Yes" ));
        mCheckTT.add(new DeanCheckAttClass( "CS" , "1st Year" , "CSE111" , "DAA" , "Teacher1" , "TEA1" ,"CSE-A" , "G1 , G2" , "12:00 - 1:00" , "210 - Block 1" , "Yes" ));
        mCheckTT.add(new DeanCheckAttClass( "CS" , "1st Year" , "CSE111" , "DAA" , "Teacher1" , "TEA1" ,"CSE-A" , "G1 , G2" , "12:00 - 1:00" , "210 - Block 1" , "Yes" ));
        mCheckTT.add(new DeanCheckAttClass( "CS" , "1st Year" , "CSE111" , "DAA" , "Teacher1" , "TEA1" ,"CSE-A" , "G1 , G2" , "12:00 - 1:00" , "210 - Block 1" , "No" ));


        */

        InfoRef = FirebaseDatabase.getInstance().getReference("Structure/" + SchId  + "/" + DeptId + "/" ) ;
        InfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    mStudTT.clear();

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        for(DataSnapshot dataSnapshot2 : dataSnapshot1.child("Subjects").getChildren()){

                            String SubjectName = dataSnapshot2.child("Name").getValue().toString();
                            String SubCode = dataSnapshot2.child("Code").getValue().toString();
                            mStudTT.add(new StudentTimeTableClass( SubCode , SubjectName , " " , " " , " "));
                            adapter.notifyDataSetChanged();

                        }

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

                Intent mainIntent = new Intent(Student_Assignment_Activity1.this, Student_Assignment_Particular1.class);
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
