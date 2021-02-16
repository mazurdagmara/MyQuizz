package com.example.myquizz;

public class Category {
    public static final int PROGRAMOWANIE = 1;
    public static final int GEOGRAFIA = 2;
    public static  int MATEMATYKA = 3;

    private int id;
    private String nazwa;

    public Category(){
    }

    public Category(String nazwa) {
        this.nazwa = nazwa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    @Override
    public String toString() {
        return getNazwa();
    } //dzięki temu spinner wypełni się nazwami
}
