package com.hvtechnologies.shardaapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class Teacher_All_TT_ListView_Adap extends BaseAdapter {


    private Context mcontext2 ;
    private List<TeacherTimeTableClass> mTTList ;


    public Teacher_All_TT_ListView_Adap(Context mcontext2, List<TeacherTimeTableClass> mTTList) {
        this.mcontext2 = mcontext2;
        this.mTTList = mTTList;
    }





    @Override
    public int getCount() {
        return mTTList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTTList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View itemView = View.inflate(mcontext2 , R.layout.teachertimetablelayoutcardview , null);
        TextView SchoolName = (TextView)itemView.findViewById(R.id.schname);
        TextView DeptName = (TextView)itemView.findViewById(R.id.deptname);
        TextView ClassName = (TextView)itemView.findViewById(R.id.classname);
        TextView SubjectCode = (TextView)itemView.findViewById(R.id.subcode);
        TextView SubjectName = (TextView)itemView.findViewById(R.id.subname);
        TextView Location = (TextView)itemView.findViewById(R.id.classloc);

        SchoolName.setText(mTTList.get(i).getSchoolName() + " - " + mTTList.get(i).getDeptName());
        SubjectName.setText(mTTList.get(i).getSubjectName());
        SubjectCode.setText(mTTList.get(i).getSubjectCode());
        Location.setText(mTTList.get(i).getLocation());
        DeptName.setText(mTTList.get(i).getTime());
        ClassName.setText( mTTList.get(i).getYearName() + " - " + mTTList.get(i).getClassName());


        return itemView;





    }
}
