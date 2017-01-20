package com.choicespropertysolutions.desta.model;

import com.choicespropertysolutions.desta.Singleton.ImageURLInstance;

public class MyVotedImagesListItems {

    public String id;
    public String imagePath;
    public String imageCategory;
    public String caption;

    public MyVotedImagesListItems() {
    }

    public MyVotedImagesListItems(String id, String imagePath, String imageCategory, String caption) {
        this.id = id;
        this.imagePath = imagePath;
        this.imageCategory = imageCategory;
        this.caption = caption;
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
}
