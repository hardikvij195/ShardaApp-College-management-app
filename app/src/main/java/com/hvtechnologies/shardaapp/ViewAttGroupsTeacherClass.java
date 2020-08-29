package com.hvtechnologies.shardaapp;

public class ViewAttGroupsTeacherClass {

    String Date , SubName , SubCode , ByName , ByCode , Time ;


    public ViewAttGroupsTeacherClass(String date, String subName, String subCode, String byName, String byCode, String time) {
        Date = date;
        SubName = subName;
        SubCode = subCode;
        ByName = byName;
        ByCode = byCode;
        Time = time;
    }


    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getSubName() {
        return SubName;
    }

    public void setSubName(String subName) {
        SubName = subName;
    }

    public String getSubCode() {
        return SubCode;
    }

    public void setSubCode(String subCode) {
        SubCode = subCode;
    }

    public String getByName() {
        return ByName;
    }

    public void setByName(String byName) {
        ByName = byName;
    }

    public String getByCode() {
        return ByCode;
    }

    public void setByCode(String byCode) {
        ByCode = byCode;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
