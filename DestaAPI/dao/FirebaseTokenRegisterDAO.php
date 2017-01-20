<?php
require_once 'BaseDAO.php';
class FirebaseTokenRegisterDAO
{
    
    private $con;
    private $msg;
    private $data;
    
    // Attempts to initialize the database connection using the supplied info.
    public function FirebaseTokenRegisterDAO() {
        $baseDAO = new BaseDAO();
        $this->con = $baseDAO->getConnection();
    }

    public function checkFirebaseToken($FirebaseDetails) {
        try {
            $sql = "SELECT * FROM firebasetokens WHERE deviceid='".$FirebaseDetails->getAndroidId()."'";
            $isChecking = mysqli_query($this->con, $sql);
            $count = mysqli_num_rows($isChecking);
            if($count == 0) {
                $this->data = $this->saveFirebaseToken($FirebaseDetails);
            } else {
                $this->data = $this->updateFirebaseToken($FirebaseDetails);
            }
        } catch(Exception $e) {
            echo 'SQL Exception: ' .$e->getMessage();
        }
        return $this->data;
    }

    public function saveFirebaseToken($FirebaseDetails) {
        try {
            $sql = "INSERT INTO firebasetokens(deviceid, token)
                        VALUES ('".$FirebaseDetails->getAndroidId()."', '".$FirebaseDetails->getToken()."')";
            $isInserted = mysqli_query($this->con, $sql);
            if($isInserted) {
                $this->data = "FIREBASE_TOKEN_SAVED";
            } else {
                $this->data = "FIREBASE_TOKEN_NOT_SAVED";
            }
        } catch(Exception $e) {
            echo 'SQL Exception: ' .$e->getMessage();
        }
        return $this->data;
    }

    public function updateFirebaseToken($FirebaseDetails) {
        try {
            $sql="UPDATE firebasetokens SET token='".$FirebaseDetails->getToken()."'
                    WHERE deviceid='".$FirebaseDetails->getAndroidId()."' ";
            $isUpdated = mysqli_query($this->con, $sql);
            if($isUpdated) {
                $this->data = "FIREBASE_TOKEN_UPDATED";
            } else {
                $this->data = "FIREBASE_TOKEN_NOT_UPDATED";
            }
        } catch(Exception $e) {
            echo 'SQL Exception: ' .$e->getMessage();
        }
        return $this->data;
    }
}
?>