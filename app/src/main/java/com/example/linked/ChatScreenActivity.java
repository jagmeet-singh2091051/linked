package com.example.linked;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatScreenActivity extends AppCompatActivity {

    List<MessageModel> messageList = new ArrayList<MessageModel>();

    RecyclerView messagesRecyclerview;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        new LoadMessages().execute();
    }

    private class LoadMessages extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            messagesRecyclerview = findViewById(R.id.chatScreenRecycleView);
            messagesRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            messageAdapter = new MessageAdapter(messageList);
            messagesRecyclerview.setAdapter(messageAdapter);
        }
    }
}
