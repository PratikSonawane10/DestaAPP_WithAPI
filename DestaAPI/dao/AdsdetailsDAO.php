<?php
require_once 'BaseDAO.php';
class AdsdetailsDAO 
{
    
    private $con;
    private $msg;
    private $data;
    
    // Attempts to initialize the database connection using the supplied info.
	
    public function AdsdetailsDAO() {
        $baseDAO = new BaseDAO();
        $this->con = $baseDAO->getConnection();
    }
	
    public function showAds($showAds) {        
        try {				
				$sql ="SELECT * FROM ads WHERE state ='".$showAds->getState()."' ";				
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
		// return $this->data="working on query";
       
    }
}
?>