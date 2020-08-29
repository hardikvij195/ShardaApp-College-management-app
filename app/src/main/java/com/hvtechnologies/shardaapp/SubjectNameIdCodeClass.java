package com.hvtechnologies.shardaapp;

public class SubjectNameIdCodeClass {

    String Name , Id , Code ;

    public SubjectNameIdCodeClass(String name, String id, String code) {
        Name = name;
        Id = id;
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
}
