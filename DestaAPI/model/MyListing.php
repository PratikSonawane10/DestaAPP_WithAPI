<?php
require_once '../dao/MyListingDAO.php';
class MyListing
{
    private $currentPage;
	private $userId;
	private $photoId;
	private $categoryOfPhoto;
	
    
    public function setCurrentPage($currentPage) {
        $this->currentPage = $currentPage;
    }    
    public function getCurrentPage() {
        return $this->currentPage;
    }
	
	public function setuserId($userId) {
        $this->userId = $userId;
    }    
    public function getuserId() {
        return $this->userId;
    }
	
	public function setPhotoId($photoId) {
        $this->photoId = $photoId;
    }    
    public function getPhotoId() {
        return $this->photoId;
    }
	
	public function setCategoryOfPhoto($categoryOfPhoto) {
		$this -> categoryOfPhoto = $categoryOfPhoto;
	}

	public function getCategoryOfPhoto() {
		return $this -> categoryOfPhoto;
	}

  // show  category wise all photo uplaoded by me
 	public function showingMyCategoryWisePhotoList($currentPage,$userId,$categoryOfPhoto) {
    // public function showingMyCategoryWisePhotoList($currentPage,$userId) {
        $showMyListingPhotoListDAO = new MyListingDAO();
        $this->setCurrentPage($currentPage);
		$this->setuserId($userId);
		$this->setCategoryOfPhoto($categoryOfPhoto);
        $returnShowMyListingPhotoListDetails = $showMyListingPhotoListDAO->showMyListingPhotoList($this);
        return $returnShowMyListingPhotoListDetails;
    }
		
	// delete category wise photo uploaded by me
	public function deletePhotoCategoryWise($photoId,$categoryOfPhoto,$userId) {
        $deleteMyListingPhotoListDAO = new MyListingDAO();
        $this->setPhotoId($photoId);
		$this->setuserId($userId);
		$this->setCategoryOfPhoto($categoryOfPhoto);
        $returnDeleteMyListingPhotoListDetails = $deleteMyListingPhotoListDAO->deleteMyListingPhotoCategoryWise($this);
        return $returnDeleteMyListingPhotoListDetails;
    }
		
}
?>