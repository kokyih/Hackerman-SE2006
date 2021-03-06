<?php
 
/*
 * Following code will get single product details
 * A product is identified by product id (pid)
 */
 
// array for JSON response
$response = array();
 
$servername = "localhost";
$username = "id16304111_admin";
$password = "C$)=e[nav8HIO[Gr";
$dbname = "id16304111_ce2006";

// Create connection
$conn = mysqli_connect($servername, $username, $password, $dbname);
 
// check for post data
if (isset($_POST["id"])) {
    $id = $_POST['id'];
 
    $sql = "SELECT * FROM feedback WHERE id= '$id' ";
 
    // get a product from products table
	$result = @mysqli_query($conn,$sql);
 
    if (!empty($result)) {
        // check for empty result
        if (mysqli_num_rows($result) > 0) {
 
            $result = mysqli_fetch_array($result);
 
            $feedback = array();
            $feedback["id"] = $result["id"];
			$feedback["targetid"] = $result["targetid"];
			$feedback["submitid"] = $result["submitid"];
			$feedback["title"] = $result["title"];
			$feedback["message"] = $result["message"];
            // success
            $response["success"] = 1;
 
            // user node
            $response["feedback"] = array();
 
            array_push($response["feedback"], $feedback);
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no product found
            $response["success"] = 0;
            $response["message"] = "No feedback found1";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no product found
        $response["success"] = 0;
        $response["message"] = "No feedback found2";
 
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