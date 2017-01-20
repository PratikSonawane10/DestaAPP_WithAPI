<?php
require_once 'BaseDAO.php';
class PhotoDetailsDAO
{
    private $con;
    private $msg;
    private $data;
    private $googleAPIKey;
    
    // Attempts to initialize the database connection using the supplied info.
    public function PhotoDetailsDAO() {
        $baseDAO = new BaseDAO();
        $this->con = $baseDAO->getConnection();
        $this->googleAPIKey = $baseDAO->getGoogleAPIKey();
    }
    public function saveDetail($photoDetail) {
        try {
				$status = 0;
				$categoriesOfPhoto1 = array();
				$categoriesOfPhoto2 = array();
				$categoriesOfPhoto3 = array();
				$categoriesOfPhoto4 = array();
				$categoriesOfPhoto5 = array();
				$categoriesOfPhoto1 = $photoDetail->getselectedCategoriesOfPhoto1();		 
				$categoriesOfPhoto2 = $photoDetail->getselectedCategoriesOfPhoto2();
				$categoriesOfPhoto3 = $photoDetail->getselectedCategoriesOfPhoto3();
				$categoriesOfPhoto4 = $photoDetail->getselectedCategoriesOfPhoto4();
				$categoriesOfPhoto5 = $photoDetail->getselectedCategoriesOfPhoto5();
				
				$photosTempNames = array($photoDetail->getFirstPhotoTemporaryName(), $photoDetail->getSecondPhotoTemporaryName(), $photoDetail->getThirdPhotoTemporaryName(), $photoDetail->getFourthPhotoTemporaryName(), $photoDetail->getFifthPhotoTemporaryName());
				$photosTargetPaths = array($photoDetail->getTargetPathOfFirstPhoto(), $photoDetail->getTargetPathOfSecondPhoto(), $photoDetail->getTargetPathOfThirdPhoto(), $photoDetail->getTargetPathOfFourthPhoto(), $photoDetail->getTargetPathOfFifthPhoto());
				
				$allCategory = array($categoriesOfPhoto1,$categoriesOfPhoto2,$categoriesOfPhoto3,$categoriesOfPhoto4,$categoriesOfPhoto5);
				
				foreach ($photosTempNames as $index => $photosTempName) {				
					
					$Photo = base64_decode(preg_replace('#^data:image/\w+;base64,#i', '', $photosTempName));
					if($photosTargetPaths[$index] != "") {
			            if(file_put_contents($photosTargetPaths[$index], $Photo)) {
							
							if(!empty($allCategory[$index])) {
								$photopath = $photosTargetPaths[$index];
								$categoriesOfEachPhoto = $allCategory[$index];
								
								foreach($categoriesOfEachPhoto as $eachCategory) {
									$eachCategory = trim($eachCategory);
									$sql = "INSERT INTO photodetails(userId,photoPath,photoCategory,post_date)
									VALUES('".$photoDetail->getuserId()."','$photopath','$eachCategory','".$photoDetail->getPostDate()."')";							
									$isInserted = mysqli_query($this->con, $sql);
								}   						
								if ($isInserted) {	
									$status = 1;
								}else{
									$status=0;
								}	
							}
			            }
			        }					
				}
				if($status = 1) {						
					$this->data = "PHOTO_SUCCESSFULLY_UPLOADED";																	
				} else {
					$this->data = "ERROR";
				}
        } catch(Exception $e) {
            echo 'SQL Exception: ' .$e->getMessage();
        }
        return $this->data;
    }
	//show user Voted Photo
	public function showUserVotedPhotoList($userVotedPhoto) {
        try {
			$sql ="SELECT * FROM votedetails WHERE userId= '".$userVotedPhoto->getuserId()."' ";
            $result = mysqli_query($this->con, $sql);			
            $this->data=array();
            while ($rowdata = mysqli_fetch_assoc($result)) {
                $this->data[]=$rowdata;
            }
            return $this->data;
        } catch(Exception $e) {
            echo 'SQL Exception: ' .$e->getMessage();
        }
        return $this->data=array();
    }	
	//show all uploaded photo by each user
	public function showDetail($pageWiseData) {
        try {
			$sql = "SELECT * FROM photodetails";
            $result = mysqli_query($this->con, $sql);
            $numOfRows = mysqli_num_rows($result);
            $rowsPerPage = 10;
            $totalPages = ceil($numOfRows / $rowsPerPage);
            $this->con->options(MYSQLI_OPT_CONNECT_TIMEOUT, 500);
            if (is_numeric($pageWiseData->getCurrentPage())) {
                $currentPage = (int) $pageWiseData->getCurrentPage();
            }
            if ($currentPage >= 1 && $currentPage <= $totalPages) {
                $offset = ($currentPage - 1) * $rowsPerPage;
						$sql="SELECT * FROM photoDetails WHERE photoCategory='".$pageWiseData->getCategoryOfPhoto()."'
								 ORDER BY post_date DESC LIMIT $offset, $rowsPerPage";
                $result = mysqli_query($this->con, $sql);				
                $this->data=array();
                while ($rowdata = mysqli_fetch_assoc($result)) {
                    $this->data[]=$rowdata;
                }			
               return $this->data;				
            }
        } catch(Exception $e) {
            echo 'SQL Exception: ' .$e->getMessage();
        }
        return $this->data=array();		
    }
}
?>