package com.inducesmile.androidpayexample.helpers;

/**
 * Created by user on 10-10-2017.
 */

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Ravi Tamada on 07/10/16.
 * www.androidhive.info
 */

@IgnoreExtraProperties
public class User {

    public String name;
    public String address;
    public String mob;
    public String email;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String address, String mob, String email) {
        this.name = name;
        this.address = address;
        this.mob = mob;

        this.email = email;
    }
}