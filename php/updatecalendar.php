<?php
 
/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
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
if (isset($_POST['date']) && isset($_POST['title'])) {
    
    $date = $_POST['date'];
    $title = $_POST['title'];
 
    $sql = " ";
 
 //check if database has entry
 if(mysqli_num_rows(mysqli_query($conn, "SELECT * FROM events WHERE edate = '$date'")) > 0 ) {
 
    $sql = "UPDATE events Set title='$title' WHERE edate = '$date'";
} else {
    $sql = "INSERT INTO events(title, message, edate) VALUES('$title', 'default', '$date')";
}
 
 
 
    // mysql inserting a new row
    //$result = mysqli_query("INSERT INTO products(name, price, description) VALUES('$name', '$price', '$description')");
	//$sql = "INSERT INTO progressreport(studentid, english, maths ,science,mothertongue) VALUES('$studentid', '$english', '$maths' , '$science' ,'$mothertongue')";
	$result = mysqli_query($conn,$sql);
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Event submitted";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = $sql;//"Oops! An error occurred.";
 
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