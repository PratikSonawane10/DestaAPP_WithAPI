<?php
require_once 'BaseDAO.php';
class UsersDetailsDAO
{
    
    private $con;
    private $msg;
    private $data;
    
    // Attempts to initialize the database connection using the supplied info.
    public function UsersDetailsDAO() {
        $baseDAO = new BaseDAO();
        $this->con = $baseDAO->getConnection();
    }
    
   
    public function insertUserDetails($UsersDetail) {
        try {
            $mobileNo =$UsersDetail->getMobileno();
            if($mobileNo == ""){
                $sql = "SELECT * FROM  userdetails WHERE email='".$UsersDetail->getEmail()."'";
                $emailValidating = mysqli_query($this->con, $sql);
                $count=mysqli_num_rows($emailValidating);
                if($count==1) {         
                    $this->data = "Email_Is_Already_Registered";
                }
                else {
                     $sql = "INSERT INTO userdetails(name,state,mobileno,email,password)
                        VALUES ('".$UsersDetail->getName()."','".$UsersDetail->getState()."', '".$UsersDetail->getMobileno()."','".$UsersDetail->getEmail()."','".$UsersDetail->getPassword()."')";
                
                    $isInserted = mysqli_query($this->con, $sql);               
                    if ($isInserted) {                  
                       $userId = mysqli_insert_id($this->con);
						$sql = "SELECT * FROM userDetails WHERE userId='$userId'";
						$result = mysqli_query($this->con, $sql);
						$userDetails=array();
						while ($rowdata = mysqli_fetch_assoc($result)) {
							$userDetails[]=$rowdata;
						}						
						$response= "USERS_DETAILS_SAVED";   
						$this->data=array($response,$userDetails);	                                 
                    } else {
                        $this->data = "ERROR";
                    }
                }
            }else{
                $sql = "SELECT * FROM  userdetails WHERE mobileNo='".$UsersDetail->getMobileno()."'";
                $mobileNoValidating = mysqli_query($this->con, $sql);
                $count=mysqli_num_rows($mobileNoValidating);
                if($count==1) {         
                    $this->data = "No_Is_Already_Registered";
                }
                else {
                    $sql = "SELECT * FROM  userdetails WHERE email='".$UsersDetail->getEmail()."'";
                    $emailValidating = mysqli_query($this->con, $sql);
                    $count=mysqli_num_rows($emailValidating);
                    if($count==1) {         
                         $this->data = "Email_Is_Already_Registered";
                    }
                    else{                                                
                         $sql = "INSERT INTO userdetails(name,state,mobileno,email,password)
                            VALUES ('".$UsersDetail->getName()."','".$UsersDetail->getState()."', '".$UsersDetail->getMobileno()."','".$UsersDetail->getEmail()."','".$UsersDetail->getPassword()."')";
                    
                        $isInserted = mysqli_query($this->con, $sql);               
                        if ($isInserted) {                  
                            $userId = mysqli_insert_id($this->con);
							$sql = "SELECT * FROM userDetails WHERE userId='$userId'";
							$result = mysqli_query($this->con, $sql);
							$userDetails=array();
							while ($rowdata = mysqli_fetch_assoc($result)) {
								$userDetails[]=$rowdata;
							}						
							$response= "USERS_DETAILS_SAVED";   
							$this->data=array($response,$userDetails);	                                   
                        } else {
                            $this->data = "ERROR";
                        }
                    }
                }
            }
            
            
        } catch(Exception $e) {
            echo 'SQL Exception: ' .$e->getMessage();
        }
        return $this->data;
    }

    

    public function saveEditDetail($EditUsersDetail) {
        try {                    
				$sql="UPDATE userdetails SET name='".$EditUsersDetail->getName()."',mobileNo='".$EditUsersDetail->getMobileno()."', state='".$EditUsersDetail->getState()."',email='".$EditUsersDetail->getEmail()."' WHERE mobileNo='".$EditUsersDetail->getOldMobileno()."'  OR email='".$EditUsersDetail->getOldEmail()."'";
                $isEdited = mysqli_query($this->con, $sql);
                if ($isEdited) {
                    $this->data = "USERS_DETAILS_EDITED";
                } else {
                    $this->data = "ERROR";
                }
        } catch(Exception $e) {
            echo 'SQL Exception: ' .$e->getMessage();
        }
        return $this->data;
    }      
}
?>