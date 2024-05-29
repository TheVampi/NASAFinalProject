package org.example.nasafinalproject.Models;

public class Copyright {
    int copyrightID;
    String name;

    public Copyright() {
    }

    public Copyright(int copyrightID, String name) {
        this.copyrightID = copyrightID;
        this.name = name;
    }

    public Copyright(String name) {
        this.name = name;
    }

    public int getCopyrightID() {
        return copyrightID;
    }

    public void setCopyrightID(int copyrightID) {
        this.copyrightID = copyrightID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return  name;
    }
}
