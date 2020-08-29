package com.hvtechnologies.shardaapp;

public class TeacherTimeTableClass {


    String SchoolName , DeptName , YearName , ClassName , SubjectName , SubjectCode , Location , Time ;

    public TeacherTimeTableClass(String schoolName, String deptName, String yearName, String className, String subjectName, String subjectCode, String location, String time) {
        SchoolName = schoolName;
        DeptName = deptName;
        YearName = yearName;
        ClassName = className;
        SubjectName = subjectName;
        SubjectCode = subjectCode;
        Location = location;
        Time = time;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getSchoolName() {
        return SchoolName;
    }

    public void setSchoolName(String schoolName) {
        SchoolName = schoolName;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public String getYearName() {
        return YearName;
    }

    public void setYearName(String yearName) {
        YearName = yearName;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
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

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
