<?php
require_once '../model/PhotoDetails.php';
require_once '../model/UsersDetails.php';
require_once '../model/LoginDetails.php';
require_once '../model/MyListing.php';
require_once '../model/VotingDetails.php';
require_once '../model/ResultDetails.php';
require_once '../model/Adsdetails.php';
require_once '../model/FirebaseTokenRegister.php';

function deliver_response($format, $api_response, $isSaveQuery) {

    // Define HTTP responses
    $http_response_code = array(200 => 'OK', 400 => 'Bad Request', 401 => 'Unauthorized', 403 => 'Forbidden', 404 => 'Not Found');

    // Set HTTP Response
    header('HTTP/1.1 ' . $api_response['status'] . ' ' . $http_response_code[$api_response['status']]);

    // Process different content types
    if (strcasecmp($format, 'json') == 0) {
		
		ignore_user_abort();
    	ob_start();

        // Set HTTP Response Content Type
        header('Content-Type: application/json; charset=utf-8');

        // Format data into a JSON response
        $json_response = json_encode($api_response);
        
        // Deliver formatted data
        echo $json_response;
		
		ob_flush();

    } elseif (strcasecmp($format, 'xml') == 0) {

        // Set HTTP Response Content Type
        header('Content-Type: application/xml; charset=utf-8');

        // Format data into an XML response (This is only good at handling string data, not arrays)
        $xml_response = '<?xml version="1.0" encoding="UTF-8"?>' . "\n" . '<response>' . "\n" . "\t" . '<code>' . $api_response['code'] . '</code>' . "\n" . "\t" . '<data>' . $api_response['data'] . '</data>' . "\n" . '</response>';

        // Deliver formatted data
        echo $xml_response;

    } else {

        // Set HTTP Response Content Type (This is only good at handling string data, not arrays)
        header('Content-Type: text/html; charset=utf-8');

        // Deliver formatted data
        echo $api_response['data'];

    }

    // End script process
    exit ;

}

// Define whether an HTTPS connection is required
$HTTPS_required = FALSE;

// Define whether user authentication is required
$authentication_required = FALSE;

// Define API response codes and their related HTTP response
$api_response_code = array(0 => array('HTTP Response' => 400, 'Message' => 'Unknown Error'), 1 => array('HTTP Response' => 200, 'Message' => 'Success'), 2 => array('HTTP Response' => 403, 'Message' => 'HTTPS Required'), 3 => array('HTTP Response' => 401, 'Message' => 'Authentication Required'), 4 => array('HTTP Response' => 401, 'Message' => 'Authentication Failed'), 5 => array('HTTP Response' => 404, 'Message' => 'Invalid Request'), 6 => array('HTTP Response' => 400, 'Message' => 'Invalid Response Format'));

// Set default HTTP response of 'ok'
$response['code'] = 0;
$response['status'] = 404;

// --- Step 2: Authorization

// Optionally require connections to be made via HTTPS
if ($HTTPS_required && $_SERVER['HTTPS'] != 'on') {
    $response['code'] = 2;
    $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
    $response['data'] = $api_response_code[$response['code']]['Message'];

    // Return Response to browser. This will exit the script.
    deliver_response($_GET['format'], $response);
}

// Optionally require user authentication
if ($authentication_required) {

    if (empty($_POST['username']) || empty($_POST['password'])) {
        $response['code'] = 3;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $response['data'] = $api_response_code[$response['code']]['Message'];

        // Return Response to browser
        deliver_response($_GET['format'], $response);

    }

    // Return an error response if user fails authentication. This is a very simplistic example
    // that should be modified for security in a production environment
    elseif ($_POST['username'] != 'foo' && $_POST['password'] != 'bar') {
        $response['code'] = 4;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $response['data'] = $api_response_code[$response['code']]['Message'];

        // Return Response to browser
        deliver_response($_GET['format'], $response);

    }
}

// --- Step 3: Process Request

// Switch based on incoming method
$checkmethod = $_SERVER['REQUEST_METHOD'];
$var = file_get_contents("php://input");
$string = json_decode($var, TRUE);
$method = $string['method'];

