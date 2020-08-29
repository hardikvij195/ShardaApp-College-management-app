package com.hvtechnologies.shardaapp;

public class AssignmentClass {


    String SubName , SubCode , Date , Topic , Desc  , DwonloadUrl ;

    public AssignmentClass(String subName, String subCode, String date, String topic, String desc, String dwonloadUrl) {
        SubName = subName;
        SubCode = subCode;
        Date = date;
        Topic = topic;
        Desc = desc;
        DwonloadUrl = dwonloadUrl;
    }


    public String getSubName() {
        return SubName;
    }

    public void setSubName(String subName) {
        SubName = subName;
    }

    public String getSubCode() {
        return SubCode;
    }

    public void setSubCode(String subCode) {
        SubCode = subCode;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getDwonloadUrl() {
        return DwonloadUrl;
    }

    public void setDwonloadUrl(String dwonloadUrl) {
        DwonloadUrl = dwonloadUrl;
    }
}
