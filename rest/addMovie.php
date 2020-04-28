<?php
    if($_SERVER['REQUEST_METHOD']=="POST"){
        $name=isset($_POST['name'])?$_POST['name']:"";
        $directedBy=isset($_POST['directedBy'])?$_POST['directedBy']:"";
        $starring=isset($_POST['starring'])?$_POST['starring']:"";
        $openingOn=isset($_POST['openingOn'])?$_POST['openingOn']:"";

        $server_name="localhost";
        $user_name="root";
        $password="";
        $dbname="androidsecondassignment";

        $conn=new mysqli($server_name,$user_name,$password,$dbname);
        if($conn->connect_error){
            die("Connection Failed".$conn->connect_error);
        }

        $sql="insert into movies values (NULL,'".$name."','".$directedBy."','".$starring."',".$openingOn.")";
        if($conn->query($sql)==TRUE){
            echo "New record created successfully";
        }
        else{
            echo "Error: ".$sql."<br>".$conn->error;
        }
        $conn->close();
    }
?>