package com.hvtechnologies.shardaapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class Student_All_TT_ListView_Adap extends BaseAdapter {


    private Context mcontext2 ;
    private List<StudentTimeTableClass> mTT ;


    public Student_All_TT_ListView_Adap(Context mcontext2, List<StudentTimeTableClass> mTT) {
        this.mcontext2 = mcontext2;
        this.mTT = mTT;
    }

    @Override
    public int getCount() {
        return mTT.size();
    }

    @Override
    public Object getItem(int position) {
        return mTT.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View itemView = View.inflate(mcontext2 , R.layout.studenttimetablelayoutcardview , null);
        TextView TeacherName = (TextView)itemView.findViewById(R.id.teachername);
        TextView SubjectCode = (TextView)itemView.findViewById(R.id.subcode);
        TextView SubjectName = (TextView)itemView.findViewById(R.id.subname);
        TextView Location = (TextView)itemView.findViewById(R.id.classloc);
        TextView Time = (TextView)itemView.findViewById(R.id.timename);

        TeacherName.setText(mTT.get(position).getTeacherName());
        SubjectName.setText(mTT.get(position).getSubjectNAme());
        SubjectCode.setText(mTT.get(position).getSubjectCode());
        Location.setText(mTT.get(position).getLocation());
        Time.setText(mTT.get(position).getTime());

        return itemView;
    }
}
