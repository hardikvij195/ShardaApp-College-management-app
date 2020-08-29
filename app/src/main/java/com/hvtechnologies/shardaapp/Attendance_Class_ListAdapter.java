package com.hvtechnologies.shardaapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Attendance_Class_ListAdapter extends RecyclerView.Adapter<Attendance_Class_ListAdapter.MyViewHolder> {


    public Teacher_Activity_Add_Attendance inflater;
    Context mcontext ;
    List<AttendanceClass> mAttList ;


    public Attendance_Class_ListAdapter(Teacher_Activity_Add_Attendance inflater, Context mcontext, List<AttendanceClass> mAttList) {
        this.inflater = inflater;
        this.mcontext = mcontext;
        this.mAttList = mAttList;
    }


    @NonNull
    @Override
    public Attendance_Class_ListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.take_attendance_list_view_adapter, viewGroup, false);
        Attendance_Class_ListAdapter.MyViewHolder holder = new Attendance_Class_ListAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {


        myViewHolder.Name.setText(mAttList.get(i).getNameOfStudent());
        myViewHolder.Att.setText(mAttList.get(i).getAbsentOrPresent());


    }

    @Override
    public int getItemCount() {
        return mAttList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView Name , Att ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = (TextView)itemView.findViewById(R.id.NameOfStudentTextTakeAttendenceAdminActivity);
            Att = (TextView)itemView.findViewById(R.id.PresentOrAbsentTextTakeAttendenceAdminActivity);

        }
    }
}
