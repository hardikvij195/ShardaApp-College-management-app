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

public class Admin_Activity_Add_Dean extends AppCompatActivity {



    DatabaseReference ClassRef , SchoolNamesRef , TimingsRef , ClassRef2;
    Button Submit ;
    private ArrayList<NameIdClass> SchoolNames = new ArrayList<>() ;
    private ArrayList<String> SchoolNamesString = new ArrayList<>() ;
    private ArrayAdapter<String> adapterSpinnerSchoolNames ;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoginProgress ;
    Spinner SchName ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin___add__dean);




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

        Submit = (Button) findViewById(R.id.SubmitAddTeacher);

        adapterSpinnerSchoolNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , SchoolNamesString);


        SchName.setAdapter(adapterSpinnerSchoolNames);


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




        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mLoginProgress.setTitle("Creating Account");
                mLoginProgress.setMessage("Please Wait While We Create New Account");
                mLoginProgress.setCanceledOnTouchOutside(false);
                mLoginProgress.show();


                final String NameOfUser = NameTxt.getText().toString().toUpperCase().trim();
                final String Code = CodeTxt.getText().toString().trim();
                final String PhoneOfUser = NumberTxt.getText().toString().toUpperCase().trim();
                final String SystemId = SystemIdTxt.getText().toString().trim();
                final String Email = EmailTxt.getText().toString().trim();
                final String Pas = PasswordTxt.getText().toString().trim();


                if ( !SchoolNames.isEmpty() && !NameOfUser.isEmpty() && !PhoneOfUser.isEmpty() && !SystemId.isEmpty() && !Email.isEmpty() && !Pas.isEmpty() && PhoneOfUser.length() == 10 ) {


                    mAuth.createUserWithEmailAndPassword(Email,Pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {

                                final FirebaseUser useremail = task.getResult().getUser();
                                final String user_id = task.getResult().getUser().getUid();
                                Date date = new Date();  // to get the date
                                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss"); // getting date in this format
                                final String formattedDate = df.format(date.getTime());


                                int ps = SchName.getSelectedItemPosition();
                                String SchNm = SchoolNames.get(ps).getName();
                                String SchId = SchoolNames.get(ps).getId();

                                ClassRef = FirebaseDatabase.getInstance().getReference("Teachers/" + formattedDate  );
                                HashMap<String, String> dataMap = new HashMap<String, String>();
                                dataMap.put("Name", NameOfUser);
                                dataMap.put("Code", Code);
                                dataMap.put("Number", PhoneOfUser);
                                dataMap.put("SystemId", SystemId);
                                dataMap.put("Email", Email);
                                dataMap.put("Uid", user_id);
                                dataMap.put("TeacherId", formattedDate);
                                dataMap.put("SchoolName", SchNm);
                                dataMap.put("SchoolId", SchId);
                                ClassRef.setValue(dataMap);

                                ClassRef = FirebaseDatabase.getInstance().getReference("Users/DeanLogin/"+ user_id + "/");
                                ClassRef.setValue(dataMap);

                                NameTxt.setText("");
                                SystemIdTxt.setText("");
                                NumberTxt.setText("");
                                EmailTxt.setText("");
                                PasswordTxt.setText("");
                                CodeTxt.setText("");
                                mAuth.signOut();

                                SharedPreferences userinformation = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
                                String email = userinformation.getString("EMAIL" ,"");
                                String pass = userinformation.getString("PASSWORD" , "");

                                mAuth.signInWithEmailAndPassword(email ,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if(task.isSuccessful()){

                                            Toast.makeText(Admin_Activity_Add_Dean.this, "Account Successfully Created" , Toast.LENGTH_SHORT).show();
                                            mLoginProgress.dismiss();

                                        }else {

                                            Toast.makeText(Admin_Activity_Add_Dean.this, "Account Successfully Created Errorrr" , Toast.LENGTH_SHORT).show();
                                            mLoginProgress.dismiss();

                                        }
                                    }
                                });

                            }
                            else {

                                try
                                {
                                    throw task.getException();
                                }
                                // if user enters wrong email.
                                catch (FirebaseAuthWeakPasswordException weakPassword)
                                {
                                    Toast.makeText(Admin_Activity_Add_Dean.this, "Weak Password" ,
                                            Toast.LENGTH_SHORT).show();
                                    mLoginProgress.dismiss();


                                }
                                // if user enters wrong password.
                                catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                                {
                                    Toast.makeText(Admin_Activity_Add_Dean.this, "Malformed Email",
                                            Toast.LENGTH_SHORT).show();
                                    mLoginProgress.dismiss();



                                }
                                catch (FirebaseAuthUserCollisionException existEmail)
                                {
                                    Toast.makeText(Admin_Activity_Add_Dean.this, "Email Already Exist" ,
                                            Toast.LENGTH_SHORT).show();
                                    mLoginProgress.dismiss();


                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(Admin_Activity_Add_Dean.this,  e.getMessage() , Toast.LENGTH_SHORT).show();
                                    mLoginProgress.dismiss();

                                }


                            }



                        }

                    });



                }
                else {

                    mLoginProgress.dismiss();
                    Toast.makeText(Admin_Activity_Add_Dean.this , "Enter All Details Correctly" , Toast.LENGTH_SHORT ).show();
                }



            }
        });


    }
}
