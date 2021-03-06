<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
$servername = "localhost";
$username = "id16304111_admin";
$password = "C$)=e[nav8HIO[Gr";
$dbname = "id16304111_ce2006";

// Create connection
$conn = mysqli_connect($servername, $username, $password, $dbname);
 
// check for required fields
if (isset($_POST['target']) && isset($_POST['title']) && isset($_POST['message']) && isset($_POST['submitid']) ) {
 
    $target = $_POST['target'];
    $title = $_POST['title'];
    $message = $_POST['message'];
	$submitid = $_POST['submitid'];
 
    // mysql inserting a new row
    //$result = mysqli_query("INSERT INTO products(name, price, description) VALUES('$name', '$price', '$description')");
	$sql = "INSERT INTO feedback(targetid, submitid, title ,message) VALUES('$target', '$submitid', '$title' , '$message')";
	$result = mysqli_query($conn,$sql);
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Feedback submitted";
 
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