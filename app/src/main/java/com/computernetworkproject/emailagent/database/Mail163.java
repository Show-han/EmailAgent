package com.computernetworkproject.emailagent.database;

import org.litepal.crud.LitePalSupport;

public class Mail163 extends LitePalSupport {
    private int id;



    private String name;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAuthcode() {
        return Authcode;
    }

    public void setAuthcode(String authcode) {
        Authcode = authcode;
    }

    private String account;
    private String Authcode;


}
