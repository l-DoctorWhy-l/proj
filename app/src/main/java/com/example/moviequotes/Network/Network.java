package com.example.moviequotes.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import com.example.moviequotes.Entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Network {
    static User userData;
    final public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseUser currentUser = mAuth.getCurrentUser();
    final public static FirebaseDatabase db = FirebaseDatabase.getInstance();
    final public static DatabaseReference quotesRef = db.getReference().child("quotes");
    public static DatabaseReference likedQuotesRef;
    final public static DatabaseReference userDB = Network.db.getReference("users");
    public static String currentUserId;
    public static DatabaseReference user;


    static {
        if(currentUser != null) {
            currentUserId = currentUser.getUid();
            user = userDB.child(currentUserId);
            likedQuotesRef = db.getReference().child("users").child(currentUserId).child("likes");
        }

    }

    public static ValueEventListener createLikedQuotesEventListener(ArrayList<String> likedQuotesIdList){
        if (likedQuotesIdList.size() != 0)
            likedQuotesIdList.clear();

        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    assert ds != null;
                    likedQuotesIdList.add(ds.child("id").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }


    public static User getCurrentUserDataFromDB(){
        setUserData();
       return userData;
    }

    public static void setUserData() {
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userData = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void logOut(){
        mAuth.signOut();
        currentUser = null;

    }
    public static void init(){
        if(currentUser == null) {
            currentUser = mAuth.getCurrentUser();
            currentUserId = currentUser.getUid();
            user = userDB.child(currentUserId);
            likedQuotesRef = db.getReference().child("users").child(currentUserId).child("likes");
        }
    }
    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }
}
