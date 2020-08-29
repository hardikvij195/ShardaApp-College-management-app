package com.hvtechnologies.shardaapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button login ;
    TextInputLayout Passtxt , Nametxt ;

    TextView forgotPass ;
    String user_id;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private ProgressDialog mLoginProgress;
    RelativeLayout r1 ;
    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            r1.setVisibility(View.VISIBLE);

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme2);
        setContentView(R.layout.activity_main);


        r1 = (RelativeLayout)findViewById(R.id.rely1);
        handler.postDelayed(runnable , 1500);


        login = (Button) findViewById(R.id.LoginActBtn);
        forgotPass = (TextView)findViewById(R.id.ForgotPasswordTextView);
        mLoginProgress = new ProgressDialog(this , R.style.StyledDialog);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Nametxt = (TextInputLayout) findViewById(R.id.NameText);
        Passtxt = (TextInputLayout) findViewById(R.id.PassText);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        Utils.getDatabase();


        if (currentUser != null) {

            mLoginProgress.setTitle("Logging In");
            mLoginProgress.setMessage("Please Wait While We Log Into Your Account");
            mLoginProgress.setCanceledOnTouchOutside(false);
            mLoginProgress.setIndeterminate(true);
            mLoginProgress.show();
            user_id = currentUser.getUid();

            DatabaseReference AlreadySignedIn1 = database.getReference("Users");
            AlreadySignedIn1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.child("TeacherLogin").child(user_id).exists() || dataSnapshot.child("MasterLogin").child(user_id).exists() || dataSnapshot.child("Admin").child(user_id).exists() || dataSnapshot.child("StudentLogin").child(user_id).exists() || dataSnapshot.child("DeanLogin").child(user_id).exists()  )
                    {


                        if(dataSnapshot.child("TeacherLogin").child(user_id).exists())
                        {

                            Intent mainIntent = new Intent(MainActivity.this, Teacher_Activity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(mainIntent);
                            finish();
                            mLoginProgress.dismiss();
                            overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

                        }
                        else if(dataSnapshot.child("Admin").child(user_id).exists())
                        {

                            Intent mainIntent = new Intent(MainActivity.this, Admin_Activity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(mainIntent);
                            finish();
                            mLoginProgress.dismiss();
                            overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

                        }
                        else if(dataSnapshot.child("StudentLogin").child(user_id).exists())
                        {

                            Intent mainIntent = new Intent(MainActivity.this, StudentActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(mainIntent);
                            finish();
                            mLoginProgress.dismiss();
                            overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

                        }
                        else if(dataSnapshot.child("MasterLogin").child(user_id).exists())
                        {

                            Intent mainIntent = new Intent(MainActivity.this, Master_Activity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(mainIntent);
                            finish();
                            mLoginProgress.dismiss();
                            overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

                        }
                        else if(dataSnapshot.child("DeanLogin").child(user_id).exists())
                        {

                            Intent mainIntent = new Intent(MainActivity.this, Dean_Activity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(mainIntent);
                            finish();
                            mLoginProgress.dismiss();
                            overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

                        }

                    }
                    else{

                        mAuth.signOut();
                        Toast.makeText(MainActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                        mLoginProgress.dismiss();

                    }


                }
                @Override
                public void onCancelled(DatabaseError databaseError) {



                }
            });


            Toast.makeText(MainActivity.this, "User Already Logged In", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(MainActivity.this, "No User Logged In ", Toast.LENGTH_SHORT).show();
        }



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network


                final String email = Nametxt.getEditText().getText().toString().trim();
                final String Pass = Passtxt.getEditText().getText().toString().trim();

                if (!email.isEmpty() && !Pass.isEmpty()) {

                    mLoginProgress.setTitle("Logging In");
                    mLoginProgress.setMessage("Please wait while we check your credentials");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();

                    mAuth.signInWithEmailAndPassword(email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                Toast.makeText(MainActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = task.getResult().getUser();
                                user_id = user.getUid();
                                SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit = sharedPrefs.edit();
                                edit.putString("USERID" , user_id );
                                edit.putString("EMAIL" , Nametxt.getEditText().getText().toString().trim());
                                edit.putString("PASSWORD" , Passtxt.getEditText().getText().toString().trim() );
                                edit.apply();
                                DatabaseReference AlreadySignedIn1 = database.getReference("Users");
                                AlreadySignedIn1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                        if(dataSnapshot.child("TeacherLogin").child(user_id).exists() || dataSnapshot.child("MasterLogin").child(user_id).exists() || dataSnapshot.child("Admin").child(user_id).exists() || dataSnapshot.child("StudentLogin").child(user_id).exists() || dataSnapshot.child("DeanLogin").child(user_id).exists() )
                                        {

                                            if(dataSnapshot.child("TeacherLogin").child(user_id).exists())
                                            {

                                                Intent mainIntent = new Intent(MainActivity.this, Teacher_Activity.class);
                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(mainIntent);
                                                finish();
                                                mLoginProgress.dismiss();
                                                overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

                                            }
                                            else if(dataSnapshot.child("Admin").child(user_id).exists())
                                            {

                                                Intent mainIntent = new Intent(MainActivity.this, Admin_Activity.class);
                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(mainIntent);
                                                finish();
                                                mLoginProgress.dismiss();
                                                overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

                                            }
                                            else if(dataSnapshot.child("StudentLogin").child(user_id).exists())
                                            {

                                                Intent mainIntent = new Intent(MainActivity.this, StudentActivity.class);
                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(mainIntent);
                                                finish();
                                                mLoginProgress.dismiss();
                                                overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

                                            }
                                            else if(dataSnapshot.child("MasterLogin").child(user_id).exists())
                                            {

                                                Intent mainIntent = new Intent(MainActivity.this, StudentActivity.class);
                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(mainIntent);
                                                finish();
                                                mLoginProgress.dismiss();
                                                overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

                                            }
                                            else if(dataSnapshot.child("DeanLogin").child(user_id).exists())
                                            {

                                                Intent mainIntent = new Intent(MainActivity.this, Dean_Activity.class);
                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(mainIntent);
                                                finish();
                                                mLoginProgress.dismiss();
                                                overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

                                            }


                                        }
                                        else{

                                            mAuth.signOut();
                                            Toast.makeText(MainActivity.this, "User Not Found _____", Toast.LENGTH_SHORT).show();
                                            mLoginProgress.dismiss();

                                        }

                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            } else {

                                try
                                {
                                    throw task.getException();
                                }
                                // if user enters wrong email.
                                catch (FirebaseAuthInvalidUserException invalidEmail)
                                {
                                    mLoginProgress.hide();
                                    Toast.makeText(MainActivity.this, "Invalid Email" , Toast.LENGTH_SHORT).show();


                                }
                                // if user enters wrong password.
                                catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                                {
                                    mLoginProgress.hide();
                                    Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();

                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(MainActivity.this,  e.getMessage() ,
                                            Toast.LENGTH_SHORT).show();
                                    mLoginProgress.dismiss();

                                }


                            }

                        }

                    });

                }
                else {

                    Toast.makeText(MainActivity.this, "FIELDS CANNOT BE EMPTY", Toast.LENGTH_SHORT).show();

                }

                }
                else{

                    Toast.makeText(MainActivity.this , "Please Check Your Internet Connection" , Toast.LENGTH_SHORT).show();

                }


            }

        });




        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this)
                        .setCancelable(false);
                View mView = getLayoutInflater().inflate(R.layout.dialog_box_forgot_password, null);

                final TextInputLayout EditTextForgotPass = (TextInputLayout) mView.findViewById(R.id.EditTextForgotPassword);
                final Button Canc = (Button) mView.findViewById(R.id.button41);
                final Button Ok = (Button) mView.findViewById(R.id.button42);


                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                Canc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });

                Ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(!EditTextForgotPass.getEditText().toString().isEmpty()){

                            mLoginProgress.setMessage("Sending Mail");
                            mLoginProgress.setCanceledOnTouchOutside(false);
                            mLoginProgress.show();
                            mAuth.sendPasswordResetEmail(EditTextForgotPass.getEditText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                        Toast.makeText(MainActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                                        mLoginProgress.dismiss();
                                        dialog.dismiss();

                                    }else {

                                        Toast.makeText(MainActivity.this, "Email Does Not Exist", Toast.LENGTH_SHORT).show();
                                        mLoginProgress.dismiss();
                                        dialog.dismiss();


                                    }
                                }
                            });
                        }
                    }
                });
            }
        });







    }
}
