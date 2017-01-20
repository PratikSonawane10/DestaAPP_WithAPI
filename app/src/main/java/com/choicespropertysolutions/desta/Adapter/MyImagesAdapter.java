package com.choicespropertysolutions.desta.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.choicespropertysolutions.desta.Connectivity.AddVoteForPhoto;
import com.choicespropertysolutions.desta.Connectivity.RemoveVoteForPhoto;
import com.choicespropertysolutions.desta.R;
import com.choicespropertysolutions.desta.SessionManager.SessionManager;
import com.choicespropertysolutions.desta.Singleton.UserVoteList;
import com.choicespropertysolutions.desta.app.AppController;
import com.choicespropertysolutions.desta.model.MyImagesListItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyImagesAdapter extends RecyclerView.Adapter<MyImagesAdapter.ViewHolder> {

    List<MyImagesListItems> myImagesList;
    View v;
    ViewHolder viewHolder;
    private int position;

    public MyImagesAdapter(List<MyImagesListItems> imagesList) {
        this.myImagesList = imagesList;
        //this.onRecyclerMyImagesDeleteClickListener = onRecyclerMyImagesDeleteClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_images_item, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        MyImagesListItems itemList = myImagesList.get(i);
        viewHolder.bindPetList(itemList);
    }

    @Override
    public int getItemCount() {
        return myImagesList.size();
    }

    /*public interface OnRecyclerMyImagesDeleteClickListener {

        void onRecyclerMyImagesDeleteClick(List<MyImagesListItems> myImagesList, MyImagesListItems myImagesItem, int position);
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView myImage;
        private MyImagesListItems myImagesListItems;
        public Button userVoteLabel;
        public ArrayList<String> userVoteIdListArray;
        UserVoteList userVoteList= new UserVoteList();
        public String votelistId;
        int isVoteForImage = 0;
        String photoListid;
        String userId;
        String photoCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            myImage = (ImageView) itemView.findViewById(R.id.myImage);
            userVoteLabel = (Button) itemView.findViewById(R.id.userVoteLabel);

            myImage.setOnClickListener(this);
            userVoteLabel.setOnClickListener(this);
        }

        public void bindPetList(MyImagesListItems myImagesListItems) {
            this.myImagesListItems = myImagesListItems;
            Glide.with(myImage.getContext()).load(myImagesListItems.getImagePath()).centerCrop().into(myImage);

            userVoteIdListArray = userVoteList.getVoteList();
            votelistId = myImagesListItems.getId();

            if(userVoteIdListArray.contains(votelistId) ){
                userVoteLabel.setBackgroundResource(R.drawable.favourite_enable);
                isVoteForImage = 1;
            }else{
                userVoteLabel.setBackgroundResource(R.drawable.favourite_disable);
                isVoteForImage = 0;
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.userVoteLabel) {
                SessionManager sessionManager = new SessionManager(v.getContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                userId = user.get(SessionManager.KEY_USER_ID);
                photoListid = myImagesListItems.getId();
                photoCategory = myImagesListItems.getImageCategory();

                if (isVoteForImage == 0) {
                    userVoteLabel.setBackgroundResource(R.drawable.favourite_enable);
                    isVoteForImage = 1;
                    try {
                        AddVoteForPhoto addVoteForPhoto = new AddVoteForPhoto(v.getContext());
                        addVoteForPhoto.addVoteOfPhoto(userId, photoListid, photoCategory);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (isVoteForImage == 1) {
                    userVoteLabel.setBackgroundResource(R.drawable.favourite_disable);
                    isVoteForImage = 0;
                    try {
                        RemoveVoteForPhoto removeVoteForPhoto = new RemoveVoteForPhoto(v.getContext());
                        removeVoteForPhoto.removeVoteOfPhoto(userId, photoListid,photoCategory);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else{
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(myImagesListItems.getImagePath()),"image/*");
                v.getContext().startActivity(intent);
            }
        }
    }
}
