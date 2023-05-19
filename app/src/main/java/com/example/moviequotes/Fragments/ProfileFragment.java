package com.example.moviequotes.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.moviequotes.Network.Network;
import com.example.moviequotes.R;
import com.example.moviequotes.Entities.User;
import com.example.moviequotes.RoomDatabase.BookDB;
import com.example.moviequotes.RoomDatabase.QuoteDAO;
import com.example.moviequotes.Validator;
import com.example.moviequotes.databinding.FragmentProfileBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.schedulers.Schedulers;


public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    User profile;
    BookDB bookDB;
    QuoteDAO quoteDAO;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        getUserDataFromDB();
        bookDB = BookDB.getInstance(requireContext());
        quoteDAO = bookDB.quoteDAO();





        binding.editSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editSettingsButton.setVisibility(View.GONE);
                binding.saveSettingsButton.setVisibility(View.VISIBLE);
                binding.profileUsername.setVisibility(View.GONE);
                binding.editProfileUsername.setVisibility(View.VISIBLE);
                binding.profileSexLayout.setVisibility(View.GONE);
                binding.editProfileSex.setVisibility(View.VISIBLE);

                binding.editProfileUsername.setText(profile.getUserName());
                if(profile.getSex().equals("Male"))
                    binding.radioBtnM.setChecked(true);
                else
                    binding.radioBtnF.setChecked(true);

                binding.editProfileSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        switch (i){
                            case R.id.radioBtnM:
                            case R.id.radioBtnF:
                                break;
                        }
                    }
                });
            }
        });

        binding.saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validator.validateName(binding.editProfileUsername.getText().toString()) == null){
                    binding.editSettingsButton.setVisibility(View.VISIBLE);
                    binding.saveSettingsButton.setVisibility(View.GONE);
                    binding.profileUsername.setVisibility(View.VISIBLE);
                    binding.editProfileUsername.setVisibility(View.GONE);
                    binding.profileSexLayout.setVisibility(View.VISIBLE);
                    binding.editProfileSex.setVisibility(View.GONE);
                    Network.user.child("userName").setValue(binding.editProfileUsername.getText().toString());
                    if(binding.radioBtnM.isChecked()){
                        Network.user.child("sex").setValue("Male");
                    } else {
                        Network.user.child("sex").setValue("Female");
                    }
                } else{
                    Toast.makeText(getContext(),Validator.validateName(binding.editProfileUsername.getText().toString()), Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Network.logOut();
                quoteDAO.deleteAllQuotes().subscribeOn(Schedulers.io()).subscribe();
                getActivity().finish();
            }
        });




        return binding.getRoot();
    }

    private void getUserDataFromDB(){
        ValueEventListener profileDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profile = snapshot.getValue(User.class);
                assert profile != null;
                setProfileData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        Network.user.addValueEventListener(profileDataListener);
    }
    void setProfileData(){
        binding.profileUsername.setText(profile.getUserName());
        binding.profileEmail.setText(profile.getEmail());
        binding.profileSex.setText(profile.getSex());
        if (profile.getSex().equals("Male")){
            binding.profileImageView.setImageResource(R.drawable.abraham_lincoln);
        } else{
            binding.profileImageView.setImageResource(R.drawable.girl);
        }
    }
}