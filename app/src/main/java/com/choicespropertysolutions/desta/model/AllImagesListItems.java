package com.choicespropertysolutions.desta.model;

import com.choicespropertysolutions.desta.Singleton.ImageURLInstance;

/**
 * Created by Aditya on 9/24/2016.
 */
public class AllImagesListItems {

    public String photoListId;
    public String imagePath;
    public String imageCategory;
    public String caption;

    public AllImagesListItems() {
    }

    public AllImagesListItems(String id, String imagePath, String imageCategory, String caption) {
        this.photoListId = id;
        this.imagePath = imagePath;
        this.imageCategory = imageCategory;
        this.caption = caption;
    }

    public String getPhotoListId() {
        return photoListId;
    }

    public void setPhotoListId(String id) {
        this.photoListId = id;
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
}
