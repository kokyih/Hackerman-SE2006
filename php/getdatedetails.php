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
if (isset($_POST["date"])) {
    $date = $_POST['date'];
 
    $sql = "SELECT * FROM events WHERE edate= '$date' ";
 
    // get a product from products table
	$result = @mysqli_query($conn,$sql);
 
    if (!empty($result)) {
        // check for empty result
        if (mysqli_num_rows($result) > 0) {
            $result = mysqli_fetch_array($result);
            //$events = array();
            //$events["title"]=$result["title"];
			$response["message"] = "Got event out";
            // success
            $response["success"] = 1;
            $response["events"]= array();
           // array_push($response["events"], $events);
			$response["details"] = $result["title"] ; //'have event';
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no product found
            $response["success"] = 0;
            $response["message"] = "No event found leh";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no product found
        $response["success"] = 0;
        $response["message"] = "No event found la";
 
        // echo no users JSON
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