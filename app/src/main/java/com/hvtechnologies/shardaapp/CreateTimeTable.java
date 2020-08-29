package com.hvtechnologies.shardaapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateTimeTable extends AppCompatActivity {


    private ArrayList<NameIdClass> DaysNames = new ArrayList<>() ;
    private ArrayList<String> DaysNamesString = new ArrayList<>() ;

    private ArrayList<NameIdClass> TimeNames = new ArrayList<>() ;
    private ArrayList<String> TimeNamesString = new ArrayList<>() ;

    private ArrayList<NameIdClass> PeriodNames = new ArrayList<>() ;
    private ArrayList<String> PeriodNamesString = new ArrayList<>() ;


    private ArrayList<NameIdCodeClass>  TeacherName = new ArrayList<>() ;
    private ArrayList<String> TeacherNamesString = new ArrayList<>() ;

    private ArrayList<SubjectNameIdCodeClass>  SubjectNames = new ArrayList<>() ;
    private ArrayList<String> SubjectNamesString = new ArrayList<>() ;


    Button SelectDay , SelectTime , SelectTeacher , Submit , SelectGroup  , SelectSubject;

    TextInputEditText Loc ;
    TextView Details , GroupDetails;

    Spinner School , Dept , Year , Class  ;

    DatabaseReference TimingsNameRef , DaysNamesRef , SchoolNamesRef , ClassRef , TeacherRef ;

    private ArrayList<NameIdClass> SchoolNames = new ArrayList<>() ;
    private ArrayList<NameIdClass> DeptNames = new ArrayList<>() ;
    private ArrayList<NameIdClass> YearNames = new ArrayList<>() ;
    private ArrayList<NameIdClass> ClassNames = new ArrayList<>() ;
    private ArrayList<NameIdClass> GroupNames = new ArrayList<>() ;
    private ArrayList<NameIdClass> mSelectedGroupNames = new ArrayList<>() ;

    private ArrayList<String> SchoolNamesString = new ArrayList<>() ;
    private ArrayList<String> DeptNamesString = new ArrayList<>() ;
    private ArrayList<String> YearNamesString = new ArrayList<>() ;
    private ArrayList<String> ClassNamesString = new ArrayList<>() ;
    private ArrayList<String> GroupNamesString = new ArrayList<>() ;
    private ArrayList<String> mSelectedGroupNamesString = new ArrayList<>() ;

    private ArrayAdapter<String> adapterSpinnerSchoolNames ;
    private ArrayAdapter<String> adapterSpinnerDeptNames ;
    private ArrayAdapter<String> adapterSpinnerYearNames ;
    private ArrayAdapter<String> adapterSpinnerClassNames ;
    private ArrayAdapter<String> adapterSpinnerGroupNames ;

    String SelectedDayName = null ;
    String SelectedDayId = null ;

    String SelectedTimeName = null ;
    String SelectedTimeId = null ;

    String SelectedTeacherName = null ;
    String SelectedTeacherId = null ;
    String SelectedTeacherCode = null ;

    String SelectedSubjectName = null ;
    String SelectedSubjectCode = null ;
    String SelectedSubjectId = null ;


    TextView DaySelectedTxt , TimeSelectedTxt  , SubjectSelectedTxt ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_time_table);


        Utils.getDatabase();
        this.setTheme(R.style.AlertDialogCustom);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
        }
        else{
            Toast.makeText(CreateTimeTable.this , "Please Check Your Internet Connection" , Toast.LENGTH_LONG).show();
        }


        DaySelectedTxt = (TextView)findViewById(R.id.DayText);
        TimeSelectedTxt = (TextView)findViewById(R.id.TimeText);
        SubjectSelectedTxt = (TextView)findViewById(R.id.textSubjectDetails);

        Loc = (TextInputEditText)findViewById(R.id.TTLoc);
        Details = (TextView)findViewById(R.id.textDetails);

        GroupDetails = (TextView)findViewById(R.id.textGroupDetails);

        SelectTeacher = (Button)findViewById(R.id.SelectTeacher);
        Submit = (Button)findViewById(R.id.Submit);
        SelectDay = (Button)findViewById(R.id.SelectDay);
        SelectTime = (Button)findViewById(R.id.SelectTime);
        SelectGroup = (Button)findViewById(R.id.SelectGroups);
        SelectSubject = (Button)findViewById(R.id.SelectSubject);

        School = (Spinner)findViewById(R.id.SchoolNameCreateTT);
        Dept = (Spinner)findViewById(R.id.DeptNameCreateTT);
        Year = (Spinner)findViewById(R.id.YearNameCreateTT);
        Class = (Spinner)findViewById(R.id.ClassNameCreateTT);


        adapterSpinnerSchoolNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , SchoolNamesString);
        adapterSpinnerDeptNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , DeptNamesString);
        adapterSpinnerYearNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , YearNamesString);
        adapterSpinnerClassNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , ClassNamesString);
        adapterSpinnerGroupNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , GroupNamesString);


        School.setAdapter(adapterSpinnerSchoolNames);
        Dept.setAdapter(adapterSpinnerDeptNames);
        Year.setAdapter(adapterSpinnerYearNames);
        Class.setAdapter(adapterSpinnerClassNames);



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



        TeacherRef = FirebaseDatabase.getInstance().getReference("Teachers/") ;
        TeacherRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                TeacherName.clear();
                TeacherNamesString.clear();

                if(dataSnapshot.exists()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        String Id = dataSnapshot1.child("TeacherId").getValue().toString();
                        String Code = dataSnapshot1.child("Code").getValue().toString();
                        String Name = dataSnapshot1.child("Name").getValue().toString();
                        TeacherName.add(new NameIdCodeClass(Name , Id , Code ));
                        TeacherNamesString.add(Name + " - " + Code);

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        DaysNamesRef = FirebaseDatabase.getInstance().getReference("Days/") ;
        DaysNamesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DaysNames.clear();
                DaysNamesString.clear();

                if(dataSnapshot.exists()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        String id = dataSnapshot1.getKey();
                        String Name = dataSnapshot1.child("Day").getValue().toString();
                        DaysNames.add(new NameIdClass(Name , id));
                        DaysNamesString.add(Name);

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        SelectDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder SelectDaysCreateTTbuilder = new AlertDialog.Builder(CreateTimeTable.this);
                SelectDaysCreateTTbuilder.setCancelable(false);
                // Set the dialog title

                SelectDaysCreateTTbuilder.setTitle("Choose Groups")
                        // Specify the list array, the items to be selected by default (null for none),
                        // and the listener through which to receive callbacks when items are selected
                        .setSingleChoiceItems(DaysNamesString.toArray(new String[DaysNamesString.size()]), -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                SelectedDayName = DaysNames.get(which).getName();
                                SelectedDayId = DaysNames.get(which).getId();
                                DaySelectedTxt.setText(SelectedDayName);
                                dialog.dismiss();
                            }
                        });

                AlertDialog mDialog = SelectDaysCreateTTbuilder.create();
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();


            }
        });



        TimingsNameRef = FirebaseDatabase.getInstance().getReference("Timings/");
        TimingsNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                TimeNames.clear();
                TimeNamesString.clear();

                if(dataSnapshot.exists()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        String id = dataSnapshot1.getKey();
                        String Start = dataSnapshot1.child("Start").getValue().toString();
                        String End = dataSnapshot1.child("End").getValue().toString();

                        TimeNames.add(new NameIdClass(Start + " - " + End  , id));
                        TimeNamesString.add(Start + " - " + End);

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        SelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder SelectDaysCreateTTbuilder = new AlertDialog.Builder(CreateTimeTable.this);
                SelectDaysCreateTTbuilder.setCancelable(false);
                // Set the dialog title

                SelectDaysCreateTTbuilder.setTitle("Choose Groups")
                        // Specify the list array, the items to be selected by default (null for none),
                        // and the listener through which to receive callbacks when items are selected
                        .setSingleChoiceItems(TimeNamesString.toArray(new String[TimeNamesString.size()]), -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                SelectedTimeName = TimeNames.get(which).getName();
                                SelectedTimeId = TimeNames.get(which).getId();
                                TimeSelectedTxt.setText(SelectedTimeName);


                                TimingsNameRef = FirebaseDatabase.getInstance().getReference("Timings/" + SelectedTimeId + "/Periods/");
                                TimingsNameRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        PeriodNames.clear();
                                        PeriodNamesString.clear();

                                        if(dataSnapshot.exists()){

                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                                String id = dataSnapshot1.getKey();
                                                String Name = dataSnapshot1.child("Name").getValue().toString();

                                                PeriodNames.add(new NameIdClass(Name  , id));
                                                PeriodNamesString.add(Name);

                                            }
                                            if(!PeriodNamesString.isEmpty()){


                                                String s = "";

                                                for (int i = 0 ; i <PeriodNamesString.size() ; i++){

                                                    s = s + PeriodNamesString.get(i).toString();
                                                }

                                                TimeSelectedTxt.setText(SelectedTimeName + " / " + s);



                                            }

                                        }


                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                dialog.dismiss();

                            }
                        });

                AlertDialog mDialog = SelectDaysCreateTTbuilder.create();
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();


            }
        });




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


        School.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int pos = School.getSelectedItemPosition();
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

                                                                SubjectNames.clear();
                                                                SubjectNamesString.clear();


                                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + deid + "/" + yrid + "/Classes/" + Id3 + "/Subjects/" ) ;
                                                                ClassRef.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                                        if(dataSnapshot.exists()){


                                                                            SubjectNames.clear();
                                                                            SubjectNamesString.clear();

                                                                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                                                String id = dataSnapshot2.getKey();

                                                                                if (!id.equals("Name") ) {

                                                                                    String Name = dataSnapshot2.child("Name").getValue().toString();
                                                                                    String Code = dataSnapshot2.child("Code").getValue().toString();

                                                                                    SubjectNames.add(new SubjectNameIdCodeClass(Name , id , Code));
                                                                                    SubjectNamesString.add(Name);

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

        Dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                int pos = School.getSelectedItemPosition();
                final String Id = SchoolNames.get(pos).getId();

                int pos2 = Dept.getSelectedItemPosition();
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


                                                SubjectNames.clear();
                                                SubjectNamesString.clear();


                                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2 + "/" + yrid + "/Classes/" + Id3 + "/Subjects/" ) ;
                                                ClassRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        if(dataSnapshot.exists()){


                                                            SubjectNames.clear();
                                                            SubjectNamesString.clear();

                                                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                                String id = dataSnapshot2.getKey();

                                                                if (!id.equals("Name") ) {

                                                                    String Name = dataSnapshot2.child("Name").getValue().toString();
                                                                    String Code = dataSnapshot2.child("Code").getValue().toString();

                                                                    SubjectNames.add(new SubjectNameIdCodeClass(Name , id , Code));
                                                                    SubjectNamesString.add(Name);

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


        Year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                int pos = School.getSelectedItemPosition();
                final String Id = SchoolNames.get(pos).getId();

                int pos2 = Dept.getSelectedItemPosition();
                final String Id2 = DeptNames.get(pos2).getId();

                int pos3 = Year.getSelectedItemPosition();
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



                                SubjectNames.clear();
                                SubjectNamesString.clear();


                                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + Id2 + "/" + Id3 + "/Classes/" + Id4 + "/Subjects/" ) ;
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.exists()){


                                            SubjectNames.clear();
                                            SubjectNamesString.clear();

                                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                String id = dataSnapshot2.getKey();

                                                if (!id.equals("Name") ) {

                                                    String Name = dataSnapshot2.child("Name").getValue().toString();
                                                    String Code = dataSnapshot2.child("Code").getValue().toString();

                                                    SubjectNames.add(new SubjectNameIdCodeClass(Name , id , Code));
                                                    SubjectNamesString.add(Name);
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

        Class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                int pos1 = School.getSelectedItemPosition();
                final String Id = SchoolNames.get(pos1).getId();

                int pos = Dept.getSelectedItemPosition();
                final String did = DeptNames.get(pos).getId();

                int pos2 = Year.getSelectedItemPosition();
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



                SubjectNames.clear();
                SubjectNamesString.clear();


                ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id + "/" + did + "/" + yrid + "/Classes/" + Id3 + "/Subjects/" ) ;
                ClassRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){


                            SubjectNames.clear();
                            SubjectNamesString.clear();
                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                String id = dataSnapshot2.getKey();

                                if (!id.equals("Name") ) {

                                    String Name = dataSnapshot2.child("Name").getValue().toString();
                                    String Code = dataSnapshot2.child("Code").getValue().toString();

                                    SubjectNames.add(new SubjectNameIdCodeClass(Name , id , Code));
                                    SubjectNamesString.add(Name);
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



        SelectGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                mSelectedGroupNames.clear();
                mSelectedGroupNamesString.clear();
                AlertDialog.Builder SelectClassesAddSubjectbuilder = new AlertDialog.Builder(CreateTimeTable.this);
                SelectClassesAddSubjectbuilder.setCancelable(false);
                // Set the dialog title

                SelectClassesAddSubjectbuilder.setTitle("Choose Groups")
                        // Specify the list array, the items to be selected by default (null for none),
                        // and the listener through which to receive callbacks when items are selected
                        .setMultiChoiceItems(GroupNamesString.toArray(new String[GroupNamesString.size()]), null,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                        if (isChecked) {
                                            // If the user checked the item, add it to the selected items
                                            if ( !mSelectedGroupNamesString.contains(GroupNamesString.get(which)) ) {
                                                mSelectedGroupNames.add(GroupNames.get(which));
                                                mSelectedGroupNamesString.add(GroupNamesString.get(which));
                                            }
                                        }else if (mSelectedGroupNamesString.contains(GroupNamesString.get(which))) {
                                            mSelectedGroupNames.remove(GroupNames.get(which));
                                            mSelectedGroupNamesString.remove(GroupNamesString.get(which));
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
                        for (int i = 0 ; i < mSelectedGroupNamesString.size() ; i++){
                            item = item + mSelectedGroupNamesString.get(i);
                            if(i != mSelectedGroupNamesString.size() - 1){
                                item = item + ",\n" ;
                            }
                        }
                        if(item.isEmpty()){
                            GroupDetails.setText("No Groups Selected");
                        }else {
                            GroupDetails.setText("Groups Selected :" + item);
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


        SelectTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder SelectDaysCreateTTbuilder = new AlertDialog.Builder(CreateTimeTable.this);
                SelectDaysCreateTTbuilder.setCancelable(false);
                // Set the dialog title

                SelectDaysCreateTTbuilder.setTitle("Choose Teacher")
                        // Specify the list array, the items to be selected by default (null for none),
                        // and the listener through which to receive callbacks when items are selected
                        .setSingleChoiceItems(TeacherNamesString.toArray(new String[TeacherNamesString.size()]), -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                SelectedTeacherName = TeacherName.get(which).getName();
                                SelectedTeacherId = TeacherName.get(which).getId();
                                SelectedTeacherCode = TeacherName.get(which).getCode();

                                Details.setText(SelectedTeacherName + " - " + SelectedTeacherCode);
                                dialog.dismiss();
                            }
                        });

                SelectDaysCreateTTbuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SelectedTeacherName = "";
                        SelectedTeacherId = "" ;
                        Details.setText("No Teacher Selected");
                        dialog.dismiss();
                    }
                });


                AlertDialog mDialog = SelectDaysCreateTTbuilder.create();
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();


            }
        });


        SelectSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder SelectDaysCreateTTbuilder = new AlertDialog.Builder(CreateTimeTable.this);
                SelectDaysCreateTTbuilder.setCancelable(false);
                // Set the dialog title

                SelectDaysCreateTTbuilder.setTitle("Choose Subject")
                        // Specify the list array, the items to be selected by default (null for none),
                        // and the listener through which to receive callbacks when items are selected
                        .setSingleChoiceItems(SubjectNamesString.toArray(new String[SubjectNamesString.size()]), -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                SelectedSubjectName = SubjectNames.get(which).getName();
                                SelectedSubjectCode = SubjectNames.get(which).getCode();
                                SelectedSubjectId = SubjectNames.get(which).getId();

                                SubjectSelectedTxt.setText(SelectedSubjectName+ " - " + SelectedSubjectCode);
                                dialog.dismiss();
                            }
                        });


                SelectDaysCreateTTbuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SelectedSubjectName = "";
                        SelectedSubjectId = "";
                        SelectedSubjectCode = "" ;
                        SubjectSelectedTxt.setText("No Subject Selected");
                        dialog.dismiss();

                    }
                });


                AlertDialog mDialog = SelectDaysCreateTTbuilder.create();
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();




            }
        });



        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(SelectedDayId != null && SelectedDayName != null && SelectedTimeName != null &&
                        SelectedTimeId != null && SelectedTeacherName != null &&
                        SelectedTeacherId != null && SelectedSubjectName != null &&
                        SelectedSubjectId != null && !SchoolNamesString.isEmpty() &&!DeptNamesString.isEmpty() &&
                        !YearNamesString.isEmpty() && !ClassNamesString.isEmpty() && !mSelectedGroupNames.isEmpty() && !Loc.getText().toString().isEmpty()
                        && !PeriodNames.isEmpty() && SelectedTeacherCode != null)
                {



                    int pos1 = School.getSelectedItemPosition();
                    final String Id1 = SchoolNames.get(pos1).getId();
                    final String schname = SchoolNames.get(pos1).getName();

                    int pos2 = Dept.getSelectedItemPosition();
                    final String Id2 = DeptNames.get(pos2).getId();
                    final String deptname = DeptNames.get(pos1).getName();

                    int pos3 = Year.getSelectedItemPosition();
                    final String Id3 = YearNames.get(pos3).getId();
                    final String yrname = YearNames.get(pos1).getName();

                    int pos4 = Class.getSelectedItemPosition();
                    final String Id4 = ClassNames.get(pos4).getId();
                    final String clsname = ClassNames.get(pos1).getName();

                    String nmn = "" ;


                    ClassRef = FirebaseDatabase.getInstance().getReference("TimeTable/TeacherWise/"+ SelectedTeacherId);
                    ClassRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            if(dataSnapshot.exists()){


                                ClassRef = FirebaseDatabase.getInstance().getReference("TimeTable/TeacherWise/"+ SelectedTeacherId + "/" + SelectedDayName + "/" + SelectedTimeId + "/" );
                                HashMap<String, String> dataMap2 = new HashMap<String, String>();
                                dataMap2.put("Time", SelectedTimeName );
                                dataMap2.put("Subject", SelectedSubjectName);
                                dataMap2.put("SubjectCode", SelectedSubjectCode);
                                dataMap2.put("SubjectId", SelectedSubjectId);
                                dataMap2.put("Loc", Loc.getText().toString());
                                dataMap2.put("SchoolName", schname);
                                dataMap2.put("SchoolId", Id1);
                                dataMap2.put("DeptName", deptname);
                                dataMap2.put("DeptId", Id2);
                                dataMap2.put("YearName", yrname);
                                dataMap2.put("YearId", Id3);
                                dataMap2.put("ClassName", clsname);
                                dataMap2.put("ClassId", Id4);
                                ClassRef.setValue(dataMap2);

                                for(int i=0 ; i<mSelectedGroupNames.size() ; i++)
                                {
                                    String Id5 = mSelectedGroupNames.get(i).getId();
                                    String nm = mSelectedGroupNames.get(i).getName();

                                    ClassRef = FirebaseDatabase.getInstance().getReference("TimeTable/TeacherWise/"+ SelectedTeacherId + "/" + SelectedDayName + "/" + SelectedTimeId + "/Groups/" + Id5 );
                                    HashMap<String, String> dataMap5 = new HashMap<String, String>();
                                    dataMap5.put("GroupName", nm);
                                    dataMap5.put("GroupId", Id5);
                                    ClassRef.setValue(dataMap5);

                                }

                                for (int j = 0 ; j < PeriodNames.size() ; j++){

                                    String Name = PeriodNames.get(j).getName();
                                    String Id6 = PeriodNames.get(j).getId();
                                    ClassRef = FirebaseDatabase.getInstance().getReference("TimeTable/TeacherWise/"+ SelectedTeacherId + "/" + SelectedDayName + "/" + SelectedTimeId + "/Periods/" + Id6 );
                                    HashMap<String, String> dataMap31 = new HashMap<String, String>();
                                    dataMap31.put("Name", Name );
                                    ClassRef.setValue(dataMap31);

                                }





                            }else {

                                ClassRef = FirebaseDatabase.getInstance().getReference("TimeTable/TeacherWise/"+ SelectedTeacherId);
                                HashMap<String, String> dataMap3 = new HashMap<String, String>();
                                dataMap3.put("TeacherName", SelectedTeacherName );
                                dataMap3.put("TeacherCode", SelectedTeacherCode );
                                dataMap3.put("TeacherId", SelectedTeacherId );
                                ClassRef.setValue(dataMap3);

                                ClassRef = FirebaseDatabase.getInstance().getReference("TimeTable/TeacherWise/"+ SelectedTeacherId + "/" + SelectedDayName + "/" + SelectedTimeId + "/" );
                                HashMap<String, String> dataMap2 = new HashMap<String, String>();
                                dataMap2.put("Time", SelectedTimeName );
                                dataMap2.put("Subject", SelectedSubjectName);
                                dataMap2.put("SubjectCode", SelectedSubjectCode);
                                dataMap2.put("SubjectId", SelectedSubjectId);
                                dataMap2.put("Loc", Loc.getText().toString());
                                dataMap2.put("SchoolName", schname);
                                dataMap2.put("SchoolId", Id1);
                                dataMap2.put("DeptName", deptname);
                                dataMap2.put("DeptId", Id2);
                                dataMap2.put("YearName", yrname);
                                dataMap2.put("YearId", Id3);
                                dataMap2.put("ClassName", clsname);
                                dataMap2.put("ClassId", Id4);
                                ClassRef.setValue(dataMap2);

                                for(int i=0 ; i<mSelectedGroupNames.size() ; i++)
                                {
                                    String Id5 = mSelectedGroupNames.get(i).getId();
                                    String nm = mSelectedGroupNames.get(i).getName();

                                    ClassRef = FirebaseDatabase.getInstance().getReference("TimeTable/TeacherWise/"+ SelectedTeacherId + "/" + SelectedDayName + "/" + SelectedTimeId + "/Groups/" + Id5 );
                                    HashMap<String, String> dataMap5 = new HashMap<String, String>();
                                    dataMap5.put("GroupName", nm);
                                    dataMap5.put("GroupId", Id5);
                                    ClassRef.setValue(dataMap5);

                                }

                                for (int j = 0 ; j < PeriodNames.size() ; j++){

                                    String Name = PeriodNames.get(j).getName();
                                    String Id6 = PeriodNames.get(j).getId();
                                    ClassRef = FirebaseDatabase.getInstance().getReference("TimeTable/TeacherWise/"+ SelectedTeacherId + "/" + SelectedDayName + "/" + SelectedTimeId + "/Periods/" + Id6 );
                                    HashMap<String, String> dataMap31 = new HashMap<String, String>();
                                    dataMap31.put("Name", Name );
                                    ClassRef.setValue(dataMap31);

                                }




                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });





                    for(int i=0 ; i<mSelectedGroupNames.size() ; i++)
                    {

                        String Id5 = mSelectedGroupNames.get(i).getId();
                        String nm = mSelectedGroupNames.get(i).getName();

                        nmn = nmn + mSelectedGroupNamesString.get(i);
                        if(i != mSelectedGroupNames.size() - 1)
                            nmn = nm + ", " ;


                        ClassRef = FirebaseDatabase.getInstance().getReference("TimeTable/ClassWise/"  + Id1 + "/" + Id2 + "/" + Id3 + "/" + Id4 + "/" + Id5 + "/" + SelectedDayName + "/"  + SelectedTimeId + "/" );
                        HashMap<String, String> dataMap = new HashMap<String, String>();
                        dataMap.put("Time", SelectedTimeName );
                        dataMap.put("Teacher", SelectedTeacherName);
                        dataMap.put("Subject", SelectedSubjectName);
                        dataMap.put("Loc", Loc.getText().toString());
                        dataMap.put("SubjectCode", SelectedSubjectCode);
                        ClassRef.setValue(dataMap);


                        for (int j = 0 ; j < PeriodNames.size() ; j++){

                            String Name = PeriodNames.get(j).getName();
                            String Id6 = PeriodNames.get(j).getId();
                            ClassRef = FirebaseDatabase.getInstance().getReference("TimeTable/ClassWise/"  + Id1 + "/" + Id2 + "/" + Id3 + "/" + Id4 + "/" + Id5 + "/" + SelectedDayName + "/"  + SelectedTimeId + "/Periods/" + Id6 );
                            HashMap<String, String> dataMap3 = new HashMap<String, String>();
                            dataMap3.put("Name", Name );
                            ClassRef.setValue(dataMap3);

                        }


                    }



                    Toast.makeText(CreateTimeTable.this , "Time Table Uploaded" , Toast.LENGTH_SHORT).show();


                }else {

                    Toast.makeText(CreateTimeTable.this , "Select Everything" , Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();



    }
}
