package com.ihp.frontoffice.helper;

public enum UserRole {

//  posisi: String = 'SUPERVISOR';
//  posisi: String = 'ACCOUNTING';
//  posisi: String = 'KAPTEN';
//  posisi: String = 'ADMIN';
//  posisi: String = 'IT';
//  posisi: String = 'KASIR';
//  posisi: String = 'SERVER'; //WAITER
//  posisi: String = 'DAPUR';
//  posisi: String = 'BAR';    //HELPER

    SUPERVISOR("SUPERVISOR"),
    ACCOUNTING("ACCOUNTING"),
    KAPTEN("KAPTEN"),
    ADMIN("ADMIN"),
    IT("IT"),
    KASIR("KASIR"),
    WAITER("WAITER"),
    SERVER("SERVER"),
    DAPUR("DAPUR"),
    BAR("BAR");

    private final String state;

    UserRole(String state) {
        this.state = state;
    }

    public String getRole() {
        return state;
    }
}