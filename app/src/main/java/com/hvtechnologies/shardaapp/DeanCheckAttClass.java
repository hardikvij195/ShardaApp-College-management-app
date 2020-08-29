package com.hvtechnologies.shardaapp;

public class DeanCheckAttClass {

    String DeptName , YrName , SubCode , SubName , TeacherName , TeacherCode , ClassName , GroupName , Time , Loc , Marked ;


    public DeanCheckAttClass(String deptName, String yrName, String subCode, String subName, String teacherName, String teacherCode, String className, String groupName, String time, String loc, String marked) {

        DeptName = deptName;
        YrName = yrName;
        SubCode = subCode;
        SubName = subName;
        TeacherName = teacherName;
        TeacherCode = teacherCode;
        ClassName = className;
        GroupName = groupName;
        Time = time;
        Loc = loc;
        Marked = marked;

    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public String getYrName() {
        return YrName;
    }

    public void setYrName(String yrName) {
        YrName = yrName;
    }

    public String getSubCode() {
        return SubCode;
    }

    public void setSubCode(String subCode) {
        SubCode = subCode;
    }

    public String getSubName() {
        return SubName;
    }

    public void setSubName(String subName) {
        SubName = subName;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public void setTeacherName(String teacherName) {
        TeacherName = teacherName;
    }

    public String getTeacherCode() {
        return TeacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        TeacherCode = teacherCode;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getLoc() {
        return Loc;
    }

    public void setLoc(String loc) {
        Loc = loc;
    }

    public String getMarked() {
        return Marked;
    }

    public void setMarked(String marked) {
        Marked = marked;
    }
}

