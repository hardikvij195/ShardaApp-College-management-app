package com.hvtechnologies.shardaapp;

public class TeacherInfoClass {


    String Name , Email , Number  , SystemId  , TeacherId;

    public TeacherInfoClass(String name, String email, String number, String systemId, String teacherId) {
        Name = name;
        Email = email;
        Number = number;
        SystemId = systemId;
        TeacherId = teacherId;
    }

    public String getTeacherId() {
        return TeacherId;
    }

    public void setTeacherId(String teacherId) {
        TeacherId = teacherId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getSystemId() {
        return SystemId;
    }

    public void setSystemId(String systemId) {
        SystemId = systemId;
    }
}
