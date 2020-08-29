package com.hvtechnologies.shardaapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import static java.lang.Compiler.disable;


public class Teacher_Activity_Add_Assignment extends AppCompatActivity {


    Button btnfile , upload ;
    private static final int FILE_SELECT_CODE = 0;

    TextView File_Location ;

    private ProgressDialog mLoginProgress;


    ProgressDialog progressDialog;

    FirebaseDatabase mDatabase ;
    FirebaseStorage mStorage ;

    DatabaseReference AssignmentRef ;

    Uri pdfUri;

    TextInputEditText Name , Description ;

    private ProgressDialog mProgress;

    public String DownloadUrl1 ;
    String tutname = null;
    String phnum = null ;

    public String Tp , Des ;

    ArrayList<Teacher_Class_Names_Spinner> mClassList ;
    Teacher_Class_Names_Spinner_Adapter mAdap ;

    Spinner spinn1 ;


    private ArrayList<String> GroupNames1 = new ArrayList<>() ;
    private ArrayList<NameIdClass> GroupNames = new ArrayList<>() ;
    private ArrayList<AttendanceClass> mStudList = new ArrayList<>() ;
    Attendance_Class_List_Adap2 adapter;


    DatabaseReference ClassRef , ClassRef2 ;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher___add__assignment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        btnfile = (Button)findViewById(R.id.Select_File_Upload_Pdf_Teacher_Activity);

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

        upload = (Button)findViewById(R.id.Upload_File_Upload_Pdf_Teacher_Activity);



        mProgress = new ProgressDialog(this , R.style.StyledDialog);


        mLoginProgress = new ProgressDialog(this);

        File_Location =(TextView)findViewById(R.id.FileTextViewTeacherActivity);


        Name = (TextInputEditText) findViewById(R.id.Namee);
        Description = (TextInputEditText) findViewById(R.id.Descc);

        mStorage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance();


        mClassList = new ArrayList<>();
        spinn1 = (Spinner)findViewById(R.id.spinner1);
        mAdap = new Teacher_Class_Names_Spinner_Adapter(this , mClassList);
        spinn1.setAdapter(mAdap);
        adapter = new Attendance_Class_List_Adap2( getApplicationContext() , mStudList );


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(Teacher_Activity_Add_Assignment.this)
                        .setCancelable(false);
                mBuilder.setTitle("Confirm");
                mBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        disable();

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





        btnfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(ContextCompat.checkSelfPermission(Teacher_Activity_Add_Assignment.this , android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){


                    SelectPdf();

                }else {

                    ActivityCompat.requestPermissions(Teacher_Activity_Add_Assignment.this , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                    //SelectPdf();
                }



            }
        });




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

                        //String ClassName = dataSnapshot1.child("ClassName").getValue().toString();
                        //String ClassId = dataSnapshot1.child("ClassId").getValue().toString();

                        String SubName = dataSnapshot1.child("SubName").getValue().toString();
                        String SubCode = dataSnapshot1.child("SubCode").getValue().toString();

                        mClassList.add(new Teacher_Class_Names_Spinner(SchName , SchId , DeptName , DeptId , SubCode , YrId , SubName , "" , YearName , SubCode , Subid));
                        mAdap.notifyDataSetChanged();


                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    private void uploadFile(Uri pdfUri) {

        mLoginProgress.dismiss();
        progressDialog = new ProgressDialog(Teacher_Activity_Add_Assignment.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgress(0);
        progressDialog.show();

        StorageReference storageReference = mStorage.getReference();

        Date date = new Date();  // to get the date
        SimpleDateFormat da = new SimpleDateFormat("yyyyMMddHHmmss"); // getting date in this format
        final String formattedDate = da.format(date.getTime());

        SimpleDateFormat dd = new SimpleDateFormat("dd-MM-yyyy");
        final String Dateformat = dd.format(date.getTime());



        storageReference.child(formattedDate).putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                String DownloadUrl = taskSnapshot.getDownloadUrl().toString();

                int pos = spinn1.getSelectedItemPosition();

                String SchId = mClassList.get(pos).getSchId();
                String DeptId = mClassList.get(pos).getDeptId();
                String YrId = mClassList.get(pos).getYearId();
                String SubId = mClassList.get(pos).getSubjectId();

                ClassRef = FirebaseDatabase.getInstance().getReference("Assignments/" + SchId + "/" + DeptId + "/" + YrId + "/" + SubId + "/"+ formattedDate+ "/");

                String TpVal = Name.getText().toString().toUpperCase().charAt(0) + Name.getText().toString().substring(1, Name.getText().toString().length());
                String DescVal = Description.getText().toString().toUpperCase().charAt(0) + Description.getText().toString().substring(1, Description.getText().toString().length());

                HashMap<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("Topic", TpVal);
                dataMap.put("Desc", DescVal);
                dataMap.put("Upload Date", Dateformat);
                dataMap.put("Download Url", DownloadUrl);
                dataMap.put("Ref", formattedDate);
                ClassRef.setValue(dataMap);

                Toast.makeText(Teacher_Activity_Add_Assignment.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                upload.setEnabled(true);
                Name.setText("");
                Description.setText("");



            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Teacher_Activity_Add_Assignment.this, "File Could Not Be Uploaded", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }

        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {


                int currentProgress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);

            }

        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                Toast.makeText(Teacher_Activity_Add_Assignment.this, "File Uploaded", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void disable(){

        upload.setEnabled(false);


        if(pdfUri != null && !Name.getText().toString().isEmpty() && !Description.getText().toString().isEmpty()){

            uploadFile(pdfUri);

        }else {
            //mLoginProgress.dismiss();
            Toast.makeText(Teacher_Activity_Add_Assignment.this, "Select A File", Toast.LENGTH_SHORT).show();
        }


    }






    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

            SelectPdf();

        }else {

            Toast.makeText(Teacher_Activity_Add_Assignment.this , "Please Provide Permission....." , Toast.LENGTH_SHORT).show();

        }

    }

    private void SelectPdf(){


        //Intent intent  = new Intent();
        //intent.setType("application/pdf");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(intent, 86);


        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /*

        if(requestCode==86 && resultCode==RESULT_OK && data!=null){


            pdfUri = data.getData();
            File_Location.setText("Selected File : " + data.getData().getLastPathSegment());

        }else {

            Toast.makeText( Teacher_Activity_Add_Assignment.this , "Please Select A File" , Toast.LENGTH_SHORT).show();
        }

        */

        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {

                    // Get the Uri of the selected file
                    //Uri uri = data.getData();
                    //Log.d(TAG, "File Uri: " + uri.toString());
                    // Get the path
                    pdfUri = data.getData();
                    File_Location.setText("Selected File : " + data.getData().getLastPathSegment());
                    //Log.d(TAG, "File Path: " + path);
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*

    private void uploadFile(Uri pdfUri){

        mLoginProgress.dismiss();
        progressDialog = new ProgressDialog(Upload_Pdf_Assignment_Activity_Teacher_Activity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgress(0);
        progressDialog.show();

        StorageReference storageReference = mStorage.getReference();

        Date date = new Date();  // to get the date
        SimpleDateFormat da = new SimpleDateFormat("yyyyMMddHHmmss"); // getting date in this format
        final String formattedDate = da.format(date.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd"); // getting date in this format
        final String formattedDate2 = df.format(date.getTime());
        SimpleDateFormat dt = new SimpleDateFormat("HHmmss"); // getting date in this format
        final String formattedDate1 = dt.format(date.getTime());

        SimpleDateFormat dd = new SimpleDateFormat("dd-MM-yyyy");
        final String Dateformat = dd.format(date.getTime());

        //Toast.makeText(Upload_Pdf_Assignment_Activity_Teacher_Activity.this , formattedDate2 + formattedDate1, Toast.LENGTH_SHORT ).show();

        final String dateupload1  = "-" + formattedDate2 ;
        //final long dateupload = -dateupload1 ;

        final String dateupload3 = "-" + formattedDate1;
        //final long dateuploaded4 = -dateupload3 ;


        storageReference.child(formattedDate).putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                final String Class = ClassNamesSpinner.getSelectedItem().toString().trim();

                String DownloadUrl = taskSnapshot.getDownloadUrl().toString();

                SharedPreferences Pref = getSharedPreferences("userinfo", MODE_PRIVATE);
                String Id = Pref.getString("Id","");

                AssignmentRef = FirebaseDatabase.getInstance().getReference("Assignment/" + Id +"/"+Class + "/" + dateupload1 + "/" + dateupload3 + "/");

                String TpVal = Name.getText().toString().toUpperCase().charAt(0) + Name.getText().toString().substring(1,Name.getText().toString().length());
                String DescVal = Description.getText().toString().toUpperCase().charAt(0) + Description.getText().toString().substring(1,Description.getText().toString().length());


                HashMap<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("Topic", TpVal);
                dataMap.put("Desc", DescVal);
                dataMap.put("Upload Date", Dateformat);
                dataMap.put("Download Url", DownloadUrl);
                dataMap.put("Ref" , formattedDate);
                dataMap.put("Type" , "Pdf");

                AssignmentRef.setValue(dataMap);
                Toast.makeText(Upload_Pdf_Assignment_Activity_Teacher_Activity.this , "Pdf Uploaded" , Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Upload.setEnabled(true);
                Name.setText("");
                Description.setText("");
                Tp = TpVal ;
                Des = DescVal ;
                DownloadUrl1 = DownloadUrl ;


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Upload_Pdf_Assignment_Activity_Teacher_Activity.this , "Pdf Could Not Be Uploaded" , Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {


                int currentProgress = (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);

            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                new SimpleTask(Upload_Pdf_Assignment_Activity_Teacher_Activity.this).execute();
                Toast.makeText(Upload_Pdf_Assignment_Activity_Teacher_Activity.this, "File Uploaded", Toast.LENGTH_SHORT).show();

            }
        });

    }


    public void disable(){

        Upload.setEnabled(false);


        if(pdfUri != null && !Name.getText().toString().isEmpty() && !Description.getText().toString().isEmpty()){
            uploadFile(pdfUri);
        }else {
            //mLoginProgress.dismiss();
            Toast.makeText(Upload_Pdf_Assignment_Activity_Teacher_Activity.this, "Select A File", Toast.LENGTH_SHORT).show();
        }


    }




    */





}
