<?php
require_once '../dao/UsersDetailsDAO.php';
class UsersDetails
{
	private $name;
    private $mobileno;
    private $state;
	private $email;
	private $oldEmail;
    private $oldMobileno;
	private $password;
	
    public function setName($name) {
        $this->name = $name;
    }
    
    public function getName() {
        return $this->name;
    }
     public function setOldEmail($oldEmail) {
        $this->oldEmail = $oldEmail;
    }
	public function getOldEmail() {
        return $this->oldEmail;
    }
      
    public function setMobileno($mobileno) {
        $this->mobileno = $mobileno;
    }
    
    public function getMobileno() {
        return $this->mobileno;
    }
	
	  public function setEmail($email) {
        $this->email = $email;
    }
    
    public function getEmail() {
        return $this->email;
    }
    
    public function setState($state) {
        $this->state = $state;
    }
    public function getState() {
        return $this->state;
    }
    
    public function setOldMobileno($oldMobileno) {
        $this->oldMobileno = $oldMobileno;
    }
    
    public function getOldMobileno() {
        return $this->oldMobileno;
    }
	
	public function setPassword($password) {
        $this->password = $password;
    }
    
    public function getPassword() {
        return $this->password;
    }
	
    public function mapIncomingUserDetailsParams($name,$mobileno,$email,$state,$password) {
        $this->setName($name);
        $this->setMobileno($mobileno);
		$this->setEmail($email);
        $this->setState($state); 
		$this->setPassword($password); 		
    }

    public function SavingUsersDetails() {
        $saveUsersDetailsDAO = new UsersDetailsDAO();
        $returnUsersDetailsSaveSuccessMessage = $saveUsersDetailsDAO->insertUserDetails($this);
        return $returnUsersDetailsSaveSuccessMessage;
    }
    public function mapIncomingEditUserDetailsParams($name,$mobileno,$state,$oldMobileno,$email,$oldEmail) {
        $this->setName($name);
        $this->setMobileno($mobileno);
		$this->setState($state);
        $this->setOldMobileno($oldMobileno);
		$this->setEmail($email);
		$this->setOldEmail($oldEmail);
    }
    public function SavingEditUsersDetails() {
        $saveUsersDetailsDAO = new UsersDetailsDAO();
        $returnUsersDetailsSaveSuccessMessage = $saveUsersDetailsDAO->saveEditDetail($this);
        return $returnUsersDetailsSaveSuccessMessage;
    }
    public function FetchingUsersDetails($oldEmail,$password) {
        $saveUsersDetailsDAO = new UsersDetailsDAO();
        $this->setOldEmail($oldEmail);
		$this->setPassword($password);
        $returnUsersDetailsSaveSuccessMessage = $saveUsersDetailsDAO->fetchUserDetail($this);
        return $returnUsersDetailsSaveSuccessMessage;
    }
	public function FetchingContactDetails($email) {
        $fetchContactDetailsDAO = new UsersDetailsDAO();
        $this->setEmail($email);	
        $returnFetchContactDetailsMessage = $fetchContactDetailsDAO->fetchContactDetail($this);
        return $returnFetchContactDetailsMessage;
    }
    
}
?>