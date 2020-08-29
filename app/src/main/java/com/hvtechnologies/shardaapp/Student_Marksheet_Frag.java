package com.hvtechnologies.shardaapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
 * {@link Student_Marksheet_Frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Student_Marksheet_Frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Student_Marksheet_Frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    CardView btn1 ;
    RecyclerView RcView;

    DatabaseReference InfoRef;

    List<StudentTimeTableClass> mStudTT = new ArrayList<>() ;
    StudentTimeTableListAdapter adapter;

    public Student_Marksheet_Frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Student_Marksheet_Frag.
     */
    // TODO: Rename and change types and number of parameters
    public static Student_Marksheet_Frag newInstance(String param1, String param2) {
        Student_Marksheet_Frag fragment = new Student_Marksheet_Frag();
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


        View v =  inflater.inflate(R.layout.fragment_student__marksheet_, container, false);
        Utils.getDatabase();

        btn1 = (CardView)v.findViewById(R.id.cardview);
        RcView = (RecyclerView)v.findViewById(R.id.recyclerView);

        final Intent intent1 = new Intent(getActivity(), Student_Marks_Activity.class);

        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
        String Id1 = sharedPrefs.getString("SchId", "");
        String Id2 = sharedPrefs.getString("DptId", "");
        String Id3 = sharedPrefs.getString("YrId", "");
        String Id4 = sharedPrefs.getString("ClsId", "");
        String Id5 = sharedPrefs.getString("GrpId", "");
        String StdId = sharedPrefs.getString("StdId", "");

        adapter = new StudentTimeTableListAdapter(inflater ,getContext() , mStudTT);
        RcView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RcView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        Date date = new Date();  // to get the date
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd"); // getting date in this format
        final String formattedDate = df.format(date.getTime());


        mStudTT.clear();

        InfoRef = FirebaseDatabase.getInstance().getReference("Marksheets/StudentWise/" + Id1 + "/" + Id2 + "/"+ Id3 + "/"+ Id4 + "/"+ Id5 + "/Att/" + StdId + "/" ) ;
        InfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    mStudTT.clear();

                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        String day = dataSnapshot1.getKey();

                        if(day.contains(formattedDate)){

                            String Sub = dataSnapshot1.child("SubName").getValue().toString();
                            String SubCode = dataSnapshot1.child("SubCode").getValue().toString();
                            String Teach = dataSnapshot1.child("ByName").getValue().toString();
                            String TeachCd = dataSnapshot1.child("ByCode").getValue().toString();

                            String Name = dataSnapshot1.child("Name").getValue().toString();
                            String Total = dataSnapshot1.child("Total").getValue().toString();

                            String Date = dataSnapshot1.child("Date").getValue().toString();
                            String Marks = dataSnapshot1.child("Marks").getValue().toString();




                            mStudTT.add(new StudentTimeTableClass( SubCode , Sub , Teach + " - " + TeachCd , Date , Name + " - " + Marks + "/" + Total));
                            adapter.notifyDataSetChanged();


                        }
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
