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
if (isset($_POST["studentid"])) {
    $studentid = $_POST['studentid'];
 
    $sql = "SELECT * FROM progressreport WHERE studentid= '$studentid' ";
 
    // get a product from products table
	$result = @mysqli_query($conn,$sql);
 
    if (!empty($result)) {
        // check for empty result
        if (mysqli_num_rows($result) > 0) {
 
            $result = mysqli_fetch_array($result);
 
            $response["studentid"] = $result["studentid"];
			$response["english"] = $result["english"];
			$response["maths"] = $result["maths"];
			$response["science"] = $result["science"];
			$response["mothertongue"] = $result["mothertongue"];
            // success
            $response["success"] = 1;

 
            // echoing JSON response
            $response["message"] = "Progress report found";
            echo json_encode($response);
            
        } else {
            // no product found
            $response["success"] = 0;
            $response["message"] = "No progress report found1";
            
            $response["english"] = "Not available";
			$response["maths"] = "Not available";
			$response["science"] = "Not available";
			$response["mothertongue"] = "Not available";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no product found
        $response["success"] = 0;
        $response["message"] = "No progress report found2";
        
        $response["english"] = "Not available";
		$response["maths"] = "Not available";
		$response["science"] = "Not available";
		$response["mothertongue"] = "Not available";
        
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