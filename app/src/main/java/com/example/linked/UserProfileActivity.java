package com.example.linked;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileActivity extends AppCompatActivity {

    ImageButton closeBtn;
    ImageButton saveBtn;
    TextView profileImage;
    TextInputLayout usernameTil;
    TextInputLayout emailTil;
    TextInputEditText usernameTie;
    TextInputEditText emailTie;
    Button logoutBtn;

    private UserModel userInstance = UserModel.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        closeBtn = findViewById(R.id.profile_close_btn);
        saveBtn = findViewById(R.id.profile_save_btn);
        profileImage = findViewById(R.id.profile_image);
        usernameTil = findViewById(R.id.profile_username_til);
        usernameTie = findViewById(R.id.profile_username);
        emailTil = findViewById(R.id.profile_email_til);
        emailTie = findViewById(R.id.profile_email);
        logoutBtn = findViewById(R.id.profile_logout);


        usernameTie.setText(userInstance.getUserName());
        emailTie.setText(userInstance.getEmail());

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, HomeScreenActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(usernameTie.getText().toString().isEmpty()){
                    usernameTil.setError("Username cannot be empty!");
                    emailTil.setError(null);
                }
                else if(emailTie.getText().toString().isEmpty()){
                    usernameTil.setError(null);
                    emailTil.setError("Please enter a valid Email Id!");
                }
                else {
                    usernameTil.setError(null);
                    emailTil.setError(null);

                    userInstance.setEmail(emailTie.getText().toString());
                    userInstance.setUserName(usernameTie.getText().toString());

                    db.collection(HomeScreenActivity.USERS_COLLECTION_PATH)
                            .document(userInstance.getUserId())
                            .set(userInstance)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Intent intent = new Intent(UserProfileActivity.this, HomeScreenActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error adding document", e);
                        }
                    });
                }
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserProfileActivity.this, SigninActivity.class);
                startActivity(intent);
                finishAffinity();

            }
        });

    }
}