if (isset($_POST['method']) || $checkmethod == 'POST') {
		
	 if(strcasecmp($method,'userLogin') == 0){
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objuserDetails = new LoginDetails();
        $email= $string['email'];
        $password= $string['confirmpassword'];
		$mobileno= $string['mobileno'];
        $response['loginDetailsResponse'] = $objuserDetails ->CheckingUsersDetails($email,$password,$mobileno);
        deliver_response($string['format'],$response,false);
    }	   
	
	if(strcasecmp($method,'userRegistration') == 0) {
		$response['code'] = 1;
		$response['status'] = $api_response_code[$response['code']]['HTTP Response'];
		$objuserDetails = new UsersDetails();
		$name = $string['name'];
		$state = $string['state'];
		$mobileno= $string['mobileno'];	
		$email= $string['email'];	
		$password= $string['confirmpassword'];		
		$objuserDetails->mapIncomingUserDetailsParams($name,$mobileno,$email,$state,$password);	
		$response['saveUsersDetailsResponse'] = $objuserDetails -> SavingUsersDetails();
		
        deliver_response($string['format'],$response,false);
	}
	else if(strcasecmp($method,'editProfile') == 0){
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objuserDetails = new UsersDetails();
        $name = $string['name'];        
        $mobileno= $string['mobileno'];
        $oldMobileNo= $string['oldMobileNo'];
		$state = $string['state'];     
		$email= $string['email'];
		$oldEmail= $string['oldEmail'];	
        $objuserDetails->mapIncomingEditUserDetailsParams($name,$mobileno,$state,$oldMobileNo,$email,$oldEmail);    
        $response['saveUsersEditDetailsResponse'] = $objuserDetails -> SavingEditUsersDetails();
        deliver_response($string['format'],$response,false);
    }
    else if(strcasecmp($method,'registerFirebaseToken') == 0){
        $response['code'] = 1;  
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objRegisterFirebaseDetails = new FirebaseTokenRegister();
        $android_id = $string['android_id'];
        $token = $string['token'];
        $response['registerFirebaseTokenResponse'] = $objRegisterFirebaseDetails -> firebaseTokenRegistration($android_id, $token);
        deliver_response($string['format'],$response,false);
    }
//voting for photo
	else if(strcasecmp($method,'voteToPhoto') == 0){
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objphotoVoteDetails = new VotingDetails();
        $photoId=$string['photoId'];        
        $photoCategory= $string['photoCategory'];
		$userId= $string['userId'];
        $response['savevoteResponse'] = $objphotoVoteDetails -> voteForphoto($photoId,$photoCategory,$userId);       
        deliver_response($string['format'],$response,false);
    }
	else if(strcasecmp($method, 'savePhotoDetails') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objPhotoDetails = new PhotoDetails();
		
        $first_Photo_tmp = "";
        $first_Photo_target_path = "";
        $second_Photo_tmp = "";
        $second_Photo_target_path = "";
        $third_Photo_tmp = "";
        $third_Photo_target_path = "";		
		$fourth_Photo_tmp = "";
        $fourth_Photo_target_path = "";		
		$fifth_Photo_tmp = "";
        $fifth_Photo_target_path = "";
		$userId = $string['userId'];
		$selectedCategoriesOfPhoto1 = "";
		$selectedCategoriesOfPhoto2 = "";
		$selectedCategoriesOfPhoto3 = "";
		$selectedCategoriesOfPhoto4 = "";
		$selectedCategoriesOfPhoto5 = "";
		             
        date_default_timezone_set('Asia/Kolkata');
        $postDate = date("Y-m-d H:i:s");
		
		if(!empty($string['firstImageTags'])) {
            $selectedCategoriesOfPhoto1 = $string['firstImageTags'];
        }
		if(!empty($string['secondImageTags'])) {
            $selectedCategoriesOfPhoto2 = $string['secondImageTags'];
        }
		if(!empty($string['thirdImageTags'])) {
            $selectedCategoriesOfPhoto3 = $string['thirdImageTags'];
        }
		if(!empty($string['fourthImageTags'])) {
            $selectedCategoriesOfPhoto4 = $string['fourthImageTags'];
        }
		if(!empty($string['fifthImageTags'])) {
            $selectedCategoriesOfPhoto5 = $string['fifthImageTags'];
        }
        if($string['firstImage'] != ""){
          $first_Photo_tmp = $string['firstImage'];
          $first_Photo_target_path = "../images/".$string['firstImageName'].".png";
        }
        if($string['secondImage'] != ""){
          $second_Photo_tmp = $string['secondImage'];
          $second_Photo_target_path = "../images/".$string['secondImageName'].".png";
        }
        if($string['thirdImage'] != ""){
          $third_Photo_tmp = $string['thirdImage'];
          $third_Photo_target_path = "../images/".$string['thirdImageName'].".png";
        }
		if($string['fourthImage'] != ""){
          $fourth_Photo_tmp = $string['fourthImage'];
          $fourth_Photo_target_path = "../images/".$string['fourthImageName'].".png";
        }
		if($string['fifthImage'] != ""){
          $fifth_Photo_tmp = $string['fifthImage'];
          $fifth_Photo_target_path = "../images/".$string['fifthImageName'].".png";
        }		           
		$objPhotoDetails->mapIncomingPhotoDetailsParams($first_Photo_tmp, $first_Photo_target_path, $second_Photo_tmp, $second_Photo_target_path, $third_Photo_tmp, $third_Photo_target_path,$fourth_Photo_tmp, $fourth_Photo_target_path,$fifth_Photo_tmp, $fifth_Photo_target_path,$selectedCategoriesOfPhoto1,$selectedCategoriesOfPhoto2,$selectedCategoriesOfPhoto3,$selectedCategoriesOfPhoto4,$selectedCategoriesOfPhoto5,$userId,$postDate);
        $response['savePhotoDetailsResponse'] = $objPhotoDetails -> savingPhotoDetails();
        deliver_response($string['format'], $response, true);
    }
}
else if (isset($_GET['method'])) {
    //show photo list of all users
	 if (strcasecmp($_GET['method'], 'showPhotoDetails') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $fetchPhotoDetails = new PhotoDetails();		
        $currentPage = $_GET['currentPage'];		
		$userId=$_GET['userId'];
		$categoryOfPhoto=$_GET['categoryOfPhoto'];	
		
		if($currentPage == 1){
			//to show my voting in all photo
			$response['showWishListResponse'] = $fetchPhotoDetails -> showingUserVotedPhoto($userId);
		}	
		$response['showPhotoDetailsResponse'] = $fetchPhotoDetails -> showingPhotoDetails($currentPage,$categoryOfPhoto);
        deliver_response($_GET['format'], $response, false);
    }
	//show phto category wise uploaded by own user
    else if (strcasecmp($_GET['method'], 'showMyPhotoListCategoryWise') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $fetchMyListingPhotoList = new MyListing();
        $currentPage = $_GET['currentPage'];
        $userId=$_GET['userId'];
		$categoryOfPhoto=$_GET['categoryOfPhoto'];
        $response['showMyPhotosListResponse'] = $fetchMyListingPhotoList -> showingMyCategoryWisePhotoList($currentPage,$userId,$categoryOfPhoto);
        // $response['showMyPhotosListResponse'] = $fetchMyListingPhotoList -> showingMyCategoryWisePhotoList($currentPage,$userId);
        deliver_response($_GET['format'], $response, false);
    }	
	//delete category wise Photo uploaded by own 
	else if(strcasecmp($_GET['method'],'deleteMyphotoCategoryWise') == 0){
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objphotoVoteDetails = new MyListing();
        $photoId=$_GET['photoId'];        
        $photoCategory= $_GET['photoCategory'];
		$userId= $_GET['userId'];
        $response['deleteMyListingPhotoResponse'] = $objphotoVoteDetails -> deletePhotoCategoryWise($photoId,$photoCategory,$userId);       
        deliver_response($_GET['format'],$response,false);
    }	
    //show photo voted by own
	else if (strcasecmp($_GET['method'], 'showMyVotingList') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $fetchPhotoVotingDetails = new VotingDetails();
        $userId=$_GET['userId'];
        $currentPage = $_GET['currentPage'];
        $response['showMyVotingListResponse'] = $fetchPhotoVotingDetailss -> showingMyVotiedPhoto($userId,$currentPage);
        deliver_response($_GET['format'], $response, false);
    }
	//delete voting
	else if(strcasecmp($_GET['method'],'unVoteToPhoto') == 0){
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objphotoVoteDetails = new VotingDetails();
        $photoId=$_GET['photoId'];        
        $photoCategory= $_GET['photoCategory'];
		$userId= $_GET['userId'];
        $response['savevoteResponse'] = $objphotoVoteDetails -> unVoteForphoto($photoId,$photoCategory,$userId);       
        deliver_response($_GET['format'],$response,false);
    }

	//show results category wise
	else if (strcasecmp($_GET['method'], 'showResultsCategoryWise') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $fetchResultsDetails = new ResultDetails();        
        $photoCategory = $_GET['photoCategory'];
		$state = $_GET['state'];
        $response['showResultsResponse'] = $fetchResultsDetails -> ShowingReusltCategoryWise($photoCategory,$state);
        deliver_response($_GET['format'], $response, false);
    }
	//show ads state wise
	else if (strcasecmp($_GET['method'], 'showStateWiseAds') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $fetchAdsDetails = new Adsdetails();               
		$state = $_GET['state'];
        $response['showStateWiseAdsResponse'] = $fetchAdsDetails -> ShowingStateWiseAds($state);
        deliver_response($_GET['format'], $response, false);
    }
   
}
?>
