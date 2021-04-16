<?php

/*
 * Following code will list all the products
 */
 
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "ce2006";

// Create connection
$conn = mysqli_connect($servername, $username, $password, $dbname);

// array for JSON response
$response = array();

$sql = "SELECT * FROM account WHERE type= 'parent' ";
 
// get all products from products table
$result = @mysqli_query($conn,$sql);
 
// check for empty result
if (mysqli_num_rows($result) > 0) {
    // looping through all results
	
    $response["nameList"] = array();
 
    while ($row = mysqli_fetch_array($result)) {
        // temp user array
        $nameList = array();
        $nameList["uid"] = $row["uid"];
        $nameList["name"] = $row["name"];
 
        // push single product into final response array
        array_push($response["nameList"], $nameList);
    }
    // success
    $response["success"] = 1;
    $response["message"] = "Pulled all data";
 
    // echoing JSON response
    echo json_encode($response);
} else {
    // no products found
    $response["success"] = 0;
    $response["message"] = "No feedbacks found";
 
    // echo no users JSON
    echo json_encode($response);
}

?>