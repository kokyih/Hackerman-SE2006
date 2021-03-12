package com.ce2006.schoolconnect;

import android.graphics.Bitmap;

public class User {

    private static String name = new String();
    private static String school = new String();
    private static String userid = new String();
    private static String role = new String();
    private static String classID = new String();

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

}
