package com.example.myapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private Context mContext;
    private ArrayList<ExampleItem> mExampleList;

    public ExampleAdapter(Context context, ArrayList<ExampleItem> exampleList) {
        mContext = context;
        mExampleList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.example_item, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        ExampleItem currentItem = mExampleList.get(position);

        String imageUrl = currentItem.getImageUrl();
        String titre = currentItem.getTitre();
        String date = currentItem.getDate();
        String description = currentItem.getdescription();
        String price = currentItem.getprice();

        holder.mTextViewTitre.setText(titre);
        holder.mTextViewDate.setText(date);
        holder.mTextViewDescription.setText(description);
        holder.mTextViewPrice.setText(price);
        Picasso.get().load(imageUrl).fit().centerInside().into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextViewTitre;
        public TextView mTextViewDate;
        public TextView mTextViewDescription;
        public TextView  mTextViewPrice;
        public ExampleViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_view);
            mTextViewTitre = itemView.findViewById(R.id.text_view_titre);
            mTextViewDate = itemView.findViewById(R.id.text_view_date);
            mTextViewDescription = itemView.findViewById(R.id.text_view_description);
            mTextViewPrice = itemView.findViewById(R.id.text_view_price);
        }
    }
}