package com.example.moviequotes.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.moviequotes.Network.Network;
import com.example.moviequotes.R;
import com.example.moviequotes.Entities.User;
import com.example.moviequotes.Validator;
import com.example.moviequotes.databinding.ActivityStartBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class StartActivity extends AppCompatActivity {
    public ActivityStartBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(Network.currentUser != null)
            moveToMain();

    }

    @Override
    public void onStart() {
        super.onStart();


        binding.signReg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checked_id) {
                switch (checked_id){
                    case R.id.radioButton_login:
                        binding.regForm.setEnabled(false);
                        binding.regForm.startAnimation(AnimationUtils.loadAnimation(StartActivity.this,R.anim.leave_left_out));
                        binding.regForm.setVisibility(View.GONE);
                        binding.loginForm.setEnabled(true);
                        binding.loginForm.startAnimation(AnimationUtils.loadAnimation(StartActivity.this,R.anim.leave_right_in));
                        binding.loginForm.setVisibility(View.VISIBLE);

                        break;
                    case R.id.radioButton_reg:
                        binding.loginForm.setEnabled(false);
                        binding.loginForm.startAnimation(AnimationUtils.loadAnimation(StartActivity.this,R.anim.leave_right_out));
                        binding.loginForm.setVisibility(View.GONE);
                        binding.regForm.setEnabled(true);
                        binding.regForm.startAnimation(AnimationUtils.loadAnimation(StartActivity.this,R.anim.leave_left_in));
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
                Network.mAuth.signInWithEmailAndPassword(binding.editTextLoginEmailAddress.getText().toString(),binding.editTextLoginPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            moveToMain();
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
                    binding.errorTextViewReg.setText("Passwords are not equal");
                    binding.errorTextViewReg.setVisibility(View.VISIBLE);
                    return;
                }
                binding.errorTextViewReg.setVisibility(View.GONE);
                Network.mAuth.createUserWithEmailAndPassword(
                        binding.editTextRegEmailAddress.getText().toString(),
                        binding.editTextRegPassword.getText().toString()
                ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            User user = new User(binding.editTextRegName.getText().toString(),binding.editTextRegEmailAddress.getText().toString());
                            Network.userDB.child(Network.mAuth.getCurrentUser().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    moveToMain();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(StartActivity.this, "Не удалось создать аккаунт(",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Toast.makeText(StartActivity.this, "Не удалось создать аккаунт(",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void moveToMain() {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}