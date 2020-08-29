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

public class Teacher_Grp_Names_Spinner_Adapter extends ArrayAdapter<Teacher_GrpNames_Spinner> {


    public Teacher_Grp_Names_Spinner_Adapter(Context context , ArrayList<Teacher_GrpNames_Spinner> classList){
        super(context , 0 , classList);



    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position , convertView , parent);
    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position , convertView , parent);


    }

    private View initView(int pos , View convertView , ViewGroup parent){


        if(convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_group_names , parent , false);

        }

        TextView txt1 = convertView.findViewById(R.id.textView3);
        Teacher_GrpNames_Spinner currentItem = getItem(pos);

        if(currentItem != null){

            txt1.setText(currentItem.getGrpName());

        }
        return convertView;

    }

}
