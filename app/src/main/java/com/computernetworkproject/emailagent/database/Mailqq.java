package com.computernetworkproject.emailagent.database;

import org.litepal.crud.LitePalSupport;

public class Mailqq extends LitePalSupport {
    private int id;

    public void setName(String name) {
        this.name = name;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setAuthcode(String authcode) {
        Authcode = authcode;
    }

    private String name;

    public int getId() {
        return id;
    }

    private String account;

    public String getName() {
        return name;
    }

    public String getAccount() {
        return account;
    }

    public String getAuthcode() {
        return Authcode;
    }

    private String Authcode;
}
