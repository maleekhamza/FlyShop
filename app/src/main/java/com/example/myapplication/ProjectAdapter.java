package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Objects;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {
    ArrayList<ProjectModel> list;
    Context context;
    DatabaseReference databaseReference;

    public ProjectAdapter(ArrayList<ProjectModel> list, Context context) {
        this.list = list;
        this.context=context;
        databaseReference = FirebaseDatabase.getInstance().getReference();


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        ProjectModel model = list.get(position);

        Picasso.get().load(model.getProductImage()).into(holder.itemImage);
        holder.itemHeadLine.setText(model.getHeadline());
        holder.itemDescription.setText(model.getDescription());
        holder.itemPrice.setText(model.getPrice());
        holder.itemBrand.setText(model.getBrand());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleProductActivity.class);
                intent.putExtra("singleImage", model.getProductImage());
                intent.putExtra("singleHeadline", model.getHeadline());
                intent.putExtra("singlePrice", model.getPrice());
                intent.putExtra("singleBrand", model.getBrand());
                intent.putExtra("singleProductType", model.getProductType());
                intent.putExtra("singleAboutProduct", model.getAboutProduct());
                intent.putExtra("singleOrigin", model.getOrigin());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemHeadLine, itemDescription, itemPrice, itemBrand;
        ImageView itemImage;



        public ViewHolder(@NonNull View itemview) {
            super(itemview);
            itemHeadLine = itemview.findViewById(R.id.itemHeadLine);
            itemDescription = itemview.findViewById(R.id.itemDescription);
            itemPrice = itemview.findViewById(R.id.itemPrice);
            itemBrand = itemview.findViewById(R.id.itemBrand);
            itemImage = itemview.findViewById(R.id.itemImage);

        }
    }

 }



