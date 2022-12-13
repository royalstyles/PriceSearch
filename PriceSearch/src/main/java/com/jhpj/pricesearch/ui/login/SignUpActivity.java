package com.jhpj.pricesearch.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jhpj.pricesearch.R;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordCheck;
    private ProgressBar progressBar;

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fAuth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.et_emailArea);
        etPassword = findViewById(R.id.et_passwordArea);
        etPasswordCheck = findViewById(R.id.et_passwordcheckArea);

        progressBar = findViewById(R.id.progress_bar);

        Button btSignup = findViewById(R.id.btn_signup);
        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                Log.d(TAG, "Email : " + etEmail.getText().toString());
                Log.d(TAG, "Password : " + etPassword.getText().toString());

                if (!isValidEmail(etEmail.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, "이메일 형식을 정확하게 입력하세요.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (etPassword.getText().toString().length() < 6) {
                    Toast.makeText(SignUpActivity.this, "비밀번호를 6자리 이상으로 등록해주세요.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (!etPassword.getText().toString().equals(etPasswordCheck.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, "비밀번호와 비밀번호확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (!etEmail.getText().toString().equals("") && !etPassword.getText().toString().equals("")) {
                    // 이메일과 비밀번호가 공백이 아닌경우
                    createUser(etEmail.getText().toString(), etPassword.getText().toString());
                } else {
                    // 이메일과 비밀번호가 공백인 경우
                    Toast.makeText(SignUpActivity.this, "계정과 비밀번호를 정확히 입력하세요.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void createUser(String email, String password) {
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    // 회원가입 성공시
                    Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                } else {
                    // 계정이 중복인 경우
                    Toast.makeText(SignUpActivity.this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isValidEmail(String email) {
        boolean error = false;
        Pattern p = Patterns.EMAIL_ADDRESS;
        if (p.matcher(email).matches()) {
            error = true;
        }
        return error;
    }
}