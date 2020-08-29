package com.hvtechnologies.shardaapp;

public class MarksheetClass {

    String Name ;
    String Id ;
    String Marks ;
    String Total ;
    String Email ;

    public MarksheetClass(String name, String id, String marks, String total, String email) {
        Name = name;
        Id = id;
        Marks = marks;
        Total = total;
        Email = email;
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

    public String getMarks() {
        return Marks;
    }

    public void setMarks(String marks) {
        Marks = marks;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
