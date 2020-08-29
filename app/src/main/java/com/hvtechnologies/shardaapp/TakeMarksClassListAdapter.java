package com.hvtechnologies.shardaapp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TakeMarksClassListAdapter extends BaseAdapter {

    private Context mcontext2 ;
    private List<TakeMarksClass> mMarksheetList ;

    public TakeMarksClassListAdapter(Context mcontext2, List<TakeMarksClass> mMarksheetList) {

        this.mcontext2 = mcontext2;
        this.mMarksheetList = mMarksheetList;

    }

    @Override
    public int getCount() {
        return mMarksheetList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMarksheetList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View v = View.inflate(mcontext2 , R.layout.take_marks_list_view_adapter , null);
        TextView NameOfStudentTextView = (TextView)v.findViewById(R.id.NameOfStudentTakeMarks);
        TextView MarksTextView = (TextView)v.findViewById(R.id.MarksTextView);


        NameOfStudentTextView.setText(mMarksheetList.get(position).getName() + "\n" + mMarksheetList.get(position).getSysId());

        MarksTextView.setText(mMarksheetList.get(position).getMarks());

        //v.setTag(mMarksheetList.get(position).getId());

        return v;



    }
}
