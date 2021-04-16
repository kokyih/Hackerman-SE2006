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
if (isset($_POST["id"])) {
    $id = $_POST['id'];
    
    $sql = "SELECT * FROM consentform WHERE id= '$id' ";
 
    // get a product from products table
	$result = @mysqli_query($conn,$sql);
 
    if (!empty($result)) {
        // check for empty result
        if (mysqli_num_rows($result) > 0) {
 
            $result = mysqli_fetch_array($result);
 
            $consentform = array();
            $consentform["id"] = $result["id"];
			$consentform["senderid"] = $result["senderid"];
			$consentform["studentid"] = $result["studentid"];
			$consentform["title"] = $result["title"];
			$consentform["message"] = $result["message"];
			$consentform["status"]=$result["status"];
            // success
            $response["success"] = 1;
 
            // user node
            $response["consentform"] = array();
 
            array_push($response["consentform"], $consentform);
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no product found
            $response["success"] = 0;
            $response["message"] = "No consent form found1";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no product found
        $response["success"] = 0;
        $response["message"] = "No consentform found2";
 
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