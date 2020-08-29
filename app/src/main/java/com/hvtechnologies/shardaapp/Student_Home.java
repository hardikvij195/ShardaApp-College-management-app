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

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Student_Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Student_Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Student_Home extends Fragment {
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

    DatabaseReference InfoRef , TTRef ;
    FirebaseAuth mAuth ;

    String Name = ""  , Number = "" , School = "" , SchoolId = "" , Dept = "" , DeptId = "" , Class = "" , ClassId = "" ,
            Group = "" , GroupId = "" , Email = ""  , Year = "" , YearId = "" , SystemId = "" , StdId = "";

    List<String> StudentSubs ;

    List<StudentTimeTableClass> mStudTT = new ArrayList<>() ;
    StudentTimeTableListAdapter adapter;

    StudentInfoClass obj1 ;







    public Student_Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Student_Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Student_Home newInstance(String param1, String param2) {
        Student_Home fragment = new Student_Home();
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



        View v =  inflater.inflate(R.layout.fragment_student__home, container, false);
        Utils.getDatabase();

        InfoDetails = (TextView)v.findViewById(R.id.TextViewAdminHomeDetails);
        TodTT = (TextView)v.findViewById(R.id.TodayTT);

        TimeTable = (RecyclerView)v.findViewById(R.id.recyclerView);


        Welcome = (TextView)v.findViewById(R.id.TextViewWelcome);
        Welcome.setText("Welcome,");
        mAuth = FirebaseAuth.getInstance();

        adapter = new StudentTimeTableListAdapter(inflater ,getContext() , mStudTT);
        TimeTable.setLayoutManager(new LinearLayoutManager(getActivity()));
        TimeTable.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        InfoRef = FirebaseDatabase.getInstance().getReference("Users/StudentLogin/" + uid + "/") ;
        InfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    Name = dataSnapshot.child("Name").getValue().toString();
                    Email = dataSnapshot.child("Email").getValue().toString();
                    StdId = dataSnapshot.child("StdId").getValue().toString();
                    SystemId = dataSnapshot.child("SystemId").getValue().toString();
                    Number = dataSnapshot.child("Number").getValue().toString();
                    School = dataSnapshot.child("School").getValue().toString();
                    SchoolId = dataSnapshot.child("SchoolId").getValue().toString();
                    Dept = dataSnapshot.child("Dept").getValue().toString();
                    DeptId = dataSnapshot.child("DeptId").getValue().toString();
                    Year = dataSnapshot.child("Year").getValue().toString();
                    YearId = dataSnapshot.child("YearId").getValue().toString();
                    Class = dataSnapshot.child("Class").getValue().toString();
                    ClassId = dataSnapshot.child("ClassId").getValue().toString();
                    Group = dataSnapshot.child("Group").getValue().toString();
                    GroupId = dataSnapshot.child("GroupId").getValue().toString();


                    SharedPreferences sharedPrefs = getActivity().getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPrefs.edit();
                    edit.putString("SchId" , SchoolId );
                    edit.putString("DptId" , DeptId );
                    edit.putString("YrId" , YearId );
                    edit.putString("ClsId" , ClassId );
                    edit.putString("GrpId" , GroupId);
                    edit.putString("StdId" , StdId);
                    edit.apply();


                    obj1 = new StudentInfoClass(Name , Email ,  School , SchoolId , Dept , DeptId , Year  ,YearId , Class , ClassId  ,Group , GroupId) ;
                    InfoDetails.setText("Name : " + Name + "\nEmail : " +Email +"\nSchool : " + School +"\nDepartment : " + Dept +"\nYear : " + Year +"\nClass : " + Class+"\nGroup : " + Group );


                    mStudTT.clear();

                    if(GroupId != null){

                        Calendar calendar = Calendar.getInstance();
                        int day = calendar.get(Calendar.DAY_OF_WEEK);

                        switch (day) {
                            case Calendar.SUNDAY:


                                // Current day is Sunday

                                break;
                            case Calendar.MONDAY:

                                TTRef = FirebaseDatabase.getInstance().getReference("TimeTable/ClassWise/" + SchoolId + "/"  + DeptId + "/" + YearId + "/" + ClassId + "/" + GroupId + "/MONDAY/" ) ;
                                TTRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

                                            mStudTT.clear();
                                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                                                String Loc = dataSnapshot1.child("Loc").getValue().toString();
                                                String Sub = dataSnapshot1.child("Subject").getValue().toString();
                                                String SubCode = dataSnapshot1.child("SubjectCode").getValue().toString();
                                                String Teach = dataSnapshot1.child("Teacher").getValue().toString();
                                                String Time = dataSnapshot1.child("Time").getValue().toString();
                                                String p = "" ;

                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Periods").getChildren()){

                                                    String Periods = dataSnapshot2.child("Name").getValue().toString();
                                                    p = p.concat(Periods) ;

                                                }

                                                mStudTT.add(new StudentTimeTableClass( SubCode , Sub , Teach , Loc , Time + "/" + p));
                                                adapter.notifyDataSetChanged();

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


                                TTRef = FirebaseDatabase.getInstance().getReference("TimeTable/ClassWise/" + SchoolId + "/"  + DeptId + "/" + YearId + "/" + ClassId + "/" + GroupId + "/TUESDAY/" ) ;
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
                                                String p = "" ;

                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Periods").getChildren()){

                                                    String Periods = dataSnapshot2.child("Name").getValue().toString();
                                                    p = p.concat(Periods) ;

                                                }

                                                mStudTT.add(new StudentTimeTableClass( SubCode , Sub , Teach , Loc , Time + "/" + p));
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


                                TTRef = FirebaseDatabase.getInstance().getReference("TimeTable/ClassWise/" + SchoolId + "/"  + DeptId + "/" + YearId + "/" + ClassId + "/" + GroupId + "/WEDNESDAY/" ) ;
                                TTRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){


                                            mStudTT.clear();
                                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){


                                                String Loc = dataSnapshot1.child("Loc").getValue().toString();
                                                String Sub = dataSnapshot1.child("Subject").getValue().toString();
                                                String SubCode = dataSnapshot1.child("SubjectCode").getValue().toString();
                                                String Teach = dataSnapshot1.child("Teacher").getValue().toString();
                                                String Time = dataSnapshot1.child("Time").getValue().toString();
                                                String p = "" ;

                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Periods").getChildren()){

                                                    String Periods = dataSnapshot2.child("Name").getValue().toString();
                                                    p = p.concat(Periods) ;

                                                }

                                                mStudTT.add(new StudentTimeTableClass( SubCode , Sub , Teach , Loc , Time + "/" + p));
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


                                TTRef = FirebaseDatabase.getInstance().getReference("TimeTable/ClassWise/" + SchoolId + "/"  + DeptId + "/" + YearId + "/" + ClassId + "/" + GroupId + "/THURSDAY/" ) ;
                                TTRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){
                                            mStudTT.clear();

                                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){


                                                String Loc = dataSnapshot1.child("Loc").getValue().toString();
                                                String Sub = dataSnapshot1.child("Subject").getValue().toString();
                                                String SubCode = dataSnapshot1.child("SubjectCode").getValue().toString();
                                                String Teach = dataSnapshot1.child("Teacher").getValue().toString();
                                                String Time = dataSnapshot1.child("Time").getValue().toString();
                                                String p = "" ;

                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Periods").getChildren()){

                                                    String Periods = dataSnapshot2.child("Name").getValue().toString();
                                                    p = p.concat(Periods) ;

                                                }

                                                mStudTT.add(new StudentTimeTableClass( SubCode , Sub , Teach , Loc , Time + "/" + p));
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


                                TTRef = FirebaseDatabase.getInstance().getReference("TimeTable/ClassWise/" + SchoolId + "/"  + DeptId + "/" + YearId + "/" + ClassId + "/" + GroupId + "/FRIDAY/" ) ;
                                TTRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){
                                            mStudTT.clear();

                                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                                                String Loc = dataSnapshot1.child("Loc").getValue().toString();
                                                String Sub = dataSnapshot1.child("Subject").getValue().toString();
                                                String SubCode = dataSnapshot1.child("SubjectCode").getValue().toString();
                                                String Teach = dataSnapshot1.child("Teacher").getValue().toString();
                                                String Time = dataSnapshot1.child("Time").getValue().toString();
                                                String p = "" ;

                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Periods").getChildren()){

                                                    String Periods = dataSnapshot2.child("Name").getValue().toString();
                                                    p = p.concat(Periods) ;

                                                }

                                                mStudTT.add(new StudentTimeTableClass( SubCode , Sub , Teach , Loc , Time + "/" + p));
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


                                TTRef = FirebaseDatabase.getInstance().getReference("TimeTable/ClassWise/" + SchoolId + "/"  + DeptId + "/" + YearId + "/" + ClassId + "/" + GroupId + "/SATURDAY/" ) ;
                                TTRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){
                                            mStudTT.clear();

                                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){


                                                String Loc = dataSnapshot1.child("Loc").getValue().toString();
                                                String Sub = dataSnapshot1.child("Subject").getValue().toString();
                                                String SubCode = dataSnapshot1.child("SubjectCode").getValue().toString();
                                                String Teach = dataSnapshot1.child("Teacher").getValue().toString();
                                                String Time = dataSnapshot1.child("Time").getValue().toString();
                                                String p = "" ;

                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("Periods").getChildren()){

                                                    String Periods = dataSnapshot2.child("Name").getValue().toString();
                                                    p = p.concat(Periods) ;

                                                }

                                                mStudTT.add(new StudentTimeTableClass( SubCode , Sub , Teach , Loc , Time + "/" + p));
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

        return  v;

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
