package com.choicespropertysolutions.desta.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.choicespropertysolutions.desta.Connectivity.AddVoteForPhoto;
import com.choicespropertysolutions.desta.Connectivity.RemoveVoteForPhoto;
import com.choicespropertysolutions.desta.R;
import com.choicespropertysolutions.desta.SessionManager.SessionManager;
import com.choicespropertysolutions.desta.Singleton.UserVoteList;
import com.choicespropertysolutions.desta.model.AllImagesListItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllImagesAdapter extends RecyclerView.Adapter<AllImagesAdapter.ViewHolder> {

    List<AllImagesListItems> allImagesList;
    View v;
    ViewHolder viewHolder;
    private int position;

    public AllImagesAdapter(List<AllImagesListItems> imagesList) {
        this.allImagesList = imagesList;
        //this.onRecyclerMyImagesDeleteClickListener = onRecyclerMyImagesDeleteClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_images_item, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        AllImagesListItems itemList = allImagesList.get(i);
        viewHolder.bindPetList(itemList);
    }

    @Override
    public int getItemCount() {
        return allImagesList.size();
    }

    /*public interface OnRecyclerMyImagesDeleteClickListener {

        void onRecyclerMyImagesDeleteClick(List<MyImagesListItems> myImagesList, MyImagesListItems myImagesItem, int position);
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView allImage;
        private AllImagesListItems allImagesListItems;

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
            allImage = (ImageView) itemView.findViewById(R.id.allImage);
            userVoteLabel = (Button) itemView.findViewById(R.id.userVoteLabel);

            allImage.setOnClickListener(this);
            userVoteLabel.setOnClickListener(this);
        }
        public void bindPetList(AllImagesListItems allImagesListItems) {
            this.allImagesListItems = allImagesListItems;
            Glide.with(allImage.getContext()).load(allImagesListItems.getImagePath()).centerCrop().into(allImage);

            userVoteIdListArray = userVoteList.getVoteList();
            votelistId = allImagesListItems.getPhotoListId();

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
                photoListid = allImagesListItems.getPhotoListId();
                photoCategory = allImagesListItems.getImageCategory();

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
                intent.setDataAndType(Uri.parse(allImagesListItems.getImagePath()),"image/*");
                v.getContext().startActivity(intent);
            }

        }
    }
}
