package com.ahuzmeza.my_app.Helpers;

public class Users_profile {

        private int     id;
        private String  username, email;

        public Users_profile(int _id, String _username, String _email) {
            this.id = _id;
            this.username = _username;
            this.email = _email;
        }

        public int getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

}
