package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://myfirstproject-28493-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText fullname = findViewById(R.id.fullname);
        final EditText email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);
        final EditText phone = findViewById(R.id.phone);
        final EditText conpassword = findViewById(R.id.conPassword);

        final Button registerbtn = findViewById(R.id.registerbtn);

        final TextView loginnow = findViewById(R.id.registernow);
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullnametxt = fullname.getText().toString();
                final String emailtxt = email.getText().toString();
                final String passwordtxt = password.getText().toString();
                final String phonetxt = phone.getText().toString();
                final String conpasswordtxt = conpassword.getText().toString();
                if (fullnametxt.isEmpty() || emailtxt.isEmpty() || passwordtxt.isEmpty() || phonetxt.isEmpty() ) {
                    Toast.makeText(Register.this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
                } else if (!passwordtxt.equals(conpasswordtxt)) {
                    Toast.makeText(Register.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(phonetxt)) {
                                Toast.makeText(Register.this, "phone already registred", Toast.LENGTH_SHORT).show();
                            } else {
                                databaseReference.child("users").child(phonetxt).child("fullname").setValue(fullnametxt);
                                databaseReference.child("users").child(phonetxt).child("email").setValue(emailtxt);
                                databaseReference.child("users").child(phonetxt).child("password").setValue(passwordtxt);

                                Toast.makeText(Register.this, "User registred successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });


                }
            }
        });
     
    }
}