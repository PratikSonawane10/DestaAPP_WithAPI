<?php
class BaseDAO {
    
    
    // private $db_host = 'localhost'; //hostname
    // private $db_user = 'appcom_petuser'; // username
    // private $db_password = 'pet@pp2015!'; // password
    // private $db_name = 'appcom_petapp'; //database name
    // private $con = null;
	
	 private $db_host = 'localhost'; //hostname
    private $db_user = 'root'; //'appcom_petuser'; // username
    private $db_password = ''; //'pet@pp2015!'; // password
    private $db_name = 'desta'; //'appcom_petapp'; //database name
    private $con = null;
    
    private $googleAPIKey = 'AIzaSyCmwKfKS6M75W814jOQ0r3o8bpVdYCoD8A';  
	
    public function getConnection() {
        $this->con=mysqli_connect($this->db_host,$this->db_user,$this->db_password,$this->db_name) or die("Failed to connect to MySQL:".mysql_error());

        if (mysqli_connect_errno()) {
            echo "Failed to connect to MySQL: " . mysqli_connect_error();
        }
        return $this->con;
    }
	public function getGoogleAPIKey() {
        return $this->googleAPIKey;
    }
}
?>