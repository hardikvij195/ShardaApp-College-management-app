package com.hvtechnologies.shardaapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class Student_Subs_Adapter extends BaseAdapter {

    private Context mcontext2 ;
    private List<StudentTimeTableClass> mSubList ;


    public Student_Subs_Adapter(Context mcontext2, List<StudentTimeTableClass> mSubList) {
        this.mcontext2 = mcontext2;
        this.mSubList = mSubList;
    }


    @Override
    public int getCount() {
        return mSubList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSubList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View v = View.inflate(mcontext2 , R.layout.subslayout , null);
        TextView SubCodeTextView = (TextView)v.findViewById(R.id.subcode);
        TextView SubNameTextView = (TextView)v.findViewById(R.id.subname);

        SubNameTextView.setText(mSubList.get(position).getSubjectNAme() );
        SubCodeTextView.setText(mSubList.get(position).getSubjectCode());

        return v;



    }
}
