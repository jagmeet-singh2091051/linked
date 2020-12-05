package com.example.linked;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity {

    List<ContactModel> contactList = new ArrayList<ContactModel>();

    RecyclerView contactsRecyclerview;
    ContactAapter contactAapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        new LoadData().execute();
    }

    private class LoadData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            contactsRecyclerview = findViewById(R.id.homeScreenRecyclerview);
            contactsRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            contactAapter = new ContactAapter(contactList);
            contactAapter.setOnClickListener(new ContactAapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    Intent intent = new Intent(HomeScreenActivity.this, ChatScreenActivity.class);
                    intent.putExtra("USERID", contactList.get(position).getUserId());
                    startActivity(intent);
                }
            });
            contactsRecyclerview.setAdapter(contactAapter);
        }
    }

}
