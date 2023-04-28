package com.example.moviequotes;

import androidx.annotation.NonNull;

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
    final public static FirebaseUser currentUser = mAuth.getCurrentUser();
    final public static String currentUserId = currentUser.getUid();
    final public static FirebaseDatabase db = FirebaseDatabase.getInstance();
    final public static DatabaseReference quotesRef = db.getReference().child("quotes");
    final public static DatabaseReference likedQuotesRef = db.getReference().child("users").child(currentUserId).child("likes");
    final public static DatabaseReference userDB = Network.db.getReference("users");
    final public static DatabaseReference user = userDB.child(currentUserId);

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
}
