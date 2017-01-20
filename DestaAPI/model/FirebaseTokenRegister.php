<?php
require_once '../dao/FirebaseTokenRegisterDAO.php';

class FirebaseTokenRegister
{
    private $android_id;
    private $token;
    
    
    public function setAndroidId($android_id) {
        $this->android_id = $android_id;
    }
    
    public function getAndroidId() {
        return $this->android_id;
    }
    
    public function setToken($token) {
        $this->token = $token;
    }
    
    public function getToken() {
        return $this->token;
    }

    public function firebaseTokenRegistration($android_id, $token) {
        $this->setAndroidId($android_id);
        $this->setToken($token);
        $saveFirebaseTokenDAO = new FirebaseTokenRegisterDAO();
        $returnSaveFirebaseTokenMessage = $saveFirebaseTokenDAO->checkFirebaseToken($this);
        return $returnSaveFirebaseTokenMessage;
    }
}
?>