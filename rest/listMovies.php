<?php
if($_SERVER['REQUEST_METHOD']=="GET") {

    $server_name = "localhost";
    $user_name = "root";
    $password = "";
    $dbname = "androidsecondassignment";
    $conn = new mysqli($server_name, $user_name, $password, $dbname);
    if ($conn->connect_error) {
        die("Connection Failed" . $conn->connect_error);
    }

    $sql = "select * from movies";
    $result = $conn->query($sql);
    if ($result->num_rows > 0) {
        $data=array();
        while($row = $result->fetch_assoc()){
            $data[]=$row['name'];
        }
        $myJSON = json_encode($data);
        echo $myJSON;
    } else {
        echo "Error";
    }

}else{
    echo "Error: Method Request";
}
$conn->close();
?>