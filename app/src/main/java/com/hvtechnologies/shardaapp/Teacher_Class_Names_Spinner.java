package com.hvtechnologies.shardaapp;

public class Teacher_Class_Names_Spinner {

    String SchName , SchId ,DeptName ,DeptId ,  YearName  , YearId , ClassName , ClassId ,  SubjectName  , SubjectCode , SubjectId ;


    public Teacher_Class_Names_Spinner(String schName, String schId, String deptName, String deptId, String yearName, String yearId, String className, String classId, String subjectName, String subjectCode, String subjectId) {
        SchName = schName;
        SchId = schId;
        DeptName = deptName;
        DeptId = deptId;
        YearName = yearName;
        YearId = yearId;
        ClassName = className;
        ClassId = classId;
        SubjectName = subjectName;
        SubjectCode = subjectCode;
        SubjectId = subjectId;
    }

    public String getSchId() {
        return SchId;
    }

    public void setSchId(String schId) {
        SchId = schId;
    }

    public String getDeptId() {
        return DeptId;
    }

    public void setDeptId(String deptId) {
        DeptId = deptId;
    }

    public String getYearId() {
        return YearId;
    }

    public void setYearId(String yearId) {
        YearId = yearId;
    }

    public String getClassId() {
        return ClassId;
    }

    public void setClassId(String classId) {
        ClassId = classId;
    }

    public String getSubjectCode() {
        return SubjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        SubjectCode = subjectCode;
    }

    public String getSubjectId() {
        return SubjectId;
    }

    public void setSubjectId(String subjectId) {
        SubjectId = subjectId;
    }

    public String getSchName() {
        return SchName;
    }

    public void setSchName(String schName) {
        SchName = schName;
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
}
