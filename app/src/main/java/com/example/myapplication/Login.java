package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
 public class Login extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://myfirstproject-28493-default-rtdb.firebaseio.com/");
     WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText phone=findViewById(R.id.phone);
     final EditText password=findViewById(R.id.password);
     final Button loginbtn =findViewById(R.id.loginbtn);
     final TextView registernow =findViewById(R.id.registernow);
     loginbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        final String phonetxt= phone.getText().toString();
        final String passwordtxt= password.getText().toString();
if (phonetxt.isEmpty() || passwordtxt.isEmpty() ){
    Toast.makeText(Login.this, "Please enter your mobile or password ", Toast.LENGTH_SHORT).show();
}
else{
    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.hasChild(phonetxt)){
final  String getpassword=snapshot.child(phonetxt).child("password").getValue(String.class);
           if (getpassword.equals(passwordtxt)){
               Toast.makeText(Login.this, "Succesfully Logged in", Toast.LENGTH_SHORT).show();

               startActivity(new Intent(Login.this,Home.class));
           finish();
           }else {
               Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
           }
            }else {
                Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
}
    }
});
registernow.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(Login.this,Register.class));
    }
});



    }

    private class load extends WebViewClient {
         @Override
         public boolean shouldOverrideUrlLoading(WebView view, String request) {
             view.loadUrl(request);
             return true;
         }
     }


}