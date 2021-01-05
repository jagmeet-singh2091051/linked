package com.example.linked;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    EditText username;
    EditText email;
    EditText password;
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.signup_username);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        signUpBtn = findViewById(R.id.signup_btn);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if(currentUser != null){
            UserModel userInstance = UserModel.getInstance();
            userInstance.setEmail(currentUser.getEmail());
            userInstance.setUsername(currentUser.getDisplayName());
            userInstance.setUserId(currentUser.getUid());

            Intent intent = new Intent(SignupActivity.this, HomeScreenActivity.class);
            startActivity(intent);
        }

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    final UserModel userInstance = UserModel.getInstance();
                                    userInstance.setEmail(user.getEmail());
                                    userInstance.setUsername(username.getText().toString());
                                    userInstance.setUserId(user.getUid());

                                    Log.e("Email", userInstance.getEmail());
                                    Log.e("Username", userInstance.getUsername());
                                    Log.e("Uid", userInstance.getUserId());

                                    /*Map<String, Object> userInfo = new HashMap<>();
                                    userInfo.put("uid", userInstance.getUserId());
                                    userInfo.put("username", userInstance.getUsername());
                                    userInfo.put("email", userInstance.getEmail());*/

                                    db.collection("users")
                                            .document(userInstance.getUserId())
                                            .set(userInstance)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    Intent intent = new Intent(SignupActivity.this, HomeScreenActivity.class);
                                                    startActivity(intent);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("TAG", "Error adding document", e);
                                        }
                                    });

                                }
                                else{
                                    Log.e("Auth Exception" , task.getException().toString());
                                    Toast.makeText(SignupActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
