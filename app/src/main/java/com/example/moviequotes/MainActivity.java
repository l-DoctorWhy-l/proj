package com.example.moviequotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

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
                if(binding.editTextLoginEmailAddress.getText().toString().equals("") || !binding.editTextLoginEmailAddress.getText().toString().contains("@")){
                    Toast.makeText(getApplicationContext(),"Неверный Email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(binding.editTextLoginPassword.getText().toString().length() < 8){
                    Toast.makeText(getApplicationContext(),"Пароль должен содержать минимум 8 символов!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        binding.buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.editTextRegEmailAddress.getText().toString().equals("") || !binding.editTextRegEmailAddress.getText().toString().contains("@")){
                    Toast.makeText(getApplicationContext(),"Неверный Email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(binding.editTextRegName.getText().toString().length() < 4){
                    Toast.makeText(getApplicationContext(),"Слишком короткое имя пользователя!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(binding.editTextRegPassword.getText().toString().length() < 8){
                    Toast.makeText(getApplicationContext(),"Пароль должен содержать минимум 8 символов!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}