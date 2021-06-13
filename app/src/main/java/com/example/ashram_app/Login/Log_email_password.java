package com.example.ashram_app.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ashram_app.MainActivity;
import com.example.ashram_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Log_email_password extends AppCompatActivity {

    private EditText edLogin, edPassword,eTEmale;
    private Button bSignUp, bSignIn, bRememberPass, bBack;
    private TextView textView3;
    private FirebaseAuth mAuth;
    private FirebaseUser cUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_email_password_layout);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        cUser = mAuth.getCurrentUser();
        if (cUser != null) {
            if(cUser.isEmailVerified()){
                Intent i = new Intent(Log_email_password.this, MainActivity.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(Log_email_password.this, "Для подтверждения регистрации проверьте вашу почту", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Введите имя пользователя и пароль", Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {
        edLogin = findViewById(R.id.edLogin);
        eTEmale = findViewById(R.id.eTEmale);
        edPassword = findViewById(R.id.edPassword);
        bSignUp = findViewById(R.id.bSignUp);
        bSignIn = findViewById(R.id.bSignIn);
        bRememberPass = findViewById(R.id.bRememberPass);
        bBack = findViewById(R.id.bBack);
        textView3 = findViewById(R.id.textView3);
        mAuth = FirebaseAuth.getInstance();
    }

    public void onClickSignUp(View view) {
        if (!TextUtils.isEmpty(edLogin.getText().toString()) && !TextUtils.isEmpty(edPassword.getText().toString())) {
            mAuth.createUserWithEmailAndPassword(edLogin.getText().toString(), edPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        sendEmailVer();
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        if(user.isEmailVerified()){
                            Intent i = new Intent(Log_email_password.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(Log_email_password.this, "Для подтверждения регистрации проверьте вашу почту", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Log_email_password.this, "Не удалось зарегистрироваться", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(Log_email_password.this, "Введите адрес электронной почты и пароль", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickSignIn(View view) {
        if (!TextUtils.isEmpty(edLogin.getText().toString()) && !TextUtils.isEmpty(edPassword.getText().toString())) {
            mAuth.signInWithEmailAndPassword(edLogin.getText().toString(), edPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        if(user.isEmailVerified()){
                            Intent i = new Intent(Log_email_password.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(Log_email_password.this, "Для подтверждения регистрации проверьте вашу почту", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Log_email_password.this, "Не удалось войти в приложение", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(Log_email_password.this, "Для входа введите имя пользователя и пароль", Toast.LENGTH_SHORT).show();
        }
    }
    private void sendEmailVer() {

        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Log_email_password.this, "Для подтверждения регистрации проверьте вашу почту", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Log_email_password.this, "Отправка не удалась", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void passwordRemember (View view) {
        edLogin.setVisibility(View.GONE);
        edPassword.setVisibility(View.GONE);
        bSignUp.setVisibility(View.GONE);
        bSignIn.setVisibility(View.GONE);
        textView3.setVisibility(View.GONE);
        eTEmale.setVisibility(View.VISIBLE);
        bRememberPass.setVisibility(View.VISIBLE);
        bBack.setVisibility(View.VISIBLE);
    }
    public void back (View view) {
        edLogin.setVisibility(View.VISIBLE);
        edPassword.setVisibility(View.VISIBLE);
        bSignUp.setVisibility(View.VISIBLE);
        bSignIn.setVisibility(View.VISIBLE);
        textView3.setVisibility(View.VISIBLE);
        eTEmale.setVisibility(View.GONE);
        bRememberPass.setVisibility(View.GONE);
        bBack.setVisibility(View.GONE);
    }
    public void rememberPass (View view) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Editable emailAddress = eTEmale.getText();

        auth.sendPasswordResetEmail(String.valueOf(emailAddress))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Log_email_password.this, "Ссылка для восстановления пароля направлена на вашу почту", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}