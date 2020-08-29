package com.hvtechnologies.shardaapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AssignmentClassListAdapter extends RecyclerView.Adapter<AssignmentClassListAdapter.MyViewHolder> {



    LayoutInflater inflater;
    private Context mcontext ;
    private List<AssignmentClass> mTTList ;


    public AssignmentClassListAdapter(LayoutInflater inflater, Context mcontext, List<AssignmentClass> mTTList) {
        this.inflater = inflater;
        this.mcontext = mcontext;
        this.mTTList = mTTList;
    }


    @Override
    public AssignmentClassListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mcontext).inflate(R.layout.studenttimetablelayoutcardview, parent, false);
        AssignmentClassListAdapter.MyViewHolder holder = new AssignmentClassListAdapter.MyViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentClassListAdapter.MyViewHolder myViewHolder, int i) {

        myViewHolder.TeacherName.setText(mTTList.get(i).getSubCode() + " - " + mTTList.get(i).getSubName());
        myViewHolder.SubjectName.setText(mTTList.get(i).getDesc());
        myViewHolder.SubjectCode.setText(mTTList.get(i).getTopic());
        myViewHolder.Time.setText(mTTList.get(i).getDate());

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

}
