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
if (isset($_POST['studentid']) && isset($_POST['english']) && isset($_POST['maths']) && isset($_POST['science'])&& isset($_POST['mothertongue']) ) {
    
    $studentid = $_POST['studentid'];
    $english = $_POST['english'];
    $maths = $_POST['maths'];
	$science = $_POST['science'];
    $mothertongue = $_POST['mothertongue'];
 
    $sql = " ";
 
 //check if database has entry
 if(mysqli_num_rows(mysqli_query($conn, "SELECT * FROM progressreport WHERE studentid = '$studentid'")) > 0 ) {
 
    $sql = "UPDATE progressreport Set english='$english', maths='$maths',science='$science',mothertongue='$mothertongue'   WHERE studentid = '$studentid'";
} else {
    $sql = "INSERT INTO progressreport(studentid, english, maths ,science,mothertongue) VALUES('$studentid', '$english', '$maths' , '$science' ,'$mothertongue')";
}
 
 
 
    // mysql inserting a new row
    //$result = mysqli_query("INSERT INTO products(name, price, description) VALUES('$name', '$price', '$description')");
	//$sql = "INSERT INTO progressreport(studentid, english, maths ,science,mothertongue) VALUES('$studentid', '$english', '$maths' , '$science' ,'$mothertongue')";
	$result = mysqli_query($conn,$sql);
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Progress report submitted";
 
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