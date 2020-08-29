package com.hvtechnologies.shardaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Student_All_TT extends AppCompatActivity {


    ListView TimeTable ;

    Spinner Days ;

    DatabaseReference InfoRef , TTRef , DayRef;

    List<StudentTimeTableClass> mStudTT = new ArrayList<>() ;
    Student_All_TT_ListView_Adap adapter;

    ArrayAdapter<String> adap2;
    List<String> Dys = new ArrayList<>() ;


    String   SchoolId = "" ,  DeptId = "" , ClassId = "" , GroupId = "" ,  YearId = "" ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__all__tt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        Days = (Spinner)findViewById(R.id.spinnerTT);
        TimeTable = (ListView)findViewById(R.id.ListViewTT);

        adapter = new Student_All_TT_ListView_Adap( getApplicationContext() , mStudTT);
        adap2 = new ArrayAdapter<String>(this , R.layout.spinner_drop_items , Dys);
        adap2.setDropDownViewResource(R.layout.spinner_item);
        TimeTable.setAdapter(adapter);
        Days.setAdapter(adap2);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        InfoRef = FirebaseDatabase.getInstance().getReference("Users/StudentLogin/" + uid + "/") ;
        InfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    SchoolId = dataSnapshot.child("SchoolId").getValue().toString();
                    DeptId = dataSnapshot.child("DeptId").getValue().toString();
                    YearId = dataSnapshot.child("YearId").getValue().toString();
                    ClassId = dataSnapshot.child("ClassId").getValue().toString();
                    GroupId = dataSnapshot.child("GroupId").getValue().toString();

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DayRef = FirebaseDatabase.getInstance().getReference("Days/") ;
        DayRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    Dys.clear();
                    adap2.notifyDataSetChanged();

                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                        String Day = dataSnapshot1.child("Day").getValue().toString();
                        Dys.add(Day);
                        adap2.notifyDataSetChanged();

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });


        Days.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String Dy = parent.getItemAtPosition(position).toString();

                mStudTT.clear();
                adapter.notifyDataSetChanged();



                TTRef = FirebaseDatabase.getInstance().getReference("TimeTable/ClassWise/" + SchoolId + "/"  + DeptId + "/" + YearId + "/" + ClassId + "/" + GroupId + "/" + Dy ) ;
                TTRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {



                        if(dataSnapshot.exists()){

                            mStudTT.clear();
                            adapter.notifyDataSetChanged();


                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                                String Loc = dataSnapshot1.child("Loc").getValue().toString();
                                String Sub = dataSnapshot1.child("Subject").getValue().toString();
                                String SubCode = dataSnapshot1.child("SubjectCode").getValue().toString();
                                String Teach = dataSnapshot1.child("Teacher").getValue().toString();
                                String Time = dataSnapshot1.child("Time").getValue().toString();
                                mStudTT.add(new StudentTimeTableClass( SubCode , Sub , Teach , Loc , Time));
                                adapter.notifyDataSetChanged();

                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }



}
