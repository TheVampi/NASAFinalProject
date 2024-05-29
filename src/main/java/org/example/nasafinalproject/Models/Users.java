package org.example.nasafinalproject.Models;

public class Users {
    String username;
    String password;
    String APIKey;
    int isAdmin;

    public Users() {
    }

    public Users(String username, String password, String APIKey, int isAdmin) {
        this.username = username;
        this.password = password;
        this.APIKey = APIKey;
        this.isAdmin = isAdmin;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAPIKey() {
        return APIKey;
    }

    public void setAPIKey(String APIKey) {
        this.APIKey = APIKey;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }
}
