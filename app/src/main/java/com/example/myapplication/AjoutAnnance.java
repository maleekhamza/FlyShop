package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AjoutAnnance extends AppCompatActivity {
    private static final int REQUEST_CALL =1 ;
    TextView headline,description,price,brand,productType,aboutProduct,origin;
ImageView uploadbtn,productImage;
Button submit;
RelativeLayout relativeLayout;
private FirebaseDatabase database;
private FirebaseStorage firebaseStorage;
    //permission constants
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    //permission arrays
    private String[] cameraPermissions;
    private String[] storagePermissions;

    //image picked uri
  Uri ImageUri;
ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_annance);
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        headline=findViewById(R.id.headline);
        description=findViewById(R.id.description);
        price=findViewById(R.id.price);
        brand=findViewById(R.id.brand);
        productType=findViewById(R.id.productType);
        aboutProduct=findViewById(R.id.aboutProduct);
        origin=findViewById(R.id.origin);
        database = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.setTitle("Uploading File");
        dialog.setCanceledOnTouchOutside(false);


        relativeLayout=findViewById(R.id.relative);
        uploadbtn=findViewById(R.id.uploadbtn);
        productImage=findViewById(R.id.productImage);


        submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AjoutAnnance.this,MainActivity.class));
                dialog.show();
        final StorageReference reference= firebaseStorage.getReference().child("ANNONCES").child(System.currentTimeMillis()+"");
                reference.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                ProjectModel model= new ProjectModel();
                                model.setProductImage(uri.toString());
                                model.setHeadline(headline.getText().toString());
                                model.setDescription(description.getText().toString());
                                model.setPrice(price.getText().toString());
                                model.setBrand(brand.getText().toString());
                                model.setProductType(productType.getText().toString());
                                model.setAboutProduct(aboutProduct.getText().toString());
                                model.setOrigin(origin.getText().toString());
                                database.getReference().child("ANNONCES").push().setValue(model)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(AjoutAnnance.this, "Annonce upload successfully", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Toast.makeText(AjoutAnnance.this, "Error Occurred", Toast.LENGTH_SHORT).show();


                                    }
                                });

                            }
                        });
                    }

                });
            }
        });
        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
                relativeLayout.setVisibility(View.VISIBLE);
               uploadbtn.setVisibility(View.GONE);
            }
        });

    }


    //init permission arrays
    private void showImagePickDialog() {
        //options to display in dialog
        String[] options = {"Camera" , "Gallery"};
        //dialog
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(AjoutAnnance.this);
        builder.setTitle("Pick Image").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle clicks
                if (which == 0){
                    //camera clicked
                    if (checkCameraPermission()) {
                        //camera permission allowed
                        pickFromCamera();
                    }
                    else {
                        //not allowed, request
                        requestCameraPermission();
                    }
                }
                else{
                    //gallery clicked
                    if(checkStoragePermission()) {
                        //storage permissions allowed
                        pickFromGallery();
                    }
                    else {
                        requestStoragePermission();
                    }
                }

            }
        }).show();
    }
    private void pickFromGallery(){
        //intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }
    private void pickFromCamera(){
        //intent to pick image from camera

        //using media store to pick high/original quality image
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE,"Temp_Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION,"Temp_Image Descrption");

        ImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,ImageUri);
        startActivityForResult(intent,IMAGE_PICK_CAMERA_CODE);
    }


    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }
    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }
    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int [] grantResults ) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        pickFromCamera();
                    }
                    else{
                        Toast.makeText(this, "camera permissions are necessary..",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        pickFromGallery();
                    }
                    else{
                        Toast.makeText(this, "storage permission is necessary..",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if(resultCode==RESULT_OK){
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                ImageUri = data.getData();
                productImage.setImageURI(ImageUri);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                //set to imageView
                productImage.setImageURI(ImageUri);
            }
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

}

