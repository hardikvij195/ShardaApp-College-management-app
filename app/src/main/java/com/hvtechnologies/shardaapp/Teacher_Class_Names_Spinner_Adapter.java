package com.hvtechnologies.shardaapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Teacher_Class_Names_Spinner_Adapter extends ArrayAdapter<Teacher_Class_Names_Spinner> {

    public Teacher_Class_Names_Spinner_Adapter(Context context , ArrayList<Teacher_Class_Names_Spinner> classList){
        super(context , 0 , classList);



    }


    @NonNull
    @Override
    public View getView(int position, @Nullable  View convertView, @NonNull ViewGroup parent) {
        return initView(position , convertView , parent);
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position , convertView , parent);


    }

    private View initView(int pos , View convertView , ViewGroup parent){


        if(convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_teacher_activity_class_names , parent , false);

        }

        TextView txt1 = convertView.findViewById(R.id.textView3);
        TextView txt2 = convertView.findViewById(R.id.textView5);
        TextView txt3 = convertView.findViewById(R.id.textView6);

        Teacher_Class_Names_Spinner currentItem = getItem(pos);

        if(currentItem != null){

            txt1.setText(currentItem.getSchName() + " - " + currentItem.getDeptName());
            txt2.setText(currentItem.getYearName() + " - " + currentItem.getClassName());
            txt3.setText(currentItem.getSubjectName());

        }

        return convertView;

    }

}
