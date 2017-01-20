<?php
require_once '../dao/ResultDetailsDAO.php';
class ResultDetails
{
	private $categoryOfPhoto;
	private $state;
	
	public function setCategoryOfPhoto($categoryOfPhoto) {
		$this -> categoryOfPhoto = $categoryOfPhoto;
	}

	public function getCategoryOfPhoto() {
		return $this -> categoryOfPhoto;
	}
	
	public function setState($state) {
		$this -> state = $state;
	}

	public function getState() {
		return $this -> state;
	}

  // show Results category wise
 	public function ShowingReusltCategoryWise($categoryOfPhoto,$state) {
        $showResultsDAO = new ResultDetailsDAO();
		$this->setCategoryOfPhoto($categoryOfPhoto);
		$this->setState($state);
        $returnShowResultsDetails = $showResultsDAO->showResults($this);
        return $returnShowResultsDetails;
    }	
		
}
?>