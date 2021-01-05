package com.example.linked;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity {

    UserModel userInstance = UserModel.getInstance();

    List<ContactModel> contactList = new ArrayList<ContactModel>();

    TextView noContactsDefaultMsg;
    RecyclerView contactsRecyclerview;
    ContactAapter contactAapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        noContactsDefaultMsg = findViewById(R.id.homeScreenNoContactsDefaultMsg);
        contactsRecyclerview = findViewById(R.id.homeScreenRecyclerview);
        contactsRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(userInstance.getUserId())
                .collection("contacts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Log.e("Data fetch", "Success");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //MessageModel lastMsg = (MessageModel) document.get("lastMsg");
                                ContactModel contact = new ContactModel(
                                        document.get("userId").toString(),
                                        document.get("userName").toString(),
                                        document.get("imageUrl").toString()
                                        //lastMsg
                                );
                                Log.e("contact uid", contact.getUserId());

                                contactList.add(contact);

                            }


                            if(!contactList.isEmpty()) {

                                contactsRecyclerview.setVisibility(View.VISIBLE);
                                noContactsDefaultMsg.setVisibility(View.GONE);

                                contactAapter = new ContactAapter(contactList);
                                contactAapter.setOnClickListener(new ContactAapter.ClickListener() {
                                    @Override
                                    public void onItemClick(int position, View v) {
                                        Log.e("Contact Clicked", contactList.get(position).getUserName());
                                        Intent intent = new Intent(HomeScreenActivity.this, ChatScreenActivity.class);
                                        intent.putExtra("CONTACT_USER_ID", contactList.get(position).getUserId());
                                        intent.putExtra("CONTACT_USER_NAME", contactList.get(position).getUserName());
                                        startActivity(intent);
                                    }
                                });
                                contactsRecyclerview.setAdapter(contactAapter);
                            }
                            else{
                                contactsRecyclerview.setVisibility(View.GONE);
                                noContactsDefaultMsg.setVisibility(View.VISIBLE);
                            }


                        }
                        else{
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}
