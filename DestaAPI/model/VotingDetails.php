<?php
require_once '../dao/VotingDetailsDAO.php';
class VotingDetails
{
	private $userId;
	private $photoCategory;
	private $photoId;
	private $currentPage;
	

public function setPhotoCategory($photoCategory) {
        $this->photoCategory = $photoCategory;
    }   
    public function getPhotoCategory() {
        return $this->photoCategory;
    }	
		
	public function setuserId($userId) {
        $this->userId = $userId;
    }   
    public function getuserId() {
        return $this->userId;
    }	
	
	public function setCurrentPage($currentPage) {
        $this->currentPage = $currentPage;
    }   
    public function getCurrentPage() {
        return $this->currentPage;
    }
	
	public function setPhotoId($photoId) {
        $this->photoId = $photoId;
    }   
    public function getPhotoId() {
        return $this->photoId;
    }
	  
     public function voteForphoto($photoId,$photoCategory,$userId) {
        $showVotingDetailsDAO = new VotingDetailsDAO();    
		$this->setuserId($userId);
		$this->setphotoId($photoId);
		$this->setPhotoCategory($photoCategory);
        $returnSaveVotingDetailsDAO = $showVotingDetailsDAO->voting($this);
        return $returnSaveVotingDetailsDAO;
    }
	
	public function unVoteForphoto($photoId,$photoCategory,$userId) {
        $showUnVotingDetailsDAO = new VotingDetailsDAO();
        $this->setphotoId($photoId);
		$this->setuserId($userId);
		$this->setPhotoCategory($photoCategory);
        $returnSaveUnVotingDetailsDAO = $showUnVotingDetailsDAO->unVoting($this);
        return $returnSaveUnVotingDetailsDAO;
    }
	public function showingMyVotiedPhoto($userId,$currentPage) {
        $showUnVotingDetailsDAO = new VotingDetailsDAO();        
		$this->setuserId($userId);
		$this->setCurrentPage($currentPage);
        $returnSaveUnVotingDetailsDAO = $showUnVotingDetailsDAO->showMyVotingList($this);
        return $returnSaveUnVotingDetailsDAO;
    }
		
}
?>