<?php
require_once '../dao/AdsdetailsDAO.php';
class Adsdetails
{
	private $state;
	
	public function setState($state) {
		$this -> state = $state;
	}

	public function getState() {
		return $this -> state;
	}

  // show Results category wise
 	public function ShowingStateWiseAds($state) {
        $showAdsDAO = new AdsdetailsDAO();
		$this->setState($state);
        $returnShowAdsDetails = $showAdsDAO->showAds($this);
        return $returnShowAdsDetails;
    }	
		
}
?>