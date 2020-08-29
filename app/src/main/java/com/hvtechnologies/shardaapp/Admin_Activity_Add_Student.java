package com.hvtechnologies.shardaapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Admin_Activity_Add_Student extends AppCompatActivity {


    TextInputEditText NameTxt , SystemIdTxt , PasswordTxt , EmailTxt , NumberTxt ;

    DatabaseReference ClassRef , SchoolNamesRef , TimingsRef , ClassRef2;

    Button Submit , SelectSubs ;


    private FirebaseAuth mAuth;


    private ArrayList<NameIdClass> SchoolNames = new ArrayList<>() ;
    private ArrayList<NameIdClass> DeptNames = new ArrayList<>() ;
    private ArrayList<NameIdClass> YearNames = new ArrayList<>() ;
    private ArrayList<NameIdClass> ClassNames = new ArrayList<>() ;
    private ArrayList<NameIdClass> GroupNames = new ArrayList<>() ;

    private ArrayList<SubjectNameIdCodeClass> SubjectNames = new ArrayList<>() ;


    private ArrayList<String> SchoolNamesString = new ArrayList<>() ;
    private ArrayList<String> DeptNamesString = new ArrayList<>() ;
    private ArrayList<String> YearNamesString = new ArrayList<>() ;
    private ArrayList<String> SubjectNamesString = new ArrayList<>() ;
    private ArrayList<String> ClassNamesString = new ArrayList<>() ;
    private ArrayList<String> GroupNamesString = new ArrayList<>() ;


    private ArrayAdapter<String> adapterSpinnerSchoolNames ;
    private ArrayAdapter<String> adapterSpinnerDeptNames ;
    private ArrayAdapter<String> adapterSpinnerYearNames ;
    private ArrayAdapter<String> adapterSpinnerSubjectNames ;
    private ArrayAdapter<String> adapterSpinnerClassNames ;
    private ArrayAdapter<String> adapterSpinnerGroupNames ;


    Spinner SchName , DeptName , YearName ,ClassName , GroupName ;


    private ArrayList<SubjectNameIdCodeClass> mSelectedSubjectNames = new ArrayList<>() ;
    ArrayList  mSelectedItems = new ArrayList();

    TextView ShowSubs ;


    private ProgressDialog mLoginProgress ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin___add__student);


        Utils.getDatabase();
        this.setTheme(R.style.AlertDialogCustom);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mLoginProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();


        NameTxt = (TextInputEditText) findViewById(R.id.StudentName);
        SystemIdTxt = (TextInputEditText) findViewById(R.id.StudentSysId);
        PasswordTxt = (TextInputEditText) findViewById(R.id.StudentPass);
        EmailTxt = (TextInputEditText) findViewById(R.id.StudentEmal);
        NumberTxt = (TextInputEditText) findViewById(R.id.StudentNumber);

        Button SelectSubs = (Button) findViewById(R.id.SelectSubjectsAddStudent);
        Button Submit = (Button) findViewById(R.id.SubmitAddStudent);

        final TextView ShowSubs = (TextView) findViewById(R.id.textView);

        final Spinner SchName = (Spinner)findViewById(R.id.SchoolNameAddStud);
        final Spinner DeptName = (Spinner)findViewById(R.id.DeptNameAddStud);
        final Spinner YearName = (Spinner)findViewById(R.id.YearNameAddStud);
        final Spinner ClassName = (Spinner)findViewById(R.id.ClassNameAddStud);
        final Spinner GroupName = (Spinner)findViewById(R.id.GroupNameAddStud);


        adapterSpinnerSchoolNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , SchoolNamesString);
        adapterSpinnerDeptNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , DeptNamesString);
        adapterSpinnerYearNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , YearNamesString);
        adapterSpinnerSubjectNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , SubjectNamesString);
        adapterSpinnerClassNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , ClassNamesString);
        adapterSpinnerGroupNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , GroupNamesString);


        SchName.setAdapter(adapterSpinnerSchoolNames);
        DeptName.setAdapter(adapterSpinnerDeptNames);
        YearName.setAdapter(adapterSpinnerYearNames);
        ClassName.setAdapter(adapterSpinnerClassNames);
        GroupName.setAdapter(adapterSpinnerGroupNames);



        SchoolNamesRef = FirebaseDatabase.getInstance().getReference("Structure/") ;
        SchoolNamesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                SchoolNames.clear();
                SchoolNamesString.clear();
                adapterSpinnerSchoolNames.notifyDataSetChanged();

                if(dataSnapshot.exists()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        String id = dataSnapshot1.getKey();
                        String Name = dataSnapshot1.child("Name").getValue().toString();
                        SchoolNames.add(new NameIdClass(Name , id));
                        SchoolNamesString.add(Name);
                        adapterSpinnerSchoolNames.notifyDataSetChanged();

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        SchName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                final String Id = SchoolNames.get(position).getId();

                DeptNames.clear();
                DeptNamesString.clear();
                YearNames.clear();
                YearNamesString.clear();
                adapterSpinnerYearNames.notifyDataSetChanged();
                adapterSpinnerSchoolNames.notifyDataSetChanged();
                adapterSpinnerDeptNames.notifyDataSetChanged();
                ClassNames.clear();
                ClassNamesString.clear();
                adapterSpinnerClassNames.notifyDataSetChanged();
                GroupNames.clear();
                GroupNamesString.clear();
                adapterSpinnerGroupNames.notifyDataSetChanged();
                SubjectNames.clear();
                SubjectNamesString.clear();

                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/") ;
                ClassRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                String id = dataSnapshot1.getKey();

                                if(!id.equals("Name")){

                                    String Name = dataSnapshot1.child("Name").getValue().toString();
                                    DeptNames.add(new NameIdClass(Name , id));
                                    DeptNamesString.add(Name);
                                    adapterSpinnerDeptNames.notifyDataSetChanged();
                                }
                            }
                            if(!DeptNames.isEmpty()) {

                                final String deid = DeptNames.get(0).getId();
                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + deid + "/");
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

                                            YearNames.clear();
                                            YearNamesString.clear();
                                            adapterSpinnerYearNames.notifyDataSetChanged();
                                            adapterSpinnerSchoolNames.notifyDataSetChanged();
                                            adapterSpinnerDeptNames.notifyDataSetChanged();
                                            ClassNames.clear();
                                            ClassNamesString.clear();
                                            adapterSpinnerClassNames.notifyDataSetChanged();
                                            GroupNames.clear();
                                            GroupNamesString.clear();
                                            adapterSpinnerGroupNames.notifyDataSetChanged();


                                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                String id = dataSnapshot2.getKey();

                                                if (!id.equals("Name")) {

                                                    String Name = dataSnapshot2.child("Name").getValue().toString();
                                                    YearNames.add(new NameIdClass(Name , id));
                                                    YearNamesString.add(Name);
                                                    adapterSpinnerYearNames.notifyDataSetChanged();

                                                }
                                            }

                                            if(!YearNames.isEmpty()) {

                                                //int pos = DeptNameDelGroup.getSelectedItemPosition();
                                                final String yrid  = YearNames.get(0).getId();

                                                GroupNames.clear();
                                                GroupNamesString.clear();
                                                adapterSpinnerGroupNames.notifyDataSetChanged();
                                                ClassNames.clear();
                                                ClassNamesString.clear();
                                                adapterSpinnerClassNames.notifyDataSetChanged();


                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + deid + "/" + yrid + "/Classes/");
                                                ClassRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        if(dataSnapshot.exists()){

                                                            GroupNames.clear();
                                                            GroupNamesString.clear();
                                                            adapterSpinnerGroupNames.notifyDataSetChanged();

                                                            ClassNames.clear();
                                                            ClassNamesString.clear();
                                                            adapterSpinnerClassNames.notifyDataSetChanged();

                                                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                                String id = dataSnapshot2.getKey();

                                                                if (!id.equals("Name") ) {

                                                                    String Name = dataSnapshot2.child("Name").getValue().toString();
                                                                    ClassNames.add(new NameIdClass(Name , id));
                                                                    ClassNamesString.add(Name);
                                                                    adapterSpinnerClassNames.notifyDataSetChanged();

                                                                }

                                                            }

                                                            if(!ClassNames.isEmpty()) {


                                                                //int pos2 = YearNameDelGroup.getSelectedItemPosition();
                                                                String Id3  = ClassNames.get(0).getId();

                                                                GroupNames.clear();
                                                                GroupNamesString.clear();
                                                                adapterSpinnerGroupNames.notifyDataSetChanged();

                                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + deid + "/" + yrid + "/Classes/" + Id3 + "/Groups/");
                                                                ClassRef.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                                        if(dataSnapshot.exists()){


                                                                            GroupNames.clear();
                                                                            GroupNamesString.clear();
                                                                            adapterSpinnerGroupNames.notifyDataSetChanged();

                                                                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                                                String id = dataSnapshot2.getKey();

                                                                                if (!id.equals("Name") ) {

                                                                                    String Name = dataSnapshot2.child("Name").getValue().toString();
                                                                                    GroupNames.add(new NameIdClass(Name , id));
                                                                                    GroupNamesString.add(Name);
                                                                                    adapterSpinnerGroupNames.notifyDataSetChanged();

                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });


                                                                SubjectNames.clear();
                                                                SubjectNamesString.clear();

                                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + deid + "/" + yrid + "/Classes/" + Id3 + "/Subjects/" ) ;
                                                                ClassRef.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                                        if(dataSnapshot.exists()){


                                                                            SubjectNames.clear();
                                                                            SubjectNamesString.clear();
                                                                            adapterSpinnerSubjectNames.notifyDataSetChanged();

                                                                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                                                String id = dataSnapshot2.getKey();

                                                                                if (!id.equals("Name") ) {

                                                                                    String Name = dataSnapshot2.child("Name").getValue().toString();
                                                                                    String Code = dataSnapshot2.child("Code").getValue().toString();

                                                                                    SubjectNames.add(new SubjectNameIdCodeClass(Name , id , Code));
                                                                                    SubjectNamesString.add(Name);
                                                                                    adapterSpinnerSubjectNames.notifyDataSetChanged();

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
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {



            }
        });

        DeptName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                int pos = SchName.getSelectedItemPosition();
                final String Id = SchoolNames.get(pos).getId();

                final String Id2 = DeptNames.get(position).getId();

                YearNames.clear();
                YearNamesString.clear();
                adapterSpinnerYearNames.notifyDataSetChanged();
                ClassNames.clear();
                ClassNamesString.clear();
                adapterSpinnerClassNames.notifyDataSetChanged();
                GroupNames.clear();
                GroupNamesString.clear();
                adapterSpinnerGroupNames.notifyDataSetChanged();
                SubjectNames.clear();
                SubjectNamesString.clear();

                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2 + "/") ;
                ClassRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){

                            YearNames.clear();
                            YearNamesString.clear();
                            adapterSpinnerYearNames.notifyDataSetChanged();
                            ClassNames.clear();
                            ClassNamesString.clear();
                            adapterSpinnerClassNames.notifyDataSetChanged();
                            GroupNames.clear();
                            GroupNamesString.clear();
                            adapterSpinnerGroupNames.notifyDataSetChanged();


                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                String id = dataSnapshot1.getKey();

                                if(!id.equals("Name")){

                                    String YearNa = dataSnapshot1.child("Name").getValue().toString();
                                    YearNames.add(new NameIdClass(YearNa , id));
                                    YearNamesString.add(YearNa);
                                    adapterSpinnerYearNames.notifyDataSetChanged();

                                }
                            }

                            if(!YearNames.isEmpty()) {

                                final String yrid  = YearNames.get(0).getId();

                                ClassNames.clear();
                                ClassNamesString.clear();
                                adapterSpinnerClassNames.notifyDataSetChanged();
                                GroupNames.clear();
                                GroupNamesString.clear();
                                adapterSpinnerGroupNames.notifyDataSetChanged();


                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + Id2 + "/" + yrid + "/Classes/");
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

                                            ClassNames.clear();
                                            ClassNamesString.clear();
                                            adapterSpinnerClassNames.notifyDataSetChanged();
                                            GroupNames.clear();
                                            GroupNamesString.clear();
                                            adapterSpinnerGroupNames.notifyDataSetChanged();

                                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                String id = dataSnapshot2.getKey();

                                                if (!id.equals("Name") ) {

                                                    String Name = dataSnapshot2.child("Name").getValue().toString();
                                                    ClassNames.add(new NameIdClass(Name , id));
                                                    ClassNamesString.add(Name);
                                                    adapterSpinnerClassNames.notifyDataSetChanged();

                                                }

                                            }
                                            if(!ClassNames.isEmpty()) {


                                                String Id3  = ClassNames.get(0).getId();

                                                GroupNames.clear();
                                                GroupNamesString.clear();
                                                adapterSpinnerGroupNames.notifyDataSetChanged();


                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + Id2 + "/" + yrid + "/Classes/" + Id3 + "/Groups/");
                                                ClassRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        if(dataSnapshot.exists()){


                                                            GroupNames.clear();
                                                            GroupNamesString.clear();
                                                            adapterSpinnerGroupNames.notifyDataSetChanged();

                                                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                                String id = dataSnapshot2.getKey();

                                                                if (!id.equals("Name") ) {

                                                                    String Name = dataSnapshot2.child("Name").getValue().toString();
                                                                    GroupNames.add(new NameIdClass(Name , id));
                                                                    GroupNamesString.add(Name);
                                                                    adapterSpinnerGroupNames.notifyDataSetChanged();

                                                                }
                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });


                                                SubjectNames.clear();
                                                SubjectNamesString.clear();


                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2 + "/" + yrid + "/Classes/" + Id3 + "/Subjects/" ) ;
                                                ClassRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        if(dataSnapshot.exists()){


                                                            SubjectNames.clear();
                                                            SubjectNamesString.clear();
                                                            adapterSpinnerSubjectNames.notifyDataSetChanged();

                                                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                                String id = dataSnapshot2.getKey();

                                                                if (!id.equals("Name") ) {

                                                                    String Name = dataSnapshot2.child("Name").getValue().toString();
                                                                    String Code = dataSnapshot2.child("Code").getValue().toString();

                                                                    SubjectNames.add(new SubjectNameIdCodeClass(Name , id , Code));
                                                                    SubjectNamesString.add(Name);
                                                                    adapterSpinnerSubjectNames.notifyDataSetChanged();

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


        YearName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                int pos = SchName.getSelectedItemPosition();
                final String Id = SchoolNames.get(pos).getId();

                int pos2 = DeptName.getSelectedItemPosition();
                final String Id2 = DeptNames.get(pos2).getId();

                final String Id3 = YearNames.get(position).getId();

                ClassNames.clear();
                ClassNamesString.clear();
                adapterSpinnerClassNames.notifyDataSetChanged();

                GroupNames.clear();
                GroupNamesString.clear();
                adapterSpinnerGroupNames.notifyDataSetChanged();
                SubjectNames.clear();
                SubjectNamesString.clear();


                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + Id2 + "/" + Id3 + "/Classes/");
                ClassRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){

                            ClassNames.clear();
                            ClassNamesString.clear();
                            adapterSpinnerClassNames.notifyDataSetChanged();
                            GroupNames.clear();
                            GroupNamesString.clear();
                            adapterSpinnerGroupNames.notifyDataSetChanged();

                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                String id = dataSnapshot2.getKey();

                                if (!id.equals("Name") ) {

                                    String Name = dataSnapshot2.child("Name").getValue().toString();
                                    ClassNames.add(new NameIdClass(Name , id));
                                    ClassNamesString.add(Name);
                                    adapterSpinnerClassNames.notifyDataSetChanged();

                                }

                            }

                            if(!ClassNames.isEmpty()) {

                                String Id4  = ClassNames.get(0).getId();

                                GroupNames.clear();
                                GroupNamesString.clear();
                                adapterSpinnerGroupNames.notifyDataSetChanged();


                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + Id2 + "/" + Id3 + "/Classes/" + Id4 + "/Groups/");
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){


                                            GroupNames.clear();
                                            GroupNamesString.clear();
                                            adapterSpinnerGroupNames.notifyDataSetChanged();

                                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                String id = dataSnapshot2.getKey();

                                                if (!id.equals("Name") ) {

                                                    String Name = dataSnapshot2.child("Name").getValue().toString();
                                                    GroupNames.add(new NameIdClass(Name , id));
                                                    GroupNamesString.add(Name);
                                                    adapterSpinnerGroupNames.notifyDataSetChanged();

                                                }
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                SubjectNames.clear();
                                SubjectNamesString.clear();


                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2 + "/" + Id3 + "/Classes/" + Id4 + "/Subjects/" ) ;
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){


                                            SubjectNames.clear();
                                            SubjectNamesString.clear();
                                            adapterSpinnerSubjectNames.notifyDataSetChanged();

                                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                String id = dataSnapshot2.getKey();

                                                if (!id.equals("Name") ) {

                                                    String Name = dataSnapshot2.child("Name").getValue().toString();
                                                    String Code = dataSnapshot2.child("Code").getValue().toString();

                                                    SubjectNames.add(new SubjectNameIdCodeClass(Name , id , Code));
                                                    SubjectNamesString.add(Name);
                                                    adapterSpinnerSubjectNames.notifyDataSetChanged();

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





            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ClassName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                int pos1 = SchName.getSelectedItemPosition();
                final String Id = SchoolNames.get(pos1).getId();

                int pos = DeptName.getSelectedItemPosition();
                final String did = DeptNames.get(pos).getId();

                int pos2 = YearName.getSelectedItemPosition();
                final String yrid = YearNames.get(pos2).getId();

                String Id3  = ClassNames.get(position).getId();

                GroupNames.clear();
                GroupNamesString.clear();
                adapterSpinnerGroupNames.notifyDataSetChanged();
                SubjectNames.clear();
                SubjectNamesString.clear();

                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + did + "/" + yrid + "/Classes/" + Id3 + "/Groups/");
                ClassRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){

                            GroupNames.clear();
                            GroupNamesString.clear();
                            adapterSpinnerGroupNames.notifyDataSetChanged();

                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                String id = dataSnapshot2.getKey();

                                if (!id.equals("Name") ) {

                                    String Name = dataSnapshot2.child("Name").getValue().toString();
                                    GroupNames.add(new NameIdClass(Name , id));
                                    GroupNamesString.add(Name);
                                    adapterSpinnerGroupNames.notifyDataSetChanged();

                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                SubjectNames.clear();
                SubjectNamesString.clear();


                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + did + "/" + yrid + "/Classes/" + Id3 + "/Subjects/" ) ;
                ClassRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){


                            SubjectNames.clear();
                            SubjectNamesString.clear();
                            adapterSpinnerSubjectNames.notifyDataSetChanged();

                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                String id = dataSnapshot2.getKey();

                                if (!id.equals("Name") ) {

                                    String Name = dataSnapshot2.child("Name").getValue().toString();
                                    String Code = dataSnapshot2.child("Code").getValue().toString();

                                    SubjectNames.add(new SubjectNameIdCodeClass(Name , id , Code));
                                    SubjectNamesString.add(Name);
                                    adapterSpinnerSubjectNames.notifyDataSetChanged();

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


        SelectSubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mSelectedSubjectNames.clear();
                mSelectedItems.clear();
                AlertDialog.Builder SelectSubjectsAddStudent = new AlertDialog.Builder(Admin_Activity_Add_Student.this);
                SelectSubjectsAddStudent.setCancelable(false);
                // Set the dialog title

                SelectSubjectsAddStudent.setTitle("Choose Classes")
                        // Specify the list array, the items to be selected by default (null for none),
                        // and the listener through which to receive callbacks when items are selected
                        .setMultiChoiceItems(SubjectNamesString.toArray(new String[SubjectNamesString.size()]), null,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        if (isChecked) {
                                            // If the user checked the item, add it to the selected items
                                            if ( !mSelectedItems.contains(SubjectNamesString.get(which)) ) {
                                                mSelectedSubjectNames.add(SubjectNames.get(which));
                                                mSelectedItems.add(SubjectNamesString.get(which));
                                            }
                                        }else if (mSelectedItems.contains(SubjectNamesString.get(which))) {
                                            mSelectedSubjectNames.remove(mSelectedSubjectNames.get(which));
                                            mSelectedItems.remove(SubjectNamesString.get(which));
                                        }
                                    }
                                });

                // Set the action buttons
                SelectSubjectsAddStudent.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        String item = "" ;
                        for (int i = 0 ; i < mSelectedItems.size() ; i++){
                            item = item + mSelectedItems.get(i);
                            if(i != mSelectedItems.size() - 1){
                                item = item + ",\n" ;
                            }
                        }
                        if(item.isEmpty()){
                            ShowSubs.setText("No Subjects Selected");
                        }else {
                            ShowSubs.setText("Subjects Selected :" + item);
                        }
                        dialog.dismiss();
                    }
                });
                SelectSubjectsAddStudent.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                AlertDialog mDialog = SelectSubjectsAddStudent.create();
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();

            }
        });


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                mLoginProgress.setTitle("Creating Account");
                mLoginProgress.setMessage("Please Wait While We Create New Account");
                mLoginProgress.setCanceledOnTouchOutside(false);
                mLoginProgress.show();

                if(mSelectedItems.isEmpty()){

                    Toast.makeText(Admin_Activity_Add_Student.this , "No Subject Selected" , Toast.LENGTH_SHORT).show();
                    mLoginProgress.dismiss();


                }else {

                    final String NameOfUser = NameTxt.getText().toString().toUpperCase().trim();
                    final String PhoneOfUser = NumberTxt.getText().toString().toUpperCase().trim();
                    final String SystemId = SystemIdTxt.getText().toString().trim();
                    final String Email = EmailTxt.getText().toString().trim();
                    final String Pas = PasswordTxt.getText().toString().trim();


                    if (!NameOfUser.isEmpty() && !PhoneOfUser.isEmpty() && !SystemId.isEmpty() && !Email.isEmpty() && !Pas.isEmpty() && PhoneOfUser.length() == 10 ) {



                        mAuth.createUserWithEmailAndPassword(Email,Pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {


                                if(task.isSuccessful()) {

                                    final FirebaseUser useremail = task.getResult().getUser();
                                    final String user_id = task.getResult().getUser().getUid();

                                    useremail.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){

                                                Toast.makeText(Admin_Activity_Add_Student.this, "Email Sent" , Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    });





                                    int pos1 = SchName.getSelectedItemPosition();
                                    final String Id1 = SchoolNames.get(pos1).getId();
                                    final String schname = SchoolNames.get(pos1).getName();

                                    int pos2 = DeptName.getSelectedItemPosition();
                                    final String Id2 = DeptNames.get(pos2).getId();
                                    final String deptname = DeptNames.get(pos1).getName();

                                    int pos3 = YearName.getSelectedItemPosition();
                                    final String Id3 = YearNames.get(pos3).getId();
                                    final String yrname = YearNames.get(pos1).getName();

                                    int pos4 = ClassName.getSelectedItemPosition();
                                    final String Id4 = ClassNames.get(pos4).getId();
                                    final String clsname = ClassNames.get(pos1).getName();

                                    int pos5 = GroupName.getSelectedItemPosition();
                                    final String Id5 = GroupNames.get(pos5).getId();
                                    final String grpname = GroupNames.get(pos5).getName();


                                    Date date = new Date();  // to get the date
                                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss"); // getting date in this format
                                    final String formattedDate = df.format(date.getTime());


                                    ClassRef = FirebaseDatabase.getInstance().getReference("Students/" + Id1 + "/" + Id2 + "/" + Id3 + "/" + Id4 + "/" + Id5 + "/" + formattedDate );
                                    HashMap<String, String> dataMap = new HashMap<String, String>();
                                    dataMap.put("Name", NameOfUser);
                                    dataMap.put("Number", PhoneOfUser);
                                    dataMap.put("SystemId", SystemId);
                                    dataMap.put("Email", Email);
                                    dataMap.put("StdId", formattedDate);
                                    dataMap.put("Uid", user_id);
                                    dataMap.put("School", schname );
                                    dataMap.put("SchoolId", Id1);
                                    dataMap.put("Dept", deptname);
                                    dataMap.put("DeptId", Id2);
                                    dataMap.put("Year", yrname);
                                    dataMap.put("YearId", Id3);
                                    dataMap.put("Class", clsname);
                                    dataMap.put("ClassId", Id4);
                                    dataMap.put("Group", grpname);
                                    dataMap.put("GroupId", Id5);
                                    ClassRef.setValue(dataMap);

                                    ClassRef = FirebaseDatabase.getInstance().getReference("Users/StudentLogin/"+ user_id + "/");
                                    ClassRef.setValue(dataMap);


                                    for (int i = 0; i < mSelectedSubjectNames.size(); i++) {

                                        String subname = mSelectedSubjectNames.get(i).getName();
                                        String subid = mSelectedSubjectNames.get(i).getId();
                                        String subcode = mSelectedSubjectNames.get(i).getCode();
                                        ClassRef2 = FirebaseDatabase.getInstance().getReference("Users/StudentLogin/"+ user_id + "/Subjects/" + subid );
                                        HashMap<String,String> dataMap9 = new HashMap<String, String>();
                                        dataMap9.put("SubName" , subname);
                                        dataMap9.put("SubCode" , subcode);
                                        ClassRef2.setValue(dataMap9);

                                    }


                                    NameTxt.setText("");
                                    SystemIdTxt.setText("");
                                    NumberTxt.setText("");
                                    EmailTxt.setText("");
                                    PasswordTxt.setText("");
                                    mSelectedSubjectNames.clear();
                                    mSelectedItems.clear();
                                    mAuth.signOut();


                                    SharedPreferences userinformation = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
                                    String email = userinformation.getString("EMAIL" ,"");
                                    String pass = userinformation.getString("PASSWORD" , "");

                                    mAuth.signInWithEmailAndPassword(email ,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if(task.isSuccessful()){

                                                Toast.makeText(Admin_Activity_Add_Student.this, "Account Successfully Created" , Toast.LENGTH_SHORT).show();
                                                mLoginProgress.dismiss();


                                                //new SimpleTask(Add_Student_TeacherSch_Activity.this).execute();

                                            }else {

                                                Toast.makeText(Admin_Activity_Add_Student.this, "Account Successfully Created Errorrr" , Toast.LENGTH_SHORT).show();
                                                mLoginProgress.dismiss();

                                            }

                                        }
                                    });

                                }else {


                                    try
                                    {
                                        throw task.getException();
                                    }
                                    // if user enters wrong email.
                                    catch (FirebaseAuthWeakPasswordException weakPassword)
                                    {
                                        Toast.makeText(Admin_Activity_Add_Student.this, "Weak Password" ,
                                                Toast.LENGTH_SHORT).show();
                                        mLoginProgress.dismiss();


                                    }
                                    // if user enters wrong password.
                                    catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                                    {
                                        Toast.makeText(Admin_Activity_Add_Student.this, "Malformed Email",
                                                Toast.LENGTH_SHORT).show();
                                        mLoginProgress.dismiss();



                                    }
                                    catch (FirebaseAuthUserCollisionException existEmail)
                                    {
                                        Toast.makeText(Admin_Activity_Add_Student.this, "Email Already Exist" ,
                                                Toast.LENGTH_SHORT).show();
                                        mLoginProgress.dismiss();


                                    }
                                    catch (Exception e)
                                    {
                                        Toast.makeText(Admin_Activity_Add_Student.this,  e.getMessage() , Toast.LENGTH_SHORT).show();
                                        mLoginProgress.dismiss();

                                    }


                                }



                            }

                        });



                    }
                    else {

                        mLoginProgress.dismiss();
                        Toast.makeText(Admin_Activity_Add_Student.this , "Enter All Details Correctly" , Toast.LENGTH_SHORT ).show();
                    }

                }





            }
        });



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();




    }
}
