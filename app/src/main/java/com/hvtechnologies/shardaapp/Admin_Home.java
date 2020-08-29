package com.hvtechnologies.shardaapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Admin_Home extends Fragment {


    TextView InfoDetails , TodTT;
    TextView Welcome;
    RecyclerView TimeTable ;

    DatabaseReference InfoRef ;
    FirebaseAuth mAuth ;

    String Name = "" ;

    List<StudentTimeTableClass> mStudTT ;
    StudentTimeTableListAdapter adapter;




    public Admin_Home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStudTT = new ArrayList<>();
        mStudTT.add(new StudentTimeTableClass("CSE111"  ,"JAVA" , "Teacher 1" , "201-Block 1" , "11:00 - 12:00"));
        mStudTT.add(new StudentTimeTableClass("CSE222"  ,"C++" , "Teacher 2" , "202-Block 1" , "11:00 - 12:00"));
        mStudTT.add(new StudentTimeTableClass("CSE333"  ,"Automata" , "Teacher 3" , "203-Block 1" , "11:00 - 12:00"));
        mStudTT.add(new StudentTimeTableClass("CSE444"  ,"Os" , "Teacher 4" , "204-Block 1" , "11:00 - 12:00"));



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v =  inflater.inflate(R.layout.fragment_admin__home, container, false);
        InfoDetails = (TextView)v.findViewById(R.id.TextViewAdminHomeDetails);
        TodTT = (TextView)v.findViewById(R.id.TodayTT);

        TimeTable = (RecyclerView)v.findViewById(R.id.recyclerView);


        Welcome = (TextView)v.findViewById(R.id.TextViewWelcome);
        Welcome.setText("Welcome,");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        InfoRef = FirebaseDatabase.getInstance().getReference("Users/Admin/" + uid + "/") ;
        InfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    Name = dataSnapshot.child("Name").getValue().toString();
                    InfoDetails.setText("Name : " + Name);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        adapter = new StudentTimeTableListAdapter(inflater ,getContext() , mStudTT);
        TimeTable.setLayoutManager(new LinearLayoutManager(getActivity()));
        TimeTable.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                TodTT.setText("Sunday");
                InfoDetails.setText("Welcome,\n\nDay : Sunday" );

                // Current day is Sunday

                break;
            case Calendar.MONDAY:

                TodTT.setText("Monday's Time Table");
                InfoDetails.setText("Welcome,\n\nDay : Monday" );

                // Current day is Monday
                break;
            case Calendar.TUESDAY:

                TodTT.setText("Tuesday's Time Table");
                InfoDetails.setText("Welcome,\n\nDay : Tuesday" );

                // etc.
                break;
            case Calendar.WEDNESDAY:

                TodTT.setText("Wednesday's Time Table");
                InfoDetails.setText("Welcome,\n\nDay : Wednesday" );

                // etc.
                break;
            case Calendar.THURSDAY:

                TodTT.setText("Thursday's Time Table");
                InfoDetails.setText("Welcome,\n\nDay : Thursday" );

                // etc.
                break;
            case Calendar.FRIDAY:

                TodTT.setText("Friday's Time Table");
                InfoDetails.setText("Welcome,\n\nDay : Friday" );
                // etc.
                break;
            case Calendar.SATURDAY:

                TodTT.setText("Saturday's Time Table");
                InfoDetails.setText("Name : Hardik\nEmail : 2017008907.hardik@ug.sharda.ac.in\nSystem Id : 2017008907\nSchool : School Of Engineering And Technology\nYear : 1st Year\nClass : Sec - A\nGroup - G1" );
                // etc.
                //InfoDetails.setText("Name : " + Name);
                break;

        }




        return v ;



        //return inflater.inflate(R.layout.fragment_admin__home, container, false);



    }




}
