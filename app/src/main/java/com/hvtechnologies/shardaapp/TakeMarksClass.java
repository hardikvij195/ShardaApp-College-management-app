package com.hvtechnologies.shardaapp;

public class TakeMarksClass {

    String Name ;
    String Marks ;
    String SysId ;
    String StdId ;

    public TakeMarksClass(String name, String marks, String sysId, String stdId) {

        Name = name;
        Marks = marks;
        SysId = sysId;
        StdId = stdId;

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMarks() {
        return Marks;
    }

    public void setMarks(String marks) {
        Marks = marks;
    }


    public String getSysId() {
        return SysId;
    }

    public void setSysId(String sysId) {
        SysId = sysId;
    }

    public String getStdId() {
        return StdId;
    }

    public void setStdId(String stdId) {
        StdId = stdId;
    }
}
