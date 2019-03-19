package com.example.chitchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    MaterialEditText username,email,pass;
    Button register;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        register = findViewById(R.id.btn_Register);

        FirebaseApp.initializeApp(RegisterActivity.this);
        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id_username = username.getText().toString();
                String id_email = email.getText().toString();
                String id_pass = pass.getText().toString();

                if(TextUtils.isEmpty(id_username)||TextUtils.isEmpty(id_email)||TextUtils.isEmpty(id_pass)){
                    Toast.makeText(RegisterActivity.this,"All Fields Are Required To Be filled",Toast.LENGTH_LONG).show();
                }else if(id_pass.length()<6){

                    Toast.makeText(RegisterActivity.this,"Password Must Be Atleast Six Characters ",Toast.LENGTH_LONG).show();
                }else{
                    registation(id_username,id_email,id_pass);
                }
            }
        });
    }
    private void registation(final String username, String email, String pass) {

        auth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                        FirebaseUser firebaseUser = auth.getCurrentUser();

                        String userid = firebaseUser.getUid();

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                        HashMap<String,String> hashMap = new HashMap<>();
                        hashMap.put("id",userid);
                        hashMap.put("username",username);
                        hashMap.put("imageURL","default");


                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                           if(task.isSuccessful()){
                               Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                               startActivity(intent);
                               finish();
                           }
                            }
                        });

                    }else{
                            Toast.makeText(RegisterActivity.this,"Can't Be Registered With this Email or Password",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
