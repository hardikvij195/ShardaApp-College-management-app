package com.hvtechnologies.shardaapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.IDNA;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class Teacher_Activity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener ,
        Teacher_Home_Frag.OnFragmentInteractionListener ,
        Teacher_Notice_Frag.OnFragmentInteractionListener ,
        Teacher_Assignment_Frag.OnFragmentInteractionListener ,
        Teacher_Marksheet_Frag.OnFragmentInteractionListener ,
        Teacher_Attendane_Frag.OnFragmentInteractionListener {


    DrawerLayout myDrawer;
    ActionBarDrawerToggle myTog ;
    NavigationView navigationView;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoginProgress;
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    MenuItem prevMenuItem;


    Teacher_Home_Frag teacher_home ;
    Teacher_Attendane_Frag teacher_attendane ;
    Teacher_Marksheet_Frag teacher_marksheet ;
    Teacher_Assignment_Frag teacher_assignment ;
    Teacher_Notice_Frag teacher_notice ;

    DatabaseReference InfoRef , TTRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_);



        Utils.getDatabase();
        this.setTheme(R.style.AlertDialogCustom);


        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
        }
        else{
            Toast.makeText(Teacher_Activity.this , "Please Check Your Internet Connection" , Toast.LENGTH_LONG).show();
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        InfoRef = FirebaseDatabase.getInstance().getReference("Users/TeacherLogin/" + uid + "/") ;
        InfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists()) {

                    String Name = dataSnapshot.child("Name").getValue().toString();
                    String SystemId = dataSnapshot.child("SystemId").getValue().toString();
                    String Code = dataSnapshot.child("Code").getValue().toString();

                    SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPrefs.edit();
                    edit.putString("NAME" , Name );
                    edit.putString("CODE" , Code );
                    edit.putString("SYSID" , SystemId );
                    edit.apply();


                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
        //SharedPreferences.Editor edit = sharedPrefs.edit();
        //edit.putString("USERID" , user_id );
        //edit.putString("EMAIL" , Nametxt.getEditText().getText().toString().trim());
        //edit.putString("PASSWORD" , Passtxt.getEditText().getText().toString().trim() );
        //edit.apply();




        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomnavTeacher);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        viewPager = (ViewPager) findViewById(R.id.containerTeacher);
        //viewPager.setCurrentItem(2,true);

        mAuth = FirebaseAuth.getInstance();
        mLoginProgress = new ProgressDialog(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        myDrawer = (DrawerLayout)findViewById(R.id.myDrawTeacher);
        myTog = new ActionBarDrawerToggle(this , myDrawer , R.string.open , R.string.close);
        myDrawer.addDrawerListener(myTog);
        myTog.syncState();
        navigationView = (NavigationView)findViewById(R.id.NavTeacher);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {


                    case R.id.AllTT:

                        Intent mainIntentAllTT = new Intent(Teacher_Activity.this, Teacher_All_TT.class);
                        startActivity(mainIntentAllTT);
                        overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

                        break;


                    case R.id.ChangePass:

                        final AlertDialog.Builder mbBuilder = new AlertDialog.Builder(Teacher_Activity.this)
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

                                                Toast.makeText(Teacher_Activity.this , "Password Successfully Changed" , Toast.LENGTH_SHORT).show();
                                                ddialog.dismiss();
                                                mLoginProgress.dismiss();
                                            }else {

                                                try {
                                                    throw task.getException();
                                                }
                                                // if user enters wrong email.
                                                catch (FirebaseAuthWeakPasswordException weakPassword) {
                                                    Toast.makeText(Teacher_Activity.this, "Weak Password",
                                                            Toast.LENGTH_SHORT).show();
                                                    mLoginProgress.dismiss();

                                                } catch (Exception e) {
                                                    Toast.makeText(Teacher_Activity.this, e.getMessage(),
                                                            Toast.LENGTH_SHORT).show();
                                                    mLoginProgress.dismiss();

                                                }
                                            }
                                        }
                                    });
                                }else {
                                    Toast.makeText(Teacher_Activity.this , "Email Cannot Be Empty" , Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        break;

                    case R.id.LogOut:

                        Toast.makeText(Teacher_Activity.this , "Sign Out" , Toast.LENGTH_SHORT ).show();
                        mLoginProgress.setTitle("Signing Out");
                        mLoginProgress.setMessage("Please Wait While We Log Out From Your Account");
                        mLoginProgress.setCanceledOnTouchOutside(false);
                        mLoginProgress.show();
                        mAuth.signOut();
                        Intent mainIntent = new Intent(Teacher_Activity.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                        mLoginProgress.dismiss();
                        overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);
                        Utils.falseDatabase();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        teacher_home = new Teacher_Home_Frag();
        teacher_assignment = new Teacher_Assignment_Frag();
        teacher_attendane = new Teacher_Attendane_Frag();
        teacher_marksheet = new Teacher_Marksheet_Frag();
        teacher_notice = new Teacher_Notice_Frag();

        adapter.addFragment(teacher_notice);
        adapter.addFragment(teacher_attendane);
        adapter.addFragment(teacher_home);
        adapter.addFragment(teacher_marksheet);
        adapter.addFragment(teacher_assignment);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
