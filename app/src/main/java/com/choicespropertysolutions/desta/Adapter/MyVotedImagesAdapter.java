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
import com.choicespropertysolutions.desta.model.MyVotedImagesListItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyVotedImagesAdapter extends RecyclerView.Adapter<MyVotedImagesAdapter.ViewHolder> {

    List<MyVotedImagesListItems> myVotedImagesList;
    View v;
    ViewHolder viewHolder;
    private int position;

    public MyVotedImagesAdapter(List<MyVotedImagesListItems> imagesList) {
        this.myVotedImagesList = imagesList;
        //this.onRecyclerMyImagesDeleteClickListener = onRecyclerMyImagesDeleteClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_voted_images_item, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        MyVotedImagesListItems itemList = myVotedImagesList.get(i);
        viewHolder.bindPetList(itemList);
    }

    @Override
    public int getItemCount() {
        return myVotedImagesList.size();
    }

    /*public interface OnRecyclerMyImagesDeleteClickListener {

        void onRecyclerMyImagesDeleteClick(List<MyVotedImagesListItems> myVotedImagesList, MyVotedImagesListItems myImagesItem, int position);
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView myImage;
        private MyVotedImagesListItems myVotedImagesListItems;
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
        }

        public void bindPetList(MyVotedImagesListItems myVotedImagesListItems) {
            this.myVotedImagesListItems = myVotedImagesListItems;
            Glide.with(myImage.getContext()).load(myVotedImagesListItems.getImagePath()).centerCrop().into(myImage);

            userVoteIdListArray = userVoteList.getVoteList();
            votelistId = myVotedImagesListItems.getId();

            userVoteLabel.setBackgroundResource(R.drawable.favourite_enable);
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.userVoteLabel) {
                SessionManager sessionManager = new SessionManager(v.getContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                //userId = user.get(SessionManager.KEY_USERID);
                userId="8";
                photoListid = myVotedImagesListItems.getId();
                if (isVoteForImage == 1) {
                    userVoteLabel.setBackgroundResource(R.drawable.favourite_disable);
                    try {
                        //remove these image list
                        RemoveVoteForPhoto.removeVoteOfPhoto(userId, photoListid,photoCategory);
                        isVoteForImage = 0;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }else{
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(myVotedImagesListItems.getImagePath()),"image/*");
                v.getContext().startActivity(intent);
            }
        }
    }
}
