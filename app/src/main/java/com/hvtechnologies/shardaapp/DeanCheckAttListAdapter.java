package com.hvtechnologies.shardaapp;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DeanCheckAttListAdapter extends BaseAdapter {

    private Context mcontext2 ;
    private List<DeanCheckAttClass> mTT ;


    public DeanCheckAttListAdapter(Context mcontext2, List<DeanCheckAttClass> mTT) {

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


        View itemView = View.inflate(mcontext2 , R.layout.dean_check_tt_list_adapter , null);
        TextView TeacherNameCode = (TextView)itemView.findViewById(R.id.teachernamecode);
        TextView SubjectCode = (TextView)itemView.findViewById(R.id.subcode);
        TextView SubjectName = (TextView)itemView.findViewById(R.id.subname);
        TextView DeptName = (TextView)itemView.findViewById(R.id.deptname);
        TextView Loc = (TextView)itemView.findViewById(R.id.timeclassloc);
        TextView YrNameClassNameGrpName = (TextView)itemView.findViewById(R.id.yrnameclassnamegrpname);

        ConstraintLayout constraintLayout = (ConstraintLayout)itemView.findViewById(R.id.constLay);
        String mrk = mTT.get(position).getMarked();
        if(mrk.equals("Yes")){

            constraintLayout.setBackgroundColor(Color.parseColor("#008000"));

        }else {

            constraintLayout.setBackgroundColor(Color.parseColor("#DC143C"));

        }
        TeacherNameCode.setText(mTT.get(position).getTeacherName() + "/" + mTT.get(position).getTeacherCode());
        SubjectName.setText(mTT.get(position).getSubName());
        SubjectCode.setText(mTT.get(position).getSubCode());
        DeptName.setText(mTT.get(position).getDeptName() + "/" + mTT.get(position).getYrName());
        Loc.setText(mTT.get(position).getLoc() + "/" + mTT.get(position).getTime());
        YrNameClassNameGrpName.setText(mTT.get(position).getClassName() + "/" + mTT.get(position).getGroupName());

        return itemView;

    }
}
