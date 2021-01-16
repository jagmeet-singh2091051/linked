package com.example.linked;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity {

    public static final String USERS_COLLECTION_PATH = "users";
    public static final String CONTACTS_COLLECTION_PATH = "contacts";
    public static final String MESSAGES_COLLECTION_PATH = "messages";
    public static final String EMAIL_PATH = "email";
    public static final String USERID_PATH = "userId";
    public static final String USERNAME_PATH = "userName";
    public static final String IMAGE_URL_PATH = "imageUrl";
    public static final String LAST_MSG_PATH = "lastMsg";
    public static final String LAST_MSG_TIME_PATH = "lastMsgTime";

    UserModel userInstance = UserModel.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    List<ContactModel> contactList = new ArrayList<ContactModel>();
    List<ContactModel> searchList = new ArrayList<ContactModel>();

    TextView noContactsDefaultMsg;
    RecyclerView contactsRecyclerview;
    RecyclerView searchRecyclerView;
    ContactAdapter contactAdapter;
    ContactAdapter contactAdapter2;
    android.widget.SearchView searchView;
    Toolbar toolbar;
    ImageButton settingsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        setSupportActionBar(toolbar);



        searchView = findViewById(R.id.search_view);
        toolbar = findViewById(R.id.home_screen_toolbar);
        noContactsDefaultMsg = findViewById(R.id.homeScreenNoContactsDefaultMsg);
        contactsRecyclerview = findViewById(R.id.homeScreenRecyclerview);
        searchRecyclerView = findViewById(R.id.homeScreenSearchView);
        settingsBtn = findViewById(R.id.homescreen_toolbar_settings_btn);


        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        contactsRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



        //get user's contacts
        db.collection(HomeScreenActivity.USERS_COLLECTION_PATH)
                .document(userInstance.getUserId())
                .collection(HomeScreenActivity.CONTACTS_COLLECTION_PATH)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            Log.e( "Listen failed: ", error.toString());
                        }
                        else {
                            contactList.clear();
                            for (QueryDocumentSnapshot document : value) {
                                if (document != null) {

                                    ContactModel contact = new ContactModel(
                                            document.get(HomeScreenActivity.USERID_PATH).toString(),
                                            document.get(HomeScreenActivity.USERNAME_PATH).toString(),
                                            document.get(HomeScreenActivity.IMAGE_URL_PATH).toString(),
                                            document.get(HomeScreenActivity.LAST_MSG_PATH).toString(),
                                            document.get(HomeScreenActivity.LAST_MSG_TIME_PATH).toString()
                                    );

                                    contactList.add(contact);

                                    if (!contactList.isEmpty()) {

                                        contactsRecyclerview.setVisibility(View.VISIBLE);
                                        noContactsDefaultMsg.setVisibility(View.GONE);

                                        contactAdapter = new ContactAdapter(contactList);
                                        contactAdapter.setOnClickListener(new ContactAdapter.ClickListener() {
                                            @Override
                                            public void onItemClick(int position, View v) {
                                                Log.e("Contact Clicked", contactList.get(position).getUserName());
                                                Intent intent = new Intent(HomeScreenActivity.this, ChatScreenActivity.class);
                                                intent.putExtra("CONTACT_USER_ID", contactList.get(position).getUserId());
                                                intent.putExtra("CONTACT_USER_NAME", contactList.get(position).getUserName());
                                                startActivity(intent);
                                            }
                                        });
                                        contactsRecyclerview.setAdapter(contactAdapter);
                                    } else {
                                        contactsRecyclerview.setVisibility(View.GONE);
                                        noContactsDefaultMsg.setVisibility(View.VISIBLE);
                                    }

                                } else {
                                    Log.w("TAG", "Error getting documents.", error);
                                }
                            }
                        }
                    }
                });


        /*searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.setQuery("", false);
                searchView.setIconified(true);

                contactsRecyclerview.setVisibility(View.VISIBLE);
                searchRecyclerView.setVisibility(View.GONE);
                return true;
            }
        });*/

        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
            /*    searchList.clear();
                contactsRecyclerview.setVisibility(View.GONE);
                noContactsDefaultMsg.setVisibility(View.GONE);
                searchRecyclerView.setVisibility(View.VISIBLE);

                db.collection(HomeScreenActivity.USERS_COLLECTION_PATH)
                        .orderBy(HomeScreenActivity.USERNAME_PATH)
                        .whereGreaterThanOrEqualTo(HomeScreenActivity.USERNAME_PATH, query)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot document : task.getResult()){
                                        ContactModel contact = new ContactModel(
                                                Objects.requireNonNull(document.get(HomeScreenActivity.USERID_PATH)).toString(),
                                                Objects.requireNonNull(document.get(HomeScreenActivity.USERNAME_PATH)).toString(),
                                                Objects.requireNonNull(document.get(HomeScreenActivity.IMAGE_URL_PATH)).toString(),
                                                "",
                                                ""
                                        );
                                        searchList.add(contact);

                                        contactAapter2 = new ContactAapter(searchList);
                                        contactAapter2.setOnClickListener(new ContactAapter.ClickListener() {
                                            @Override
                                            public void onItemClick(int position, View v) {
                                                //Add new contact to contact list

                                                db.collection(HomeScreenActivity.USERS_COLLECTION_PATH)
                                                        .document(userInstance.getUserId())
                                                        .collection(HomeScreenActivity.CONTACTS_COLLECTION_PATH)
                                                        .document(searchList.get(position).getUserId())
                                                        .set(searchList.get(position))
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                searchView.setQuery("", false);
                                                                searchView.setIconified(true);

                                                                contactsRecyclerview.setVisibility(View.VISIBLE);
                                                                searchRecyclerView.setVisibility(View.GONE);
                                                            }
                                                        });
                                            }
                                        });

                                        searchRecyclerView.setAdapter(contactAapter2);
                                    }
                                }
                            }
                        });
*/
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchList.clear();

                db.collection(HomeScreenActivity.USERS_COLLECTION_PATH)
                        .orderBy(HomeScreenActivity.USERNAME_PATH)
                        .whereGreaterThanOrEqualTo(HomeScreenActivity.USERNAME_PATH, query)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot document : task.getResult()){
                                        ContactModel contact = new ContactModel(
                                                document.get(HomeScreenActivity.USERID_PATH).toString(),
                                                document.get(HomeScreenActivity.USERNAME_PATH).toString(),
                                                document.get(HomeScreenActivity.IMAGE_URL_PATH).toString(),
                                                "",
                                                ""
                                        );
                                        searchList.add(contact);

                                        contactAdapter2 = new ContactAdapter(searchList);
                                        searchRecyclerView.setAdapter(contactAdapter2);
                                        contactAdapter2.setOnClickListener(new ContactAdapter.ClickListener() {
                                            @Override
                                            public void onItemClick(int position, View v) {
                                                //Add new contact to contact list

                                                db.collection(HomeScreenActivity.USERS_COLLECTION_PATH)
                                                        .document(userInstance.getUserId())
                                                        .collection(HomeScreenActivity.CONTACTS_COLLECTION_PATH)
                                                        .document(searchList.get(position).getUserId())
                                                        .set(searchList.get(position))
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                searchView.setQuery("", false);
                                                                searchView.setIconified(true);

                                                                searchList.clear();
                                                                contactAdapter2.notifyDataSetChanged();

                                                                /*finish();
                                                                startActivity(getIntent());*/
                                                            }
                                                        });

                                            }
                                        });
                                    }
                                }
                            }
                        });

                return false;
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

    }
}
