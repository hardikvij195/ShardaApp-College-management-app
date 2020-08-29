package com.hvtechnologies.shardaapp;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class StudentTimeTableListAdapter extends RecyclerView.Adapter<StudentTimeTableListAdapter.MyViewHolder> {



    LayoutInflater inflater;
    private Context mcontext ;
    private List<StudentTimeTableClass> mTTList ;


    public StudentTimeTableListAdapter(LayoutInflater inflater, Context mcontext, List<StudentTimeTableClass> mTTList) {
        this.inflater = inflater;
        this.mcontext = mcontext;
        this.mTTList = mTTList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.studenttimetablelayoutcardview, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull StudentTimeTableListAdapter.MyViewHolder myViewHolder, int i) {

        myViewHolder.TeacherName.setText(mTTList.get(i).getTeacherName());
        myViewHolder.SubjectName.setText(mTTList.get(i).getSubjectNAme());
        myViewHolder.SubjectCode.setText(mTTList.get(i).getSubjectCode());
        myViewHolder.Location.setText(mTTList.get(i).getLocation());
        myViewHolder.Time.setText(mTTList.get(i).getTime());

    }



    @Override
    public int getItemCount() {
        return mTTList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView TeacherName;
        TextView SubjectCode;
        TextView SubjectName;
        TextView Location , Time;


        public MyViewHolder(View itemView) {
            super(itemView);

            TeacherName = (TextView)itemView.findViewById(R.id.teachername);
            SubjectCode = (TextView)itemView.findViewById(R.id.subcode);
            SubjectName = (TextView)itemView.findViewById(R.id.subname);
            Location = (TextView)itemView.findViewById(R.id.classloc);
            Time = (TextView)itemView.findViewById(R.id.timename);

        }
    }

    /*

    public StudentTimeTableListAdapter(View item){

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //Log.d("RecyclerView", "onClick：" + getAdapterPosition());


            }
        });


    }

    */

}
