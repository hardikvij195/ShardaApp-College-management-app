package com.hvtechnologies.shardaapp;

public class AttendanceClass {



    private String NameOfStudent ;
    private String AbsentOrPresent ;
    private String StudentId ;
    private String Email ;

    public AttendanceClass(String nameOfStudent, String absentOrPresent, String studentId, String email) {
        NameOfStudent = nameOfStudent;
        AbsentOrPresent = absentOrPresent;
        StudentId = studentId;
        Email = email;
    }

    public String getNameOfStudent() {
        return NameOfStudent;
    }

    public void setNameOfStudent(String nameOfStudent) {
        NameOfStudent = nameOfStudent;
    }

    public String getAbsentOrPresent() {
        return AbsentOrPresent;
    }

    public void setAbsentOrPresent(String absentOrPresent) {
        AbsentOrPresent = absentOrPresent;
    }

    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(String studentId) {
        StudentId = studentId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
