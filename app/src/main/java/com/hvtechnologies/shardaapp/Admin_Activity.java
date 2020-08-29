package com.hvtechnologies.shardaapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

public class Admin_Activity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    DrawerLayout myDrawer;
    ActionBarDrawerToggle myTog ;
    NavigationView navigationView;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoginProgress;
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    MenuItem prevMenuItem;
    Admin_Home admin_home;
    Admin_Attendance admin_attendance ;
    Admin_Marksheet admin_marksheet ;
    Admin_Notice admin_notice ;
    Admin_Assignment admin_assignment ;


    DatabaseReference ClassRef , SchoolNamesRef , TimingsRef , ClassRef2;


    private ArrayList<NameIdClass> SchoolNames = new ArrayList<>() ;
    private ArrayList<NameIdClass> DeptNames = new ArrayList<>() ;
    private ArrayList<NameIdClass> YearNames = new ArrayList<>() ;
    private ArrayList<NameIdClass> SubjectNames = new ArrayList<>() ;
    private ArrayList<NameIdClass> ClassNames = new ArrayList<>() ;
    private ArrayList<NameIdClass> GroupNames = new ArrayList<>() ;
    private ArrayList<NameIdClass> mSelectedClassNames = new ArrayList<>() ;
    private ArrayList<NameIdClass> DaysNames = new ArrayList<>() ;
    private ArrayList<NameIdClass> TimeNames = new ArrayList<>() ;
    private ArrayList<NameIdClass> PeriodNames = new ArrayList<>() ;

    private ArrayList<String> SchoolNamesString = new ArrayList<>() ;
    private ArrayList<String> DeptNamesString = new ArrayList<>() ;
    private ArrayList<String> YearNamesString = new ArrayList<>() ;
    private ArrayList<String> SubjectNamesString = new ArrayList<>() ;
    private ArrayList<String> ClassNamesString = new ArrayList<>() ;
    private ArrayList<String> GroupNamesString = new ArrayList<>() ;
    private ArrayList<String> EditTimeString = new ArrayList<>() ;
    private ArrayList<String> DaysNamesString = new ArrayList<>() ;
    private ArrayList<String> TimeNamesString = new ArrayList<>() ;
    private ArrayList<String> PeriodNamesString = new ArrayList<>() ;


    private ArrayAdapter<String> adapterSpinnerSchoolNames ;
    private ArrayAdapter<String> adapterSpinnerDeptNames ;
    private ArrayAdapter<String> adapterSpinnerYearNames ;
    private ArrayAdapter<String> adapterSpinnerSubjectNames ;
    private ArrayAdapter<String> adapterSpinnerClassNames ;
    private ArrayAdapter<String> adapterSpinnerGroupNames ;
    private ArrayAdapter<String> adapterSpinnerEditTime ;
    private ArrayAdapter<String> adapterSpinnerDaysNames ;
    private ArrayAdapter<String> adapterSpinnerTimingsNames ;
    private ArrayAdapter<String> adapterSpinnerPeriodNames ;



    ArrayList  mSelectedItems = new ArrayList();  // Where we track the selected items
    private ArrayList<String> BatchNames = new ArrayList<>() ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_);

        Utils.getDatabase();
        this.setTheme(R.style.AlertDialogCustom);


        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
        }
        else{
            Toast.makeText(Admin_Activity.this , "Please Check Your Internet Connection" , Toast.LENGTH_LONG).show();
        }

        adapterSpinnerSchoolNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , SchoolNamesString);
        adapterSpinnerDeptNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , DeptNamesString);
        adapterSpinnerYearNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , YearNamesString);
        adapterSpinnerSubjectNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , SubjectNamesString);
        adapterSpinnerClassNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , ClassNamesString);
        adapterSpinnerEditTime = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , EditTimeString);
        adapterSpinnerGroupNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , GroupNamesString);
        adapterSpinnerDaysNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , DaysNamesString);
        adapterSpinnerTimingsNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , TimeNamesString);
        adapterSpinnerPeriodNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , PeriodNamesString);


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


        SchoolNamesRef = FirebaseDatabase.getInstance().getReference("Days/") ;
        SchoolNamesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DaysNames.clear();
                DaysNamesString.clear();
                adapterSpinnerDaysNames.notifyDataSetChanged();

                if(dataSnapshot.exists()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        String id = dataSnapshot1.getKey();
                        String Name = dataSnapshot1.child("Day").getValue().toString();
                        DaysNames.add(new NameIdClass(Name , id));
                        DaysNamesString.add(Name);
                        adapterSpinnerDaysNames.notifyDataSetChanged();

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        TimingsRef = FirebaseDatabase.getInstance().getReference("Timings/") ;
        TimingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                TimeNames.clear();
                TimeNamesString.clear();
                adapterSpinnerTimingsNames.notifyDataSetChanged();

                if(dataSnapshot.exists()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        String id = dataSnapshot1.getKey();
                        String Start = dataSnapshot1.child("Start").getValue().toString();
                        String End = dataSnapshot1.child("End").getValue().toString();
                        TimeNames.add(new NameIdClass(Start + " - " + End , id));
                        TimeNamesString.add(Start + " - " + End);
                        adapterSpinnerTimingsNames.notifyDataSetChanged();

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomnavadmin);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        viewPager = (ViewPager) findViewById(R.id.containerAdmin);
        //viewPager.setCurrentItem(2,true);

        mAuth = FirebaseAuth.getInstance();
        mLoginProgress = new ProgressDialog(this);

        myDrawer = (DrawerLayout)findViewById(R.id.myDraw);
        myTog = new ActionBarDrawerToggle(this , myDrawer , R.string.open , R.string.close);
        myDrawer.addDrawerListener(myTog);
        myTog.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView)findViewById(R.id.NavAdmin);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.ExportData:

                        Intent mainIntentExpData = new Intent(Admin_Activity.this, Export_Data.class);
                        startActivity(mainIntentExpData);
                        overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);


                    case R.id.AddMaster:
                        //Do
                        return true;

                    case R.id.EditMaster:
                        //Do
                        break;



                    case R.id.AddDean:

                        Intent mainIntentAddDean = new Intent(Admin_Activity.this, Admin_Activity_Add_Dean.class);
                        startActivity(mainIntentAddDean);
                        overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

                        //Do

                        return true;

                    case R.id.EditDean:
                        //Do
                        break;




                    case R.id.AddTeacher:

                        Intent mainIntentAddTeacher = new Intent(Admin_Activity.this, Admin_Activity_Add_Teacher.class);
                        startActivity(mainIntentAddTeacher);
                        overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

                        //Do
                        break;

                    case R.id.EditTeacher:
                        //Do
                        break;

                    case R.id.AddStudent:

                        Intent mainIntentAddStudent = new Intent(Admin_Activity.this, Admin_Activity_Add_Student.class);
                        startActivity(mainIntentAddStudent);
                        overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

                        //Do
                        break;

                    case R.id.EditStudent:


                        //Do
                        break;

                    case R.id.AddSchool:

                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Admin_Activity.this)
                                .setCancelable(false);
                        View mView = getLayoutInflater().inflate(R.layout.dialog_box_add_schools, null);
                        final TextInputEditText NameOfClass = (TextInputEditText) mView.findViewById(R.id.ClassNameCreateClass);
                        final Button canc1 = (Button) mView.findViewById(R.id.button19);
                        final Button ok1 = (Button) mView.findViewById(R.id.button20);


                        mBuilder.setView(mView);
                        final AlertDialog dialog = mBuilder.create();
                        dialog.show();


                        ok1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if( !NameOfClass.getText().toString().isEmpty()){

                                    Date date = new Date();  // to get the date
                                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss"); // getting date in this format
                                    final String formattedDate = df.format(date.getTime());
                                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + formattedDate ) ;
                                    HashMap<String,String> dataMap = new HashMap<String, String>();
                                    dataMap.put("Name" , NameOfClass.getText().toString().trim().toUpperCase());
                                    ClassRef.setValue(dataMap);
                                    Toast.makeText(Admin_Activity.this , "School : " + NameOfClass.getText().toString() + " Added" , Toast.LENGTH_SHORT).show();

                                }else{

                                    Toast.makeText(Admin_Activity.this , "No School Added" , Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                        canc1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();

                            }
                        });

                        //Do
                        break;



                    case R.id.DeleteSchool:


                        final AlertDialog.Builder mremovschBuilder = new AlertDialog.Builder(Admin_Activity.this)
                                .setCancelable(false);
                        View mremovschView = getLayoutInflater().inflate(R.layout.dialog_box_delete_school, null);
                        final Spinner RemovSchName = (Spinner)mremovschView.findViewById(R.id.SchoolNameCreateDept);
                        final Button canc5 = (Button) mremovschView.findViewById(R.id.button19);
                        final Button ok5 = (Button) mremovschView.findViewById(R.id.button20);


                        RemovSchName.setAdapter(adapterSpinnerSchoolNames);

                        mremovschBuilder.setView(mremovschView);
                        final AlertDialog removschdialog = mremovschBuilder.create();
                        removschdialog.show();

                        ok5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if( !SchoolNames.isEmpty()){

                                    int number = RemovSchName.getSelectedItemPosition();
                                    String Id = SchoolNames.get(number).getId();
                                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id ) ;
                                    ClassRef.removeValue();
                                    Toast.makeText(Admin_Activity.this , "School Removed" , Toast.LENGTH_SHORT).show();

                                }else{

                                    Toast.makeText(Admin_Activity.this , "No Dept Added" , Toast.LENGTH_SHORT).show();
                                }


                            }
                        });


                        canc5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                removschdialog.dismiss();

                            }
                        });

                        break;

                    case R.id.AddDepartment:

                        final AlertDialog.Builder mdeptBuilder = new AlertDialog.Builder(Admin_Activity.this)
                                .setCancelable(false);
                        View mdeptView = getLayoutInflater().inflate(R.layout.dialog_box_add_departments, null);
                        final TextInputEditText DeptName = (TextInputEditText) mdeptView.findViewById(R.id.Department);
                        final Spinner SchName = (Spinner)mdeptView.findViewById(R.id.SchoolNameCreateDept);
                        final Button canc2 = (Button) mdeptView.findViewById(R.id.button19);
                        final Button ok2 = (Button) mdeptView.findViewById(R.id.button20);


                        SchName.setAdapter(adapterSpinnerSchoolNames);

                        mdeptBuilder.setView(mdeptView);
                        final AlertDialog deptdialog = mdeptBuilder.create();
                        deptdialog.show();

                        ok2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if( !DeptName.getText().toString().isEmpty()){

                                    int number = SchName.getSelectedItemPosition();
                                    String Id = SchoolNames.get(number).getId();
                                    Date date = new Date();  // to get the date
                                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss"); // getting date in this format
                                    final String formattedDate = df.format(date.getTime());
                                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + formattedDate  ) ;
                                    HashMap<String,String> dataMap = new HashMap<String, String>();
                                    dataMap.put("Name" , DeptName.getText().toString().trim().toUpperCase());
                                    ClassRef.setValue(dataMap);
                                    Toast.makeText(Admin_Activity.this , "Dept : " + DeptName.getText().toString() + " Added" , Toast.LENGTH_SHORT).show();

                                }else{

                                    Toast.makeText(Admin_Activity.this , "No Dept Added" , Toast.LENGTH_SHORT).show();
                                }


                            }
                        });


                        canc2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                deptdialog.dismiss();

                            }
                        });

                        //Do
                        break;

                    case R.id.DeleteDepartment:

                        final AlertDialog.Builder deldepBuilder = new AlertDialog.Builder(Admin_Activity.this)
                                .setCancelable(false);
                        View deldepView = getLayoutInflater().inflate(R.layout.dialog_box_delete_departments, null);
                        final Spinner SchNameDeptDel = (Spinner)deldepView.findViewById(R.id.SchoolNameAddYear);
                        final Spinner DeptNameDeptDel = (Spinner)deldepView.findViewById(R.id.DeptNameAddYear);
                        final Button canc6 = (Button) deldepView.findViewById(R.id.button19);
                        final Button ok6 = (Button) deldepView.findViewById(R.id.button20);

                        SchNameDeptDel.setAdapter(adapterSpinnerSchoolNames);
                        DeptNameDeptDel.setAdapter(adapterSpinnerDeptNames);

                        deldepBuilder.setView(deldepView);
                        final AlertDialog DeptDeldialog = deldepBuilder.create();
                        DeptDeldialog.show();


                        canc6.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                DeptDeldialog.dismiss();
                            }
                        });

                        ok6.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if( !DeptNamesString.isEmpty()){

                                    int number = SchNameDeptDel.getSelectedItemPosition();
                                    String Id = SchoolNames.get(number).getId();
                                    int number2 = DeptNameDeptDel.getSelectedItemPosition();
                                    String Id2 = DeptNames.get(number2).getId();
                                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2  ) ;
                                    ClassRef.removeValue();
                                    Toast.makeText(Admin_Activity.this , "Department Deleted" , Toast.LENGTH_SHORT).show();

                                }else{

                                    Toast.makeText(Admin_Activity.this , "No Department Deleted" , Toast.LENGTH_LONG).show();
                                }


                            }
                        });

                        SchNameDeptDel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                int pos = SchNameDeptDel.getSelectedItemPosition();
                                String Id = SchoolNames.get(pos).getId();
                                DeptNames.clear();
                                DeptNamesString.clear();
                                adapterSpinnerSchoolNames.notifyDataSetChanged();
                                adapterSpinnerDeptNames.notifyDataSetChanged();

                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/") ;
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

                                            DeptNames.clear();
                                            DeptNamesString.clear();
                                            adapterSpinnerSchoolNames.notifyDataSetChanged();
                                            adapterSpinnerDeptNames.notifyDataSetChanged();


                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                                String Id = dataSnapshot1.getKey();

                                                if(!Id.equals("Name")){

                                                    String Name = dataSnapshot1.child("Name").getValue().toString();
                                                    DeptNames.add(new NameIdClass(Name , Id));
                                                    DeptNamesString.add(Name);
                                                    adapterSpinnerDeptNames.notifyDataSetChanged();
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






                        //Do
                        break;


                    case R.id.AddYears:

                        final AlertDialog.Builder myearBuilder = new AlertDialog.Builder(Admin_Activity.this)
                                .setCancelable(false);
                        View myearView = getLayoutInflater().inflate(R.layout.dialog_box_add_year, null);
                        final TextInputEditText YearName = (TextInputEditText) myearView.findViewById(R.id.Year);
                        final Spinner SchNameYear = (Spinner)myearView.findViewById(R.id.SchoolNameAddYear);
                        final Spinner DeptNameYear = (Spinner)myearView.findViewById(R.id.DeptNameAddYear);
                        final Button canc3 = (Button) myearView.findViewById(R.id.button19);
                        final Button ok3 = (Button) myearView.findViewById(R.id.button20);

                        SchNameYear.setAdapter(adapterSpinnerSchoolNames);
                        DeptNameYear.setAdapter(adapterSpinnerDeptNames);

                        myearBuilder.setView(myearView);
                        final AlertDialog yeardialog = myearBuilder.create();
                        yeardialog.show();


                        canc3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                yeardialog.dismiss();
                            }
                        });

                        ok3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if( !YearName.getText().toString().isEmpty() && !DeptNames.isEmpty() && !SchoolNames.isEmpty()){

                                    int number = SchNameYear.getSelectedItemPosition();
                                    String Id = SchoolNames.get(number).getId();
                                    int number2 = DeptNameYear.getSelectedItemPosition();
                                    String Id2 = DeptNames.get(number2).getId();

                                    Date date = new Date();  // to get the date
                                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss"); // getting date in this format
                                    final String formattedDate = df.format(date.getTime());
                                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2 + "/" +formattedDate  ) ;
                                    HashMap<String,String> dataMap = new HashMap<String, String>();
                                    dataMap.put("Name" , YearName.getText().toString().trim().toUpperCase());
                                    ClassRef.setValue(dataMap);
                                    Toast.makeText(Admin_Activity.this , "Year : " + YearName.getText().toString() + " Added" , Toast.LENGTH_SHORT).show();

                                }else{

                                    Toast.makeText(Admin_Activity.this , "No Year Added" , Toast.LENGTH_SHORT).show();
                                }


                            }
                        });

                        SchNameYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                int pos = SchNameYear.getSelectedItemPosition();
                                String Id = SchoolNames.get(pos).getId();
                                DeptNames.clear();
                                DeptNamesString.clear();
                                adapterSpinnerSchoolNames.notifyDataSetChanged();
                                adapterSpinnerDeptNames.notifyDataSetChanged();

                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/") ;
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

                                            DeptNames.clear();
                                            DeptNamesString.clear();
                                            adapterSpinnerSchoolNames.notifyDataSetChanged();
                                            adapterSpinnerDeptNames.notifyDataSetChanged();


                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                                String Id = dataSnapshot1.getKey();

                                                if(!Id.equals("Name")){

                                                    String Name = dataSnapshot1.child("Name").getValue().toString();
                                                    DeptNames.add(new NameIdClass(Name , Id));
                                                    DeptNamesString.add(Name);
                                                    adapterSpinnerDeptNames.notifyDataSetChanged();
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

                        //Do
                        break;




                    case R.id.DeleteYear:

                        final AlertDialog.Builder mDelYrBuilder = new AlertDialog.Builder(Admin_Activity.this)
                                .setCancelable(false);
                        View mDelYrView = getLayoutInflater().inflate(R.layout.dialog_box_delete_year, null);
                        final Spinner SchNameDelYr = (Spinner)mDelYrView.findViewById(R.id.SchoolNameAddSubject);
                        final Spinner DeptNameDelYr = (Spinner)mDelYrView.findViewById(R.id.DeptNameAddSubject);
                        final Spinner YearNameDelYr = (Spinner)mDelYrView.findViewById(R.id.YearNameAddSubject);
                        final Button cancDelYr = (Button) mDelYrView.findViewById(R.id.button19);
                        final Button okDelYr = (Button) mDelYrView.findViewById(R.id.button20);

                        DeptNames.clear();
                        DeptNamesString.clear();
                        YearNames.clear();
                        YearNamesString.clear();
                        adapterSpinnerYearNames.notifyDataSetChanged();
                        adapterSpinnerSchoolNames.notifyDataSetChanged();
                        adapterSpinnerDeptNames.notifyDataSetChanged();

                        SchNameDelYr.setAdapter(adapterSpinnerSchoolNames);
                        DeptNameDelYr.setAdapter(adapterSpinnerDeptNames);
                        YearNameDelYr.setAdapter(adapterSpinnerYearNames);

                        mDelYrBuilder.setView(mDelYrView);
                        final AlertDialog DelYrdialog = mDelYrBuilder.create();
                        DelYrdialog.show();



                        SchNameDelYr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                int pos = SchNameDelYr.getSelectedItemPosition();
                                final String Id = SchoolNames.get(pos).getId();

                                DeptNames.clear();
                                DeptNamesString.clear();
                                YearNames.clear();
                                YearNamesString.clear();
                                adapterSpinnerYearNames.notifyDataSetChanged();
                                adapterSpinnerSchoolNames.notifyDataSetChanged();
                                adapterSpinnerDeptNames.notifyDataSetChanged();

                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/") ;
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

                                            DeptNames.clear();
                                            DeptNamesString.clear();
                                            YearNames.clear();
                                            YearNamesString.clear();
                                            adapterSpinnerYearNames.notifyDataSetChanged();
                                            adapterSpinnerSchoolNames.notifyDataSetChanged();
                                            adapterSpinnerDeptNames.notifyDataSetChanged();


                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                                String id = dataSnapshot1.getKey();

                                                if(!id.equals("Name")){

                                                    String Name = dataSnapshot1.child("Name").getValue().toString();
                                                    DeptNames.add(new NameIdClass(Name , id));
                                                    DeptNamesString.add(Name);
                                                    adapterSpinnerDeptNames.notifyDataSetChanged();
                                                }
                                            }
                                            if(!DeptNames.isEmpty() && !DeptNamesString.isEmpty()) {


                                                YearNames.clear();
                                                YearNamesString.clear();
                                                ClassNames.clear();
                                                ClassNamesString.clear();
                                                adapterSpinnerYearNames.notifyDataSetChanged();
                                                adapterSpinnerSchoolNames.notifyDataSetChanged();
                                                adapterSpinnerDeptNames.notifyDataSetChanged();
                                                adapterSpinnerClassNames.notifyDataSetChanged();

                                                //int pos = DeptNameDelYr.getSelectedItemPosition();
                                                String yrid = DeptNames.get(0).getId();

                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + yrid + "/");
                                                ClassRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        if(dataSnapshot.exists()){

                                                            YearNames.clear();
                                                            YearNamesString.clear();
                                                            ClassNames.clear();
                                                            ClassNamesString.clear();
                                                            adapterSpinnerYearNames.notifyDataSetChanged();
                                                            adapterSpinnerSchoolNames.notifyDataSetChanged();
                                                            adapterSpinnerDeptNames.notifyDataSetChanged();
                                                            adapterSpinnerClassNames.notifyDataSetChanged();


                                                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                                String id = dataSnapshot2.getKey();

                                                                if (!id.equals("Name")) {

                                                                    String Name = dataSnapshot2.child("Name").getValue().toString();
                                                                    YearNames.add(new NameIdClass(Name , id));
                                                                    YearNamesString.add(Name);
                                                                    adapterSpinnerYearNames.notifyDataSetChanged();
                                                                }
                                                            }
                                                            if(!YearNames.isEmpty() && !YearNamesString.isEmpty()) {

                                                                int pos = DeptNameDelYr.getSelectedItemPosition();
                                                                final String did = DeptNames.get(pos).getId();
                                                                //int pos2 = YearNameDelYr.getSelectedItemPosition();
                                                                String yrid  = YearNames.get(0).getId();

                                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + did + "/" + yrid + "/Classes/");
                                                                ClassRef.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                                        if(dataSnapshot.exists()){

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

                        DeptNameDelYr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                int pos = SchNameDelYr.getSelectedItemPosition();
                                final String Id = SchoolNames.get(pos).getId();

                                int pos2 = DeptNameDelYr.getSelectedItemPosition();
                                final String Id2 = DeptNames.get(pos2).getId();

                                YearNames.clear();
                                YearNamesString.clear();

                                adapterSpinnerYearNames.notifyDataSetChanged();
                                adapterSpinnerSchoolNames.notifyDataSetChanged();
                                adapterSpinnerDeptNames.notifyDataSetChanged();


                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2 + "/") ;
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

                                            YearNames.clear();
                                            YearNamesString.clear();
                                            adapterSpinnerYearNames.notifyDataSetChanged();
                                            adapterSpinnerSchoolNames.notifyDataSetChanged();
                                            adapterSpinnerDeptNames.notifyDataSetChanged();


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

                                                int pos = DeptNameDelYr.getSelectedItemPosition();
                                                final String did = DeptNames.get(pos).getId();
                                                String yrid  = YearNames.get(0).getId();
                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + did + "/" + yrid + "/Classes/");
                                                ClassRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        if(dataSnapshot.exists()){


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

                        YearNameDelYr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                int pos = SchNameDelYr.getSelectedItemPosition();
                                final String Id = SchoolNames.get(pos).getId();

                                int pos2 = DeptNameDelYr.getSelectedItemPosition();
                                final String Id2 = DeptNames.get(pos2).getId();

                                int pos3 = YearNameDelYr.getSelectedItemPosition();
                                final String Id3 = YearNames.get(pos3).getId();

                                ClassNames.clear();
                                ClassNamesString.clear();
                                adapterSpinnerClassNames.notifyDataSetChanged();


                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + Id2 + "/" + Id3 + "/Classes/");
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

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

                        cancDelYr.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                DelYrdialog.dismiss();

                            }
                        });


                        okDelYr.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if( !YearNamesString.isEmpty()){

                                    int number = SchNameDelYr.getSelectedItemPosition();
                                    String Id = SchoolNames.get(number).getId();
                                    int number2 = DeptNameDelYr.getSelectedItemPosition();
                                    String Id2 = DeptNames.get(number2).getId();
                                    int number3 = YearNameDelYr.getSelectedItemPosition();
                                    String Id3 = YearNames.get(number3).getId();
                                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2 + "/" + Id3 ) ;
                                    ClassRef.removeValue();

                                }else{

                                    Toast.makeText(Admin_Activity.this , "No Year Added" , Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                        //Do
                        break;



                    case R.id.AddSubjects:

                        final AlertDialog.Builder mSubBuilder = new AlertDialog.Builder(Admin_Activity.this)
                                .setCancelable(false);
                        View mSubView = getLayoutInflater().inflate(R.layout.dialog_box_add_subjects, null);
                        final TextInputEditText SubjectName = (TextInputEditText) mSubView.findViewById(R.id.SubjectName);
                        final TextInputEditText SubjectCode = (TextInputEditText) mSubView.findViewById(R.id.SubjectCode);
                        final Spinner SchNameSub = (Spinner)mSubView.findViewById(R.id.SchoolNameAddSubject);
                        final Spinner DeptNameSub = (Spinner)mSubView.findViewById(R.id.DeptNameAddSubject);
                        final Spinner YearNameSub = (Spinner)mSubView.findViewById(R.id.YearNameAddSubject);
                        final Button SelectClasses = (Button)mSubView.findViewById(R.id.SelectClassesAddSubjects);
                        final Button canc4 = (Button) mSubView.findViewById(R.id.button19);
                        final Button ok4 = (Button) mSubView.findViewById(R.id.button20);
                        final TextView txt = (TextView) mSubView.findViewById(R.id.textClassesSelectedAddSubjects);


                        SchNameSub.setAdapter(adapterSpinnerSchoolNames);
                        DeptNameSub.setAdapter(adapterSpinnerDeptNames);
                        YearNameSub.setAdapter(adapterSpinnerYearNames);

                        SchNameSub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                int pos = SchNameSub.getSelectedItemPosition();
                                final String Id = SchoolNames.get(pos).getId();
                                DeptNames.clear();
                                DeptNamesString.clear();
                                YearNames.clear();
                                YearNamesString.clear();
                                adapterSpinnerYearNames.notifyDataSetChanged();
                                adapterSpinnerSchoolNames.notifyDataSetChanged();
                                adapterSpinnerDeptNames.notifyDataSetChanged();

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

                                                String yrid = DeptNames.get(0).getId();
                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + yrid + "/");
                                                ClassRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        if(dataSnapshot.exists()){

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

                                                                int pos = DeptNameSub.getSelectedItemPosition();
                                                                final String did = DeptNames.get(pos).getId();
                                                                String yrid  = YearNames.get(0).getId();
                                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + did + "/" + yrid + "/Classes/");
                                                                ClassRef.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                                        if(dataSnapshot.exists()){


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

                        DeptNameSub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                int pos = SchNameSub.getSelectedItemPosition();
                                final String Id = SchoolNames.get(pos).getId();

                                int pos2 = DeptNameSub.getSelectedItemPosition();
                                final String Id2 = DeptNames.get(pos2).getId();

                                YearNames.clear();
                                YearNamesString.clear();
                                adapterSpinnerYearNames.notifyDataSetChanged();

                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2 + "/") ;
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

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

                                                int pos = DeptNameSub.getSelectedItemPosition();
                                                final String did = DeptNames.get(pos).getId();
                                                String yrid  = YearNames.get(0).getId();
                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + did + "/" + yrid + "/Classes/");
                                                ClassRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        if(dataSnapshot.exists()){


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

                        YearNameSub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                int pos = SchNameSub.getSelectedItemPosition();
                                final String Id = SchoolNames.get(pos).getId();

                                int pos2 = DeptNameSub.getSelectedItemPosition();
                                final String Id2 = DeptNames.get(pos2).getId();

                                int pos3 = YearNameSub.getSelectedItemPosition();
                                final String Id3 = YearNames.get(pos3).getId();


                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + Id2 + "/" + Id3 + "/Classes/");
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

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


                        SelectClasses.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                mSelectedClassNames.clear();
                                mSelectedItems.clear();
                                AlertDialog.Builder SelectClassesAddSubjectbuilder = new AlertDialog.Builder(Admin_Activity.this);
                                SelectClassesAddSubjectbuilder.setCancelable(false);
                                // Set the dialog title

                                SelectClassesAddSubjectbuilder.setTitle("Choose Classes")
                                        // Specify the list array, the items to be selected by default (null for none),
                                        // and the listener through which to receive callbacks when items are selected
                                        .setMultiChoiceItems(ClassNamesString.toArray(new String[ClassNamesString.size()]), null,
                                                new DialogInterface.OnMultiChoiceClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                                        if (isChecked) {
                                                            // If the user checked the item, add it to the selected items
                                                            if ( !mSelectedItems.contains(ClassNamesString.get(which)) ) {
                                                                mSelectedClassNames.add(ClassNames.get(which));
                                                                mSelectedItems.add(ClassNamesString.get(which));
                                                            }
                                                        }else if (mSelectedItems.contains(ClassNamesString.get(which))) {
                                                            mSelectedClassNames.remove(mSelectedClassNames.get(which));
                                                            mSelectedItems.remove(ClassNamesString.get(which));
                                                        }
                                                    }
                                                });

                                // Set the action buttons
                                SelectClassesAddSubjectbuilder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
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
                                            txt.setText("No Classes Selected");
                                        }else {
                                            txt.setText("Classes Selected :" + item);
                                        }
                                        dialog.dismiss();
                                    }
                                });
                                SelectClassesAddSubjectbuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                     }
                                });

                                AlertDialog mDialog = SelectClassesAddSubjectbuilder.create();
                                mDialog.setCanceledOnTouchOutside(false);
                                mDialog.show();


                            }
                        });

                        mSubBuilder.setView(mSubView);
                        final AlertDialog Subdialog = mSubBuilder.create();
                        Subdialog.show();


                        ok4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if( !SubjectName.getText().toString().isEmpty() && !SubjectCode.getText().toString().isEmpty() && !mSelectedItems.isEmpty() && !YearNamesString.isEmpty() ){


                                    int number = SchNameSub.getSelectedItemPosition();
                                    String Id = SchoolNames.get(number).getId();
                                    int number2 = DeptNameSub.getSelectedItemPosition();
                                    String Id2 = DeptNames.get(number2).getId();
                                    int number3 = YearNameSub.getSelectedItemPosition();
                                    String Id3 = YearNames.get(number3).getId();

                                    Date date = new Date();  // to get the date
                                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss"); //SSS for milli seconds getting date in this format
                                    final String formattedDate = df.format(date.getTime());

                                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2 + "/" + Id3 + "/Subjects/" + formattedDate  ) ;
                                    HashMap<String,String> dataMap = new HashMap<String, String>();
                                    dataMap.put("Name" , SubjectName.getText().toString().trim().toUpperCase());
                                    dataMap.put("Code" , SubjectCode.getText().toString().trim().toUpperCase());
                                    ClassRef.setValue(dataMap);


                                    for ( int i=0 ; i< mSelectedClassNames.size() ; i++){


                                        //Toast.makeText(Admin_Activity.this , "Subject Added" , Toast.LENGTH_SHORT).show();

                                        String ids = mSelectedClassNames.get(i).getId();
                                        String names = mSelectedClassNames.get(i).getName();

                                        ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2 + "/" + Id3 + "/Classes/" + ids + "/Subjects/" + formattedDate  ) ;
                                        ClassRef.setValue(dataMap);

                                        ClassRef2 = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2 + "/" + Id3 + "/Subjects/" + formattedDate + "/Classes/" + ids + "/") ;
                                        HashMap<String,String> dataMap2 = new HashMap<String, String>();
                                        dataMap2.put("Name" , names);
                                        ClassRef2.setValue(dataMap2);

                                        Toast.makeText(Admin_Activity.this , "Subject Added" , Toast.LENGTH_SHORT).show();


                                    }

                              }else{

                                    Toast.makeText(Admin_Activity.this , "No Year Added" , Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                        canc4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                Subdialog.dismiss();

                            }
                        });


                        //Do
                        break;

                    case R.id.EditSubjects:





                        //Do
                        break;

                    case R.id.DeleteSubjects:



                        final AlertDialog.Builder mDelSubsBuilder = new AlertDialog.Builder(Admin_Activity.this)
                                .setCancelable(false);
                        View mDelSubsView = getLayoutInflater().inflate(R.layout.dialog_box_delete_subjects, null);
                        final Spinner SchNameDelSubs = (Spinner)mDelSubsView.findViewById(R.id.SchoolNameDelClass);
                        final Spinner DeptNameDelSubs = (Spinner)mDelSubsView.findViewById(R.id.DeptNameDelClass);
                        final Spinner YearNameDelSubs = (Spinner)mDelSubsView.findViewById(R.id.YearNameDelClass);
                        final Spinner SubsNameDelSubs = (Spinner)mDelSubsView.findViewById(R.id.ClassNameDelClass);
                        final Button cancDelSubs = (Button) mDelSubsView.findViewById(R.id.button19);
                        final Button okDelSubs = (Button) mDelSubsView.findViewById(R.id.button20);

                        SchNameDelSubs.setAdapter(adapterSpinnerSchoolNames);
                        DeptNameDelSubs.setAdapter(adapterSpinnerDeptNames);
                        YearNameDelSubs.setAdapter(adapterSpinnerYearNames);
                        SubsNameDelSubs.setAdapter(adapterSpinnerSubjectNames);

                        SchNameDelSubs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                int pos = SchNameDelSubs.getSelectedItemPosition();
                                final String Id = SchoolNames.get(pos).getId();


                                DeptNames.clear();
                                DeptNamesString.clear();
                                YearNames.clear();
                                YearNamesString.clear();
                                SubjectNames.clear();
                                SubjectNamesString.clear();
                                adapterSpinnerSubjectNames.notifyDataSetChanged();
                                adapterSpinnerYearNames.notifyDataSetChanged();
                                adapterSpinnerSchoolNames.notifyDataSetChanged();
                                adapterSpinnerDeptNames.notifyDataSetChanged();

                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/") ;
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

                                            DeptNames.clear();
                                            DeptNamesString.clear();
                                            YearNames.clear();
                                            YearNamesString.clear();
                                            SubjectNames.clear();
                                            SubjectNamesString.clear();
                                            adapterSpinnerSubjectNames.notifyDataSetChanged();
                                            adapterSpinnerYearNames.notifyDataSetChanged();
                                            adapterSpinnerSchoolNames.notifyDataSetChanged();
                                            adapterSpinnerDeptNames.notifyDataSetChanged();

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

                                                String deid = DeptNames.get(0).getId();

                                                YearNames.clear();
                                                YearNamesString.clear();
                                                SubjectNames.clear();
                                                SubjectNamesString.clear();
                                                adapterSpinnerSubjectNames.notifyDataSetChanged();
                                                adapterSpinnerYearNames.notifyDataSetChanged();
                                                adapterSpinnerSchoolNames.notifyDataSetChanged();

                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + deid + "/");
                                                ClassRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        if(dataSnapshot.exists()){


                                                            YearNames.clear();
                                                            YearNamesString.clear();
                                                            SubjectNames.clear();
                                                            SubjectNamesString.clear();
                                                            adapterSpinnerSubjectNames.notifyDataSetChanged();
                                                            adapterSpinnerYearNames.notifyDataSetChanged();
                                                            adapterSpinnerSchoolNames.notifyDataSetChanged();

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

                                                                int pos = DeptNameDelSubs.getSelectedItemPosition();
                                                                final String did = DeptNames.get(pos).getId();
                                                                String yrid  = YearNames.get(0).getId();


                                                                SubjectNames.clear();
                                                                SubjectNamesString.clear();
                                                                adapterSpinnerSubjectNames.notifyDataSetChanged();

                                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + did + "/" + yrid + "/Subjects/");
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
                                                                                    SubjectNames.add(new NameIdClass(Name , id));
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

                        DeptNameDelSubs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                int pos = SchNameDelSubs.getSelectedItemPosition();
                                final String Id = SchoolNames.get(pos).getId();

                                int pos2 = DeptNameDelSubs.getSelectedItemPosition();
                                final String Id2 = DeptNames.get(pos2).getId();

                                YearNames.clear();
                                YearNamesString.clear();
                                adapterSpinnerYearNames.notifyDataSetChanged();
                                SubjectNames.clear();
                                SubjectNamesString.clear();
                                adapterSpinnerSubjectNames.notifyDataSetChanged();

                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2 + "/") ;
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

                                            YearNames.clear();
                                            YearNamesString.clear();
                                            adapterSpinnerYearNames.notifyDataSetChanged();
                                            SubjectNames.clear();
                                            SubjectNamesString.clear();
                                            adapterSpinnerSubjectNames.notifyDataSetChanged();


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

                                                int pos = DeptNameDelSubs.getSelectedItemPosition();
                                                final String did = DeptNames.get(pos).getId();
                                                String yrid  = YearNames.get(0).getId();

                                                SubjectNames.clear();
                                                SubjectNamesString.clear();
                                                adapterSpinnerSubjectNames.notifyDataSetChanged();


                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + did + "/" + yrid + "/Subjects/");
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
                                                                    SubjectNames.add(new NameIdClass(Name , id));
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


                        YearNameDelSubs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                int pos = SchNameDelSubs.getSelectedItemPosition();
                                final String Id = SchoolNames.get(pos).getId();

                                int pos2 = DeptNameDelSubs.getSelectedItemPosition();
                                final String Id2 = DeptNames.get(pos2).getId();

                                int pos3 = YearNameDelSubs.getSelectedItemPosition();
                                final String Id3 = YearNames.get(pos3).getId();


                                SubjectNames.clear();
                                SubjectNamesString.clear();
                                adapterSpinnerSubjectNames.notifyDataSetChanged();


                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + Id2 + "/" + Id3 + "/Subjects/");
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
                                                    SubjectNames.add(new NameIdClass(Name , id));
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


                        mDelSubsBuilder.setView(mDelSubsView);
                        final AlertDialog DelSubsdialog = mDelSubsBuilder.create();
                        DelSubsdialog.show();


                        okDelSubs.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if( !SubjectNamesString.isEmpty()){


                                    int number = SchNameDelSubs.getSelectedItemPosition();
                                    String Id = SchoolNames.get(number).getId();
                                    int number2 = DeptNameDelSubs.getSelectedItemPosition();
                                    String Id2 = DeptNames.get(number2).getId();
                                    int number3 = YearNameDelSubs.getSelectedItemPosition();
                                    String Id3 = YearNames.get(number3).getId();
                                    int number4 = SubsNameDelSubs.getSelectedItemPosition();
                                    String Id4 = SubjectNames.get(number4).getId();

                                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2 + "/" + Id3 + "/Subjects/" + Id4  + "/Classes/" ) ;

                                    ClassRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {


                                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                                String id = dataSnapshot1.getKey();

                                                if (!id.equals("Name") ) {

                                                    String Name = dataSnapshot1.child("Name").getValue().toString();
                                                    mSelectedClassNames.add(new NameIdClass(Name , id));

                                                }

                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    for ( int i=0 ; i< mSelectedClassNames.size() ; i++){

                                        //Toast.makeText(Admin_Activity.this , "Subject Added" , Toast.LENGTH_SHORT).show();

                                        String ids = mSelectedClassNames.get(i).getId();

                                        ClassRef2 = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2 + "/" + Id3 + "/Classes/" + ids + "/Subjects/" + Id4 + "/") ;
                                        ClassRef2.removeValue();


                                    }


                                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2 + "/" + Id3 + "/Subjects/" + Id4   ) ;
                                    ClassRef.removeValue();

                                    Toast.makeText(Admin_Activity.this , "Subject Deleted" , Toast.LENGTH_SHORT).show();


                                }else{

                                    Toast.makeText(Admin_Activity.this , "No Subject Deleted" , Toast.LENGTH_SHORT).show();
                                }


                            }
                        });


                        cancDelSubs.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                DelSubsdialog.dismiss();


                            }
                        });









                        //Do
                        break;



                    case R.id.AddClasses:


                        final AlertDialog.Builder mClassBuilder = new AlertDialog.Builder(Admin_Activity.this)
                                .setCancelable(false);
                        View mClassView = getLayoutInflater().inflate(R.layout.dialog_box_add_class, null);
                        final TextInputEditText ClassName = (TextInputEditText) mClassView.findViewById(R.id.ClassName);
                        final Spinner SchNameClass = (Spinner)mClassView.findViewById(R.id.SchoolNameAddClass);
                        final Spinner DeptNameClass = (Spinner)mClassView.findViewById(R.id.DeptNameAddClass);
                        final Spinner YearNameClass = (Spinner)mClassView.findViewById(R.id.YearNameAddClass);
                        final Button cancAddClass = (Button) mClassView.findViewById(R.id.button19);
                        final Button okAddClass = (Button) mClassView.findViewById(R.id.button20);

                        SchNameClass.setAdapter(adapterSpinnerSchoolNames);
                        DeptNameClass.setAdapter(adapterSpinnerDeptNames);
                        YearNameClass.setAdapter(adapterSpinnerYearNames);

                        SchNameClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                int pos = SchNameClass.getSelectedItemPosition();
                                final String Id = SchoolNames.get(pos).getId();
                                DeptNames.clear();
                                DeptNamesString.clear();
                                YearNames.clear();
                                YearNamesString.clear();
                                adapterSpinnerYearNames.notifyDataSetChanged();
                                adapterSpinnerSchoolNames.notifyDataSetChanged();
                                adapterSpinnerDeptNames.notifyDataSetChanged();

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

                                                String yrid = DeptNames.get(0).getId();
                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + yrid + "/");
                                                ClassRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        if(dataSnapshot.exists()){

                                                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                                String id = dataSnapshot2.getKey();

                                                                if (!id.equals("Name")) {

                                                                    String Name = dataSnapshot2.child("Name").getValue().toString();
                                                                    YearNames.add(new NameIdClass(Name , id));
                                                                    YearNamesString.add(Name);
                                                                    adapterSpinnerYearNames.notifyDataSetChanged();

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

                        DeptNameClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                int pos = SchNameClass.getSelectedItemPosition();
                                final String Id = SchoolNames.get(pos).getId();

                                int pos2 = DeptNameClass.getSelectedItemPosition();
                                final String Id2 = DeptNames.get(pos2).getId();

                                YearNames.clear();
                                YearNamesString.clear();
                                adapterSpinnerYearNames.notifyDataSetChanged();

                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2 + "/") ;
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                                String id = dataSnapshot1.getKey();

                                                if(!id.equals("Name")){

                                                    String YearNa = dataSnapshot1.child("Name").getValue().toString();
                                                    YearNames.add(new NameIdClass(YearNa , id));
                                                    YearNamesString.add(YearNa);
                                                    adapterSpinnerYearNames.notifyDataSetChanged();

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



                        mClassBuilder.setView(mClassView);
                        final AlertDialog Classdialog = mClassBuilder.create();
                        Classdialog.show();


                        okAddClass.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                if( !ClassName.getText().toString().isEmpty()){

                                    int number = SchNameClass.getSelectedItemPosition();
                                    String Id = SchoolNames.get(number).getId();
                                    int number2 = DeptNameClass.getSelectedItemPosition();
                                    String Id2 = DeptNames.get(number2).getId();
                                    int number3 = YearNameClass.getSelectedItemPosition();
                                    String Id3 = YearNames.get(number3).getId();
                                    Date date = new Date();  // to get the date
                                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss"); // getting date in this format
                                    final String formattedDate = df.format(date.getTime());
                                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2 + "/" + Id3 + "/Classes/" +formattedDate  ) ;
                                    HashMap<String,String> dataMap = new HashMap<String, String>();
                                    dataMap.put("Name" , ClassName.getText().toString().trim().toUpperCase());
                                    ClassRef.setValue(dataMap);
                                    Toast.makeText(Admin_Activity.this , "Class Added" , Toast.LENGTH_SHORT).show();

                                }else{

                                    Toast.makeText(Admin_Activity.this , "No Class Added" , Toast.LENGTH_SHORT).show();
                                }



                            }
                        });

                        cancAddClass.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Classdialog.dismiss();


                            }
                        });

                        //Do
                        break;



                    case R.id.DeleteClasses:


                        final AlertDialog.Builder mDelClassBuilder = new AlertDialog.Builder(Admin_Activity.this)
                                .setCancelable(false);
                        View mDelClassView = getLayoutInflater().inflate(R.layout.dialog_box_delete_class, null);
                        final Spinner SchNameDelClass = (Spinner)mDelClassView.findViewById(R.id.SchoolNameDelClass);
                        final Spinner DeptNameDelClass = (Spinner)mDelClassView.findViewById(R.id.DeptNameDelClass);
                        final Spinner YearNameDelClass = (Spinner)mDelClassView.findViewById(R.id.YearNameDelClass);
                        final Spinner ClassNameDelClass = (Spinner)mDelClassView.findViewById(R.id.ClassNameDelClass);
                        final Button cancDelClass = (Button) mDelClassView.findViewById(R.id.button19);
                        final Button okDelClass = (Button) mDelClassView.findViewById(R.id.button20);

                        SchNameDelClass.setAdapter(adapterSpinnerSchoolNames);
                        DeptNameDelClass.setAdapter(adapterSpinnerDeptNames);
                        YearNameDelClass.setAdapter(adapterSpinnerYearNames);
                        ClassNameDelClass.setAdapter(adapterSpinnerClassNames);

                        SchNameDelClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                int pos = SchNameDelClass.getSelectedItemPosition();
                                final String Id = SchoolNames.get(pos).getId();
                                DeptNames.clear();
                                DeptNamesString.clear();
                                YearNames.clear();
                                YearNamesString.clear();
                                ClassNames.clear();
                                ClassNamesString.clear();
                                adapterSpinnerClassNames.notifyDataSetChanged();
                                adapterSpinnerYearNames.notifyDataSetChanged();
                                adapterSpinnerSchoolNames.notifyDataSetChanged();
                                adapterSpinnerDeptNames.notifyDataSetChanged();

                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/") ;
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

                                            DeptNames.clear();
                                            DeptNamesString.clear();
                                            YearNames.clear();
                                            YearNamesString.clear();
                                            ClassNames.clear();
                                            ClassNamesString.clear();
                                            adapterSpinnerClassNames.notifyDataSetChanged();
                                            adapterSpinnerYearNames.notifyDataSetChanged();
                                            adapterSpinnerSchoolNames.notifyDataSetChanged();
                                            adapterSpinnerDeptNames.notifyDataSetChanged();

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

                                                String deid = DeptNames.get(0).getId();

                                                YearNames.clear();
                                                YearNamesString.clear();
                                                ClassNames.clear();
                                                ClassNamesString.clear();
                                                adapterSpinnerClassNames.notifyDataSetChanged();
                                                adapterSpinnerYearNames.notifyDataSetChanged();
                                                adapterSpinnerSchoolNames.notifyDataSetChanged();

                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + deid + "/");
                                                ClassRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        if(dataSnapshot.exists()){


                                                            YearNames.clear();
                                                            YearNamesString.clear();
                                                            ClassNames.clear();
                                                            ClassNamesString.clear();
                                                            adapterSpinnerClassNames.notifyDataSetChanged();
                                                            adapterSpinnerYearNames.notifyDataSetChanged();
                                                            adapterSpinnerSchoolNames.notifyDataSetChanged();

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

                                                                int pos = DeptNameDelClass.getSelectedItemPosition();
                                                                final String did = DeptNames.get(pos).getId();
                                                                String yrid  = YearNames.get(0).getId();
                                                                ClassNames.clear();
                                                                ClassNamesString.clear();
                                                                adapterSpinnerClassNames.notifyDataSetChanged();

                                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + did + "/" + yrid + "/Classes/");
                                                                ClassRef.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                                        if(dataSnapshot.exists()){


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

                        DeptNameDelClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                int pos = SchNameDelClass.getSelectedItemPosition();
                                final String Id = SchoolNames.get(pos).getId();

                                int pos2 = DeptNameDelClass.getSelectedItemPosition();
                                final String Id2 = DeptNames.get(pos2).getId();

                                YearNames.clear();
                                YearNamesString.clear();
                                adapterSpinnerYearNames.notifyDataSetChanged();
                                ClassNames.clear();
                                ClassNamesString.clear();
                                adapterSpinnerClassNames.notifyDataSetChanged();

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

                                                int pos = DeptNameDelClass.getSelectedItemPosition();
                                                final String did = DeptNames.get(pos).getId();
                                                String yrid  = YearNames.get(0).getId();

                                                ClassNames.clear();
                                                ClassNamesString.clear();
                                                adapterSpinnerClassNames.notifyDataSetChanged();


                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + did + "/" + yrid + "/Classes/");
                                                ClassRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        if(dataSnapshot.exists()){

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


                        YearNameDelClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                int pos = SchNameDelClass.getSelectedItemPosition();
                                final String Id = SchoolNames.get(pos).getId();

                                int pos2 = DeptNameDelClass.getSelectedItemPosition();
                                final String Id2 = DeptNames.get(pos2).getId();

                                int pos3 = YearNameDelClass.getSelectedItemPosition();
                                final String Id3 = YearNames.get(pos3).getId();


                                ClassNames.clear();
                                ClassNamesString.clear();
                                adapterSpinnerClassNames.notifyDataSetChanged();


                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + Id2 + "/" + Id3 + "/Classes/");
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

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







                        mDelClassBuilder.setView(mDelClassView);
                        final AlertDialog DelClassdialog = mDelClassBuilder.create();
                        DelClassdialog.show();


                        okDelClass.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if( !ClassNamesString.isEmpty()){

                                    int number = SchNameDelClass.getSelectedItemPosition();
                                    String Id = SchoolNames.get(number).getId();
                                    int number2 = DeptNameDelClass.getSelectedItemPosition();
                                    String Id2 = DeptNames.get(number2).getId();
                                    int number3 = YearNameDelClass.getSelectedItemPosition();
                                    String Id3 = YearNames.get(number3).getId();
                                    int number4 = ClassNameDelClass.getSelectedItemPosition();
                                    String Id4 = ClassNames.get(number4).getId();
                                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2 + "/" + Id3 + "/Classes/" + Id4   ) ;
                                    ClassRef.removeValue();
                                    Toast.makeText(Admin_Activity.this , "Class Deleted" , Toast.LENGTH_SHORT).show();

                                }else{

                                    Toast.makeText(Admin_Activity.this , "No Class Deleted" , Toast.LENGTH_SHORT).show();
                                }


                            }
                        });


                        cancDelClass.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                DelClassdialog.dismiss();


                            }
                        });






                        //Do
                        break;


                    case R.id.AddGroups:

                        final AlertDialog.Builder mGroupBuilder = new AlertDialog.Builder(Admin_Activity.this)
                                .setCancelable(false);
                        View mGroupView = getLayoutInflater().inflate(R.layout.dialog_box_add_groups, null);
                        final TextInputEditText GroupName = (TextInputEditText) mGroupView.findViewById(R.id.GroupName);
                        final Spinner SchNameGroup = (Spinner)mGroupView.findViewById(R.id.SchoolNameAddGroups);
                        final Spinner DeptNameGroup = (Spinner)mGroupView.findViewById(R.id.DeptNameAddGroups);
                        final Spinner YearNameGroup = (Spinner)mGroupView.findViewById(R.id.YearNameAddGroups);
                        final Spinner ClassNameGroup = (Spinner)mGroupView.findViewById(R.id.ClassNameAddGroups);
                        final Button cancAddGroup = (Button) mGroupView.findViewById(R.id.button19);
                        final Button okAddGroup = (Button) mGroupView.findViewById(R.id.button20);


                        SchNameGroup.setAdapter(adapterSpinnerSchoolNames);
                        DeptNameGroup.setAdapter(adapterSpinnerDeptNames);
                        YearNameGroup.setAdapter(adapterSpinnerYearNames);
                        ClassNameGroup.setAdapter(adapterSpinnerClassNames);

                        SchNameGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                int pos = SchNameGroup.getSelectedItemPosition();
                                final String Id = SchoolNames.get(pos).getId();
                                DeptNames.clear();
                                DeptNamesString.clear();
                                YearNames.clear();
                                YearNamesString.clear();
                                ClassNames.clear();
                                ClassNamesString.clear();
                                adapterSpinnerClassNames.notifyDataSetChanged();
                                adapterSpinnerYearNames.notifyDataSetChanged();
                                adapterSpinnerSchoolNames.notifyDataSetChanged();
                                adapterSpinnerDeptNames.notifyDataSetChanged();

                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/") ;
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){


                                            DeptNames.clear();
                                            DeptNamesString.clear();
                                            YearNames.clear();
                                            YearNamesString.clear();
                                            ClassNames.clear();
                                            ClassNamesString.clear();
                                            adapterSpinnerClassNames.notifyDataSetChanged();
                                            adapterSpinnerYearNames.notifyDataSetChanged();
                                            adapterSpinnerSchoolNames.notifyDataSetChanged();
                                            adapterSpinnerDeptNames.notifyDataSetChanged();

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

                                                String deid = DeptNames.get(0).getId();


                                                YearNames.clear();
                                                YearNamesString.clear();
                                                ClassNames.clear();
                                                ClassNamesString.clear();
                                                adapterSpinnerClassNames.notifyDataSetChanged();
                                                adapterSpinnerYearNames.notifyDataSetChanged();
                                                adapterSpinnerSchoolNames.notifyDataSetChanged();
                                                adapterSpinnerDeptNames.notifyDataSetChanged();

                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + deid + "/");
                                                ClassRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        if(dataSnapshot.exists()){

                                                            YearNames.clear();
                                                            YearNamesString.clear();
                                                            ClassNames.clear();
                                                            ClassNamesString.clear();
                                                            adapterSpinnerClassNames.notifyDataSetChanged();
                                                            adapterSpinnerYearNames.notifyDataSetChanged();
                                                            adapterSpinnerSchoolNames.notifyDataSetChanged();
                                                            adapterSpinnerDeptNames.notifyDataSetChanged();



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

                                                                int pos = DeptNameGroup.getSelectedItemPosition();
                                                                final String did = DeptNames.get(pos).getId();
                                                                String yrid  = YearNames.get(0).getId();

                                                                ClassNames.clear();
                                                                ClassNamesString.clear();
                                                                adapterSpinnerClassNames.notifyDataSetChanged();
                                                                adapterSpinnerYearNames.notifyDataSetChanged();
                                                                adapterSpinnerSchoolNames.notifyDataSetChanged();
                                                                adapterSpinnerDeptNames.notifyDataSetChanged();



                                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + did + "/" + yrid + "/Classes/");
                                                                ClassRef.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                                        if(dataSnapshot.exists()){


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

                        DeptNameGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                int pos = SchNameGroup.getSelectedItemPosition();
                                final String Id = SchoolNames.get(pos).getId();

                                int pos2 = DeptNameGroup.getSelectedItemPosition();
                                final String Id2 = DeptNames.get(pos2).getId();

                                YearNames.clear();
                                YearNamesString.clear();
                                ClassNames.clear();
                                ClassNamesString.clear();
                                adapterSpinnerClassNames.notifyDataSetChanged();
                                adapterSpinnerYearNames.notifyDataSetChanged();
                                adapterSpinnerSchoolNames.notifyDataSetChanged();
                                adapterSpinnerDeptNames.notifyDataSetChanged();


                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2 + "/") ;
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

                                            YearNames.clear();
                                            YearNamesString.clear();
                                            ClassNames.clear();
                                            ClassNamesString.clear();
                                            adapterSpinnerClassNames.notifyDataSetChanged();
                                            adapterSpinnerYearNames.notifyDataSetChanged();
                                            adapterSpinnerSchoolNames.notifyDataSetChanged();
                                            adapterSpinnerDeptNames.notifyDataSetChanged();



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

                                                int pos = DeptNameGroup.getSelectedItemPosition();
                                                final String did = DeptNames.get(pos).getId();
                                                String yrid  = YearNames.get(0).getId();

                                                ClassNames.clear();
                                                ClassNamesString.clear();
                                                adapterSpinnerClassNames.notifyDataSetChanged();
                                                adapterSpinnerYearNames.notifyDataSetChanged();
                                                adapterSpinnerSchoolNames.notifyDataSetChanged();
                                                adapterSpinnerDeptNames.notifyDataSetChanged();


                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + did + "/" + yrid + "/Classes/");
                                                ClassRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        if(dataSnapshot.exists()){

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


                        YearNameGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                int pos = SchNameGroup.getSelectedItemPosition();
                                final String Id = SchoolNames.get(pos).getId();

                                int pos2 = DeptNameGroup.getSelectedItemPosition();
                                final String Id2 = DeptNames.get(pos2).getId();

                                int pos3 = YearNameGroup.getSelectedItemPosition();
                                final String Id3 = YearNames.get(pos3).getId();

                                ClassNames.clear();
                                ClassNamesString.clear();
                                adapterSpinnerClassNames.notifyDataSetChanged();

                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/"  + Id2 + "/" + Id3 + "/Classes/");
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

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

                        ClassNameGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {




                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                        mGroupBuilder.setView(mGroupView);
                        final AlertDialog Groupdialog = mGroupBuilder.create();
                        Groupdialog.show();



                        okAddGroup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                if( !GroupName.getText().toString().isEmpty()){

                                    int number = SchNameGroup.getSelectedItemPosition();
                                    String Id = SchoolNames.get(number).getId();
                                    int number2 = DeptNameGroup.getSelectedItemPosition();
                                    String Id2 = DeptNames.get(number2).getId();
                                    int number3 = YearNameGroup.getSelectedItemPosition();
                                    String Id3 = YearNames.get(number3).getId();
                                    int number4 = ClassNameGroup.getSelectedItemPosition();
                                    String Id4 = ClassNames.get(number4).getId();
                                    Date date = new Date();  // to get the date
                                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss"); // getting date in this format
                                    final String formattedDate = df.format(date.getTime());
                                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2 + "/" + Id3 + "/Classes/" + Id4 + "/Groups/" + formattedDate  ) ;
                                    HashMap<String,String> dataMap = new HashMap<String, String>();
                                    dataMap.put("Name" , GroupName.getText().toString().trim().toUpperCase());
                                    ClassRef.setValue(dataMap);
                                    Toast.makeText(Admin_Activity.this , "Group Added" , Toast.LENGTH_SHORT).show();

                                }else{

                                    Toast.makeText(Admin_Activity.this , "No Group Added" , Toast.LENGTH_SHORT).show();
                                }

                            }
                        });


                        cancAddGroup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Groupdialog.dismiss();



                            }
                        });


                        //Do
                        break;

                    case R.id.DeleteGroups:

                        final AlertDialog.Builder mDelGroupBuilder = new AlertDialog.Builder(Admin_Activity.this)
                                .setCancelable(false);
                        View mDelGroupView = getLayoutInflater().inflate(R.layout.dialog_box_delete_groups, null);
                        final Spinner SchNameDelGroup = (Spinner)mDelGroupView.findViewById(R.id.SchoolNameDelGroups);
                        final Spinner DeptNameDelGroup = (Spinner)mDelGroupView.findViewById(R.id.DeptNameDelGroups);
                        final Spinner YearNameDelGroup = (Spinner)mDelGroupView.findViewById(R.id.YearNameDelGroups);
                        final Spinner ClassNameDelGroup = (Spinner)mDelGroupView.findViewById(R.id.ClassNameDelGroups);
                        final Spinner GroupNameDelGroup = (Spinner)mDelGroupView.findViewById(R.id.GroupNameDelGroups);

                        final Button cancDelGroup = (Button) mDelGroupView.findViewById(R.id.button19);
                        final Button okDelGroup = (Button) mDelGroupView.findViewById(R.id.button20);


                        SchNameDelGroup.setAdapter(adapterSpinnerSchoolNames);
                        DeptNameDelGroup.setAdapter(adapterSpinnerDeptNames);
                        YearNameDelGroup.setAdapter(adapterSpinnerYearNames);
                        ClassNameDelGroup.setAdapter(adapterSpinnerClassNames);
                        GroupNameDelGroup.setAdapter(adapterSpinnerGroupNames);

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


                        SchNameDelGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                int pos = SchNameDelGroup.getSelectedItemPosition();
                                final String Id = SchoolNames.get(pos).getId();

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

                        DeptNameDelGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                int pos = SchNameDelGroup.getSelectedItemPosition();
                                final String Id = SchoolNames.get(pos).getId();

                                int pos2 = DeptNameDelGroup.getSelectedItemPosition();
                                final String Id2 = DeptNames.get(pos2).getId();

                                YearNames.clear();
                                YearNamesString.clear();
                                adapterSpinnerYearNames.notifyDataSetChanged();
                                ClassNames.clear();
                                ClassNamesString.clear();
                                adapterSpinnerClassNames.notifyDataSetChanged();
                                GroupNames.clear();
                                GroupNamesString.clear();
                                adapterSpinnerGroupNames.notifyDataSetChanged();

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


                        YearNameDelGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                int pos = SchNameDelGroup.getSelectedItemPosition();
                                final String Id = SchoolNames.get(pos).getId();

                                int pos2 = DeptNameDelGroup.getSelectedItemPosition();
                                final String Id2 = DeptNames.get(pos2).getId();

                                int pos3 = YearNameDelGroup.getSelectedItemPosition();
                                final String Id3 = YearNames.get(pos3).getId();

                                ClassNames.clear();
                                ClassNamesString.clear();
                                adapterSpinnerClassNames.notifyDataSetChanged();

                                GroupNames.clear();
                                GroupNamesString.clear();
                                adapterSpinnerGroupNames.notifyDataSetChanged();


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

                        ClassNameDelGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                int pos1 = SchNameDelGroup.getSelectedItemPosition();
                                final String Id = SchoolNames.get(pos1).getId();

                                int pos = DeptNameDelGroup.getSelectedItemPosition();
                                final String did = DeptNames.get(pos).getId();

                                int pos2 = YearNameDelGroup.getSelectedItemPosition();
                                final String yrid = YearNames.get(pos2).getId();

                                String Id3  = ClassNames.get(position).getId();

                                GroupNames.clear();
                                GroupNamesString.clear();
                                adapterSpinnerGroupNames.notifyDataSetChanged();

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




                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                        mDelGroupBuilder.setView(mDelGroupView);
                        final AlertDialog DelGroupdialog = mDelGroupBuilder.create();
                        DelGroupdialog.show();



                        okDelGroup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                if( !GroupNames.isEmpty()){

                                    int number = SchNameDelGroup.getSelectedItemPosition();
                                    String Id = SchoolNames.get(number).getId();
                                    int number2 = DeptNameDelGroup.getSelectedItemPosition();
                                    String Id2 = DeptNames.get(number2).getId();
                                    int number3 = YearNameDelGroup.getSelectedItemPosition();
                                    String Id3 = YearNames.get(number3).getId();
                                    int number4 = ClassNameDelGroup.getSelectedItemPosition();
                                    String Id4 = ClassNames.get(number4).getId();
                                    int number5 = GroupNameDelGroup.getSelectedItemPosition();
                                    String Id5 = GroupNames.get(number5).getId();


                                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2 + "/" + Id3 + "/Classes/" + Id4 + "/Groups/" + Id5  ) ;
                                    ClassRef.removeValue();
                                    Toast.makeText(Admin_Activity.this , "Group Added" , Toast.LENGTH_SHORT).show();

                                }else{

                                    Toast.makeText(Admin_Activity.this , "No Group Added" , Toast.LENGTH_SHORT).show();
                                }

                            }
                        });


                        cancDelGroup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                DelGroupdialog.dismiss();



                            }
                        });






                        break;



                    case R.id.AddTimings:


                        AlertDialog.Builder mTimeBuilder = new AlertDialog.Builder(Admin_Activity.this)
                                .setCancelable(false);
                        View mTimeView = getLayoutInflater().inflate(R.layout.dialog_box_add_timings, null);
                        final TextInputEditText StartTime = (TextInputEditText) mTimeView.findViewById(R.id.StartTime);
                        final TextInputEditText EndTime = (TextInputEditText) mTimeView.findViewById(R.id.EndTime);

                        mTimeBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if( !StartTime.getText().toString().isEmpty() && !EndTime.getText().toString().isEmpty() ){

                                    Date date = new Date();  // to get the date
                                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss"); // getting date in this format
                                    final String formattedDate = df.format(date.getTime());
                                    ClassRef = FirebaseDatabase.getInstance().getReference("Timings/" + formattedDate ) ;
                                    HashMap<String,String> dataMap = new HashMap<String, String>();
                                    dataMap.put("Start" , StartTime.getText().toString().trim().toUpperCase());
                                    dataMap.put("End" , EndTime.getText().toString().trim().toUpperCase());
                                    ClassRef.setValue(dataMap);
                                    Toast.makeText(Admin_Activity.this , "Time Added" , Toast.LENGTH_SHORT).show();

                                }else{

                                    Toast.makeText(Admin_Activity.this , "No School Added" , Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                        mTimeBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        });

                        mTimeBuilder.setView(mTimeView);
                        AlertDialog dialogTime = mTimeBuilder.create();
                        dialogTime.show();

                        //Do
                        break;


                    case R.id.DeleteTimings:


                        final AlertDialog.Builder mDelTimingsBuilder = new AlertDialog.Builder(Admin_Activity.this)
                                .setCancelable(false);
                        View mDelTimingsView = getLayoutInflater().inflate(R.layout.dialog_box_delete_timings, null);
                        final Spinner DelTimings = (Spinner)mDelTimingsView.findViewById(R.id.DaysNameDelTimings);
                        final Button cancDelTimings = (Button) mDelTimingsView.findViewById(R.id.button19);
                        final Button okDelTimings = (Button) mDelTimingsView.findViewById(R.id.button20);


                        DelTimings.setAdapter(adapterSpinnerTimingsNames);
                        mDelTimingsBuilder.setView(mDelTimingsView);
                        final AlertDialog DelTimingsdialog = mDelTimingsBuilder.create();
                        DelTimingsdialog.show();

                        okDelTimings.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if( !DaysNames.isEmpty()){

                                    int number = DelTimings.getSelectedItemPosition();
                                    String Id = TimeNames.get(number).getId();
                                    ClassRef = FirebaseDatabase.getInstance().getReference("Timings/" + Id ) ;
                                    ClassRef.removeValue();
                                    Toast.makeText(Admin_Activity.this , "Timing Removed" , Toast.LENGTH_SHORT).show();

                                }else{

                                    Toast.makeText(Admin_Activity.this , "No Timing Removed" , Toast.LENGTH_SHORT).show();
                                }


                            }
                        });


                        cancDelTimings.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                DelTimingsdialog.dismiss();

                            }
                        });





                        break;

                    case R.id.AddDays:

                        AlertDialog.Builder mDaysBuilder = new AlertDialog.Builder(Admin_Activity.this)
                                .setCancelable(false);
                        View mDaysView = getLayoutInflater().inflate(R.layout.dialog_box_add_days, null);
                        final TextInputEditText Day = (TextInputEditText) mDaysView.findViewById(R.id.AddDays);

                        mDaysBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if( !Day.getText().toString().isEmpty()  ){

                                    Date date = new Date();  // to get the date
                                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss"); // getting date in this format
                                    final String formattedDate = df.format(date.getTime());
                                    ClassRef = FirebaseDatabase.getInstance().getReference("Days/" + formattedDate ) ;
                                    HashMap<String,String> dataMap = new HashMap<String, String>();
                                    dataMap.put("Day" , Day.getText().toString().trim().toUpperCase());
                                    ClassRef.setValue(dataMap);
                                    Toast.makeText(Admin_Activity.this , "Day Added" , Toast.LENGTH_SHORT).show();

                                }else{

                                    Toast.makeText(Admin_Activity.this , "No Day Added" , Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                        mDaysBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        });

                        mDaysBuilder.setView(mDaysView);
                        AlertDialog dialogDays = mDaysBuilder.create();
                        dialogDays.setCanceledOnTouchOutside(false);
                        dialogDays.show();
                        //Do

                        break;


                    case R.id.DeleteDays:

                        final AlertDialog.Builder mDelDaysBuilder = new AlertDialog.Builder(Admin_Activity.this)
                                .setCancelable(false);
                        View mDelDaysView = getLayoutInflater().inflate(R.layout.dialog_box_delete_days,  null);
                        final Spinner DelDays = (Spinner)mDelDaysView.findViewById(R.id.DaysNameDelDays);
                        final Button cancDelDays = (Button) mDelDaysView.findViewById(R.id.button19);
                        final Button okDelDays = (Button) mDelDaysView.findViewById(R.id.button20);


                        DelDays.setAdapter(adapterSpinnerDaysNames);

                        mDelDaysBuilder.setView(mDelDaysView);
                        final AlertDialog DelDaysdialog = mDelDaysBuilder.create();
                        DelDaysdialog.show();

                        okDelDays.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if( !DaysNames.isEmpty()){

                                    int number = DelDays.getSelectedItemPosition();
                                    String Id = DaysNames.get(number).getId();
                                    ClassRef = FirebaseDatabase.getInstance().getReference("Days/" + Id ) ;
                                    ClassRef.removeValue();
                                    Toast.makeText(Admin_Activity.this , "Day Removed" , Toast.LENGTH_SHORT).show();

                                }else{

                                    Toast.makeText(Admin_Activity.this , "No Days Removed" , Toast.LENGTH_SHORT).show();
                                }


                            }
                        });


                        cancDelDays.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                DelDaysdialog.dismiss();

                            }
                        });


                        break;

                    case R.id.AddTimeTable:

                        Intent mainIntentCreateTT = new Intent(Admin_Activity.this, CreateTimeTable.class);
                        startActivity(mainIntentCreateTT);
                        overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);


                        break;

                    case R.id.EditTimeTable:


                        break;


                    case R.id.DeleteTimeTable:


                        break;


                    case R.id.ChangePass:

                        final AlertDialog.Builder mbBuilder = new AlertDialog.Builder(Admin_Activity.this)
                                .setCancelable(false);
                        View mvView = getLayoutInflater().inflate(R.layout.dialog_box_change_password, null);
                        final TextInputEditText EditTextChangePass = (TextInputEditText) mvView.findViewById(R.id.EditTextChangePassword);
                        final Button canc = (Button) mvView.findViewById(R.id.button19);
                        final Button ok = (Button) mvView.findViewById(R.id.button20);

                        mbBuilder.setView(mvView);
                        final AlertDialog ddialog = mbBuilder.create();
                        ddialog.show();

                        canc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ddialog.dismiss();

                            }
                        });


                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                if(!EditTextChangePass.getText().toString().isEmpty()){

                                    mLoginProgress.setMessage("Changing Password");
                                    mLoginProgress.show();

                                    FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser() ;
                                    User.updatePassword(EditTextChangePass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){

                                                Toast.makeText(Admin_Activity.this , "Password Successfully Changed" , Toast.LENGTH_SHORT).show();
                                                ddialog.dismiss();
                                                mLoginProgress.dismiss();
                                            }else {

                                                try {
                                                    throw task.getException();
                                                }
                                                // if user enters wrong email.
                                                catch (FirebaseAuthWeakPasswordException weakPassword) {
                                                    Toast.makeText(Admin_Activity.this, "Weak Password",
                                                            Toast.LENGTH_SHORT).show();
                                                    mLoginProgress.dismiss();

                                                } catch (Exception e) {
                                                    Toast.makeText(Admin_Activity.this, e.getMessage(),
                                                            Toast.LENGTH_SHORT).show();
                                                    mLoginProgress.dismiss();

                                                }
                                            }
                                        }
                                    });
                                }else {
                                    Toast.makeText(Admin_Activity.this , "Email Cannot Be Empty" , Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        break;

                    case R.id.LogOut:

                        Toast.makeText(Admin_Activity.this , "Sign Out" , Toast.LENGTH_SHORT ).show();
                        mLoginProgress.setTitle("Signing Out");
                        mLoginProgress.setMessage("Please Wait While We Log Out From Your Account");
                        mLoginProgress.setCanceledOnTouchOutside(false);
                        mLoginProgress.show();
                        mAuth.signOut();
                        Intent mainIntent = new Intent(Admin_Activity.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                        mLoginProgress.dismiss();
                        overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);
                        Utils.falseDatabase();
                        break;

                    case R.id.AddPeriods:


                        final AlertDialog.Builder mperiodBuilder = new AlertDialog.Builder(Admin_Activity.this)
                                .setCancelable(false);
                        View mperiodView = getLayoutInflater().inflate(R.layout.dialog_box_add_periods, null);
                        final TextInputEditText PeriodName = (TextInputEditText) mperiodView.findViewById(R.id.Department);
                        final Spinner TimingsName = (Spinner)mperiodView.findViewById(R.id.TimingsNameCreatePeriods);
                        final Button cancp = (Button) mperiodView.findViewById(R.id.button19);
                        final Button okp = (Button) mperiodView.findViewById(R.id.button20);


                        TimingsName.setAdapter(adapterSpinnerTimingsNames);

                        mperiodBuilder.setView(mperiodView);
                        final AlertDialog perioddialog = mperiodBuilder.create();
                        perioddialog.show();

                        okp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if( !PeriodName.getText().toString().isEmpty()){

                                    int number = TimingsName.getSelectedItemPosition();
                                    String Id = TimeNames.get(number).getId();

                                    Date date = new Date();  // to get the date
                                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss"); // getting date in this format
                                    final String formattedDate = df.format(date.getTime());

                                    ClassRef = FirebaseDatabase.getInstance().getReference("Timings/" + Id + "/Periods/" + formattedDate) ;
                                    HashMap<String,String> dataMap11 = new HashMap<String, String>();
                                    dataMap11.put("Name" ,  PeriodName.getText().toString().trim().toUpperCase());
                                    ClassRef.setValue(dataMap11);
                                    Toast.makeText(Admin_Activity.this , "Period : " + PeriodName.getText().toString() + " Added" , Toast.LENGTH_SHORT).show();

                                }else{

                                    Toast.makeText(Admin_Activity.this , "No Period Added" , Toast.LENGTH_SHORT).show();
                                }


                            }
                        });


                        cancp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                perioddialog.dismiss();

                            }
                        });


                        break;

                    case R.id.DeletePeriods:


                        final AlertDialog.Builder delperiodBuilder = new AlertDialog.Builder(Admin_Activity.this)
                                .setCancelable(false);
                        View delperiodView = getLayoutInflater().inflate(R.layout.dialog_box_delete_periods, null);
                        final Spinner TimingsNameDelPeriod = (Spinner)delperiodView.findViewById(R.id.TimingsNameDelPeriod);
                        final Spinner PeriodsNameDelPeriod = (Spinner)delperiodView.findViewById(R.id.PeriodsNameDelPeriod);
                        final Button cancpe = (Button) delperiodView.findViewById(R.id.button19);
                        final Button okpe = (Button) delperiodView.findViewById(R.id.button20);

                        TimingsNameDelPeriod.setAdapter(adapterSpinnerTimingsNames);
                        PeriodsNameDelPeriod.setAdapter(adapterSpinnerPeriodNames);

                        delperiodBuilder.setView(delperiodView);
                        final AlertDialog DepPerdialog = delperiodBuilder.create();
                        DepPerdialog.show();


                        cancpe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                DepPerdialog.dismiss();
                            }
                        });

                        okpe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if( !PeriodNames.isEmpty()){

                                    int number = TimingsNameDelPeriod.getSelectedItemPosition();
                                    String Id = TimeNames.get(number).getId();
                                    int number2 = PeriodsNameDelPeriod.getSelectedItemPosition();
                                    String Id2 = PeriodNames.get(number2).getId();
                                    ClassRef = FirebaseDatabase.getInstance().getReference("Timings/" + Id + "/Periods/" + Id2  ) ;
                                    ClassRef.removeValue();
                                    Toast.makeText(Admin_Activity.this , "Period Deleted" , Toast.LENGTH_SHORT).show();

                                }else{

                                    Toast.makeText(Admin_Activity.this , "No Period Deleted" , Toast.LENGTH_LONG).show();
                                }


                            }
                        });

                        TimingsNameDelPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                int pos = TimingsNameDelPeriod.getSelectedItemPosition();
                                String Id = TimeNames.get(pos).getId();

                                PeriodNames.clear();
                                PeriodNamesString.clear();

                                adapterSpinnerTimingsNames.notifyDataSetChanged();
                                adapterSpinnerPeriodNames.notifyDataSetChanged();

                                ClassRef = FirebaseDatabase.getInstance().getReference("Timings/" + Id + "/Periods/") ;
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){

                                            PeriodNames.clear();
                                            PeriodNamesString.clear();
                                            adapterSpinnerPeriodNames.notifyDataSetChanged();

                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                                String Id = dataSnapshot1.getKey();

                                                if(!Id.equals("Name")){

                                                    String Name = dataSnapshot1.child("Name").getValue().toString();
                                                    PeriodNames.add(new NameIdClass(Name , Id));
                                                    PeriodNamesString.add(Name);
                                                    adapterSpinnerPeriodNames.notifyDataSetChanged();

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

                        break;


                }
                return true;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);
    }
    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        admin_home = new Admin_Home();
        admin_assignment = new Admin_Assignment();
        admin_attendance = new Admin_Attendance();
        admin_marksheet = new Admin_Marksheet();
        admin_notice = new Admin_Notice();

        adapter.addFragment(admin_notice);
        adapter.addFragment(admin_attendance);
        adapter.addFragment(admin_home);
        adapter.addFragment(admin_marksheet);
        adapter.addFragment(admin_assignment);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(2);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(myTog.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.HomeAdmin:
                viewPager.setCurrentItem(2);
                return true;
            case R.id.MarksheetAdmin:
                viewPager.setCurrentItem(3);
                return true;
            case R.id.AttAdmin:
                viewPager.setCurrentItem(1);
                return true;
            case R.id.NoticeAdmin:
                viewPager.setCurrentItem(0);
                return true;
            case R.id.AssignmentsAdmin:
                viewPager.setCurrentItem(4);
                return true;
        }
        return false;
    }
}
