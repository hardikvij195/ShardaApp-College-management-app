package com.hvtechnologies.shardaapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Dean_Activity_Check_Att_Today extends AppCompatActivity {


    //Spinner Sp1 , Sp2 ;
    TextView ch;
    ListView listView;

    private FirebaseAuth mAuth;
    private ProgressDialog mLoginProgress;

    DatabaseReference InfoRef , TTRef , ClassRef , TtOfTeacherOfSchool;

    String Name = "" , SchoolName = "" ,SchoolId = "" ;

    List<Dean_TT_Check_Class> mTeachTT  = new ArrayList<>() ;

    List<Dean_TT_Check_Class> mAttCheckTT  = new ArrayList<>() ;
    List<Dean_TT_Check_Class> mAttFilterTT  = new ArrayList<>() ;



    private ArrayList<NameIdClass> DeptNames = new ArrayList<>() ;
    private ArrayList<NameIdClass> YearNames = new ArrayList<>() ;
    private ArrayList<String> DeptNamesString = new ArrayList<>() ;
    private ArrayList<String> YearNamesString = new ArrayList<>() ;

    private ArrayAdapter<String> adapterSpinnerDeptNames ;
    private ArrayAdapter<String> adapterSpinnerYearNames ;


    DeanCheckAttListAdapter adapter1;
    List<DeanCheckAttClass> mCheckTT = new ArrayList<>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dean___check__att__today);



        listView = (ListView)findViewById(R.id.listTT);

        adapter1 = new DeanCheckAttListAdapter(this , mCheckTT );
        listView.setAdapter(adapter1);

        ch = (TextView)findViewById(R.id.textCheckTT);

        //adapterSpinnerDeptNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , DeptNamesString);
        //adapterSpinnerYearNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , YearNamesString);

        //Sp1.setAdapter(adapterSpinnerDeptNames);
        //Sp2.setAdapter(adapterSpinnerYearNames);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();



        InfoRef = FirebaseDatabase.getInstance().getReference("Users/DeanLogin/" + uid + "/") ;
        InfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {

                    Name = dataSnapshot.child("Name").getValue().toString();
                    SchoolName = dataSnapshot.child("SchoolName").getValue().toString();
                    SchoolId = dataSnapshot.child("SchoolId").getValue().toString();

                }
                if(SchoolId != ""){

                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + SchoolId + "/") ;
                    ClassRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){

                                DeptNames.clear();
                                DeptNamesString.clear();
                                YearNames.clear();
                                YearNamesString.clear();


                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                    String id = dataSnapshot1.getKey();

                                    if(!id.equals("Name")){

                                        String Name = dataSnapshot1.child("Name").getValue().toString();
                                        DeptNames.add(new NameIdClass(Name , id));
                                        DeptNamesString.add(Name);
                                    }
                                }
                                if(!DeptNames.isEmpty() && !DeptNamesString.isEmpty()) {


                                    YearNames.clear();
                                    YearNamesString.clear();

                                    //int pos = DeptNameDelYr.getSelectedItemPosition();
                                    String yrid = DeptNames.get(0).getId();

                                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + SchoolId + "/" + yrid + "/");
                                    ClassRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.exists()){

                                                YearNames.clear();
                                                YearNamesString.clear();


                                                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                                    String id = dataSnapshot2.getKey();

                                                    if (!id.equals("Name")) {

                                                        String Name = dataSnapshot2.child("Name").getValue().toString();
                                                        YearNames.add(new NameIdClass(Name , id));
                                                        YearNamesString.add(Name);


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
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {

            case Calendar.SUNDAY:





                break;


            case Calendar.MONDAY:


                mTeachTT.clear();

                TTRef = FirebaseDatabase.getInstance().getReference("TimeTable/TeacherWise/") ;
                TTRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        mTeachTT.clear();

                        if (dataSnapshot.exists()){

                            mTeachTT.clear();

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){


                                String tename = dataSnapshot1.child("TeacherName").getValue().toString();
                                String tecode = dataSnapshot1.child("TeacherCode").getValue().toString();
                                String teId = dataSnapshot1.child("TeacherId").getValue().toString();

                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("MONDAY").getChildren()){


                                    String Sch = dataSnapshot2.child("SchoolName").getValue().toString();

                                    if(Sch.equals(SchoolName)){

                                        String DeptId = dataSnapshot1.child("DeptId").getValue().toString();
                                        String YearId = dataSnapshot1.child("YearId").getValue().toString();
                                        String ClassId = dataSnapshot1.child("ClassId").getValue().toString();
                                        String DeptName = dataSnapshot1.child("DeptName").getValue().toString();
                                        String YearName = dataSnapshot1.child("YearName").getValue().toString();
                                        String ClassName = dataSnapshot1.child("ClassName").getValue().toString();
                                        String Sub = dataSnapshot1.child("Subject").getValue().toString();
                                        String SubCode = dataSnapshot1.child("SubjectCode").getValue().toString();
                                        String Time = dataSnapshot1.child("Time").getValue().toString();


                                        for ( DataSnapshot dataSnapshot3 : dataSnapshot2.child("Groups").getChildren()){

                                            String GroupId = dataSnapshot3.child("GroupId").getValue().toString();
                                            String GroupName = dataSnapshot3.child("GroupName").getValue().toString();

                                            for ( DataSnapshot dataSnapshot4 : dataSnapshot2.child("Periods").getChildren()){

                                                String PeriodName = dataSnapshot4.child("Name").getValue().toString();
                                                mTeachTT.add(new Dean_TT_Check_Class( DeptName , DeptId , YearName , YearId , ClassName ,ClassId , Time , GroupName , GroupId , Sub , SubCode , tename , tecode  , teId, PeriodName , "No"));

                                            }
                                        }
                                    }

                                }

                            }

                            if(!mTeachTT.isEmpty()){


                                Date date = new Date();  // to get the date
                                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd"); // getting date in this format
                                final String formattedDate = df.format(date.getTime());
                                String Id1 = SchoolId;

                                mAttFilterTT.clear();

                                ClassRef = FirebaseDatabase.getInstance().getReference("Attendance/GroupWise/" + Id1 + "/");
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                                //All Departments -------------
                                                String Id2 = dataSnapshot1.getKey();

                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                                                    //All Years ----------------
                                                    String Id3  = dataSnapshot2.getKey();

                                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()){
                                                        //All Classes -------------
                                                        String Id4 = dataSnapshot3.getKey();

                                                        for (DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()){
                                                            //All Groups --------------
                                                            String Id5 = dataSnapshot4.getKey();

                                                            for (DataSnapshot dataSnapshot5 : dataSnapshot4.getChildren()){

                                                                String IdFormattedDate = dataSnapshot5.getKey();
                                                                if(IdFormattedDate.contains(formattedDate)) {

                                                                    String SubName2 = dataSnapshot1.child("SubName").getValue().toString();
                                                                    String SubCode2 = dataSnapshot1.child("SubCode").getValue().toString();
                                                                    String ByName2 = dataSnapshot1.child("ByName").getValue().toString();
                                                                    String ByCode2 = dataSnapshot1.child("ByCode").getValue().toString();

                                                                    // Create A different List Of Attendance Marked Today ----------
                                                                    //mAttCheckTT.add(new Dean_TT_Check_Class("", Id2, "", Id3, "", Id4, "", "", Id5, SubName2, SubCode2, ByName2, ByCode2, "", ""));
                                                                    mAttFilterTT.add(new Dean_TT_Check_Class("", Id2, "", Id3, "", Id4, "", "", Id5, SubName2, SubCode2, ByName2, ByCode2, "", "No" , "No"));

                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }else {



                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });



                                for ( int i =0 ; i < mTeachTT.size() ; i++){


                                    String TeachId2 = mTeachTT.get(i).getDeptId();
                                    String TeachId3 = mTeachTT.get(i).getYearId();
                                    String TeachId4 = mTeachTT.get(i).getClassId();
                                    String TeachId5 = mTeachTT.get(i).getGroupId();

                                    for (int j = 0 ; j< mAttFilterTT.size() ; j++){



                                   //     String TeacherId = mTeachTT.get(i).getGroupId();
                                   //     String SubjectCode = mTeachTT.get(i).getGroupId();


                                        String AttId2 = mAttFilterTT.get(j).getDeptId();
                                        String AttId3 = mAttFilterTT.get(j).getYearId();
                                        String AttId4 = mAttFilterTT.get(j).getClassId();
                                        String AttId5 = mAttFilterTT.get(j).getGroupId();

                                        String AttTeacherId = mAttFilterTT.get(j).getGroupId();
                                        String AttSubjectId = mAttFilterTT.get(j).getGroupId();

                                        Toast.makeText(Dean_Activity_Check_Att_Today.this , " --- " , Toast.LENGTH_LONG).show();



                                        /*

                                        if(TeachId2.equals(AttId2) && TeachId3.equals(AttId3) && TeachId4.equals(AttId4) &&
                                                TeachId5.equals(AttId5) && TeacherId.equals(AttTeacherId)
                                                && SubjectCode.equals(AttSubjectId)
                                                && mTeachTT.get(i).getMarked().equals("No")
                                        && mAttFilterTT.get(j).getMarked().equals("No")){


                                            mTeachTT.get(i).setMarked("Yes");
                                            mAttFilterTT.get(j).setMarked("Yes");


                                        }

                                        */
                                    }

                                    for (int j = 0 ; j< mTeachTT.size() ; j++) {


                                        String DptName = mTeachTT.get(j).getDeptName();
                                        String YrName = mTeachTT.get(j).getYearName();
                                        String ClassName = mTeachTT.get(j).getClassName();
                                        String GrpName = mTeachTT.get(j).getGroupName();
                                        String SubName = mTeachTT.get(j).getSubjectName();
                                        String SubCode = mTeachTT.get(j).getSubjectCode();
                                        String TeacherName = mTeachTT.get(j).getTeacherName();
                                        String TeacherCode = mTeachTT.get(j).getTeacherCode();
                                        String Time = mTeachTT.get(j).getTime();

                                        if(mTeachTT.get(j).getMarked().equals("Yes")){


                                            //Add in green --------
                                            mCheckTT.add(new DeanCheckAttClass( DptName , YrName , SubCode , SubName , TeacherName , TeacherCode ,ClassName , GrpName , Time , "" , "Yes" ));


                                        }else {

                                            //Add in red -----------
                                            mCheckTT.add(new DeanCheckAttClass( DptName , YrName , SubCode , SubName , TeacherName , TeacherCode ,ClassName , GrpName , Time , "" , "No" ));

                                        }

                                    }


                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                break;

            case Calendar.TUESDAY:




                break;

            case Calendar.WEDNESDAY:



                mTeachTT.clear();

                TTRef = FirebaseDatabase.getInstance().getReference("TimeTable/TeacherWise/") ;
                TTRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        mTeachTT.clear();

                        if (dataSnapshot.exists()) {

                            mTeachTT.clear();

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                                String tename = dataSnapshot1.child("TeacherName").getValue().toString();
                                String tecode = dataSnapshot1.child("TeacherCode").getValue().toString();
                                String teId = dataSnapshot1.child("TeacherId").getValue().toString();

                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("WEDNESDAY").getChildren()) {


                                    String Sch = dataSnapshot2.child("SchoolName").getValue().toString();

                                    if (Sch.equals(SchoolName)) {


                                        String DeptId = dataSnapshot2.child("DeptId").getValue().toString();
                                        String YearId = dataSnapshot2.child("YearId").getValue().toString();
                                        String ClassId = dataSnapshot2.child("ClassId").getValue().toString();
                                        String DeptName = dataSnapshot2.child("DeptName").getValue().toString();
                                        String YearName = dataSnapshot2.child("YearName").getValue().toString();
                                        String ClassName = dataSnapshot2.child("ClassName").getValue().toString();
                                        String Sub = dataSnapshot2.child("Subject").getValue().toString();
                                        String SubCode = dataSnapshot2.child("SubjectCode").getValue().toString();
                                        String Time = dataSnapshot2.child("Time").getValue().toString();


                                        for (DataSnapshot dataSnapshot3 : dataSnapshot2.child("Groups").getChildren()) {

                                            String GroupId = dataSnapshot3.child("GroupId").getValue().toString();
                                            String GroupName = dataSnapshot3.child("GroupName").getValue().toString();

                                            for (DataSnapshot dataSnapshot4 : dataSnapshot2.child("Periods").getChildren()) {

                                                String PeriodName = dataSnapshot4.child("Name").getValue().toString();
                                                mTeachTT.add(new Dean_TT_Check_Class(DeptName, DeptId, YearName, YearId, ClassName, ClassId, Time, GroupName, GroupId, Sub, SubCode, tename, tecode, teId, PeriodName, "No"));

                                            }
                                        }
                                    }

                                }

                            }

                            if (!mTeachTT.isEmpty()) {


                                Date date = new Date();  // to get the date
                                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd"); // getting date in this format
                                final String formattedDate = df.format(date.getTime());
                                String Id1 = SchoolId;

                                mAttFilterTT.clear();

                                ClassRef = FirebaseDatabase.getInstance().getReference("Attendance/GroupWise/" + Id1 + "/");
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {

                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                //All Departments -------------
                                                String Id2 = dataSnapshot1.getKey();

                                                for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                                    //All Years ----------------
                                                    String Id3 = dataSnapshot2.getKey();

                                                    for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                                        //All Classes -------------
                                                        String Id4 = dataSnapshot3.getKey();

                                                        for (DataSnapshot dataSnapshot4 : dataSnapshot3.getChildren()) {
                                                            //All Groups --------------
                                                            String Id5 = dataSnapshot4.getKey();

                                                            for (DataSnapshot dataSnapshot5 : dataSnapshot4.getChildren()) {

                                                                String IdFormattedDate = dataSnapshot5.getKey();

                                                                if (IdFormattedDate.contains(formattedDate)) {

                                                                    String SubName2 = dataSnapshot5.child("SubName").getValue().toString();
                                                                    String SubCode2 = dataSnapshot5.child("SubCode").getValue().toString();
                                                                    String ByName2 = dataSnapshot5.child("ByName").getValue().toString();
                                                                    String ByCode2 = dataSnapshot5.child("ByCode").getValue().toString();

                                                                    // Create A different List Of Attendance Marked Today ----------
                                                                    //mAttCheckTT.add(new Dean_TT_Check_Class("", Id2, "", Id3, "", Id4, "", "", Id5, SubName2, SubCode2, ByName2, ByCode2, "", ""));
                                                                    mAttFilterTT.add(new Dean_TT_Check_Class("", Id2, "", Id3, "", Id4, "", "", Id5, SubName2, SubCode2, ByName2, ByCode2, "", "No", "No"));
                                                                    //adapter1.notifyDataSetChanged();

                                                                    //adapter1.notifyDataSetChanged();

                                                                }








                                                            }
                                                        }
                                                    }
                                                }
                                            }


                                            for (int i = 0; i < mTeachTT.size(); i++) {

                                                String TeachId2 = mTeachTT.get(i).getDeptId();
                                                String TeachId3 = mTeachTT.get(i).getYearId();
                                                String TeachId4 = mTeachTT.get(i).getClassId();
                                                String TeachId5 = mTeachTT.get(i).getGroupId();
                                                String TeachSubCode = mTeachTT.get(i).getSubjectCode();
                                                String TeachTeachCode = mTeachTT.get(i).getTeacherCode();

                                                String mrk = mTeachTT.get(i).getMarked();

                                                //mTeachTT.get(i).setMarked("Yes");

                                                if (mrk.equals("No") && !mAttFilterTT.isEmpty()) {


                                                    for (int j = 0; j < mAttFilterTT.size(); j++) {


                                                        String AttId2 = mAttFilterTT.get(j).getDeptId();
                                                        String AttId3 = mAttFilterTT.get(j).getYearId();
                                                        String AttId4 = mAttFilterTT.get(j).getClassId();
                                                        String AttId5 = mAttFilterTT.get(j).getGroupId();
                                                        String AttSubCode = mAttFilterTT.get(j).getSubjectCode();
                                                        String AttTeachCode = mAttFilterTT.get(j).getTeacherCode();

                                                        if (TeachId2.equals(AttId2) && TeachId3.equals(AttId3) && TeachId4.equals(AttId4)
                                                                && TeachId5.equals(AttId5) && TeachSubCode.equals(AttSubCode) && TeachTeachCode.equals(AttTeachCode)
                                                                && mTeachTT.get(i).getMarked().equals("No")
                                                                && mAttFilterTT.get(j).getMarked().equals("No")) {

                                                            {


                                                                mTeachTT.get(i).setMarked("Yes");
                                                                mAttFilterTT.get(j).setMarked("Yes");

                                                                adapter1.notifyDataSetChanged();

                                                            }
                                                        }
                                                    }

                                                }
                                            }


                                            mCheckTT.clear();

                                            for (int j = 0; j < mTeachTT.size(); j++) {


                                                String DptName = mTeachTT.get(j).getDeptName();
                                                String YrName = mTeachTT.get(j).getYearName();
                                                String ClassName = mTeachTT.get(j).getClassName();
                                                String GrpName = mTeachTT.get(j).getGroupName();
                                                String SubName = mTeachTT.get(j).getSubjectName();
                                                String SubCode = mTeachTT.get(j).getSubjectCode();
                                                String TeacherName = mTeachTT.get(j).getTeacherName();
                                                String TeacherCode = mTeachTT.get(j).getTeacherCode();
                                                String Time = mTeachTT.get(j).getTime();


                                                if (mTeachTT.get(j).getMarked().equals("Yes")) {


                                                    //Add in green --------
                                                    mCheckTT.add(new DeanCheckAttClass(DptName, YrName, SubCode, SubName, TeacherName, TeacherCode, ClassName, GrpName, Time, "", "Yes"));
                                                    adapter1.notifyDataSetChanged();

                                                } else {

                                                    //Add in red -----------
                                                    mCheckTT.add(new DeanCheckAttClass(DptName, YrName, SubCode, SubName, TeacherName, TeacherCode, ClassName, GrpName, Time, "", "No"));
                                                    adapter1.notifyDataSetChanged();
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



                break;

            case Calendar.THURSDAY:



                break;


            case Calendar.FRIDAY:


                break;

            case Calendar.SATURDAY:


                break;



        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                String TeachId2 = mTeachTT.get(position).getDeptId();
                String TeachId3 = mTeachTT.get(position).getYearId();
                String TeachId4 = mTeachTT.get(position).getClassId();
                String TeachId5 = mTeachTT.get(position).getGroupId();
                //String TeacherCode = mTeachTT.get(i).getTeacherCode();
                //String SubjectCode = mTeachTT.get(i).getSubjectCode();




                String AttId2 = mAttFilterTT.get(position).getDeptId();
                String AttId3 = mAttFilterTT.get(position).getYearId();
                String AttId4 = mAttFilterTT.get(position).getClassId();
                String AttId5 = mAttFilterTT.get(position).getGroupId();
                //String AttTeacherCode = mAttFilterTT.get(j).getTeacherCode();
                //String AttSubjectCode = mAttFilterTT.get(j).getSubjectCode();


                Toast.makeText(Dean_Activity_Check_Att_Today.this , TeachId2 + " - " + AttId2 + "\n" + TeachId3 + " - " + AttId3 + "\n" + TeachId4 + " - " + AttId4 + "\n" + TeachId5 + " - " + AttId5  , Toast.LENGTH_LONG).show();



            }
        });











        /*




        Sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(SchoolId != "") {

                    int pos2 = parent.getSelectedItemPosition();
                    final String Id2 = DeptNames.get(pos2).getId();

                    YearNames.clear();
                    YearNamesString.clear();

                    adapterSpinnerYearNames.notifyDataSetChanged();
                    adapterSpinnerDeptNames.notifyDataSetChanged();


                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + SchoolId + "/" + Id2 + "/") ;
                    ClassRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){

                                YearNames.clear();
                                YearNamesString.clear();
                                adapterSpinnerYearNames.notifyDataSetChanged();
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



        Sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {











            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });



        */





    }
}
