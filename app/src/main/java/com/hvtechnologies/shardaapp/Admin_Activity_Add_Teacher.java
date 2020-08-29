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

public class Admin_Activity_Add_Teacher extends AppCompatActivity {


    DatabaseReference ClassRef , SchoolNamesRef , TimingsRef , ClassRef2;

    Button Submit , SelectSubs , ChangeSubs;

    private ArrayList<NameIdClass> SchoolNames = new ArrayList<>() ;
    private ArrayList<NameIdClass> DeptNames = new ArrayList<>() ;
    private ArrayList<NameIdClass> YearNames = new ArrayList<>() ;
    private ArrayList<SubjectNameIdCodeClass> SubjectNames = new ArrayList<>() ;

    private ArrayList<NameIdClass> ClassNames = new ArrayList<>() ;


    private ArrayList<String> SchoolNamesString = new ArrayList<>() ;
    private ArrayList<String> DeptNamesString = new ArrayList<>() ;
    private ArrayList<String> YearNamesString = new ArrayList<>() ;
    private ArrayList<String> ClassNamesString = new ArrayList<>() ;
    private ArrayList<String> SubjectCodesString = new ArrayList<>() ;
    private ArrayList<String> mSelectedSubjectCodesString = new ArrayList<>() ;
    private ArrayList<String> mChangeSubjectCodesString = new ArrayList<>() ;

    private ArrayList<String>  mSelectedItems = new ArrayList<>() ;

    private ArrayAdapter<String> adapterSpinnerSchoolNames ;
    private ArrayAdapter<String> adapterSpinnerDeptNames ;
    private ArrayAdapter<String> adapterSpinnerYearNames ;
    private ArrayAdapter<String> adapterSpinnerSubjectCodes ;
    private ArrayAdapter<String> adapterSpinnerClassNames ;


    private FirebaseAuth mAuth;

    private ProgressDialog mLoginProgress ;


    Spinner SchName , DeptName , YearName ,ClassName , SubjectName;

    private ArrayList<SelectSubjectTeacherClass> mSelectedSubjectNames = new ArrayList<>() ;
    private ArrayList<SelectSubjectTeacherClass> mSubjectNames = new ArrayList<>() ;
    private ArrayList<SelectSubjectTeacherClass> mChangeSubjectNames = new ArrayList<SelectSubjectTeacherClass>() ;




