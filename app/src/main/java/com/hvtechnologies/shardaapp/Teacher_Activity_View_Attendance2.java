package com.hvtechnologies.shardaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Teacher_Activity_View_Attendance2 extends AppCompatActivity {


    ArrayList<Teacher_Class_Names_Spinner> mClassList ;
    Teacher_Class_Names_Spinner_Adapter mAdap ;

    Spinner spinn1 , spinn2 ;

    Button SubmitBtn ;
    ListView listView ;


    seeAttListAdap adapter;

    DatabaseReference ClassRef , ClassRef2 ;

    private FirebaseAuth mAuth;

    private ArrayList<Teacher_GrpNames_Spinner> GroupNames1 = new ArrayList<>() ;
    private Teacher_Grp_Names_Spinner_Adapter adapterSpinnerGrpName ;


    private ArrayList<ViewAttGroupsTeacherClass> mAttList = new ArrayList<>() ;

    //List<TeacherTimeTableClass> mTeachTT  = new ArrayList<>() ;
    //TeacherTimeTableListAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher___view__attendance2);



        //Utils.getDatabase();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mClassList = new ArrayList<>();
        spinn1 = (Spinner)findViewById(R.id.spinner1);
        spinn2 = (Spinner)findViewById(R.id.spinner2);
        SubmitBtn = (Button)findViewById(R.id.button2);
        mAuth = FirebaseAuth.getInstance();

        String uid = mAuth.getCurrentUser().getUid();

        mAdap = new Teacher_Class_Names_Spinner_Adapter(this , mClassList);

        spinn1.setAdapter(mAdap);

        adapterSpinnerGrpName = new Teacher_Grp_Names_Spinner_Adapter(getApplicationContext(), GroupNames1);
        spinn2.setAdapter(adapterSpinnerGrpName);
        listView = (ListView) findViewById(R.id.AttListView);


        adapter = new seeAttListAdap(getApplicationContext() , mAttList);
        listView.setAdapter(adapter);



        ClassRef2 = FirebaseDatabase.getInstance().getReference("Users/TeacherLogin/"+ uid + "/Subjects/" );

        ClassRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mClassList.clear();
                mAdap.notifyDataSetChanged();

                if(dataSnapshot.exists()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        String SchName = dataSnapshot1.child("SchoolName").getValue().toString();
                        String SchId = dataSnapshot1.child("SchoolId").getValue().toString();

                        String DeptName = dataSnapshot1.child("DeptName").getValue().toString();
                        String DeptId = dataSnapshot1.child("DeptId").getValue().toString();

                        String YearName = dataSnapshot1.child("YearName").getValue().toString();
                        String YrId = dataSnapshot1.child("YearId").getValue().toString();

                        String ClassName = dataSnapshot1.child("ClassName").getValue().toString();
                        String ClassId = dataSnapshot1.child("ClassId").getValue().toString();


                        mClassList.add(new Teacher_Class_Names_Spinner(SchName , SchId , DeptName , DeptId , YearName , YrId , ClassName , ClassId , "" , "" , ""));
                        mAdap.notifyDataSetChanged();


                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        spinn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(!mClassList.isEmpty()){

                    Teacher_Class_Names_Spinner clickedItem = (Teacher_Class_Names_Spinner)parent.getItemAtPosition(position);
                    final String Id1 = clickedItem.getSchId();
                    final String Id2 = clickedItem.getDeptId();
                    final String Id3 = clickedItem.getYearId();
                    final String Id4 = clickedItem.getClassId();

                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id1 + "/" + Id2 + "/" + Id3 + "/Classes/" + Id4 + "/Groups/");
                    ClassRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){

                                GroupNames1.clear();

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                    String GroupId = dataSnapshot1.getKey();
                                    String GroupNm = dataSnapshot1.child("Name").getValue().toString();
                                    GroupNames1.add(new Teacher_GrpNames_Spinner(GroupNm , GroupId));
                                    adapterSpinnerGrpName.notifyDataSetChanged();

                                }

                            }

                            if(!mClassList.isEmpty() && !GroupNames1.isEmpty()){


                                String Id5 = GroupNames1.get(0).getGrpId();
                                ClassRef = FirebaseDatabase.getInstance().getReference("Attendance/GroupWise/" + Id1 + "/" + Id2 + "/" + Id3 + "/" + Id4 + "/" + Id5);
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        mAttList.clear();
                                        adapter.notifyDataSetChanged();

                                        if(dataSnapshot.exists()){

                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){


                                                String Date = dataSnapshot1.child("Date").getValue().toString();
                                                String SubName = dataSnapshot1.child("SubName").getValue().toString();
                                                String SubCode = dataSnapshot1.child("SubCode").getValue().toString();
                                                String ByName = dataSnapshot1.child("ByName").getValue().toString();
                                                String ByCode = dataSnapshot1.child("ByCode").getValue().toString();
                                               // String Time = dataSnapshot1.child("Time").getValue().toString();


                                                mAttList.add(new ViewAttGroupsTeacherClass( Date , SubName , SubCode , ByName , ByCode  , ""));
                                                adapter.notifyDataSetChanged();



                                            }

                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinn2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if(!GroupNames1.isEmpty()){

                    String Id5 = GroupNames1.get(position).getGrpId();
                    Teacher_Class_Names_Spinner clickedItem = (Teacher_Class_Names_Spinner)spinn1.getItemAtPosition(position);
                    String Id1 = clickedItem.getSchId();
                    String Id2 = clickedItem.getDeptId();
                    String Id3 = clickedItem.getYearId();
                    String Id4 = clickedItem.getClassId();

                    ClassRef = FirebaseDatabase.getInstance().getReference("Attendance/GroupWise/" + Id1 + "/" + Id2 + "/" + Id3 + "/" + Id4 + "/" + Id5);
                    ClassRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            mAttList.clear();
                            adapter.notifyDataSetChanged();

                            if(dataSnapshot.exists()){

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){


                                    String Date = dataSnapshot1.child("Date").getValue().toString();
                                    String SubName = dataSnapshot1.child("SubName").getValue().toString();
                                    String SubCode = dataSnapshot1.child("SubCode").getValue().toString();
                                    String ByName = dataSnapshot1.child("ByName").getValue().toString();
                                    String ByCode = dataSnapshot1.child("ByCode").getValue().toString();
                                    //String Time = dataSnapshot1.child("Time").getValue().toString();


                                    mAttList.add(new ViewAttGroupsTeacherClass( Date , SubName , SubCode , ByName , ByCode  , ""));
                                    adapter.notifyDataSetChanged();

                                }


                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






    }
}
