package com.example.moviequotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.moviequotes.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.signReg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked_id) {
                switch (checked_id){
                    case R.id.radioButton_login:
                        binding.loginForm.setVisibility(View.VISIBLE);
                        binding.regForm.setVisibility(View.GONE);
                        break;
                    case R.id.radioButton_reg:
                        binding.loginForm.setVisibility(View.GONE);
                        binding.regForm.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}