package com.choicespropertysolutions.desta.model;

import com.choicespropertysolutions.desta.Singleton.ImageURLInstance;

public class ResultImagesListItems {

    public String id;
    public String imagePath;
    public String imageCategory;
    public String caption;
    public String userName;
    public String userState;
    public String mobileNo;
    public String email;
    public String totalVote;

    public ResultImagesListItems() {
    }

    public ResultImagesListItems(String id, String imagePath, String imageCategory, String caption,
                                 String userName,String userState,String mobileNo,String email,String totalVote) {
        this.id = id;
        this.imagePath = imagePath;
        this.imageCategory = imageCategory;
        this.caption = caption;
        this.userName = userName;
        this.userState = userState;
        this.mobileNo = mobileNo;
        this.email = email;
        this.totalVote = totalVote;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = ImageURLInstance.getUrl() + "images/" + imagePath;
    }

    public String getImageCategory() {
        return imageCategory;
    }

    public void setImageCategory(String imageCategory) {
        this.imageCategory = imageCategory;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTotalVote() {
        return totalVote;
    }

    public void setTotalVote(String totalVote) {
        this.totalVote = totalVote;
    }
}
