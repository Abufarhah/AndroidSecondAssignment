<?php
    if($_SERVER['REQUEST_METHOD']=="GET") {
        $id = 0;
        if (isset($_GET['id'])) {
            $id = $_GET['id'];
        }
        $server_name = "localhost";
        $user_name = "root";
        $password = "";
        $dbname = "androidsecondassignment";
        $conn = new mysqli($server_name, $user_name, $password, $dbname);
        if ($conn->connect_error) {
            die("Connection Failed" . $conn->connect_error);
        }
        if ($id != 0) {
            $sql = "select * from movies where id='" . $id . "'";
            $result = $conn->query($sql);
            if ($result->num_rows > 0) {
                $data = $result->fetch_assoc();
                $myObject= new \stdClass();
                $myObject->id=(int)$data['id'];
                $myObject->name=$data['name'];
                $myObject->directedBy=$data['directedBy'];
                $myObject->starring=$data['starring'];
                $myObject->openingOn=(int)$data['openingOn'];
                $myJSON = json_encode($myObject);
                echo $myJSON;
            } else {
                echo "Movie not found";
            }
        } else {
            $sql = "select * from movies";
            $result = $conn->query($sql);
            if ($result->num_rows > 0) {
                $data=array();
                while(($row=$result->fetch_assoc())!=NULL){
                    $myObject= new \stdClass();
                    $myObject->id=(int)$row['id'];
                    $myObject->name=$row['name'];
                    $myObject->directedBy=$row['directedBy'];
                    $myObject->starring=$row['starring'];
                    $myObject->openingOn=(int)$row['openingOn'];
                    $data[]=$myObject;
                }
                $myJSON = json_encode($data);
                echo $myJSON;
            } else {
                echo "Movie not found";
            }
        }
    }else{
        echo "Error: Method Request";
    }
    $conn->close();
?>