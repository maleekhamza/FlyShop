package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URI;

public class SingleProductActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 1 ;
    TextView singleHeadline,singlePrice,singleBrand,singleProductType,singleAboutProduct,singleOrigin;
    ImageView singleImage;
    Button btnappel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);
        singleHeadline = findViewById(R.id.singleHeadline);
        singlePrice= findViewById(R.id.singlePrice);
        singleBrand = findViewById(R.id.singleBrand);
        singleProductType= findViewById(R.id.singleProductType);
        singleAboutProduct = findViewById(R.id.singleAboutProduct);
        singleOrigin = findViewById(R.id.singleOrigin);
        singleImage = findViewById(R.id.singleImage);
        btnappel =findViewById(R.id.btnappel);

        btnappel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
      if(singleBrand !=null && singleBrand.equals("")){
          makePhoneCall();
      }
                

            }
        });
        Picasso.get().load(getIntent().getStringExtra("singleImage"))
                .placeholder(R.drawable.instagram)
                .into(singleImage);
        singleHeadline.setText(getIntent().getStringExtra("singleHeadline"));
        singlePrice.setText(getIntent().getStringExtra("singlePrice"));
        singleBrand.setText(getIntent().getStringExtra("singleBrand"));
        singleProductType.setText(getIntent().getStringExtra("singleProductType"));
        singleAboutProduct.setText(getIntent().getStringExtra("singleAboutProduct"));
        singleOrigin.setText(getIntent().getStringExtra("singleOrigin"));

    }

    private void makePhoneCall() {
        String number = singleBrand.getText().toString();
        if (number.trim().length() > 0) {

            if (ContextCompat.checkSelfPermission(SingleProductActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SingleProductActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        } else {
            Toast.makeText(SingleProductActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}