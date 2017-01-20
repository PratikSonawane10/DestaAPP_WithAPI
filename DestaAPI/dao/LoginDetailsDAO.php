<?php
require_once 'BaseDAO.php';
class LoginDetailsDAO
{
    
    private $con;
    private $msg;
    private $data;
    
    // Attempts to initialize the database connection using the supplied info.
    public function LoginDetailsDAO() {
        $baseDAO = new BaseDAO();
        $this->con = $baseDAO->getConnection();
    }

    public function loginDetail($LoginDetails) {
        try {
            $sql = "SELECT * FROM userDetails WHERE email='".$LoginDetails->getEmail()."' OR mobileNo='".$LoginDetails->getMobileno()."' AND password='".$LoginDetails->getPassword()."'";        
            $isValidating = mysqli_query($this->con, $sql);
            $count=mysqli_num_rows($isValidating);
            if($count==1) {
                $response = "LOGIN_SUCCESSFULL";
				$userDetails=array();
				while ($rowdata = mysqli_fetch_assoc($result)) {
					$userDetails[]=$rowdata;
				}										
				$this->data=array($response,$userDetails);
            } else {
                $this->data = "LOGIN_FAILED";
            }
        } catch(Exception $e) {
            echo 'SQL Exception: ' .$e->getMessage();
        }
        return $this->data;
    }

                
}
?>