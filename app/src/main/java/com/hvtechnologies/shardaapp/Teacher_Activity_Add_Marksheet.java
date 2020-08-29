package com.hvtechnologies.shardaapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Teacher_Activity_Add_Marksheet extends AppCompatActivity {


    ArrayList<Teacher_Class_Names_Spinner> mClassList ;
    Teacher_Class_Names_Spinner_Adapter mAdap ;

    Spinner spinn1 , spinn2 ;

    Button SubmitBtn ;
    ListView listView ;

    DatabaseReference ClassRef , ClassRef2 ;

    private FirebaseAuth mAuth;

    private ArrayList<Teacher_GrpNames_Spinner> GroupNames1 = new ArrayList<>() ;
    private Teacher_Grp_Names_Spinner_Adapter adapterSpinnerGrpName ;

    private TakeMarksClassListAdapter adapter ;
    private List<TakeMarksClass> mMarksList = new ArrayList<TakeMarksClass>();


    TextInputLayout MarksheetNametxt , Markstxt ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher___add__marksheet);


        Utils.getDatabase();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTheme(R.style.AppTheme);

        mClassList = new ArrayList<>();
        spinn1 = (Spinner)findViewById(R.id.spinner1);
        spinn2 = (Spinner)findViewById(R.id.spinner2);
        listView = (ListView)findViewById(R.id.AttListView);
        SubmitBtn = (Button)findViewById(R.id.button2);
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        mAdap = new Teacher_Class_Names_Spinner_Adapter(this , mClassList);
        spinn1.setAdapter(mAdap);
        adapterSpinnerGrpName = new Teacher_Grp_Names_Spinner_Adapter(getApplicationContext(), GroupNames1);
        spinn2.setAdapter(adapterSpinnerGrpName);
        adapter = new TakeMarksClassListAdapter( getApplicationContext() , mMarksList );
        listView.setAdapter(adapter);

        MarksheetNametxt = (TextInputLayout) findViewById(R.id.MarksheetNameText);
        Markstxt = (TextInputLayout) findViewById(R.id.TotalMarksText);



        ClassRef2 = FirebaseDatabase.getInstance().getReference("Users/TeacherLogin/"+ uid + "/Subjects/" );
        ClassRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mClassList.clear();
                mAdap.notifyDataSetChanged();

                if(dataSnapshot.exists()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        String Subid = dataSnapshot1.getKey();
                        String SchName = dataSnapshot1.child("SchoolName").getValue().toString();
                        String SchId = dataSnapshot1.child("SchoolId").getValue().toString();

                        String DeptName = dataSnapshot1.child("DeptName").getValue().toString();
                        String DeptId = dataSnapshot1.child("DeptId").getValue().toString();

                        String YearName = dataSnapshot1.child("YearName").getValue().toString();
                        String YrId = dataSnapshot1.child("YearId").getValue().toString();

                        String ClassName = dataSnapshot1.child("ClassName").getValue().toString();
                        String ClassId = dataSnapshot1.child("ClassId").getValue().toString();

                        String SubName = dataSnapshot1.child("SubName").getValue().toString();
                        String SubCode = dataSnapshot1.child("SubCode").getValue().toString();

                        mClassList.add(new Teacher_Class_Names_Spinner(SchName , SchId , DeptName , DeptId , YearName , YrId , ClassName , ClassId , SubName , SubCode , Subid));
                        mAdap.notifyDataSetChanged();


                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        spinn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(!mClassList.isEmpty()){

                    Teacher_Class_Names_Spinner clickedItem = (Teacher_Class_Names_Spinner)parent.getItemAtPosition(position);
                    final String Id1 = clickedItem.getSchId();
                    final String Id2 = clickedItem.getDeptId();
                    final String Id3 = clickedItem.getYearId();
                    final String Id4 = clickedItem.getClassId();

                    ClassRef = FirebaseDatabase.getInstance().getReference("Structure/" + Id1 + "/" + Id2 + "/" + Id3 + "/Classes/" + Id4 + "/Groups/");
                    ClassRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){

                                GroupNames1.clear();

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                    String GroupId = dataSnapshot1.getKey();
                                    String GroupNm = dataSnapshot1.child("Name").getValue().toString();
                                    GroupNames1.add(new Teacher_GrpNames_Spinner(GroupNm , GroupId));
                                    adapterSpinnerGrpName.notifyDataSetChanged();

                                }

                            }

                            if(!mClassList.isEmpty()  && !GroupNames1.isEmpty()){


                                String Id5 = GroupNames1.get(0).getGrpId();
                                ClassRef = FirebaseDatabase.getInstance().getReference("Students/" + Id1 + "/" + Id2 + "/" + Id3 + "/" + Id4 + "/" + Id5);
                                ClassRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        mMarksList.clear();
                                        adapter.notifyDataSetChanged();

                                        if(dataSnapshot.exists()){

                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                                String name = dataSnapshot1.child("Name").getValue().toString();
                                                String id = dataSnapshot1.child("SystemId").getValue().toString();
                                                String StdId = dataSnapshot1.getKey();

                                                mMarksList.add(new TakeMarksClass(name , "0"  , id , StdId ));
                                                adapter.notifyDataSetChanged();
                                            }


                                            Collections.sort(mMarksList, new Comparator<TakeMarksClass>() {
                                                @Override
                                                public int compare(TakeMarksClass o1, TakeMarksClass o2) {
                                                    return o1.getName().compareToIgnoreCase(o2.getName());
                                                }
                                            });

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
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        spinn2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if(!GroupNames1.isEmpty()){

                    String Id5 = GroupNames1.get(position).getGrpId();
                    Teacher_Class_Names_Spinner clickedItem = (Teacher_Class_Names_Spinner)spinn1.getItemAtPosition(position);
                    String Id1 = clickedItem.getSchId();
                    String Id2 = clickedItem.getDeptId();
                    String Id3 = clickedItem.getYearId();
                    String Id4 = clickedItem.getClassId();

                    ClassRef = FirebaseDatabase.getInstance().getReference("Students/" + Id1 + "/" + Id2 + "/" + Id3 + "/" + Id4 + "/" + Id5);
                    ClassRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            mMarksList.clear();
                            adapter.notifyDataSetChanged();

                            if(dataSnapshot.exists()){

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                    String name = dataSnapshot1.child("Name").getValue().toString();
                                    String id = dataSnapshot1.child("SystemId").getValue().toString();
                                    String StdId = dataSnapshot1.getKey();

                                    mMarksList.add(new TakeMarksClass(name , "0"  , id , StdId ));
                                    adapter.notifyDataSetChanged();


                                }


                                Collections.sort(mMarksList, new Comparator<TakeMarksClass>() {
                                    @Override
                                    public int compare(TakeMarksClass o1, TakeMarksClass o2) {
                                        return o1.getName().compareToIgnoreCase(o2.getName());
                                    }
                                });

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {



                String Name = mMarksList.get(position).getName();
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Teacher_Activity_Add_Marksheet.this)
                        .setCancelable(false);
                View mView = getLayoutInflater().inflate(R.layout.dialog_box_enter_marks, null);
                final EditText Marks = (EditText) mView.findViewById(R.id.editTextEnterMARKS);
                final TextView NameOfStud = (TextView)mView.findViewById(R.id.textViewNameOfStudMARKS);
                NameOfStud.setText(Name);

                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        String mk = Marks.getText().toString() ;

                        if(mk.isEmpty()){

                            mMarksList.get(position).setMarks("0");
                            adapter.notifyDataSetChanged();

                        }else {

                            mMarksList.get(position).setMarks(mk);
                            adapter.notifyDataSetChanged();

                        }

                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();




            }
        });



        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Teacher_Activity_Add_Marksheet.this)
                        .setCancelable(false);
                mBuilder.setTitle("Confirm");
                mBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        if(!mClassList.isEmpty() && !GroupNames1.isEmpty() && !mMarksList.isEmpty()) {


                            Date date = new Date();  // to get the date
                            SimpleDateFormat du = new SimpleDateFormat("dd-MM-yyyy"); // getting date in this format
                            final String formattedDateRealDate = du.format(date.getTime());

                            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss"); // getting date in this format
                            final String formattedDate = df.format(date.getTime());

                            SimpleDateFormat dt = new SimpleDateFormat("HHmmss"); // getting date in this format
                            //final String formattedDate1 = dt.format(date.getTime());

                            SimpleDateFormat dtd = new SimpleDateFormat("HH:mm"); // getting date in this format
                            final String formattedDateRealTime = "Time : " + dtd.format(date.getTime());



                            int position = spinn2.getSelectedItemPosition();
                            String Id5 = GroupNames1.get(position).getGrpId();
                            String Nm5 = GroupNames1.get(position).getGrpName();

                            Teacher_Class_Names_Spinner clickedItem = (Teacher_Class_Names_Spinner)spinn1.getItemAtPosition(position);

                            String Id1 = clickedItem.getSchId();
                            String Nm1 = clickedItem.getSchName();

                            String Id2 = clickedItem.getDeptId();
                            String Nm2 = clickedItem.getDeptName();

                            String Id3 = clickedItem.getYearId();
                            String Nm3 = clickedItem.getYearName();

                            String Id4 = clickedItem.getClassId();
                            String Nm4 = clickedItem.getClassName();

                            String subId = clickedItem.getSubjectId();
                            String subCd = clickedItem.getSubjectCode();
                            String subNm = clickedItem.getSubjectName();



                            SharedPreferences Pref = getSharedPreferences("userinfo", MODE_PRIVATE);
                            String NameT = Pref.getString("NAME", "");
                            String CodeT = Pref.getString("CODE", "");
                            String SysIdT = Pref.getString("SYSID", "");


                            final String TotalMarks = Markstxt.getEditText().getText().toString().trim();
                            final String MarksheetName = MarksheetNametxt.getEditText().getText().toString().trim();


                            ClassRef = FirebaseDatabase.getInstance().getReference("Attendance/GroupWise/" + Id1 + "/" + Id2 + "/" + Id3 + "/" + Id4 + "/" + Id5 + "/" + formattedDate + "/");
                            HashMap<String, String> AttDt = new HashMap<String, String>();
                            AttDt.put("Date", formattedDateRealDate);
                            AttDt.put("SubName", subNm);
                            AttDt.put("SubCode", subCd);
                            AttDt.put("SubId", subId);
                            AttDt.put("ByName", NameT);
                            AttDt.put("ByCode", CodeT);
                            AttDt.put("BySys", SysIdT);
                            AttDt.put("MName" , MarksheetName);
                            AttDt.put("MTotal" , TotalMarks);
                            ClassRef.setValue(AttDt);



                            for (int i = 0; i < mMarksList.size(); i++) {

                                String Name = mMarksList.get(i).getName();
                                String Userid = mMarksList.get(i).getStdId();
                                String Marks = mMarksList.get(i).getMarks();
                                String SysId = mMarksList.get(i).getSysId();

                                ClassRef = FirebaseDatabase.getInstance().getReference("Marksheets/GroupWise/" + Id1 + "/" + Id2 + "/" + Id3 + "/" + Id4 + "/" + Id5 + "/" + formattedDate + "/Marks/" + Userid);
                                HashMap<String, String> StudentAttendencDateWise = new HashMap<String, String>();
                                StudentAttendencDateWise.put("StudentName" , Name);
                                StudentAttendencDateWise.put("Name", MarksheetName);
                                StudentAttendencDateWise.put("StdId", Userid);
                                StudentAttendencDateWise.put("Marks", Marks);
                                StudentAttendencDateWise.put("SystemId", SysId);
                                StudentAttendencDateWise.put("Date", formattedDateRealDate);
                                StudentAttendencDateWise.put("SubId", subId);
                                StudentAttendencDateWise.put("SubCode",subCd );
                                StudentAttendencDateWise.put("SubName", subNm);
                                ClassRef.setValue(StudentAttendencDateWise);

                                ClassRef2 = FirebaseDatabase.getInstance().getReference("Marksheets/StudentWise/" + Id1 + "/" + Id2 + "/" + Id3 + "/" + Id4 + "/" + Id5 + "/Att/" + Userid + "/"+  formattedDate + "/"  );
                                HashMap<String, String> Mrk = new HashMap<String, String>();
                                Mrk.put("StudentName", Name);
                                Mrk.put("Name", MarksheetName);
                                Mrk.put("Marks", Marks);
                                Mrk.put("Total", TotalMarks);
                                Mrk.put("Date", formattedDateRealDate);
                                Mrk.put("SubCode",subCd );
                                Mrk.put("StdId", Userid);
                                Mrk.put("SystemId" ,SysId );
                                Mrk.put("SubName", subNm);
                                Mrk.put("ByName", NameT);
                                Mrk.put("ByCode", CodeT);
                                ClassRef2.setValue(Mrk);

                            }



                        }
                    }
                });


                mBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });

                AlertDialog dialog = mBuilder.create();
                dialog.show();















            }
        });




    }


}
