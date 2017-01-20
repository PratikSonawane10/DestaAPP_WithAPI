<?php
require_once 'BaseDAO.php';
class VotingDetailsDAO
{    
    private $con;
    private $msg;
    private $data;   
    // Attempts to initialize the database connection using the supplied info.
    public function VotingDetailsDAO() {
        $baseDAO = new BaseDAO();
        $this->con = $baseDAO->getConnection();
    }
    
    public function voting($votingDetails) {		
        try {
			
				$sql = "SELECT * FROM voteDetails WHERE userId='".$votingDetails->getuserId()."' AND photoCategory = '".$votingDetails->getPhotoCategory()."' AND photoId='".$votingDetails->getphotoId()."'";
				$isValidating = mysqli_query($this->con, $sql);
				$count=mysqli_num_rows($isValidating);
				if($count==1) {         
					$this->data = "Alread_Vote_For_This_Photo";
				}else{
					$sql = "INSERT INTO voteDetails(userId,photoId,photoCategory)
							VALUES ('".$votingDetails->getuserId()."', '".$votingDetails->getphotoId()."', '".$votingDetails->getPhotoCategory()."')";
			
					$isInserted = mysqli_query($this->con, $sql);
					if ($isInserted) {
						$this->data = "VOTING_SUCCESS";
					} else {
						$this->data = "ERROR";
					}
				}
                
        } catch(Exception $e) {
            echo 'SQL Exception: ' .$e->getMessage();
        }
        return $this->data;
    }
	
	public function unVoting($unVotingDetails) {
		 try {            
			$sql = "DELETE FROM voteDetails WHERE photoId='".$unVotingDetails->getphotoId()."' AND userId='".$unVotingDetails->getuserId()."'  AND photoCategory='".$unVotingDetails->getPhotoCategory()."' ";
			$isDeleted = mysqli_query($this->con, $sql);
            if ($isDeleted) {
                $this->data = "UNVOTING_SUCCESS";                
            } else {
                $this->data = "ERROR";
            }
			 
		} catch(Exception $e) {
            echo 'SQL Exception: ' .$e->getMessage();
        }
        return $this->data;
	}
	public function showMyVotingList($MyVotingLis) {
         $sql = "SELECT * FROM voteDetails WHERE userId='".$MyVotingLis->getuserId()."'";
        try {
            $result = mysqli_query($this->con, $sql);
            $numOfRows = mysqli_num_rows($result);
            
            $rowsPerPage = 10;
            $totalPages = ceil($numOfRows / $rowsPerPage);
            
            $this->con->options(MYSQLI_OPT_CONNECT_TIMEOUT, 500);
            
            if (is_numeric($MyVotingLis->getCurrentPage())) {
                $currentPage = (int) $MyVotingLis->getCurrentPage();
            }
            
            if ($currentPage >= 1 && $currentPage <= $totalPages) {
                $offset = ($currentPage - 1) * $rowsPerPage;
            
                $sql = "SELECT * FROM voteDetails WHERE userId='".$MyVotingLis->getuserId()."' LIMIT $offset, $rowsPerPage";
				
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