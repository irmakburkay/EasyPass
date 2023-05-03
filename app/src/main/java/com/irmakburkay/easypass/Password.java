package com.irmakburkay.easypass;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "password")
public class Password {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name, pass;
    private int icon;

    public Password(long id, String name, String pass, int icon) {
        this.id = id;
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
        return getId() == password.getId() && getIcon() == password.getIcon() && getName().equals(password.getName()) && getPass().equals(password.getPass());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPass(), getIcon());
    }
}
