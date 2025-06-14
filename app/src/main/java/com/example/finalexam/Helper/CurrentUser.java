// com.example.finalexam.Helper.CurrentUser.java
package com.example.finalexam.Helper;

import com.example.finalexam.Domain.UserModel;

public class CurrentUser {
    private static UserModel currentUser;

    public static void setUser(UserModel user) {
        currentUser = user;
    }

    public static UserModel getUser() {
        return currentUser;
    }

    public static void clear() {
        currentUser = null;
    }
}
