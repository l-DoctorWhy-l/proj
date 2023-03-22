package com.example.moviequotes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviequotes.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        binding.editSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editSettingsButton.setVisibility(View.GONE);
                binding.saveSettingsButton.setVisibility(View.VISIBLE);
                binding.profileUsername.setVisibility(View.GONE);
                binding.editProfileUsername.setVisibility(View.VISIBLE);
                binding.profileSexLayout.setVisibility(View.GONE);
                binding.editProfileSex.setVisibility(View.VISIBLE);
            }
        });

        binding.saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editSettingsButton.setVisibility(View.VISIBLE);
                binding.saveSettingsButton.setVisibility(View.GONE);
                binding.profileUsername.setVisibility(View.VISIBLE);
                binding.editProfileUsername.setVisibility(View.GONE);
                binding.profileSexLayout.setVisibility(View.VISIBLE);
                binding.editProfileSex.setVisibility(View.GONE);


                // FUNC меняем данные
            }
        });






        return binding.getRoot();
    }
}