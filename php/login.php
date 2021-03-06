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
if (isset($_POST["email"]) && isset($_POST["password"])) {
    $email = $_POST['email'];
	$password = $_POST['password'];
 
    $sql = "SELECT * FROM account WHERE email= '$email' ";
 
    // get a product from products table
	$result = @mysqli_query($conn,$sql);
 
    if (!empty($result)) {
        // check for empty result
        if (mysqli_num_rows($result) > 0) {
            $result = mysqli_fetch_array($result);
			
			if( $result["logged"] == false)
			{
				if ( STRCMP($result["password"] ,$password) == 0 )
				{
					//$user = array();
					//$user["name"] = $result["name"];
					$response["name"] = $result["name"];
					$response["school"] = $result["school"];
					$response["userid"] = $result["uid"];
					$response["role"] = $result["type"];
					// success
					$response["success"] = 1;
					$response["message"] = "Logged in successfully";
		 
					// user node
					//$response["user"] = array();
		 
					//array_push($response["user"], $user);
		 
					// echoing JSON response
					echo json_encode($response); 
				}
				else {
					// already logged in
					$response["success"] = 0;
					$response["message"] = "Wrong password";
		 
					// echo no users JSON
					echo json_encode($response);
				}
				
			} else {
				// already logged in
				$response["success"] = 0;
				$response["message"] = "Already logged in";
	 
				// echo no users JSON
				echo json_encode($response);
			}
			
        } else {
            // no product found
            $response["success"] = 0;
            $response["message"] = "No users found 1";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no product found
        $response["success"] = 0;
        $response["message"] = "No users found 2";
 
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