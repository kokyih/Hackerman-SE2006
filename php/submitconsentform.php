<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ce2006";

// Create connection
$conn = mysqli_connect($servername, $username, $password, $dbname);
 
// check for required fields
if (isset($_POST['studentid']) && isset($_POST['senderid']) && isset($_POST['title']) && isset($_POST['message']) ) {
    
    $studentid = $_POST['studentid'];
    $senderid = $_POST['senderid'];
    $title = $_POST['title'];
	$message = $_POST['message'];
    
    $status = 0;
    $sql = " ";
 
 //check if database has entry

    $sql = "INSERT INTO consentform(senderid, studentid, title ,message,status) VALUES('$senderid', '$studentid', '$title' , '$message' ,'$status')";

 
 
 
    // mysql inserting a new row
    //$result = mysqli_query("INSERT INTO products(name, price, description) VALUES('$name', '$price', '$description')");
	//$sql = "INSERT INTO progressreport(studentid, english, maths ,science,mothertongue) VALUES('$studentid', '$english', '$maths' , '$science' ,'$mothertongue')";
	$result = mysqli_query($conn,$sql);
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "consent form submitted";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>