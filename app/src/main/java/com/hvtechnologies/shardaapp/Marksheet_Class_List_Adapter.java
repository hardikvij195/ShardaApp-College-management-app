package com.hvtechnologies.shardaapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Marksheet_Class_List_Adapter extends RecyclerView.Adapter<Marksheet_Class_List_Adapter.MyViewHolder> {


    LayoutInflater inflater;
    Context mcontext ;
    List<MarksheetClass> mMarkList ;


    public Marksheet_Class_List_Adapter(LayoutInflater inflater, Context mcontext, List<MarksheetClass> mMarkList) {
        this.inflater = inflater;
        this.mcontext = mcontext;
        this.mMarkList = mMarkList;
    }


    @Override
    public Marksheet_Class_List_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.marksheet_class_wise_list_adap, parent, false);
        Marksheet_Class_List_Adapter.MyViewHolder holder = new Marksheet_Class_List_Adapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Marksheet_Class_List_Adapter.MyViewHolder myViewHolder, int i) {

        myViewHolder.Name.setText(mMarkList.get(i).getName());
        myViewHolder.Marks.setText(mMarkList.get(i).getMarks());
    }



    @Override
    public int getItemCount() {
        return mMarkList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Name , Marks ;


        public MyViewHolder(View itemView) {
            super(itemView);

            Name = (TextView)itemView.findViewById(R.id.schname);
            Marks = (TextView)itemView.findViewById(R.id.deptname);


        }

    }

}
