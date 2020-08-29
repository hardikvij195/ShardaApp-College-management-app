package com.hvtechnologies.shardaapp;

public class SelectSubjectTeacherClass {

    String SchId , DeptId , YearId , ClassId , ClassName , SubjectId , SubjectName , SubjectCode ;

    public SelectSubjectTeacherClass(String schId, String deptId, String yearId, String classId, String className, String subjectId, String subjectName, String subjectCode) {
        SchId = schId;
        DeptId = deptId;
        YearId = yearId;
        ClassId = classId;
        ClassName = className;
        SubjectId = subjectId;
        SubjectName = subjectName;
        SubjectCode = subjectCode;
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

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getSubjectId() {
        return SubjectId;
    }

    public void setSubjectId(String subjectId) {
        SubjectId = subjectId;
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
}
