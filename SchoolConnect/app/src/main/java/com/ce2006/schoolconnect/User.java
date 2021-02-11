package com.ce2006.schoolconnect;

public class User {

    private static String name = new String();
    private static String school = new String();

    public static void setName(String newname)
    {
        name = newname;
    }

    public static String getName()
    {
        return name;
    }

    public static void setSchool(String schl)
    {
        school = schl;
    }

    public static String getSchool()
    {
        return school;
    }

    public static void Logout()
    {
        name = "Default";
    }

}
