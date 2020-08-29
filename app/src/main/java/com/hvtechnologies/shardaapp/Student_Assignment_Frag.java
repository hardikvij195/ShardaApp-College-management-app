package com.hvtechnologies.shardaapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Student_Assignment_Frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Student_Assignment_Frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Student_Assignment_Frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    CardView btn1 , btn2 ;
    RecyclerView RcView;
    DatabaseReference InfoRef;

    List<AssignmentClass> mStudTT = new ArrayList<>() ;
    List<StudentTimeTableClass> mStudTT2 = new ArrayList<>() ;
    AssignmentClassListAdapter adapter;


    public Student_Assignment_Frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Student_Assignment_Frag.
     */
    // TODO: Rename and change types and number of parameters
    public static Student_Assignment_Frag newInstance(String param1, String param2) {
        Student_Assignment_Frag fragment = new Student_Assignment_Frag();
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

        View v =  inflater.inflate(R.layout.fragment_student__assignment_, container, false);
        Utils.getDatabase();

        btn1 = (CardView)v.findViewById(R.id.cardview);
        btn2 = (CardView)v.findViewById(R.id.cardview3);
        RcView = (RecyclerView)v.findViewById(R.id.recyclerView);

        final Intent intent1 = new Intent(getActivity(), Student_Assignment_Activity1.class);
        final Intent intent2 = new Intent(getActivity(), Student_Assignment_Activity2.class);


        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
        final String Id1 = sharedPrefs.getString("SchId", "");
        final String Id2 = sharedPrefs.getString("DptId", "");
        final String Id3 = sharedPrefs.getString("YrId", "");
        String Id4 = sharedPrefs.getString("ClsId", "");
        String Id = sharedPrefs.getString("USERID", "");

        adapter = new AssignmentClassListAdapter(inflater ,getContext() , mStudTT);
        RcView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RcView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Date date = new Date();  // to get the date
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd"); // getting date in this format
        final String formattedDate = df.format(date.getTime());

        mStudTT.clear();
        mStudTT2.clear();

        //Get Student Subs ------------------


        InfoRef = FirebaseDatabase.getInstance().getReference("Users/StudentLogin/" + Id + "/Subjects/") ;
        InfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        final String subid = dataSnapshot1.getKey();
                        final String SubjectName = dataSnapshot1.child("SubName").getValue().toString();
                        final String SubCode = dataSnapshot1.child("SubCode").getValue().toString();
                        mStudTT2.add(new StudentTimeTableClass( SubCode , SubjectName , subid , " " , " "));

                        InfoRef = FirebaseDatabase.getInstance().getReference("Assignments/" + Id1 + "/" + Id2 + "/" + Id3 + "/" + subid + "/" ) ;
                        InfoRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists())
                                {
                                    mStudTT.clear();

                                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                        String day = dataSnapshot1.getKey();

                                        if(day.contains(formattedDate)){

                                            String Date = dataSnapshot1.child("Upload Date").getValue().toString();
                                            String Topic = dataSnapshot1.child("Topic").getValue().toString();
                                            String Desc = dataSnapshot1.child("Desc").getValue().toString();
                                            String DownloadUrl = dataSnapshot1.child("Download Url").getValue().toString();
                                            mStudTT.add(new AssignmentClass(  SubjectName, SubCode , Date , Topic, Desc , DownloadUrl));
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
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent1);



            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent2);


            }
        });


        return v;

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
