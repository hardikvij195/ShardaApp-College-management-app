package com.hvtechnologies.shardaapp;

public class Dean_TT_Check_Class {


    String   DeptName , DeptId , YearName , YearId , ClassName , ClassId , Time , GroupName , GroupId,  SubjectName , SubjectCode , TeacherName , TeacherCode , TeacherId , PeriodName , Marked;


    public Dean_TT_Check_Class(String deptName, String deptId, String yearName, String yearId, String className, String classId, String time, String groupName, String groupId, String subjectName, String subjectCode, String teacherName, String teacherCode, String teacherId, String periodName, String marked) {
        DeptName = deptName;
        DeptId = deptId;
        YearName = yearName;
        YearId = yearId;
        ClassName = className;
        ClassId = classId;
        Time = time;
        GroupName = groupName;
        GroupId = groupId;
        SubjectName = subjectName;
        SubjectCode = subjectCode;
        TeacherName = teacherName;
        TeacherCode = teacherCode;
        TeacherId = teacherId;
        PeriodName = periodName;
        Marked = marked;
    }

    public String getMarked() {
        return Marked;
    }

    public void setMarked(String marked) {
        Marked = marked;
    }

    public String getTeacherId() {
        return TeacherId;
    }

    public void setTeacherId(String teacherId) {
        TeacherId = teacherId;
    }

    public String getPeriodName() {
        return PeriodName;
    }

    public void setPeriodName(String periodName) {
        PeriodName = periodName;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public String getDeptId() {
        return DeptId;
    }

    public void setDeptId(String deptId) {
        DeptId = deptId;
    }

    public String getYearName() {
        return YearName;
    }

    public void setYearName(String yearName) {
        YearName = yearName;
    }

    public String getYearId() {
        return YearId;
    }

    public void setYearId(String yearId) {
        YearId = yearId;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getClassId() {
        return ClassId;
    }

    public void setClassId(String classId) {
        ClassId = classId;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public String getSubjectName() {
        return SubjectName;
    }

    public void setSubjectName(String subjectName) {
        SubjectName = subjectName;
    }

    public String getSubjectCode() {
        return SubjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        SubjectCode = subjectCode;
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
}
