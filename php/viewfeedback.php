<?php

/*
 * Following code will list all the products
 */
 
$servername = "localhost";
$username = "id16304111_admin";
$password = "C$)=e[nav8HIO[Gr";
$dbname = "id16304111_ce2006";

// Create connection
$conn = mysqli_connect($servername, $username, $password, $dbname);

// array for JSON response
$response = array();
 
$sql = "SELECT * FROM feedback";
 
// get all products from products table
$result = @mysqli_query($conn,$sql);
 
// check for empty result
if (mysqli_num_rows($result) > 0) {
    // looping through all results
	
    $response["feedback"] = array();
 
    while ($row = mysqli_fetch_array($result)) {
        // temp user array
        $feedback = array();
        $feedback["id"] = $row["id"];
        $feedback["targetid"] = $row["targetid"];
        $feedback["submitid"] = $row["submitid"];
        $feedback["title"] = $row["title"];
        $feedback["message"] = $row["message"];
 
        // push single product into final response array
        array_push($response["feedback"], $feedback);
    }
    // success
    $response["success"] = 1;
 
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