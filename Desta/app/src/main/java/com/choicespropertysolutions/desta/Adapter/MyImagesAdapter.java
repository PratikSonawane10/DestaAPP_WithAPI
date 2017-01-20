package com.choicespropertysolutions.desta.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.choicespropertysolutions.desta.R;
import com.choicespropertysolutions.desta.app.AppController;
import com.choicespropertysolutions.desta.model.MyImagesListItems;

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

        public ViewHolder(View itemView) {
            super(itemView);
            myImage = (ImageView) itemView.findViewById(R.id.myImage);

            myImage.setOnClickListener(this);
        }

        public void bindPetList(MyImagesListItems myImagesListItems) {
            this.myImagesListItems = myImagesListItems;
            Glide.with(myImage.getContext()).load(myImagesListItems.getImagePath()).centerCrop().into(myImage);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(myImagesListItems.getImagePath()),"image/*");
            v.getContext().startActivity(intent);
        }
    }
}
