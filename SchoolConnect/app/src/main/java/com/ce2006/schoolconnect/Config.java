package com.ce2006.schoolconnect;

public class Config {

    private static String ipaddr = "116.14.2.247:8080";

    private static String prefix = "http://";
    private static String postfix = "/CE2006/";
    private static final String urlhead = prefix + ipaddr + postfix;


    public static String login = urlhead + "login.php";
    public static String register = urlhead + "register.php";
    public static String upload = urlhead + "uploadfile.php";
    public static String submitfeedback = urlhead + "submitfeedback.php";
    public static String viewfeedback = urlhead + "viewfeedback.php";
    public static String view1feedback = urlhead + "getfeedback.php";

    public static String download = urlhead + "uploadfolder/" ;

}