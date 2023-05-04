package com.irmakburkay.easypass;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "password")
public class Password {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private int order;
    private String name, pass;
    private int icon;

    public Password() {}

    public Password(Password password) {
        this.id = password.getId();
        this.order = password.getOrder();
        this.name = password.getName();
        this.pass = password.getPass();
        this.icon = password.getIcon();
    }

    public Password(int order, String name, String pass, int icon) {
        this.order = order;
        this.name = name;
        this.pass = pass;
        this.icon = icon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Password)) return false;
        Password password = (Password) o;
        return getId() == password.getId() && getOrder() == password.getOrder() && getIcon() == password.getIcon() && getName().equals(password.getName()) && getPass().equals(password.getPass());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOrder(), getName(), getPass(), getIcon());
    }

    public Password copy() {
        return new Password(this);
    }

}