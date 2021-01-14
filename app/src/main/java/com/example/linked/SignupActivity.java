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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private UserModel userInstance = UserModel.getInstance();

    TextInputEditText username;
    TextInputEditText email;
    TextInputEditText password;
    TextInputLayout usernameTil;
    TextInputLayout emailTil;
    TextInputLayout passwordTil;
    Button signUpBtn;
    TextView signInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.signup_username);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        signUpBtn = findViewById(R.id.signup_btn);
        signInBtn = findViewById(R.id.signup_signin_btn);
        usernameTil = findViewById(R.id.signup_username_til);
        emailTil = findViewById(R.id.signup_email_til);
        passwordTil = findViewById(R.id.signup_password_til);

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

            Intent intent = new Intent(SignupActivity.this, HomeScreenActivity.class);
            startActivity(intent);
            finish();
        }

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(username.getText().toString().isEmpty()){
                    usernameTil.setError("Username cannot be empty!");
                }
                else if(email.getText().toString().isEmpty()){
                    usernameTil.setError(null);
                    emailTil.setError("Please enter a valid Email Id!");
                }
                else if(password.getText().toString().isEmpty() || password.getText().toString().length() < 6){
                    usernameTil.setError(null);
                    emailTil.setError(null);
                    passwordTil.setError("Password must be at least 6 characters long!");
                }
                else {
                    usernameTil.setError(null);
                    emailTil.setError(null);
                    passwordTil.setError(null);
                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = firebaseAuth.getCurrentUser();

                                        userInstance.setEmail(user.getEmail());
                                        userInstance.setUserName(username.getText().toString());
                                        userInstance.setUserId(user.getUid());
                                        userInstance.setImageUrl("default url");

                                        db.collection(HomeScreenActivity.USERS_COLLECTION_PATH)
                                                .document(userInstance.getUserId())
                                                .set(userInstance)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Intent intent = new Intent(SignupActivity.this, HomeScreenActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error adding document", e);
                                            }
                                        });

                                    } else {
                                        Log.e("Auth Exception", task.getException().toString());
                                        Toast.makeText(SignupActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
