<?php
require_once '../dao/LoginDetailsDAO.php';

class LoginDetails
{
    private $email;
	private $mobileno;
    private $password;
    private $randomNoForUser;
    private $activationCode;
    private $newPassword;
    
    
    public function setEmail($email) {
        $this->email = $email;
    }
    
    public function getEmail() {
        return $this->email;
    }
	
	public function setMobileno($mobileno) {
        $this->mobileno = $mobileno;
    }
    
    public function getMobileno() {
        return $this->mobileno;
    }
    
    public function setRandomNoForUser($randomNoForUser) {
        $this->randomNoForUser = $randomNoForUser;
    }
    
    public function getRandomNoForUser() {
        return $this->randomNoForUser;
    }
    public function setActivationCode($activationCode) {
        $this->activationCode = $activationCode;
    }
    
    public function getActivationCode() {
        return $this->activationCode;
    }
     public function setNewPassword($newPassword) {
        $this->newPassword = $newPassword;
    }
    
    public function getNewPassword() {
        return $this->newPassword;
    }
    public function setPassword($password) {
        $this->password = $password;
    }
    
    public function getPassword() {
        return $this->password;
    }
//for login
    public function CheckingUsersDetails($email,$password,$mobileno) {
        $this->setEmail($email);
        $this->setPassword($password);
		$this->setMobileno($mobileno);
        $showLoginDetailsDAO = new LoginDetailsDAO();
        $returnShowLoginDetails = $showLoginDetailsDAO->loginDetail($this);
        return $returnShowLoginDetails;
    }
	
	public function PasswordChecking($email,$password) {
        $this->setEmail($email);
        $this->setPassword($password);		
        $showLoginDetailsDAO = new LoginDetailsDAO();
        $returnCheckPasswordDetails = $showLoginDetailsDAO->checkPassword($this);
        return $returnCheckPasswordDetails;
    }
    public function SettingNewPassword($activationCode,$newPassword,$email) {
        $this->setActivationCode($activationCode);
        $this->setNewPassword($newPassword);
        $this->setEmail($email);
		
        $newPasswordDetailsDAO = new LoginDetailsDAO();
        $returnNewPasswordDetails = $newPasswordDetailsDAO->setNewPassword($this);
        return $returnNewPasswordDetails;
    }
	
 //checking email for valid user or not..
    public function CheckingEmail($email) {
        $this->setEmail($email);
        $showLoginDetailsDAO = new LoginDetailsDAO();
        $returnShowLoginDetails = $showLoginDetailsDAO->emailDetail($this);
        return $returnShowLoginDetails;
    }
    
     public function GenarateRandomNo($email) {
        //Call RandomNoGenarator class to create Random no
        $this->setEmail($email);
        $randomno = new RandomNoGenarator();
        $genaratedRandomNo = $randomno->GenarateCode(6);
        $this->setRandomNoForUser($genaratedRandomNo);
        
        // call GenarateEmailForUSer to send Randomno to user
        $returnSuccessRandomNo = $this->GenarateEmailForUSer();
        
        //call LoginDetailsDAO to save random no as per user 
        $saveRandomNoDAO= new LoginDetailsDAO();
        $returnSuccessRandomNo = $saveRandomNoDAO->savingRandomNo($this);
        return $returnSuccessRandomNo;
    }
    //Call Email Class to create email for user
    public function GenarateEmailForUSer(){
        $emailSender = new EmailGenarator();
        $emailSender->setTo($this->getEmail());//write user mail id
        $emailSender->setFrom('From: no-reply@app.com' . "\r\n" . 'Reply-To: no-reply@app.com' . "\r\n" . 'X-Mailer: PHP/' . phpversion());//write pet App mail id
        $emailSender->setMessage($this->createMessageToSendUser());
        $emailSender->setSubject("Password Recovery Code");// from petapp email
        return $emailSender->sendEmail($emailSender);        
    } 
    public function createMessageToSendUser(){
        $emailMessage="Your activation code is ".$this->getRandomNoForUser();
        return $emailMessage;
    }
	// Ngo Not Vefify
	public function NgoNotVerify($NgoEmail){
        $emailSender = new EmailGenarator();
        $emailSender->setTo($NgoEmail);//write user mail id
        $emailSender->setFrom('From: donations@petoandme.com' . "\r\n" . 'Reply-To: no-reply@app.com' . "\r\n" . 'X-Mailer: PHP/' . phpversion());//write pet App mail id
        $emailSender->setMessage($this->createMessageToSendNGO());
        $emailSender->setSubject("NGO Verification");// from petapp email      
		$returnEmailNgo =  $emailSender->sendEmail($emailSender);		
		if($returnEmailNgo==true){
			return returnEmailNgo;
		}else {
			$emailSender->sendEmail($emailSender);
		}      
    } 
    public function createMessageToSendNGO(){
        $emailMessage="Hi there ! \nThank you for signing up as an NGO with us. We hope to work together in helping as many animals as possible. Petoandme.com is a venture of CourageComm Solutions Private Limited.  We are focused around animal and pet needs. \nWe will be activating your account post a quick simple verification round done at our end for your NGO. By doing this we ensure that we are donating money for the right needs of animals.\nThank you for being so patient and we hope to establish a long term relationship in working for all needy animals. \nThanking you,\nTeam Peto\n\n\n\n Post Activation Email: \n Hi,\n Congratulations ! Your account with Peto has been activated. You can now create, view modify and delete campaigns for helping animals. \n Log into you Peto mobile app or use our NGO dashboard to login by clicking the following link: \n <LINK>\n\n Please feel free to contact us if you are having any difficulty or query on:\n support@petoandme.com and we will respond to your query within 24 hours. \n Thanking you,\n Team Peto.";	 
		return $emailMessage;
    }
}
?>