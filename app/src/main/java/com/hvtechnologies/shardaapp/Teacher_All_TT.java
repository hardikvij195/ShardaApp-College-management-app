package com.hvtechnologies.shardaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Teacher_All_TT extends AppCompatActivity {

    ListView TimeTable ;

    Spinner Days ;

    DatabaseReference InfoRef , TTRef , DayRef;

    List<TeacherTimeTableClass> mTeachTT = new ArrayList<>() ;
    Teacher_All_TT_ListView_Adap adapter;

    ArrayAdapter<String> adap2;
    List<String> Dys = new ArrayList<>() ;

    String   TeacherId = "" ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__all__tt);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Days = (Spinner)findViewById(R.id.spinnerTT);
        TimeTable = (ListView)findViewById(R.id.ListViewTT);

        adapter = new Teacher_All_TT_ListView_Adap( getApplicationContext() , mTeachTT);
        adap2 = new ArrayAdapter<String>(this , R.layout.spinner_drop_items , Dys);
        adap2.setDropDownViewResource(R.layout.spinner_item);
        TimeTable.setAdapter(adapter);
        Days.setAdapter(adap2);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        InfoRef = FirebaseDatabase.getInstance().getReference("Users/TeacherLogin/" + uid + "/") ;
        InfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    TeacherId = dataSnapshot.child("TeacherId").getValue().toString();

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

                mTeachTT.clear();
                adapter.notifyDataSetChanged();

                TTRef = FirebaseDatabase.getInstance().getReference("TimeTable/TeacherWise/" + TeacherId + "/" + Dy ) ;
                TTRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {



                        if(dataSnapshot.exists()){

                            mTeachTT.clear();
                            adapter.notifyDataSetChanged();

                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                                if(dataSnapshot1.exists()){

                                    String g = ""  , p = "" ;

                                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.child("Groups").getChildren()){

                                        if(dataSnapshot2.exists()){

                                            String Groups = dataSnapshot2.child("GroupName").getValue().toString();
                                            g = g + Groups +" ";


                                        }
                                    }

                                    for(DataSnapshot dataSnapshot3 : dataSnapshot1.child("Periods").getChildren()){
                                        if(dataSnapshot3.exists()){
                                            String Period = dataSnapshot3.child("Name").getValue().toString();
                                            p = p + Period + " ";
                                            //p.concat(Period + ",");

                                        }
                                    }

                                    if(!g.isEmpty() && !p.isEmpty()){


                                        String Sch = dataSnapshot1.child("SchoolName").getValue().toString();
                                        String Dept = dataSnapshot1.child("DeptName").getValue().toString();
                                        String Year = dataSnapshot1.child("YearName").getValue().toString();
                                        String Class = dataSnapshot1.child("ClassName").getValue().toString();
                                        String Loc = dataSnapshot1.child("Loc").getValue().toString();
                                        String Sub = dataSnapshot1.child("Subject").getValue().toString();
                                        String SubCode = dataSnapshot1.child("SubjectCode").getValue().toString();
                                        String Time = dataSnapshot1.child("Time").getValue().toString();



                                        mTeachTT.add(new TeacherTimeTableClass( Sch , Dept , Year , Class + " / " + g  , Sub , SubCode , Loc , Time + " / " + p));
                                        adapter.notifyDataSetChanged();

                                    }
                                }
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
