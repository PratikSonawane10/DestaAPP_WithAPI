<?php
require_once '../dao/PhotoDetailsDAO.php';
class PhotoDetails
{	
	private $first_Photo_tmp;
	private $first_Photo_target_path;
    private $second_Photo_tmp;    
    private $second_Photo_target_path;
	private $third_Photo_tmp;
    private $third_Photo_target_path;	
	private $fourth_Photo_tmp;
    private $fourth_Photo_target_path;
	private $fifth_Photo_tmp;
    private $fifth_Photo_target_path;
	private $selectedCategoriesOfPhoto1;
	private $selectedCategoriesOfPhoto2;
	private $selectedCategoriesOfPhoto3;
	private $selectedCategoriesOfPhoto4;
	private $selectedCategoriesOfPhoto5;
    private $categoryOfPhoto;
    private $postDate;
    private $currentPage;
    private $userId;
	
    public function setFirstPhotoTemporaryName($first_Photo_tmp) {
        $this->first_Photo_tmp = $first_Photo_tmp;
    }    
    public function getFirstPhotoTemporaryName() {
        return $this->first_Photo_tmp;
    }    
    public function setSecondPhotoTemporaryName($second_Photo_tmp) {
        $this->second_Photo_tmp = $second_Photo_tmp;
    }    
    public function getSecondPhotoTemporaryName() {
        return $this->second_Photo_tmp;
    }    
    public function setThirdPhotoTemporaryName($third_Photo_tmp) {
        $this->third_Photo_tmp = $third_Photo_tmp;
    }    
    public function getThirdPhotoTemporaryName() {
        return $this->third_Photo_tmp;
    }
    public function setTargetPathOfFirstPhoto($first_Photo_target_path) {
        $this->first_Photo_target_path = $first_Photo_target_path;
    }    
    public function getTargetPathOfFirstPhoto() {
        return $this->first_Photo_target_path;
    }    
    public function setTargetPathOfSecondPhoto($second_Photo_target_path) {
        $this->second_Photo_target_path = $second_Photo_target_path;
    }    
    public function getTargetPathOfSecondPhoto() {
        return $this->second_Photo_target_path;
    }    
    public function setTargetPathOfThirdPhoto($third_Photo_target_path) {
        $this->third_Photo_target_path = $third_Photo_target_path;
    }    
    public function getTargetPathOfThirdPhoto() {
        return $this->third_Photo_target_path;
    }
	 public function setFourthPhotoTemporaryName($fourth_Photo_tmp) {
        $this->fourth_Photo_tmp = $fourth_Photo_tmp;
    }    
    public function getFourthPhotoTemporaryName() {
        return $this->fourth_Photo_tmp;
    } 
	
	public function setTargetPathOfFourthPhoto($fourth_Photo_target_path) {
        $this->fourth_Photo_target_path = $fourth_Photo_target_path;
    }    
    public function getTargetPathOfFourthPhoto() {
        return $this->fourth_Photo_target_path;
    }
	public function setFifthPhotoTemporaryName($fifth_Photo_tmp) {
        $this->fifth_Photo_tmp = $fifth_Photo_tmp;
    }    
    public function getFifthPhotoTemporaryName() {
        return $this->fifth_Photo_tmp;
    } 
	public function setTargetPathOfFifthPhoto($fifth_Photo_target_path) {
        $this->fifth_Photo_target_path = $fifth_Photo_target_path;
    }    
    public function getTargetPathOfFifthPhoto() {
        return $this->fifth_Photo_target_path;
    }
    public function setCategoryOfPhoto($categoryOfPhoto) {
        $this->categoryOfPhoto = $categoryOfPhoto;
    }    
    public function getCategoryOfPhoto() {
        return $this->categoryOfPhoto;
    }
    public function setCurrentPage($currentPage) {
        $this->currentPage = $currentPage;
    }    
    public function getCurrentPage() {
        return $this->currentPage;
    }
    public function setPostDate($postDate) {
        $this->postDate = $postDate;
    }    
    public function getPostDate() {
        return $this->postDate;
    }    
    public function setuserId($userId) {
        $this->userId = $userId;
    }
    public function getuserId() {
        return $this->userId;
    }	
	
	
	public function setselectedCategoriesOfPhoto1($selectedCategoriesOfPhoto1) {
        $this->selectedCategoriesOfPhoto1 = $selectedCategoriesOfPhoto1;
    }
    
    public function getselectedCategoriesOfPhoto1() {
        return $this->selectedCategoriesOfPhoto1;
    }
	
	public function setselectedCategoriesOfPhoto2($selectedCategoriesOfPhoto2) {
        $this->selectedCategoriesOfPhoto2 = $selectedCategoriesOfPhoto2;
    }
    
