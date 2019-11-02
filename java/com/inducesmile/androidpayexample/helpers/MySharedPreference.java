package com.inducesmile.androidpayexample.helpers;


import android.content.Context;
import android.content.SharedPreferences;



//Shared Preferences allow you to save and retrieve data in the form of key,value pair.
//In order to use shared preferences, you have to call a method getSharedPreferences()
// that returns a SharedPreference instance pointing to the file that contains the values of preferences.

//SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//              The first parameter is the KEY and the second parameter is the MODE


public class MySharedPreference {

    private SharedPreferences prefs;

    private Context context;

    public MySharedPreference(Context context){
        this.context = context;
        prefs = context.getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE);
    }

    public void addProductToTheCart(String product){
        SharedPreferences.Editor edits = prefs.edit();
        edits.putString(Constants.PRODUCT_ID, product);
        edits.apply();
    }


    public String retrieveProductFromCart(){
        return prefs.getString(Constants.PRODUCT_ID, "");
    }

    public void addProductCount(int productCount){
        SharedPreferences.Editor edits = prefs.edit();
        edits.putInt(Constants.PRODUCT_COUNT, productCount);
        edits.apply();
    }


    public int retrieveProductCount(){
       return prefs.getInt(Constants.PRODUCT_COUNT, 0);
    }
}
