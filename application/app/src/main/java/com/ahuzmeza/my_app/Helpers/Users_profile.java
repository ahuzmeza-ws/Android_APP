package com.ahuzmeza.my_app.Helpers;

public class Users_profile {

    private String  username;
    private String email;

    public Users_profile(String _username, String _email) {
        this.username = _username;
        this.email = _email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

}
