package com.hvtechnologies.shardaapp;

public class StudentTimeTableClass {


    String SubjectCode , SubjectNAme , TeacherName , Location  , Time;

    public StudentTimeTableClass(String subjectCode, String subjectNAme, String teacherName, String location, String time) {
        SubjectCode = subjectCode;
        SubjectNAme = subjectNAme;
        TeacherName = teacherName;
        Location = location;
        Time = time;
    }


    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getSubjectCode() {
        return SubjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        SubjectCode = subjectCode;
    }

    public String getSubjectNAme() {
        return SubjectNAme;
    }

    public void setSubjectNAme(String subjectNAme) {
        SubjectNAme = subjectNAme;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public void setTeacherName(String teacherName) {
        TeacherName = teacherName;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