    public function getselectedCategoriesOfPhoto2() {
        return $this->selectedCategoriesOfPhoto2;
    }
	
	public function setselectedCategoriesOfPhoto3($selectedCategoriesOfPhoto3) {
        $this->selectedCategoriesOfPhoto3 = $selectedCategoriesOfPhoto3;
    }
    
    public function getselectedCategoriesOfPhoto3() {
        return $this->selectedCategoriesOfPhoto3;
    }
	
	public function setselectedCategoriesOfPhoto4($selectedCategoriesOfPhoto4) {
        $this->selectedCategoriesOfPhoto4 = $selectedCategoriesOfPhoto4;
    }
    
    public function getselectedCategoriesOfPhoto4() {
        return $this->selectedCategoriesOfPhoto4;
    }
	public function setselectedCategoriesOfPhoto5($selectedCategoriesOfPhoto5) {
        $this->selectedCategoriesOfPhoto5 = $selectedCategoriesOfPhoto5;
    }
    
    public function getselectedCategoriesOfPhoto5() {
        return $this->selectedCategoriesOfPhoto5;
    }
    
	//public function mapIncomingPhotoDetailsParams($first_Photo_tmp, $first_Photo_target_path, $second_Photo_tmp, $second_Photo_target_path, $third_Photo_tmp, $third_Photo_target_path, $fourth_Photo_tmp, $fourth_Photo_target_path,$fifth_Photo_tmp, $fifth_Photo_target_path,$categoryOfPhoto,$postDate, $userId) {
    public function mapIncomingPhotoDetailsParams($first_Photo_tmp, $first_Photo_target_path, $second_Photo_tmp, $second_Photo_target_path, $third_Photo_tmp, $third_Photo_target_path,$fourth_Photo_tmp, $fourth_Photo_target_path,$fifth_Photo_tmp, $fifth_Photo_target_path,$selectedCategoriesOfPhoto1,$selectedCategoriesOfPhoto2,$selectedCategoriesOfPhoto3,$selectedCategoriesOfPhoto4,$selectedCategoriesOfPhoto5,$userId,$postDate){
        $this->setFirstPhotoTemporaryName($first_Photo_tmp);
		$this->setTargetPathOfFirstPhoto($first_Photo_target_path);		
        $this->setSecondPhotoTemporaryName($second_Photo_tmp);
		$this->setTargetPathOfSecondPhoto($second_Photo_target_path);		
        $this->setThirdPhotoTemporaryName($third_Photo_tmp);               
        $this->setTargetPathOfThirdPhoto($third_Photo_target_path);		
		$this->setFourthPhotoTemporaryName($fourth_Photo_tmp);               
        $this->setTargetPathOfFourthPhoto($fourth_Photo_target_path);		
		$this->setFifthPhotoTemporaryName($fifth_Photo_tmp);               
        $this->setTargetPathOfFifthPhoto($fifth_Photo_target_path);		       
		$this->setPostDate($postDate);
        $this->setuserId($userId);
		$this->setselectedCategoriesOfPhoto1($selectedCategoriesOfPhoto1);
		$this->setselectedCategoriesOfPhoto2($selectedCategoriesOfPhoto2);
		$this->setselectedCategoriesOfPhoto3($selectedCategoriesOfPhoto3);
		$this->setselectedCategoriesOfPhoto4($selectedCategoriesOfPhoto4);
		$this->setselectedCategoriesOfPhoto5($selectedCategoriesOfPhoto5);
    }
	//save photo of users
    public function savingPhotoDetails() {
        $savePetDetailsDAO = new PhotoDetailsDAO();
        $returnPetDetailSaveSuccessMessage = $savePetDetailsDAO->saveDetail($this);
        return $returnPetDetailSaveSuccessMessage;
    }
	//show all users photo
	public function showingPhotoDetails($currentPage,$categoryOfPhoto) {
        $showPhotoDetailsDAO = new PhotoDetailsDAO();
        $this->setCurrentPage($currentPage);
		$this->setCategoryOfPhoto($categoryOfPhoto);
        $returnShowPhotoDetails = $showPhotoDetailsDAO->showDetail($this);
        return $returnShowPhotoDetails;
    }
	//show user voted photo list
	public function showingUserVotedPhoto($userId) {
        $showshowUserVotedPhotoDAO = new PhotoDetailsDAO();
        $this->setuserId($userId);
        $returnShowPhotoDetails = $showshowUserVotedPhotoDAO->showUserVotedPhotoList($this);
        return $returnShowPhotoDetails;
    }		       
}
?>