    TextView subsNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin___add__teacher);

        Utils.getDatabase();
        this.setTheme(R.style.AlertDialogCustom);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLoginProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();


        final TextInputEditText NameTxt = (TextInputEditText) findViewById(R.id.TeacherName);
        final TextInputEditText SystemIdTxt = (TextInputEditText) findViewById(R.id.TeacherSysId);
        final TextInputEditText PasswordTxt = (TextInputEditText) findViewById(R.id.TeacherPass);
        final TextInputEditText EmailTxt = (TextInputEditText) findViewById(R.id.TeacherEmail);
        final TextInputEditText NumberTxt = (TextInputEditText) findViewById(R.id.TeacherNumber);
        final TextInputEditText CodeTxt = (TextInputEditText) findViewById(R.id.TeacherCode);



        SchName = (Spinner)findViewById(R.id.SchoolNameAddTeacher);
        DeptName = (Spinner)findViewById(R.id.DeptNameAddTeacher);
        YearName = (Spinner)findViewById(R.id.YearNameAddTeacher);
        ClassName = (Spinner)findViewById(R.id.ClassNameAddTeacher);
        SubjectName = (Spinner)findViewById(R.id.SubjectNameAddTeacher);


        SelectSubs = (Button) findViewById(R.id.SelectSubjectsAddTeacher);
        Submit = (Button) findViewById(R.id.SubmitAddTeacher);
        ChangeSubs = (Button)findViewById(R.id.ChangeSubsAddTeacher);
        subsNames = (TextView)findViewById(R.id.textViewSubsAddTeacher);

        adapterSpinnerSchoolNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , SchoolNamesString);
        adapterSpinnerDeptNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , DeptNamesString);
        adapterSpinnerYearNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , YearNamesString);
        adapterSpinnerSubjectCodes = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , SubjectCodesString);
        adapterSpinnerClassNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , ClassNamesString);


        SchName.setAdapter(adapterSpinnerSchoolNames);
        DeptName.setAdapter(adapterSpinnerDeptNames);
        YearName.setAdapter(adapterSpinnerYearNames);
        ClassName.setAdapter(adapterSpinnerClassNames);
        SubjectName.setAdapter(adapterSpinnerSubjectCodes);


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


                if(!SchoolNames.isEmpty() && !SchoolNamesString.isEmpty()){


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
                    SubjectNames.clear();
                    SubjectCodesString.clear();

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
                                                SubjectNames.clear();
                                                SubjectCodesString.clear();


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

                                                    SubjectNames.clear();
                                                    SubjectCodesString.clear();
                                                    ClassNames.clear();
                                                    ClassNamesString.clear();
                                                    adapterSpinnerClassNames.notifyDataSetChanged();


                                                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + deid + "/" + yrid + "/Classes/");
                                                    ClassRef.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            if(dataSnapshot.exists()){

                                                                SubjectNames.clear();
                                                                SubjectCodesString.clear();

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

                                                                    SubjectNames.clear();
                                                                    SubjectCodesString.clear();

                                                                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + deid + "/" + yrid + "/Classes/" + Id3 + "/Subjects/");
                                                                    ClassRef.addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                                            if(dataSnapshot.exists()){

                                                                                SubjectNames.clear();
                                                                                SubjectNames.clear();
                                                                                SubjectCodesString.clear();

                                                                                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                                                    String id = dataSnapshot2.getKey();

                                                                                    if (!id.equals("Name") ) {

                                                                                        String Name = dataSnapshot2.child("Name").getValue().toString();
                                                                                        String Code = dataSnapshot2.child("Code").getValue().toString();

                                                                                        SubjectNames.add(new SubjectNameIdCodeClass(Name , id , Code));
                                                                                        SubjectCodesString.add(Code);
                                                                                        adapterSpinnerSubjectCodes.notifyDataSetChanged();

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

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {



            }
        });

        DeptName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if(!SchoolNames.isEmpty() && !DeptNames.isEmpty()){


                    int pos = SchName.getSelectedItemPosition();
                    final String Id = SchoolNames.get(pos).getId();

                    final String Id2 = DeptNames.get(position).getId();

                    YearNames.clear();
                    YearNamesString.clear();
                    adapterSpinnerYearNames.notifyDataSetChanged();
                    ClassNames.clear();
                    ClassNamesString.clear();
                    adapterSpinnerClassNames.notifyDataSetChanged();
                    SubjectNames.clear();
                    SubjectCodesString.clear();

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
                                SubjectNames.clear();
                                SubjectCodesString.clear();


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
                                    SubjectNames.clear();
                                    SubjectCodesString.clear();


                                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + Id2 + "/" + yrid + "/Classes/");
                                    ClassRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.exists()){

                                                ClassNames.clear();
                                                ClassNamesString.clear();
                                                adapterSpinnerClassNames.notifyDataSetChanged();
                                                SubjectNames.clear();
                                                SubjectCodesString.clear();
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

                                                    SubjectNames.clear();
                                                    SubjectCodesString.clear();

                                                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + Id2 + "/" + yrid + "/Classes/" + Id3 + "/Subjects/");
                                                    ClassRef.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            if(dataSnapshot.exists()){

                                                                SubjectNames.clear();
                                                                SubjectCodesString.clear();
                                                                adapterSpinnerSubjectCodes.notifyDataSetChanged();

                                                                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                                    String id = dataSnapshot2.getKey();

                                                                    if (!id.equals("Name") ) {

                                                                        String Name = dataSnapshot2.child("Name").getValue().toString();
                                                                        String Code = dataSnapshot2.child("Code").getValue().toString();

                                                                        SubjectNames.add(new SubjectNameIdCodeClass(Name , id , Code));
                                                                        SubjectCodesString.add(Code);
                                                                        adapterSpinnerSubjectCodes.notifyDataSetChanged();

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
            @Override
            public void onNothingSelected(AdapterView<?> parent) {



            }
        });


        YearName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                if(!SchoolNames.isEmpty() && !DeptNames.isEmpty() && !YearNames.isEmpty()){


                    int pos = SchName.getSelectedItemPosition();
                    final String Id = SchoolNames.get(pos).getId();

                    int pos2 = DeptName.getSelectedItemPosition();
                    final String Id2 = DeptNames.get(pos2).getId();

                    final String Id3 = YearNames.get(position).getId();

                    ClassNames.clear();
                    ClassNamesString.clear();
                    adapterSpinnerClassNames.notifyDataSetChanged();

                    SubjectNames.clear();
                    SubjectCodesString.clear();


                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + Id2 + "/" + Id3 + "/Classes/");
                    ClassRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){

                                ClassNames.clear();
                                ClassNamesString.clear();
                                adapterSpinnerClassNames.notifyDataSetChanged();

                                SubjectNames.clear();
                                SubjectCodesString.clear();

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

                                    SubjectNames.clear();
                                    SubjectCodesString.clear();


                                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + Id2 + "/" + Id3 + "/Classes/" + Id4 + "/Subjects/");
                                    ClassRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.exists()){

                                                SubjectNames.clear();
                                                SubjectCodesString.clear();

                                                adapterSpinnerSubjectCodes.notifyDataSetChanged();

                                                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                    String id = dataSnapshot2.getKey();

                                                    if (!id.equals("Name") ) {

                                                        String Name = dataSnapshot2.child("Name").getValue().toString();
                                                        String Code = dataSnapshot2.child("Code").getValue().toString();

                                                        SubjectNames.add(new SubjectNameIdCodeClass(Name , id , Code));
                                                        SubjectCodesString.add(Code);
                                                        adapterSpinnerSubjectCodes.notifyDataSetChanged();

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
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ClassName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if(!SchoolNames.isEmpty() && !DeptNames.isEmpty() && !YearNames.isEmpty() && !ClassNames.isEmpty()){




                    int pos1 = SchName.getSelectedItemPosition();
                    final String Id = SchoolNames.get(pos1).getId();

                    int pos = DeptName.getSelectedItemPosition();
                    final String did = DeptNames.get(pos).getId();

                    int pos2 = YearName.getSelectedItemPosition();
                    final String yrid = YearNames.get(pos2).getId();

                    String Id3  = ClassNames.get(position).getId();

                    SubjectNames.clear();
                    SubjectCodesString.clear();


                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + did + "/" + yrid + "/Classes/" + Id3 + "/Subjects/");
                    ClassRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){

                                SubjectNames.clear();
                                SubjectCodesString.clear();

                                adapterSpinnerSubjectCodes.notifyDataSetChanged();

                                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                    String id = dataSnapshot2.getKey();

                                    if (!id.equals("Name") ) {

                                        String Name = dataSnapshot2.child("Name").getValue().toString();
                                        String Code = dataSnapshot2.child("Code").getValue().toString();

                                        SubjectNames.add(new SubjectNameIdCodeClass(Name , id , Code));
                                        SubjectCodesString.add(Code);
                                        adapterSpinnerSubjectCodes.notifyDataSetChanged();

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
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        SelectSubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!SchoolNames.isEmpty() && !DeptNames.isEmpty() && !YearNames.isEmpty() &&  !ClassNames.isEmpty() && !SubjectNames.isEmpty()  ){



                    int pos1 = SchName.getSelectedItemPosition();
                    final String Id = SchoolNames.get(pos1).getId();

                    int pos2 = DeptName.getSelectedItemPosition();
                    final String Id2 = DeptNames.get(pos2).getId();

                    int pos3 = YearName.getSelectedItemPosition();
                    final String Id3 = YearNames.get(pos3).getId();

                    int pos4 = ClassName.getSelectedItemPosition();
                    final String Id4 = ClassNames.get(pos4).getId();
                    final String clsnm = ClassNames.get(pos4).getName();

                    int pos5 = SubjectName.getSelectedItemPosition();
                    final String Id5 = SubjectNames.get(pos5).getId();
                    final String subnm = SubjectNames.get(pos5).getName();
                    final String Code = SubjectNames.get(pos5).getCode();

                    if(!mSelectedSubjectCodesString.contains(clsnm + " - " + Code)){

                        mSelectedItems.add(clsnm + " - " + Code);
                        mSelectedSubjectCodesString.add(clsnm + " - " + Code);
                        mSelectedSubjectNames.add(new SelectSubjectTeacherClass( Id , Id2 , Id3 , Id4 , clsnm , Id5 , subnm , Code ));
                        mSubjectNames.add(new SelectSubjectTeacherClass( Id , Id2 , Id3 , Id4 , clsnm , Id5 , subnm , Code ));
                        mChangeSubjectNames.add(new SelectSubjectTeacherClass( Id , Id2 , Id3 , Id4 , clsnm , Id5 , subnm , Code ));
                        mChangeSubjectCodesString.add(clsnm + " - " + Code);


                        String item = "" ;
                        for (int i = 0 ; i < mSelectedSubjectNames.size() ; i++){
                            item = item +  mChangeSubjectNames.get(i).getClassName() + " - " + mSelectedSubjectNames.get(i).getSubjectCode();
                            if(i != mSelectedSubjectNames.size() - 1){
                                item = item + ",\n" ;
                            }
                        }
                        if(item.isEmpty()){

                            subsNames.setText("No Subjects Selected");

                        }else {

                            subsNames.setText("Subjects Selected :\n" + item);

                        }




                    }else {


                        Toast.makeText(Admin_Activity_Add_Teacher.this , "Already Conatins This Subject" , Toast.LENGTH_SHORT).show();

                    }



                }


            }
        });



        ChangeSubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mChangeSubjectCodesString.clear();
                mChangeSubjectNames.clear();

                AlertDialog.Builder SelectSubjectsAddStudent = new AlertDialog.Builder(Admin_Activity_Add_Teacher.this);
                SelectSubjectsAddStudent.setCancelable(false);

                SelectSubjectsAddStudent.setTitle("Choose Subjects")
                        // Specify the list array, the items to be selected by default (null for none),
                        // and the listener through which to receive callbacks when items are selected
                        .setMultiChoiceItems(mSelectedSubjectCodesString.toArray(new String[mSelectedSubjectCodesString.size()]), null,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        if (isChecked) {
                                            // If the user checked the item, add it to the selected items
                                            if ( !mChangeSubjectCodesString.contains(mSelectedItems.get(which)) ) {

                                                mChangeSubjectCodesString.add(mSelectedItems.get(which));
                                                mChangeSubjectNames.add(mSelectedSubjectNames.get(which));
                                            }
                                        }else if (mSelectedItems.contains(mSelectedItems.get(which))) {

                                            mChangeSubjectCodesString.remove(mSelectedItems.get(which));
                                            mChangeSubjectNames.remove(mSelectedSubjectNames.get(which));

                                        }
                                    }
                                });

                // Set the action buttons
                SelectSubjectsAddStudent.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog

                        mSelectedItems.clear();
                        mSelectedSubjectCodesString.clear();
                        mSelectedSubjectNames.clear();
                        mSubjectNames.clear();
                        //mChangeSubjectNames.;
                        //mChangeSubjectCodesString.add(Code);



                        String item = "" ;

                        for (int i = 0 ; i < mChangeSubjectCodesString.size() ; i++){

                            mSelectedItems.add(mChangeSubjectCodesString.get(i));
                            mSelectedSubjectCodesString.add(mChangeSubjectCodesString.get(i));
                            mSubjectNames.add(mChangeSubjectNames.get(i));
                            mSelectedSubjectNames.add(mChangeSubjectNames.get(i));


                            item = item + mChangeSubjectCodesString.get(i);
                            if(i != mChangeSubjectCodesString.size() - 1){
                                item = item + ",\n" ;
                            }

                        }
                        if(item.isEmpty()){

                            subsNames.setText("No Subjects Selected");

                        }else {

                            subsNames.setText("Subjects Selected :" + item);

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

                    Toast.makeText(Admin_Activity_Add_Teacher.this , "No Subject Selected" , Toast.LENGTH_SHORT).show();
                    mLoginProgress.dismiss();


                }else {

                    final String NameOfUser = NameTxt.getText().toString().toUpperCase().trim();
                    final String Code = CodeTxt.getText().toString().trim();
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


                                    /*

                                    useremail.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){

                                                Toast.makeText(Admin_Activity_Add_Teacher.this, "Email Sent" , Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    });

                                    */

                                    Date date = new Date();  // to get the date
                                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss"); // getting date in this format
                                    final String formattedDate = df.format(date.getTime());


                                    ClassRef = FirebaseDatabase.getInstance().getReference("Teachers/" + formattedDate  );
                                    HashMap<String, String> dataMap = new HashMap<String, String>();
                                    dataMap.put("Name", NameOfUser);
                                    dataMap.put("Code", Code);
                                    dataMap.put("Number", PhoneOfUser);
                                    dataMap.put("SystemId", SystemId);
                                    dataMap.put("Email", Email);
                                    dataMap.put("Uid", user_id);
                                    dataMap.put("TeacherId", formattedDate);
                                    ClassRef.setValue(dataMap);


                                    ClassRef = FirebaseDatabase.getInstance().getReference("Users/TeacherLogin/"+ user_id + "/");
                                    ClassRef.setValue(dataMap);


                                    for (int i = 0; i < mChangeSubjectNames.size(); i++) {

                                        String subname = mChangeSubjectNames.get(i).getSubjectName();
                                        String subid = mChangeSubjectNames.get(i).getSubjectId();
                                        String subcode = mChangeSubjectNames.get(i).getSubjectCode();
                                        String clsId = mChangeSubjectNames.get(i).getClassId();
                                        String clsName = mChangeSubjectNames.get(i).getClassName();
                                        String schid = mChangeSubjectNames.get(i).getSchId();
                                        String deptid = mChangeSubjectNames.get(i).getDeptId();
                                        String yrid = mChangeSubjectNames.get(i).getYearId();
                                        int pos1 = SchName.getSelectedItemPosition();
                                        final String SchName = SchoolNames.get(pos1).getName();
                                        int pos2 = DeptName.getSelectedItemPosition();
                                        final String DeptName = DeptNames.get(pos2).getName();

                                        int pos3 = YearName.getSelectedItemPosition();
                                        final String YrName = YearNames.get(pos3).getName();


                                        ClassRef2 = FirebaseDatabase.getInstance().getReference("Users/TeacherLogin/"+ user_id + "/Subjects/" + subid );
                                        HashMap<String,String> dataMap9 = new HashMap<String, String>();
                                        dataMap9.put("SubName" , subname);
                                        dataMap9.put("SubCode" , subcode);
                                        dataMap9.put("SubId" , subid);
                                        dataMap9.put("ClassId" , clsId);
                                        dataMap9.put("ClassName" , clsName);
                                        dataMap9.put("SchoolId" , schid);
                                        dataMap9.put("SchoolName" , SchName);
                                        dataMap9.put("DeptId" , deptid);
                                        dataMap9.put("DeptName" , DeptName);
                                        dataMap9.put("YearId" , yrid);
                                        dataMap9.put("YearName" , YrName);
                                        ClassRef2.setValue(dataMap9);


                                    }

                                    NameTxt.setText("");
                                    SystemIdTxt.setText("");
                                    NumberTxt.setText("");
                                    EmailTxt.setText("");
                                    PasswordTxt.setText("");
                                    CodeTxt.setText("");
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

                                                Toast.makeText(Admin_Activity_Add_Teacher.this, "Account Successfully Created" , Toast.LENGTH_SHORT).show();
                                                mLoginProgress.dismiss();


                                            }else {

                                                Toast.makeText(Admin_Activity_Add_Teacher.this, "Account Successfully Created Errorrr" , Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(Admin_Activity_Add_Teacher.this, "Weak Password" ,
                                                Toast.LENGTH_SHORT).show();
                                        mLoginProgress.dismiss();


                                    }
                                    // if user enters wrong password.
                                    catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                                    {
                                        Toast.makeText(Admin_Activity_Add_Teacher.this, "Malformed Email",
                                                Toast.LENGTH_SHORT).show();
                                        mLoginProgress.dismiss();



                                    }
                                    catch (FirebaseAuthUserCollisionException existEmail)
                                    {
                                        Toast.makeText(Admin_Activity_Add_Teacher.this, "Email Already Exist" ,
                                                Toast.LENGTH_SHORT).show();
                                        mLoginProgress.dismiss();


                                    }
                                    catch (Exception e)
                                    {
                                        Toast.makeText(Admin_Activity_Add_Teacher.this,  e.getMessage() , Toast.LENGTH_SHORT).show();
                                        mLoginProgress.dismiss();

                                    }


                                }



                            }

                        });



                    }
                    else {

                        mLoginProgress.dismiss();
                        Toast.makeText(Admin_Activity_Add_Teacher.this , "Enter All Details Correctly" , Toast.LENGTH_SHORT ).show();
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
