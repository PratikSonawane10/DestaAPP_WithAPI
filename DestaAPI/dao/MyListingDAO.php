<?php
require_once 'BaseDAO.php';
class MyListingDAO
{
    
    private $con;
    private $msg;
    private $data;
    
    // Attempts to initialize the database connection using the supplied info.
	
    public function MyListingDAO() {
        $baseDAO = new BaseDAO();
        $this->con = $baseDAO->getConnection();
    }

    public function showMyListingPhotoList($MyListingPhotoList) {
        $sql = "SELECT * FROM photoDetails WHERE userId='".$MyListingPhotoList->getuserId()."' AND photoCategory ='".$MyListingPhotoList->getCategoryOfPhoto()."' ";
        //$sql = "SELECT * FROM photodetails WHERE userId='".$MyListingPhotoList->getuserId()."' ";
        try {
            $result = mysqli_query($this->con, $sql);
            $numOfRows = mysqli_num_rows($result);
            
            $rowsPerPage = 10;
            $totalPages = ceil($numOfRows / $rowsPerPage);
            
            $this->con->options(MYSQLI_OPT_CONNECT_TIMEOUT, 500);
            
            if (is_numeric($MyListingPhotoList->getCurrentPage())) {
                $currentPage = (int) $MyListingPhotoList->getCurrentPage();
            }
            
            if ($currentPage >= 1 && $currentPage <= $totalPages) {
                $offset = ($currentPage - 1) * $rowsPerPage;
            
                $sql = "SELECT * FROM photoDetails WHERE userId='".$MyListingPhotoList->getuserId()."' AND photoCategory ='".$MyListingPhotoList->getCategoryOfPhoto()."'  ORDER BY post_date DESC LIMIT $offset, $rowsPerPage";
                // $sql = "SELECT * FROM photodetails WHERE userId='".$MyListingPhotoList->getuserId()."' ORDER BY post_date DESC LIMIT $offset, $rowsPerPage";
				
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
    
	public function deleteMyListingPhotoCategoryWise($MyListingPhotoList) {
		 try {
            $sql = "DELETE FROM photodetails WHERE photoId='".$MyListingPhotoList->getphotoId()."' AND userId='".$MyListingPhotoList->getuserId()."'  AND photoCategory='".$MyListingPhotoList->getCategoryOfPhoto()."' ";
			$isDeleted = mysqli_query($this->con, $sql);
            if ($isDeleted) {
                $this->data = "DELETED_SUCCESSFULLY";                
            } else {
                $this->data = "ERROR";
            }
		} catch(Exception $e) {
            echo 'SQL Exception: ' .$e->getMessage();
        }
        return $this->data;	 		
	}
	
  
}
?>