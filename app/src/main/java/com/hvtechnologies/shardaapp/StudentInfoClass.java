package com.hvtechnologies.shardaapp;

public class StudentInfoClass {


    String Name , Email , SchoolName , SchoolId , DeptName , DeptId , YearName , YearId , ClassName , ClassId , GroupName , GroupId ;


    public StudentInfoClass(String name, String email, String schoolName, String schoolId, String deptName, String deptId, String yearName, String yearId, String className, String classId, String groupName, String groupId) {
        Name = name;
        Email = email;
        SchoolName = schoolName;
        SchoolId = schoolId;
        DeptName = deptName;
        DeptId = deptId;
        YearName = yearName;
        YearId = yearId;
        ClassName = className;
        ClassId = classId;
        GroupName = groupName;
        GroupId = groupId;
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

    public String getSchoolName() {
        return SchoolName;
    }

    public void setSchoolName(String schoolName) {
        SchoolName = schoolName;
    }

    public String getSchoolId() {
        return SchoolId;
    }

    public void setSchoolId(String schoolId) {
        SchoolId = schoolId;
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
}
