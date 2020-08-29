package com.hvtechnologies.shardaapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TeacherTimeTableListAdapter extends RecyclerView.Adapter<TeacherTimeTableListAdapter.MyViewHolder> {


    LayoutInflater inflater;
    Context mcontext ;
    List<TeacherTimeTableClass> mTTList ;


    public TeacherTimeTableListAdapter(LayoutInflater inflater, Context mcontext, List<TeacherTimeTableClass> mTTList) {
        this.inflater = inflater;
        this.mcontext = mcontext;
        this.mTTList = mTTList;
    }

    public TeacherTimeTableListAdapter(Context applicationContext, List<TeacherTimeTableClass> mTeachTT) {
        this.mcontext = applicationContext;
        this.mTTList = mTeachTT;
    }


    @Override
    public TeacherTimeTableListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.teachertimetablelayoutcardview, parent, false);
        TeacherTimeTableListAdapter.MyViewHolder holder = new TeacherTimeTableListAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherTimeTableListAdapter.MyViewHolder myViewHolder, int i) {

        myViewHolder.SchoolName.setText(mTTList.get(i).getSchoolName() + " - " + mTTList.get(i).getDeptName());
        myViewHolder.SubjectName.setText(mTTList.get(i).getSubjectName());
        myViewHolder.SubjectCode.setText(mTTList.get(i).getSubjectCode());
        myViewHolder.Location.setText(mTTList.get(i).getLocation());
        myViewHolder.DeptName.setText(mTTList.get(i).getTime());
        myViewHolder.ClassName.setText( mTTList.get(i).getYearName() + " - " + mTTList.get(i).getClassName());
    }



    @Override
    public int getItemCount() {
        return mTTList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView SchoolName , DeptName , ClassName ;
        TextView SubjectCode;
        TextView SubjectName;
        TextView Location ;


        public MyViewHolder(View itemView) {
            super(itemView);

            SchoolName = (TextView)itemView.findViewById(R.id.schname);
            DeptName = (TextView)itemView.findViewById(R.id.deptname);
            ClassName = (TextView)itemView.findViewById(R.id.classname);
            SubjectCode = (TextView)itemView.findViewById(R.id.subcode);
            SubjectName = (TextView)itemView.findViewById(R.id.subname);
            Location = (TextView)itemView.findViewById(R.id.classloc);


        }

    }

}
