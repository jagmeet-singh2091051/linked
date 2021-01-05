package com.example.linked;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatScreenActivity extends AppCompatActivity {

    UserModel userInstance = UserModel.getInstance();
    List<MessageModel> messageList = new ArrayList<MessageModel>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView usernameTV;
    ImageButton sendBtn;
    EditText messageET;

    RecyclerView messagesRecyclerview;
    MessageAdapter messageAdapter;
    private String contactUserId;
    private String contactUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        messageET = findViewById(R.id.text_send);
        sendBtn = findViewById(R.id.send_button);
        usernameTV = findViewById(R.id.username);

        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra("CONTACT_USER_ID")){
            contactUserId = intentThatStartedThisActivity.getStringExtra("CONTACT_USER_ID");
        }
        if(intentThatStartedThisActivity.hasExtra("CONTACT_USER_NAME")){
            contactUsername = intentThatStartedThisActivity.getStringExtra("CONTACT_USER_NAME");
        }

        usernameTV.setText(contactUsername);

        messagesRecyclerview = findViewById(R.id.chatScreenRecycleView);
        messagesRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        db.collection("users").document(userInstance.getUserId())
                .collection("contacts").document(contactUserId).collection("messages")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e( "Listen failed: ", error.toString());
                            return;
                        }

                        for(QueryDocumentSnapshot doc : value){
                            if(doc.get("message") != null){
                                MessageModel msg = new MessageModel(doc.getString("message"), doc.getDate("timeSent"),
                                        doc.getDate("timeReceived"), doc.getBoolean("sent"));

                                messageList.add(msg);
                            }
                        }
                    }
                });


        messageAdapter = new MessageAdapter(messageList);
        messagesRecyclerview.setAdapter(messageAdapter);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageET.getText() != null){
                    
                }
            }
        });


    }

}
