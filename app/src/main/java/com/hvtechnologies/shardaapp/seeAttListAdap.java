package com.hvtechnologies.shardaapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class seeAttListAdap extends BaseAdapter {


    private Context mcontext5 ;
    private List<ViewAttGroupsTeacherClass> mAttendanceDateWiseList ;

    public seeAttListAdap(Context mcontext5, List<ViewAttGroupsTeacherClass> mAttendanceDateWiseList) {
        this.mcontext5 = mcontext5;
        this.mAttendanceDateWiseList = mAttendanceDateWiseList;
    }

    @Override
    public int getCount() {
        return mAttendanceDateWiseList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAttendanceDateWiseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mcontext5 , R.layout.studenttimetablelayoutcardview , null);
        TextView TeacherName = (TextView)v.findViewById(R.id.teachername);
        TextView SubName = (TextView)v.findViewById(R.id.subname);
        TextView SubCode = (TextView)v.findViewById(R.id.subcode);
        TextView Time = (TextView)v.findViewById(R.id.timename);
        TextView Date = (TextView)v.findViewById(R.id.classloc);

        SubCode.setText(mAttendanceDateWiseList.get(position).getByCode());
        Date.setText(mAttendanceDateWiseList.get(position).getDate());
        TeacherName.setText(mAttendanceDateWiseList.get(position).getSubCode());
        Time.setText(mAttendanceDateWiseList.get(position).getTime());
        SubName.setText(mAttendanceDateWiseList.get(position).getSubName());

        return v;



    }

}
