<?php

$response = array();

//if ((($_FILES["file"]["type"] == "image/gif") || ($_FILES["file"]["type"] == "image/jpeg") || ($_FILES["file"]["type"] == "image/png"))
//&& ($_FILES["file"]["size"] > 20000) ) //&& isset($_POST["name"]) isset($_POST["type"]))
//{
	if ($_FILES["file"]["error"] > 0)
	{
		$response["message"] =  "Return Code: " . $_FILES["file"]["error"] ;
		$response["success"] = 0;
		
		echo json_encode($response); 
	}
	else
	{
		//echo "Upload: " . $_FILES["file"]["name"] . "<br />";
		//echo "Type: " . $_FILES["file"]["type"] . "<br />";
		//echo "Size: " . ($_FILES["file"]["size"] / 1024) . " Kb<br />";
		
		if (is_uploaded_file($_FILES['file']['tmp_name'])) 
		{ 
			move_uploaded_file($_FILES['file']['tmp_name'], 'uploadfolder/'.basename($_FILES['file']['name']));
			$response["message"] = "Successfully uploaded file";
			$response["success"] = 1;
			//$response["tamade"] = $_POST["type"];
			//$response["name"] = $_POST['name'];
			
			echo json_encode($response); 
		}

		if (file_exists("upload/" . $_FILES["file"]["name"]))
		{
			//echo $_FILES["file"]["name"] . " already exists. ";
			$response["message"] = $_FILES["file"]["name"] . " already exists. ";
			$response["success"] = 0;
			
			echo json_encode($response); 
		}
		else
		{
			// im having problem in uploading to wamp server
		
			$response["message"] = "Cant upload leh";
			$response["success"] = 0;
	
			//echo json_encode($response); 
		
		}
	}
//}
//else
//{
	//echo "Invalid file";
	//$response["message"] = "Invalid file";
	//$response["success"] = 0;
	
	//echo json_encode($response); 
//}

?>