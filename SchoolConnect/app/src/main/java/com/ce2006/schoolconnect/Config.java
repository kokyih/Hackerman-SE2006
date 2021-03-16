package com.ce2006.schoolconnect;

public class Config {

    private static String ipaddr = "116.14.2.247:8080";

    private static String prefix = "http://";
    //private static String postfix = "/CE2006/";
    private static String postfix = "ce2006project2.000webhostapp.com/";
    private static final String urlhead = prefix + postfix; //ipaddr + postfix;


    public static String login = urlhead + "login.php";
    public static String register = urlhead + "register.php";

    public static String upload = urlhead + "uploadfile.php";

    public static String submitfeedback = urlhead + "submitfeedback.php";
    public static String viewfeedback = urlhead + "viewfeedback.php";
    public static String view1feedback = urlhead + "getfeedback.php";
    public static String getfeedbackNames = urlhead + "getNameFeedback.php";

    public static String viewConsentForm = urlhead + "viewconsentform.php";
    public static String view1ConsentForm = urlhead + "getconsentform.php";
    public static String updateConsentForm = urlhead + "updateconsentform.php";
    public static String submitConsentForm = urlhead + "submitconsentform.php";
    public static String getClassIdList = urlhead + "getclassidlist.php";

    public static String updateLocation = urlhead + "updateBusLoc.php";

    public static String updatecalendar = urlhead + "updatecalendar.php";
    public static String getdatedetails = urlhead + "getdatedetails.php";

    public static String updateProgressReport = urlhead + "submitprogressreport.php";
    public static String getProgressReport = urlhead + "getprogressreport.php";
    public static String getStudentIdList = urlhead + "getstudentidlist.php";

    public static String getClassEnd = urlhead + "getClassEnd.php";
    public static String endClass = urlhead + "endClass.php";

    public static String download = urlhead + "uploadfolder/" ;
    public static String downloadTB = urlhead + "timetable/" ;

}
