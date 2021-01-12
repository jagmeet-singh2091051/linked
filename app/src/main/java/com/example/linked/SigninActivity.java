package com.example.linked;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SigninActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private UserModel userInstance = UserModel.getInstance();

    EditText email;
    EditText password;
    Button signInBtn;
    TextView signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        email = findViewById(R.id.signin_email);
        password = findViewById(R.id.signin_password);
        signInBtn = findViewById(R.id.signin_btn);
        signUpBtn = findViewById(R.id.signin_signup_btn);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if(currentUser != null){

            userInstance.setUserId(currentUser.getUid());

            db.collection(HomeScreenActivity.USERS_COLLECTION_PATH)
                    .document(currentUser.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if (document != null) {
                                    if(document.exists()){
                                        userInstance.setUserName(document.get(HomeScreenActivity.USERNAME_PATH).toString());
                                        userInstance.setEmail(document.get(HomeScreenActivity.EMAIL_PATH).toString());
                                        userInstance.setImageUrl(document.get(HomeScreenActivity.IMAGE_URL_PATH).toString());
                                    }
                                    else{
                                        Log.e("SignInActivity", "Document not found!");
                                    }
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error retrieving document", e);
                        }
                    });

            Intent intent = new Intent(SigninActivity.this, HomeScreenActivity.class);
            startActivity(intent);
        }

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(SigninActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user = firebaseAuth.getCurrentUser();

                                    userInstance.setUserId(user.getUid());


                                    db.collection(HomeScreenActivity.USERS_COLLECTION_PATH)
                                            .document(user.getUid())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document != null) {
                                                            if(document.exists()){
                                                                userInstance.setUserName(document.get(HomeScreenActivity.USERNAME_PATH).toString());
                                                                userInstance.setEmail(document.get(HomeScreenActivity.EMAIL_PATH).toString());
                                                                userInstance.setImageUrl(document.get(HomeScreenActivity.IMAGE_URL_PATH).toString());

                                                                Intent intent = new Intent(SigninActivity.this, HomeScreenActivity.class);
                                                                startActivity(intent);
                                                            }
                                                            else{
                                                                Log.e("SignInActivity", "Document not found!");
                                                            }
                                                        }
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("TAG", "Error retrieving document", e);
                                                }
                                            });

                                }
                                else{
                                    Log.e("Auth Exception" , task.getException().toString());
                                    Toast.makeText(SigninActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

    }

}
