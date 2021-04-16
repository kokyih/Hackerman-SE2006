<?php
 
/*
 * Following code will get single product details
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
 
// check for post data
if (isset($_POST["classID"]) && isset($_POST["setEndClass"])) 
{
    $classID = $_POST['classID'];
    $endClass = $_POST['setEndClass'];
    
    //"UPDATE account SET location = '$location' WHERE uid = $uid";
 
    $sql = "UPDATE account SET EndClass = '$endClass' WHERE classID= '$classID'  AND type= 'parent' ";
 
    // get a product from products table
	$result = @mysqli_query($conn,$sql);
 
    // check if row inserted or not
    if ($result) 
    {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "End class successfully updated.";
 
        // echoing JSON response
        echo json_encode($response);
    } 
    else 
    {
        $response["success"] = 0;
        $response["message"] = "End class failed to update.";
 
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