<?php
 
/*
 * Following code will update a product information
 * A product is identified by product id (pid)
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
if (isset($_POST['uid']) && isset($_POST['location'])) {
 
    $uid = $_POST['uid'];
    $location = $_POST['location'];
 
    // mysql update row with matched pid
	$sql = "UPDATE account SET location = '$location' WHERE uid = $uid";
    $result = mysqli_query($conn, $sql);
 
    // check if row inserted or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Location successfully updated.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
 
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>