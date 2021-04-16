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

//if( isset($_GET["id"]))
//{
    //$id = $_GET["id"];
    
    $sql = "SELECT * FROM consentform ";//WHERE targetid='$studentid' ";
     
    // get all products from products table
    $result = @mysqli_query($conn,$sql);
     
    // check for empty result
    if (mysqli_num_rows($result) > 0) {
        // looping through all results
    	
        $response["consentform"] = array();
     
        while ($row = mysqli_fetch_array($result)) {
            // temp user array
            $consentform = array();
            $consentform["id"] = $row["id"];
            $consentform["senderid"] = $row["senderid"];
            $consentform["studentid"] = $row["studentid"];
            $consentform["title"] = $row["title"];
            $consentform["message"] = $row["message"];
            $consentform["status"] = $row["status"];
     
            // push single product into final response array
            array_push($response["consentform"], $consentform);
        }
        // success
        $response["success"] = 1;
     
        // echoing JSON response
        echo json_encode($response);
    } else {
        // no products found
        $response["success"] = 0;
        $response["message"] = "No consent form found";
     
        // echo no users JSON
        echo json_encode($response);
    }
//}
?>