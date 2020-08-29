package com.hvtechnologies.shardaapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Teacher_Home_Frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Teacher_Home_Frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Teacher_Home_Frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    TextView InfoDetails , TodTT;
    TextView Welcome;
    RecyclerView TimeTable ;

    DatabaseReference InfoRef , TTRef;
    FirebaseAuth mAuth ;


    List<TeacherTimeTableClass> mTeachTT  = new ArrayList<>() ;
    TeacherTimeTableListAdapter adapter;

    String Name = ""  , Number = "" , TeacherId = "" ,  Email = ""  , SystemId = "" ;

    TeacherInfoClass obj1 ;


    public Teacher_Home_Frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Teacher_Home_Frag.
     */
    // TODO: Rename and change types and number of parameters
    public static Teacher_Home_Frag newInstance(String param1, String param2) {
        Teacher_Home_Frag fragment = new Teacher_Home_Frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        Utils.getDatabase();

        View v =  inflater.inflate(R.layout.fragment_teacher__home_, container, false);

        InfoDetails = (TextView)v.findViewById(R.id.TextViewAdminHomeDetails);
        TodTT = (TextView)v.findViewById(R.id.TodayTT);
        TimeTable = (RecyclerView)v.findViewById(R.id.recyclerView);

        Welcome = (TextView)v.findViewById(R.id.TextViewWelcome);
        Welcome.setText("Welcome,");
        mAuth = FirebaseAuth.getInstance();


        adapter = new TeacherTimeTableListAdapter(inflater ,getContext() , mTeachTT);
        TimeTable.setLayoutManager(new LinearLayoutManager(getActivity()));
        TimeTable.setAdapter(adapter);

        adapter.notifyDataSetChanged();


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        InfoRef = FirebaseDatabase.getInstance().getReference("Users/TeacherLogin/" + uid + "/") ;
        InfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    Name = dataSnapshot.child("Name").getValue().toString();
                    Email = dataSnapshot.child("Email").getValue().toString();
                    SystemId = dataSnapshot.child("SystemId").getValue().toString();
                    Number = dataSnapshot.child("Number").getValue().toString();
                    TeacherId = dataSnapshot.child("TeacherId").getValue().toString();
                    obj1 = new TeacherInfoClass(Name , Email ,  SystemId , Number , TeacherId) ;

                    InfoDetails.setText("Name : " + Name + "\nEmail : " +Email +"\nSystemId : " + SystemId + "\nNumber : " + Number );

                    if( TeacherId != ""){

                        Calendar calendar = Calendar.getInstance();
                        int day = calendar.get(Calendar.DAY_OF_WEEK);

                        switch (day) {
                            case Calendar.SUNDAY:


                                // Current day is Sunday

                                break;
                            case Calendar.MONDAY:

                                mTeachTT.clear();
                                adapter.notifyDataSetChanged();

                                TTRef = FirebaseDatabase.getInstance().getReference("TimeTable/TeacherWise/" + TeacherId + "/MONDAY/" ) ;
                                TTRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

                                            mTeachTT.clear();
                                            adapter.notifyDataSetChanged();

                                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                                                String Key = dataSnapshot1.getKey();

                                                if(!Key.equals("TeacherName") && !Key.equals("TeacherCode") && !Key.equals("TeacherId")  ){


                                                    String Sch = dataSnapshot1.child("SchoolName").getValue().toString();
                                                    String Dept = dataSnapshot1.child("DeptName").getValue().toString();
                                                    String Year = dataSnapshot1.child("YearName").getValue().toString();
                                                    String Class = dataSnapshot1.child("ClassName").getValue().toString();
                                                    String Loc = dataSnapshot1.child("Loc").getValue().toString();
                                                    String Sub = dataSnapshot1.child("Subject").getValue().toString();
                                                    String SubCode = dataSnapshot1.child("SubjectCode").getValue().toString();
                                                    String Time = dataSnapshot1.child("Time").getValue().toString();

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

                                // Current day is Monday
                                break;
                            case Calendar.TUESDAY:

                                mTeachTT.clear();
                                adapter.notifyDataSetChanged();


                                TTRef = FirebaseDatabase.getInstance().getReference("TimeTable/TeacherWise/" + TeacherId + "/TUESDAY/" ) ;
                                TTRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

                                            mTeachTT.clear();
                                            adapter.notifyDataSetChanged();

                                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){


                                                String Sch = dataSnapshot1.child("SchoolName").getValue().toString();
                                                String Dept = dataSnapshot1.child("DeptName").getValue().toString();
                                                String Year = dataSnapshot1.child("YearName").getValue().toString();
                                                String Class = dataSnapshot1.child("ClassName").getValue().toString();

                                                String Loc = dataSnapshot1.child("Loc").getValue().toString();
                                                String Sub = dataSnapshot1.child("Subject").getValue().toString();
                                                String SubCode = dataSnapshot1.child("SubjectCode").getValue().toString();
                                                String Time = dataSnapshot1.child("Time").getValue().toString();


                                                String g = "" ;
                                                String p = "" ;

                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Periods").getChildren()){

                                                    String Periods = dataSnapshot2.child("Name").getValue().toString();
                                                    p = p.concat(Periods) ;

                                                }
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Groups").getChildren()){


                                                    String Periods = dataSnapshot2.child("GroupName").getValue().toString();
                                                    g = g.concat(Periods) ;
                                                }

                                                mTeachTT.add(new TeacherTimeTableClass( Sch , Dept , Year , Class + " - " + g  , Sub , SubCode , Loc , Time + " / " + p));
                                                adapter.notifyDataSetChanged();


                                            }





                                        }

                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                // etc.
                                break;
                            case Calendar.WEDNESDAY:

                                mTeachTT.clear();
                                adapter.notifyDataSetChanged();


                                TTRef = FirebaseDatabase.getInstance().getReference("TimeTable/TeacherWise/" + TeacherId + "/WEDNESDAY/" ) ;
                                TTRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

                                            mTeachTT.clear();
                                            adapter.notifyDataSetChanged();

                                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){


                                                String Sch = dataSnapshot1.child("SchoolName").getValue().toString();
                                                String Dept = dataSnapshot1.child("DeptName").getValue().toString();
                                                String Year = dataSnapshot1.child("YearName").getValue().toString();
                                                String Class = dataSnapshot1.child("ClassName").getValue().toString();

                                                String Loc = dataSnapshot1.child("Loc").getValue().toString();
                                                String Sub = dataSnapshot1.child("Subject").getValue().toString();
                                                String SubCode = dataSnapshot1.child("SubjectCode").getValue().toString();
                                                String Time = dataSnapshot1.child("Time").getValue().toString();



                                                String g = "" ;
                                                String p = "" ;

                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Periods").getChildren()){

                                                    String Periods = dataSnapshot2.child("Name").getValue().toString();
                                                    p = p.concat(Periods) ;

                                                }
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Groups").getChildren()){


                                                    String Periods = dataSnapshot2.child("GroupName").getValue().toString();
                                                    g = g.concat(Periods) ;
                                                }

                                                mTeachTT.add(new TeacherTimeTableClass( Sch , Dept , Year , Class + " - " + g  , Sub , SubCode , Loc , Time + " / " + p));
                                                adapter.notifyDataSetChanged();



                                            }





                                        }

                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                                // etc.
                                break;
                            case Calendar.THURSDAY:

                                mTeachTT.clear();
                                adapter.notifyDataSetChanged();


                                TTRef = FirebaseDatabase.getInstance().getReference("TimeTable/TeacherWise/" + TeacherId + "/THURSDAY/" ) ;
                                TTRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

                                            mTeachTT.clear();
                                            adapter.notifyDataSetChanged();

                                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){


                                                String Sch = dataSnapshot1.child("SchoolName").getValue().toString();
                                                String Dept = dataSnapshot1.child("DeptName").getValue().toString();
                                                String Year = dataSnapshot1.child("YearName").getValue().toString();
                                                String Class = dataSnapshot1.child("ClassName").getValue().toString();

                                                String Loc = dataSnapshot1.child("Loc").getValue().toString();
                                                String Sub = dataSnapshot1.child("Subject").getValue().toString();
                                                String SubCode = dataSnapshot1.child("SubjectCode").getValue().toString();
                                                String Time = dataSnapshot1.child("Time").getValue().toString();



                                                String g = "" ;
                                                String p = "" ;

                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Periods").getChildren()){

                                                    String Periods = dataSnapshot2.child("Name").getValue().toString();
                                                    p = p.concat(Periods) ;

                                                }
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Groups").getChildren()){


                                                    String Periods = dataSnapshot2.child("GroupName").getValue().toString();
                                                    g = g.concat(Periods) ;
                                                }

                                                mTeachTT.add(new TeacherTimeTableClass( Sch , Dept , Year , Class + " - " + g  , Sub , SubCode , Loc , Time + " / " + p));
                                                adapter.notifyDataSetChanged();



                                            }

                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                // etc.
                                break;
                            case Calendar.FRIDAY:

                                mTeachTT.clear();
                                adapter.notifyDataSetChanged();


                                TTRef = FirebaseDatabase.getInstance().getReference("TimeTable/TeacherWise/" + TeacherId + "/FRIDAY/" ) ;
                                TTRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

                                            mTeachTT.clear();
                                            adapter.notifyDataSetChanged();

                                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){


                                                String Sch = dataSnapshot1.child("SchoolName").getValue().toString();
                                                String Dept = dataSnapshot1.child("DeptName").getValue().toString();
                                                String Year = dataSnapshot1.child("YearName").getValue().toString();
                                                String Class = dataSnapshot1.child("ClassName").getValue().toString();

                                                String Loc = dataSnapshot1.child("Loc").getValue().toString();
                                                String Sub = dataSnapshot1.child("Subject").getValue().toString();
                                                String SubCode = dataSnapshot1.child("SubjectCode").getValue().toString();
                                                String Time = dataSnapshot1.child("Time").getValue().toString();


                                                String g = "" ;
                                                String p = "" ;

                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Periods").getChildren()){

                                                    String Periods = dataSnapshot2.child("Name").getValue().toString();
                                                    p = p.concat(Periods) ;

                                                }
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Groups").getChildren()){


                                                    String Periods = dataSnapshot2.child("GroupName").getValue().toString();
                                                    g = g.concat(Periods) ;
                                                }

                                                mTeachTT.add(new TeacherTimeTableClass( Sch , Dept , Year , Class + " - " + g  , Sub , SubCode , Loc , Time + " / " + p));
                                                adapter.notifyDataSetChanged();

                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                                break;
                            case Calendar.SATURDAY:

                                mTeachTT.clear();
                                adapter.notifyDataSetChanged();

                                TTRef = FirebaseDatabase.getInstance().getReference("TimeTable/TeacherWise/" + TeacherId + "/SATURDAY/" ) ;
                                TTRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

                                            mTeachTT.clear();
                                            adapter.notifyDataSetChanged();

                                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){


                                                String Sch = dataSnapshot1.child("SchoolName").getValue().toString();
                                                String Dept = dataSnapshot1.child("DeptName").getValue().toString();
                                                String Year = dataSnapshot1.child("YearName").getValue().toString();
                                                String Class = dataSnapshot1.child("ClassName").getValue().toString();

                                                String Loc = dataSnapshot1.child("Loc").getValue().toString();
                                                String Sub = dataSnapshot1.child("Subject").getValue().toString();
                                                String SubCode = dataSnapshot1.child("SubjectCode").getValue().toString();
                                                String Time = dataSnapshot1.child("Time").getValue().toString();



                                                String g = "" ;
                                                String p = "" ;

                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Periods").getChildren()){

                                                    String Periods = dataSnapshot2.child("Name").getValue().toString();
                                                    p = p.concat(Periods) ;

                                                }
                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Groups").getChildren()){


                                                    String Periods = dataSnapshot2.child("GroupName").getValue().toString();
                                                    g = g.concat(Periods) ;
                                                }

                                                mTeachTT.add(new TeacherTimeTableClass( Sch , Dept , Year , Class + " - " + g  , Sub , SubCode , Loc , Time + " / " + p));
                                                adapter.notifyDataSetChanged();


                                            }





                                        }

                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                //InfoDetails.setText("Name : " + Name);
                                break;

                        }

                    }


                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v ;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);


    }
}
