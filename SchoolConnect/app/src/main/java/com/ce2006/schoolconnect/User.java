package com.ce2006.schoolconnect;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;

public class User {

    private static String name = new String();
    private static String school = new String();
    private static String userid = new String();
    private static String role = new String();
    private static String classID = new String();

    private static float Lat = 0;
    private static float Long = 0;

    public static void setLat(float newLat) { Lat = newLat; }
    public static float getLat()
    {
        return Lat;
    }

    public static void setLong(float newLong) { Long = newLong; }
    public static float getLong()
    {
        return Long;
    }

    public static void setName(String newname)
    {
        name = newname;
    }
    public static String getName()
    {
        return name;
    }

    public static void setRole(String newrole)
    {
        role = newrole;
    }
    public static String getRole()
    {
        return role;
    }

    public static void setID(String newid)
    {
        userid = newid;
    }
    public static String getID()
    {
        return userid;
    }

    public static void setSchool(String schl)
    {
        school = schl;
    }
    public static String getSchool()
    {
        return school;
    }

    public static void setClassID(String nclassID)
    {
        classID = nclassID;
    }
    public static String getClassID()
    {
        return classID;
    }

    public static void Logout()
    {
        name = "Default";
    }

    public static Bitmap picture;

    public static void setBitmap(Bitmap newbit)
    {
        picture = newbit;
    }

    public static void save(String valueKey, String value, Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(valueKey, value);
        edit.commit();
    }

    public static String read(String valueKey, String valueDefault, Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return prefs.getString(valueKey, valueDefault);
    }

}
