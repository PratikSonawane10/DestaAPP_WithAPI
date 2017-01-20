package com.choicespropertysolutions.desta.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.choicespropertysolutions.desta.Connectivity.AddVoteForPhoto;
import com.choicespropertysolutions.desta.Connectivity.RemoveVoteForPhoto;
import com.choicespropertysolutions.desta.R;
import com.choicespropertysolutions.desta.ResultDetails;
import com.choicespropertysolutions.desta.SessionManager.SessionManager;
import com.choicespropertysolutions.desta.Singleton.UserVoteList;
import com.choicespropertysolutions.desta.model.MyImagesListItems;
import com.choicespropertysolutions.desta.model.ResultImagesListItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResultImagesAdapter extends RecyclerView.Adapter<ResultImagesAdapter.ViewHolder> {

    List<ResultImagesListItems> resultImagesList;
    View v;
    ViewHolder viewHolder;
    private int position;

    public ResultImagesAdapter(List<ResultImagesListItems> imagesList) {
        this.resultImagesList = imagesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.result_images_item, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        ResultImagesListItems itemList = resultImagesList.get(i);
        viewHolder.bindPetList(itemList);
    }

    @Override
    public int getItemCount() {
        return resultImagesList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView resultImage;
        private ResultImagesListItems resultImagesListItems;
        public String totalvotes;
        String ownerName;
        String photoCategory;
        public View cardView;
        TextView photoOwnerName;
        TextView noOfVote;

        public ViewHolder(View itemView) {
            super(itemView);
            resultImage = (ImageView) itemView.findViewById(R.id.resultImage);
            noOfVote = (TextView) itemView.findViewById(R.id.noOfVotes);
            photoOwnerName = (TextView) itemView.findViewById(R.id.photoOwnerName);

            cardView = itemView;
            cardView.setOnClickListener(this);
            resultImage.setOnClickListener(this);
        }

        public void bindPetList(ResultImagesListItems resultImagesListItems) {
            this.resultImagesListItems = resultImagesListItems;
            Glide.with(resultImage.getContext()).load(resultImagesListItems.getImagePath()).centerCrop().into(resultImage);

            photoOwnerName.setText(resultImagesListItems.getUserName());
            noOfVote.setText("Total Votes : " + resultImagesListItems.getTotalVote());

        }

        @Override
        public void onClick(View v) {
            if (this.resultImagesListItems != null) {
                Intent FullInformation = new Intent(v.getContext(), ResultDetails.class);
                FullInformation.putExtra("photoPath", resultImagesListItems.getImagePath ());
                FullInformation.putExtra("photoId", resultImagesListItems.getId());
                FullInformation.putExtra("photoCategory", resultImagesListItems.getImageCategory());
                FullInformation.putExtra("totalVote", resultImagesListItems.getTotalVote());
                FullInformation.putExtra("state", resultImagesListItems.getUserState());
                FullInformation.putExtra("name", resultImagesListItems.getUserName());
                FullInformation.putExtra("email", resultImagesListItems.getEmail());
                FullInformation.putExtra("mobileNo", resultImagesListItems.getMobileNo());
                v.getContext().startActivity(FullInformation);
            }else{
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(resultImagesListItems.getImagePath()),"image/*");
                v.getContext().startActivity(intent);
            }
        }
    }
}
