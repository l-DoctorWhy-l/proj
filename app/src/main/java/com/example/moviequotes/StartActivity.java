package com.example.moviequotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.moviequotes.databinding.ActivityStartBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {
    ActivityStartBinding binding;
    private FirebaseAuth mAuth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

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
                mAuth.signInWithEmailAndPassword(binding.editTextLoginEmailAddress.getText().toString(),binding.editTextLoginPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(StartActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(StartActivity.this, "Неверный логин или пароль",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                mAuth.createUserWithEmailAndPassword(binding.editTextRegEmailAddress.getText().toString(),binding.editTextRegPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Intent intent = new Intent(StartActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(StartActivity.this, "Не удалось создать аккаунт(",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}