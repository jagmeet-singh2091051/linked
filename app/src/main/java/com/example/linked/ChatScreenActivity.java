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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatScreenActivity extends AppCompatActivity {

    UserModel userInstance = UserModel.getInstance();
    List<MessageModel> messageList = new ArrayList<MessageModel>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView usernameTV;
    TextView initialsTV;
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
        initialsTV = findViewById(R.id.profile_image);


        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra("CONTACT_USER_ID")){
            contactUserId = intentThatStartedThisActivity.getStringExtra("CONTACT_USER_ID");
        }
        if(intentThatStartedThisActivity.hasExtra("CONTACT_USER_NAME")){
            contactUsername = intentThatStartedThisActivity.getStringExtra("CONTACT_USER_NAME");
        }

        initialsTV.setText(String.valueOf(contactUsername.toUpperCase().charAt(0)));
        usernameTV.setText(contactUsername);

        messagesRecyclerview = findViewById(R.id.chatScreenRecycleView);
        messagesRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        db.collection(HomeScreenActivity.USERS_COLLECTION_PATH)
                .document(userInstance.getUserId())
                .collection(HomeScreenActivity.CONTACTS_COLLECTION_PATH)
                .document(contactUserId)
                .collection(HomeScreenActivity.MESSAGES_COLLECTION_PATH)
                .orderBy("timestamp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e( "Listen failed: ", error.toString());
                            return;
                        }
                        else{
                            messageList.clear();
                        }

                        for(QueryDocumentSnapshot doc : value){
                            if(doc.get("message") != null){
                                MessageModel msg = new MessageModel(doc.getString("message"), doc.getString("timeSent"),
                                        doc.getString("timeReceived"), doc.getBoolean("sent"), doc.getTimestamp("timestamp"));

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
                if(!messageET.getText().toString().equals("")){

                    DateFormat df = new SimpleDateFormat("h:mm a", Locale.getDefault());
                    String date = df.format(Calendar.getInstance().getTime());

                    MessageModel newSentMsg = new MessageModel(messageET.getText().toString(), date, date, true, Timestamp.now());
                    MessageModel newReceivedMsg = new MessageModel(messageET.getText().toString(), date, date, false, Timestamp.now());

                    messageList.add(newSentMsg);
                    messageAdapter.notifyDataSetChanged();


                    //Send message to our db
                    db.collection(HomeScreenActivity.USERS_COLLECTION_PATH)
                            .document(userInstance.getUserId())
                            .collection(HomeScreenActivity.CONTACTS_COLLECTION_PATH)
                            .document(contactUserId)
                            .collection(HomeScreenActivity.MESSAGES_COLLECTION_PATH)
                            .add(newSentMsg)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    //messageAdapter.notifyDataSetChanged();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Message not sent: ", e.toString());
                                }
                            });


                    //Send message to contact's db
                    db.collection(HomeScreenActivity.USERS_COLLECTION_PATH)
                            .document(contactUserId)
                            .collection(HomeScreenActivity.CONTACTS_COLLECTION_PATH)
                            .document(userInstance.getUserId())
                            .collection(HomeScreenActivity.MESSAGES_COLLECTION_PATH)
                            .add(newReceivedMsg)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    //messageAdapter.notifyDataSetChanged();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Message not sent: ", e.toString());
                                }
                            });
                }

                //update last msg
                if (!messageList.isEmpty()) {
                    db.collection(HomeScreenActivity.USERS_COLLECTION_PATH)
                            .document(userInstance.getUserId())
                            .collection(HomeScreenActivity.CONTACTS_COLLECTION_PATH)
                            .document(contactUserId)
                            .update(HomeScreenActivity.LAST_MSG_PATH, messageList.get(messageList.size() - 1).getMessage()
                                    , HomeScreenActivity.LAST_MSG_TIME_PATH, messageList.get(messageList.size() - 1).getTimeSent())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.e("LastMsgUpdate Success", messageList.get(messageList.size() - 1).getMessage());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Log.e("LastMsgUpdate Failed", e.toString());
                                }
                            });
                }

                messageET.setText("");
            }
        });


    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();

        if (!messageList.isEmpty()) {
            db.collection(HomeScreenActivity.USERS_COLLECTION_PATH)
                    .document(userInstance.getUserId())
                    .collection(HomeScreenActivity.CONTACTS_COLLECTION_PATH)
                    .document(contactUserId)
                    .update(HomeScreenActivity.LAST_MSG_PATH, messageList.get(messageList.size() - 1).getMessage()
                            , HomeScreenActivity.LAST_MSG_TIME_PATH, messageList.get(messageList.size() - 1).getTimeSent())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
        }
    }*/

}
