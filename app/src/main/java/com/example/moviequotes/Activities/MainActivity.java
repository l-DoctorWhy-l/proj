package com.example.moviequotes.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.example.moviequotes.Fragments.BookFragment;
import com.example.moviequotes.Fragments.HomeFragment;
import com.example.moviequotes.Fragments.ProfileFragment;
import com.example.moviequotes.Fragments.SearchFragment;
import com.example.moviequotes.R;
import com.example.moviequotes.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth mAuth;

    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        assert currentUser != null;
        String currentUserId = currentUser.getUid();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("users").child(currentUserId);


        replaceFragment(new HomeFragment());
        binding.menu.setSelectedItemId(R.id.home);


        binding.menu.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.search:
                    replaceFragment(new SearchFragment());
                    break;

                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;

                case R.id.book:
                    replaceFragment(new BookFragment());
                    break;

                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
            }

            return true;
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_layout, fragment);
        fragmentTransaction.commit();
    }
}