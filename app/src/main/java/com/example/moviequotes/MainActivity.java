package com.example.moviequotes;

import static com.example.moviequotes.Validator.validateEmail;

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
                binding.errorTextViewLogin.setVisibility(View.INVISIBLE);
                binding.errorTextViewReg.setVisibility(View.INVISIBLE);
            }
        });

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String errorText =  Validator.validateEmail(binding.editTextLoginEmailAddress.getText().toString());
                if(errorText != null){
                    binding.errorTextViewLogin.setText(errorText);
                    binding.errorTextViewLogin.setVisibility(View.VISIBLE);
                    return;
                }
                errorText =  Validator.validatePassword(binding.editTextLoginPassword.getText().toString());
                if(errorText != null ){
                    binding.errorTextViewLogin.setText(errorText);
                    binding.errorTextViewLogin.setVisibility(View.VISIBLE);
                    return;
                }
                binding.errorTextViewLogin.setVisibility(View.GONE);
            }
        });

        binding.buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String errorText =  Validator.validateEmail(binding.editTextRegEmailAddress.getText().toString());
                if(errorText != null){
                    binding.errorTextViewReg.setText(errorText);
                    binding.errorTextViewReg.setVisibility(View.VISIBLE);
                    return;
                }
                errorText =  Validator.validateName(binding.editTextRegName.getText().toString());
                if(errorText != null){
                    binding.errorTextViewReg.setText(errorText);
                    binding.errorTextViewReg.setVisibility(View.VISIBLE);
                    return;
                }
                errorText =  Validator.validatePassword(binding.editTextRegPassword.getText().toString());
                if(errorText != null ){
                    binding.errorTextViewReg.setText(errorText);
                    binding.errorTextViewReg.setVisibility(View.VISIBLE);
                    return;
                }
                errorText =  Validator.validateConfirmPassword(binding.editTextRegSecondPassword.getText().toString());
                if(errorText != null ){
                    binding.errorTextViewReg.setText(errorText);
                    binding.errorTextViewReg.setVisibility(View.VISIBLE);
                    return;
                }
                if (!binding.editTextRegPassword.getText().toString().equals(binding.editTextRegSecondPassword.getText().toString())){
                    binding.errorTextViewReg.setText("Passwords are not equals");
                    binding.errorTextViewReg.setVisibility(View.VISIBLE);
                    return;
                }
                binding.errorTextViewReg.setVisibility(View.GONE);
            }
        });
    }
}