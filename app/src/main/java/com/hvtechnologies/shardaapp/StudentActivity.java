package com.hvtechnologies.shardaapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

public class StudentActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener ,
Student_Home.OnFragmentInteractionListener ,
Student_Attendance_Frag.OnFragmentInteractionListener ,
Student_Marksheet_Frag.OnFragmentInteractionListener ,
Student_Assignment_Frag.OnFragmentInteractionListener ,
Student_Notice_Frag.OnFragmentInteractionListener{


    DrawerLayout myDrawer;
    ActionBarDrawerToggle myTog ;
    NavigationView navigationView;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoginProgress;
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    MenuItem prevMenuItem;


    Student_Home student_home;
    Student_Assignment_Frag student_assignment;
    Student_Attendance_Frag student_attendance;
    Student_Marksheet_Frag student_marksheet ;
    Student_Notice_Frag student_notice ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);



        Utils.getDatabase();
        this.setTheme(R.style.AlertDialogCustom);


        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
        }
        else{
            Toast.makeText(StudentActivity.this , "Please Check Your Internet Connection" , Toast.LENGTH_LONG).show();
        }



        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomnavStudent);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        viewPager = (ViewPager) findViewById(R.id.containerStudent);
        //viewPager.setCurrentItem(2,true);

        mAuth = FirebaseAuth.getInstance();
        mLoginProgress = new ProgressDialog(this);

        myDrawer = (DrawerLayout)findViewById(R.id.myDrawStudent);
        myTog = new ActionBarDrawerToggle(this , myDrawer , R.string.open , R.string.close);
        myDrawer.addDrawerListener(myTog);
        myTog.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView)findViewById(R.id.NavStudent);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.ChangePass:

                        final AlertDialog.Builder mbBuilder = new AlertDialog.Builder(StudentActivity.this)
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

                                                Toast.makeText(StudentActivity.this , "Password Successfully Changed" , Toast.LENGTH_SHORT).show();
                                                ddialog.dismiss();
                                                mLoginProgress.dismiss();
                                            }else {

                                                try {
                                                    throw task.getException();
                                                }
                                                // if user enters wrong email.
                                                catch (FirebaseAuthWeakPasswordException weakPassword) {
                                                    Toast.makeText(StudentActivity.this, "Weak Password",
                                                            Toast.LENGTH_SHORT).show();
                                                    mLoginProgress.dismiss();

                                                } catch (Exception e) {
                                                    Toast.makeText(StudentActivity.this, e.getMessage(),
                                                            Toast.LENGTH_SHORT).show();
                                                    mLoginProgress.dismiss();

                                                }
                                            }
                                        }
                                    });
                                }else {
                                    Toast.makeText(StudentActivity.this , "Email Cannot Be Empty" , Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        break;

                    case R.id.LogOut:

                        Toast.makeText(StudentActivity.this , "Sign Out" , Toast.LENGTH_SHORT ).show();
                        mLoginProgress.setTitle("Signing Out");
                        mLoginProgress.setMessage("Please Wait While We Log Out From Your Account");
                        mLoginProgress.setCanceledOnTouchOutside(false);
                        mLoginProgress.show();
                        mAuth.signOut();
                        Intent mainIntent = new Intent(StudentActivity.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                        mLoginProgress.dismiss();
                        overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);
                        Utils.falseDatabase();
                        break;



                    case R.id.AllTT:

                        Intent mainIntent2 = new Intent(StudentActivity.this, Student_All_TT.class);
                        startActivity(mainIntent2);
                        overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

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

        student_home =  new Student_Home();
        student_attendance = new Student_Attendance_Frag();
        student_assignment = new Student_Assignment_Frag();
        student_marksheet = new Student_Marksheet_Frag();
        student_notice = new Student_Notice_Frag();

        adapter.addFragment(student_notice);
        adapter.addFragment(student_attendance);
        adapter.addFragment(student_home);
        adapter.addFragment(student_marksheet);
        adapter.addFragment(student_assignment);
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
